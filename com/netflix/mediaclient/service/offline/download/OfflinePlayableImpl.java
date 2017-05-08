// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.offline.download;

import com.netflix.mediaclient.util.log.OfflineLogUtils;
import com.netflix.mediaclient.servicemgr.interface_.offline.WatchState;
import java.util.concurrent.TimeUnit;
import android.telephony.TelephonyManager;
import android.net.wifi.WifiManager;
import com.netflix.mediaclient.service.offline.manifest.OfflineManifestCallback;
import com.netflix.mediaclient.servicemgr.interface_.offline.DownloadVideoQuality;
import com.netflix.mediaclient.util.ConnectivityUtils;
import com.netflix.mediaclient.service.player.OfflinePlaybackInterface$OfflineManifest;
import com.netflix.mediaclient.service.offline.manifest.OfflinePlayableManifestImpl;
import com.netflix.mediaclient.service.pdslogging.DownloadContext;
import com.netflix.mediaclient.service.offline.license.OfflineLicenseManager$DownloadCompleteAndActivateCallback;
import com.netflix.mediaclient.service.offline.agent.PlayabilityEnforcer;
import com.netflix.mediaclient.util.AndroidUtils;
import com.netflix.mediaclient.android.app.NetflixStatus;
import com.netflix.mediaclient.StatusCode;
import com.netflix.mediaclient.util.LogUtils;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.util.ThreadUtils;
import com.netflix.mediaclient.service.offline.license.OfflineLicenseManagerCallback;
import com.netflix.mediaclient.util.FileUtils;
import com.netflix.mediaclient.service.offline.utils.OfflineUtils;
import com.netflix.mediaclient.service.offline.utils.OfflinePathUtils;
import java.util.Iterator;
import com.netflix.mediaclient.service.offline.agent.OfflineAgentInterface$PlayableRefreshLicenseCallBack;
import com.netflix.mediaclient.service.player.manifest.NfManifest;
import com.netflix.mediaclient.service.player.bladerunnerclient.IBladeRunnerClient$OfflineRefreshInvoke;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.service.player.bladerunnerclient.OfflineLicenseResponse;
import com.netflix.mediaclient.servicemgr.interface_.offline.StopReason;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.interface_.offline.DownloadState;
import java.util.ArrayList;
import com.android.volley.RequestQueue;
import com.netflix.mediaclient.service.offline.agent.OfflineNrdpLogger;
import com.netflix.mediaclient.service.offline.manifest.OfflineManifestManager;
import com.netflix.mediaclient.service.offline.license.OfflineLicenseManager;
import java.io.File;
import android.content.Context;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import java.util.List;
import android.os.HandlerThread;

public class OfflinePlayableImpl implements CdnUrlDownloadListener, OfflinePlayable
{
    private static final long DISK_FREE_SPACE_SAFETY_MARGIN = 10000000L;
    private static final long MIN_PERCENTAGE_UPDATE_PERIOD_MS = 2000L;
    private static final int MSG_CDN_URL_DOWNLOADED_SESSION_END = 4;
    private static final int MSG_CDN_URL_EXPIRED_OR_MOVED = 3;
    private static final int MSG_CDN_URL_GEO_CHECK_ERROR = 5;
    private static final int MSG_NETWORK_ERROR = 2;
    private static final int MSG_URL_DOWNLOAD_DISK_IO_ERROR = 1;
    private static final String TAG = "nf_offlinePlayable";
    private int mActiveDownloadableCount;
    private OfflinePlayableImpl$BackGroundMessageHandler mBackGroundMessageHandler;
    private final HandlerThread mBackgroundThread;
    private final List<CdnUrlDownloader> mCdnUrlDownloaderList;
    private final IClientLogging mClientLogging;
    private int mCompletedDownloadableCount;
    private final Context mContext;
    private final String mDirPathOfPlayable;
    private final File mDirPathOfPlayableFileObject;
    private boolean mLicenseActivating;
    private boolean mLicenseRefreshing;
    private final OfflineLicenseManager mOfflineLicenseManager;
    private final OfflineManifestManager mOfflineManifestManager;
    private final OfflineNrdpLogger mOfflineNrdpLogger;
    private final OfflinePlayableListener mOfflinePlayableListener;
    private final OfflinePlayablePersistentData mOfflinePlayablePersistentData;
    private final PlayableProgressInfo mPlayableProgressInfo;
    private final OfflinePlayableImpl$ProgressWatchRunnable mProgressWatchRunnable;
    private final RequestQueue mRequestQueue;
    
    public OfflinePlayableImpl(final Context mContext, final OfflinePlayablePersistentData mOfflinePlayablePersistentData, final PlayableProgressInfo mPlayableProgressInfo, final String mDirPathOfPlayable, final RequestQueue mRequestQueue, final OfflineManifestManager mOfflineManifestManager, final OfflineLicenseManager mOfflineLicenseManager, final OfflinePlayableListener mOfflinePlayableListener, final HandlerThread mBackgroundThread, final OfflineNrdpLogger mOfflineNrdpLogger, final IClientLogging mClientLogging) {
        this.mCdnUrlDownloaderList = new ArrayList<CdnUrlDownloader>();
        this.mProgressWatchRunnable = new OfflinePlayableImpl$ProgressWatchRunnable(this, null);
        this.mContext = mContext;
        this.mOfflinePlayablePersistentData = mOfflinePlayablePersistentData;
        this.mPlayableProgressInfo = mPlayableProgressInfo;
        this.mDirPathOfPlayable = mDirPathOfPlayable;
        this.mDirPathOfPlayableFileObject = new File(mDirPathOfPlayable);
        this.mRequestQueue = mRequestQueue;
        this.mOfflineManifestManager = mOfflineManifestManager;
        this.mOfflineLicenseManager = mOfflineLicenseManager;
        this.mOfflinePlayableListener = mOfflinePlayableListener;
        this.mBackGroundMessageHandler = new OfflinePlayableImpl$BackGroundMessageHandler(this, mBackgroundThread.getLooper());
        this.mBackgroundThread = mBackgroundThread;
        this.mOfflineNrdpLogger = mOfflineNrdpLogger;
        this.mClientLogging = mClientLogging;
        if (this.mOfflinePlayablePersistentData.getDownloadState() == DownloadState.InProgress) {
            Log.i("nf_offlinePlayable", "OfflinePlayableImpl constructor marking item stopped");
            this.mOfflinePlayablePersistentData.setDownloadStateStopped(StopReason.WaitingToBeStarted);
        }
        if (!this.checkAllDownloadablesExists()) {
            Log.e("nf_offlinePlayable", "OfflinePlayableImpl checkAllDownloadablesExists false");
            this.mOfflinePlayablePersistentData.setDownloadStateStopped(StopReason.WaitingToBeStarted);
        }
        this.buildDownloadableProgressInfoMap();
        if (this.mOfflineManifestManager == null || this.mOfflineLicenseManager == null) {
            throw new RuntimeException("mOfflineManifestManager or mOfflineLicenseManager can't be null");
        }
    }
    
    private void buildCdnUrlDownloaderAndAddToList(final List<? extends DownloadableInfo> list, final DownloadablePersistentData downloadablePersistentData, final List<CdnUrlDownloader> list2, final String s) {
        final DownloadableInfo downloadInfo = this.getDownloadInfo(list, downloadablePersistentData.mDownloadableId);
        if (downloadInfo != null) {
            list2.add(this.createCdnUrlDownloader(downloadablePersistentData, downloadInfo, s));
        }
    }
    
    private void buildDownloadableProgressInfoMap() {
        final Iterator<DownloadablePersistentData> iterator = this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList.iterator();
        while (iterator.hasNext()) {
            this.getProgressInfoForDownloadable(iterator.next(), DownloadableType.Audio);
        }
        final Iterator<DownloadablePersistentData> iterator2 = this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList.iterator();
        while (iterator2.hasNext()) {
            this.getProgressInfoForDownloadable(iterator2.next(), DownloadableType.Video);
        }
        final Iterator<DownloadablePersistentData> iterator3 = this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList.iterator();
        while (iterator3.hasNext()) {
            this.getProgressInfoForDownloadable(iterator3.next(), DownloadableType.Subtitle);
        }
        final Iterator<DownloadablePersistentData> iterator4 = this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList.iterator();
        while (iterator4.hasNext()) {
            this.getProgressInfoForDownloadable(iterator4.next(), DownloadableType.TrickPlay);
        }
        this.mPlayableProgressInfo.updateProgressPercentage();
    }
    
    private void buildUrlDownloaderAndPersistentDataAndAddToList(final DownloadableInfo downloadableInfo, final List<CdnUrlDownloader> list, final List<DownloadablePersistentData> list2, final String s) {
        if (downloadableInfo != null) {
            final DownloadablePersistentData downloadablePersistentData = new DownloadablePersistentData(downloadableInfo);
            list.add(this.createCdnUrlDownloader(downloadablePersistentData, downloadableInfo, s));
            list2.add(downloadablePersistentData);
        }
    }
    
    private boolean canRefreshOrDeleteLicense() {
        return !this.mLicenseRefreshing && !this.mLicenseActivating;
    }
    
    private boolean checkAllDownloadablesExists() {
        final Iterator<DownloadablePersistentData> iterator = this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList.iterator();
        boolean b = true;
        while (iterator.hasNext()) {
            final DownloadablePersistentData downloadablePersistentData = iterator.next();
            final File fileObjectForDownloadable = OfflinePathUtils.getFileObjectForDownloadable(this.mDirPathOfPlayable, downloadablePersistentData.mDownloadableId, DownloadableType.Audio);
            if (!fileObjectForDownloadable.exists()) {
                downloadablePersistentData.mIsComplete = false;
                b = false;
            }
            else {
                if (!this.isIncompleteDownloadableCompletedByFileSize(downloadablePersistentData, fileObjectForDownloadable)) {
                    continue;
                }
                Log.e("nf_offlinePlayable", "audio downloadable marking complete.");
                downloadablePersistentData.mIsComplete = true;
            }
        }
        for (final DownloadablePersistentData downloadablePersistentData2 : this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList) {
            final File fileObjectForDownloadable2 = OfflinePathUtils.getFileObjectForDownloadable(this.mDirPathOfPlayable, downloadablePersistentData2.mDownloadableId, DownloadableType.Video);
            if (!fileObjectForDownloadable2.exists()) {
                downloadablePersistentData2.mIsComplete = false;
                b = false;
            }
            else {
                if (!this.isIncompleteDownloadableCompletedByFileSize(downloadablePersistentData2, fileObjectForDownloadable2)) {
                    continue;
                }
                Log.e("nf_offlinePlayable", "video downloadable marking complete.");
                downloadablePersistentData2.mIsComplete = true;
            }
        }
        for (final DownloadablePersistentData downloadablePersistentData3 : this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList) {
            final File fileObjectForDownloadable3 = OfflinePathUtils.getFileObjectForDownloadable(this.mDirPathOfPlayable, downloadablePersistentData3.mDownloadableId, DownloadableType.Subtitle);
            if (!fileObjectForDownloadable3.exists()) {
                downloadablePersistentData3.mIsComplete = false;
                b = false;
            }
            else {
                if (!this.isIncompleteDownloadableCompletedByFileSize(downloadablePersistentData3, fileObjectForDownloadable3)) {
                    continue;
                }
                Log.e("nf_offlinePlayable", "subtitle downloadable marking complete.");
                downloadablePersistentData3.mIsComplete = true;
            }
        }
        for (final DownloadablePersistentData downloadablePersistentData4 : this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList) {
            final File fileObjectForDownloadable4 = OfflinePathUtils.getFileObjectForDownloadable(this.mDirPathOfPlayable, downloadablePersistentData4.mDownloadableId, DownloadableType.TrickPlay);
            if (!fileObjectForDownloadable4.exists()) {
                downloadablePersistentData4.mIsComplete = false;
                b = false;
            }
            else {
                if (!this.isIncompleteDownloadableCompletedByFileSize(downloadablePersistentData4, fileObjectForDownloadable4)) {
                    continue;
                }
                Log.e("nf_offlinePlayable", "trickplay downloadable marking complete.");
                downloadablePersistentData4.mIsComplete = true;
            }
        }
        return b;
    }
    
    private CdnUrlDownloader createCdnUrlDownloader(final DownloadablePersistentData downloadablePersistentData, final DownloadableInfo downloadableInfo, final String s) {
        return new CdnUrlDownloader(this.mContext, this.mBackgroundThread.getLooper(), downloadablePersistentData, downloadableInfo, this.getProgressInfoForDownloadable(downloadablePersistentData, downloadableInfo.getDownloadableType()), OfflinePathUtils.getFileObjectForDownloadable(this.mDirPathOfPlayable, downloadablePersistentData.mDownloadableId, downloadableInfo.getDownloadableType()), this.mRequestQueue, CommonCdnLogBlobData.create(this.mOfflinePlayablePersistentData, downloadableInfo, s), this.mClientLogging, this);
    }
    
    private boolean createCdnUrlDownloadersFromUpdatedManifest(final NfManifest nfManifest) {
        Log.i("nf_offlinePlayable", "createCdnUrlDownloadersFromUpdatedManifest");
        final List<AudioDownloadableInfo> selectAudioDownloadables = DownloadableSelector.selectAudioDownloadables(nfManifest, OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList));
        final List<VideoDownloadableInfo> selectVideoDownloadables = DownloadableSelector.selectVideoDownloadables(nfManifest, OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList));
        final List<SubtitleDownloadableInfo> selectSubtitleDownloadables = DownloadableSelector.selectSubtitleDownloadables(nfManifest, OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList));
        final List<TrickPlayDownloadableInfo> selectTrickPlayDownloadables = DownloadableSelector.selectTrickPlayDownloadables(nfManifest, OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList));
        if (!OfflineUtils.areAllDownloadablesStillFound(this.mOfflinePlayablePersistentData, selectAudioDownloadables, selectVideoDownloadables, selectSubtitleDownloadables, selectTrickPlayDownloadables)) {
            return false;
        }
        this.mCdnUrlDownloaderList.clear();
        final String playbackContextId = nfManifest.getPlaybackContextId();
        final Iterator<DownloadablePersistentData> iterator = this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList.iterator();
        while (iterator.hasNext()) {
            this.buildCdnUrlDownloaderAndAddToList(selectAudioDownloadables, iterator.next(), this.mCdnUrlDownloaderList, playbackContextId);
        }
        final Iterator<DownloadablePersistentData> iterator2 = this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList.iterator();
        while (iterator2.hasNext()) {
            this.buildCdnUrlDownloaderAndAddToList(selectVideoDownloadables, iterator2.next(), this.mCdnUrlDownloaderList, playbackContextId);
        }
        final Iterator<DownloadablePersistentData> iterator3 = this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList.iterator();
        while (iterator3.hasNext()) {
            this.buildCdnUrlDownloaderAndAddToList(selectSubtitleDownloadables, iterator3.next(), this.mCdnUrlDownloaderList, playbackContextId);
        }
        final Iterator<DownloadablePersistentData> iterator4 = this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList.iterator();
        while (iterator4.hasNext()) {
            this.buildCdnUrlDownloaderAndAddToList(selectTrickPlayDownloadables, iterator4.next(), this.mCdnUrlDownloaderList, playbackContextId);
        }
        this.buildDownloadableProgressInfoMap();
        return true;
    }
    
    private Status createFreshCdnUrlDownloadersFromManifest(final NfManifest nfManifest) {
        Log.i("nf_offlinePlayable", "createFreshCdnUrlDownloadersFromManifest");
        final List<AudioDownloadableInfo> selectAudioDownloadables = DownloadableSelector.selectAudioDownloadables(nfManifest, null);
        final List<VideoDownloadableInfo> selectVideoDownloadables = DownloadableSelector.selectVideoDownloadables(nfManifest, null);
        final List<SubtitleDownloadableInfo> selectSubtitleDownloadables = DownloadableSelector.selectSubtitleDownloadables(nfManifest, null);
        final List<TrickPlayDownloadableInfo> selectTrickPlayDownloadables = DownloadableSelector.selectTrickPlayDownloadables(nfManifest, null);
        this.mCdnUrlDownloaderList.clear();
        final String playbackContextId = nfManifest.getPlaybackContextId();
        this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList.clear();
        final Iterator<AudioDownloadableInfo> iterator = selectAudioDownloadables.iterator();
        while (iterator.hasNext()) {
            this.buildUrlDownloaderAndPersistentDataAndAddToList(iterator.next(), this.mCdnUrlDownloaderList, this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList, playbackContextId);
        }
        this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList.clear();
        final Iterator<VideoDownloadableInfo> iterator2 = selectVideoDownloadables.iterator();
        while (iterator2.hasNext()) {
            this.buildUrlDownloaderAndPersistentDataAndAddToList(iterator2.next(), this.mCdnUrlDownloaderList, this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList, playbackContextId);
        }
        this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList.clear();
        final Iterator<SubtitleDownloadableInfo> iterator3 = selectSubtitleDownloadables.iterator();
        while (iterator3.hasNext()) {
            this.buildUrlDownloaderAndPersistentDataAndAddToList(iterator3.next(), this.mCdnUrlDownloaderList, this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList, playbackContextId);
        }
        this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList.clear();
        final Iterator<TrickPlayDownloadableInfo> iterator4 = selectTrickPlayDownloadables.iterator();
        while (iterator4.hasNext()) {
            this.buildUrlDownloaderAndPersistentDataAndAddToList(iterator4.next(), this.mCdnUrlDownloaderList, this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList, playbackContextId);
        }
        this.buildDownloadableProgressInfoMap();
        return this.hasAtLeastOneTrackForDownload();
    }
    
    private boolean createPlayableDirectoryIfRequired() {
        return FileUtils.createDirectoryIfRequired(this.mDirPathOfPlayable);
    }
    
    public static void deleteLicense(final OfflineLicenseManager offlineLicenseManager, final OfflinePlayablePersistentData offlinePlayablePersistentData, final OfflinePlayable$PlayableDeleteCompleteCallBack offlinePlayable$PlayableDeleteCompleteCallBack) {
        final byte[] keySetIdOrNull = OfflineUtils.getKeySetIdOrNull(offlinePlayablePersistentData);
        if (keySetIdOrNull == null || keySetIdOrNull.length == 0) {
            if (offlinePlayable$PlayableDeleteCompleteCallBack != null) {
                offlinePlayable$PlayableDeleteCompleteCallBack.onDeleteCompleted(offlinePlayablePersistentData);
            }
            return;
        }
        offlineLicenseManager.deleteLicense(offlinePlayablePersistentData.mPlayableId, keySetIdOrNull, offlinePlayablePersistentData.getDlStateBeforeDelete() == DownloadState.Complete, offlinePlayablePersistentData.mLicenseData.mLinkDeactivate, new OfflinePlayableImpl$12(offlinePlayable$PlayableDeleteCompleteCallBack, offlinePlayablePersistentData));
    }
    
    private Status doDeleteDownload() {
        ThreadUtils.assertNotOnMain();
        this.doStopDownload();
        this.mOfflineManifestManager.notifyDeletingPlayable(this.getPlayableId());
        this.mOfflinePlayablePersistentData.setDownloadStateDeleted();
        if (this.canRefreshOrDeleteLicense()) {
            deleteLicense(this.mOfflineLicenseManager, this.mOfflinePlayablePersistentData, null);
        }
        if (OfflineUtils.deletePlayableDirectory(this.mDirPathOfPlayable)) {
            return CommonStatus.OK;
        }
        LogUtils.reportErrorSafely("deletePlayableDirectory failed:" + this.mDirPathOfPlayable, null);
        return new NetflixStatus(StatusCode.DL_CANT_DELETE_PLAYABLE_DIRECTORY);
    }
    
    private void doStopDownload() {
        ThreadUtils.assertNotOnMain();
        final Iterator<CdnUrlDownloader> iterator = this.mCdnUrlDownloaderList.iterator();
        while (iterator.hasNext()) {
            iterator.next().stopDownload();
        }
        this.mBackGroundMessageHandler.removeCallbacksAndMessages((Object)null);
    }
    
    private boolean ensureEnoughDiskSpace() {
        final long n = this.getTotalEstimatedSpace() - this.getCurrentEstimatedSpace() + 10000000L;
        final long freeSpaceOnFileSystem = AndroidUtils.getFreeSpaceOnFileSystem(this.mDirPathOfPlayableFileObject);
        if (n > freeSpaceOnFileSystem) {
            if (Log.isLoggable()) {
                Log.e("nf_offlinePlayable", "ensureEnoughDiskSpace freeSpaceNeeded=" + n + " freeSpaceOnFileSystem=" + freeSpaceOnFileSystem);
            }
            return false;
        }
        return true;
    }
    
    private void fetchFreshLicenseOnRefreshFailure(final NfManifest nfManifest, final OfflineAgentInterface$PlayableRefreshLicenseCallBack offlineAgentInterface$PlayableRefreshLicenseCallBack) {
        Log.d("nf_offlinePlayable", "Fetching fresh license on refresh failure");
        this.mOfflineLicenseManager.requestNewLicense(this.getPlayableId(), nfManifest.getDrmHeader(), nfManifest.getLicenseLinkJson().toString(), new OfflinePlayableImpl$11(this, offlineAgentInterface$PlayableRefreshLicenseCallBack));
    }
    
    private DownloadableInfo getDownloadInfo(final List<? extends DownloadableInfo> list, final String s) {
        for (final DownloadableInfo downloadableInfo : list) {
            if (downloadableInfo.getDownloadableId().equals(s)) {
                return downloadableInfo;
            }
        }
        return null;
    }
    
    private DownloadableProgressInfo getProgressInfoForDownloadable(final DownloadablePersistentData downloadablePersistentData, final DownloadableType downloadableType) {
        DownloadableProgressInfo downloadableProgressInfo;
        if ((downloadableProgressInfo = this.mPlayableProgressInfo.mDownloadableProgressInfoMap.get(downloadablePersistentData.mDownloadableId)) == null) {
            downloadableProgressInfo = new DownloadableProgressInfo();
        }
        downloadableProgressInfo.mBytesOnTheDisk = OfflinePathUtils.getFileObjectForDownloadable(this.mDirPathOfPlayable, downloadablePersistentData.mDownloadableId, downloadableType).length();
        if (downloadablePersistentData.mIsComplete) {
            downloadableProgressInfo.mTotalBytesToDownload = downloadableProgressInfo.mBytesOnTheDisk;
            if (Log.isLoggable()) {
                Log.e("nf_offlinePlayable", "getProgressInfoForDownloadable complete downloadableId=" + downloadablePersistentData.mDownloadableId + " mBytesOnTheDisk=" + downloadableProgressInfo.mBytesOnTheDisk + " mTotalBytesToDownload=" + downloadableProgressInfo.mTotalBytesToDownload);
            }
        }
        else {
            downloadableProgressInfo.mTotalBytesToDownload = downloadablePersistentData.mSizeOfDownloadable;
            if (Log.isLoggable()) {
                Log.e("nf_offlinePlayable", "getProgressInfoForDownloadable notComplete downloadableId " + downloadablePersistentData.mDownloadableId + " mBytesOnTheDisk=" + downloadableProgressInfo.mBytesOnTheDisk + " mTotalBytesToDownload=" + downloadableProgressInfo.mTotalBytesToDownload);
            }
        }
        this.mPlayableProgressInfo.mDownloadableProgressInfoMap.put(downloadablePersistentData.mDownloadableId, downloadableProgressInfo);
        return downloadableProgressInfo;
    }
    
    private void handleCdnUrlDownloadSessionEnd(final CdnUrlDownloader cdnUrlDownloader) {
        this.updateActiveAndCompleteDownloadableCount();
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", " completeTrackCount=" + this.mCompletedDownloadableCount + " activeTrackCount=" + this.mActiveDownloadableCount + " downloadableId=" + cdnUrlDownloader.getDownloadableId());
        }
        if (this.mCompletedDownloadableCount == this.mCdnUrlDownloaderList.size()) {
            Log.i("nf_offlinePlayable", "all tracks downloaded");
            this.handleDownloadComplete();
            this.mOfflinePlayablePersistentData.setDownloadStateComplete();
            this.mOfflineNrdpLogger.onDownloadFinished(this.mOfflinePlayablePersistentData);
            this.mPlayableProgressInfo.markComplete();
            this.mOfflinePlayableListener.onDownloadCompletedSuccess(this);
        }
        else if (this.mActiveDownloadableCount > 0) {
            Log.i("nf_offlinePlayable", "still waiting for other tracks to download");
        }
        else if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "completeTrackCount=" + this.mCompletedDownloadableCount + " activeTrackCount=" + this.mActiveDownloadableCount);
        }
        this.mOfflinePlayableListener.requestSaveToRegistry();
        PlayabilityEnforcer.updateLastContactNetflix(this.mContext);
    }
    
    private void handleCdnUrlExpiredOrMoved(final Status status) {
        if (Log.isLoggable()) {
            Log.e("nf_offlinePlayable", "handleCdnUrlExpiredOrMoved status=" + status);
        }
        this.mOfflineNrdpLogger.onCdnUrlExpiredOrMoved(this.mOfflinePlayablePersistentData, status);
        this.doStopDownload();
        this.refreshManifestFromServerAndContinue();
    }
    
    private void handleCdnUrlGeoCheckError(final Status status) {
        if (Log.isLoggable()) {
            Log.e("nf_offlinePlayable", "handleCdnUrlGeoCheckError status=" + status);
        }
        this.mOfflineNrdpLogger.onCdnUrlGeoCheckError(this.mOfflinePlayablePersistentData, status);
        this.stopAndSendNetworkError(status, StopReason.GeoCheckError);
    }
    
    private void handleDownloadComplete() {
        this.mOfflineLicenseManager.downloadCompleteAndActivateLicense(this.getPlayableId(), this.mOfflinePlayablePersistentData.mLicenseData.mLinkDownloadAndActivate, new OfflinePlayableImpl$8(this));
        this.setLicenseActivating(true);
    }
    
    private void handleFirstTimeClearContent() {
        this.mOfflinePlayablePersistentData.setDownloadStateStopped(StopReason.WaitingToBeStarted);
        this.mOfflinePlayableListener.onInitialized(this, CommonStatus.OK);
    }
    
    private void handleFirstTimeLicenseReceived(final String s, final OfflineLicenseResponse offlineLicenseResponse, final Status persistentError) {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "handleFirstTimeLicenseReceived playable=" + s + " status=" + persistentError);
        }
        if (persistentError.isSucces()) {
            this.mOfflinePlayablePersistentData.setDownloadStateStopped(StopReason.WaitingToBeStarted);
            offlineLicenseResponse.populateLicenseData(this.mOfflinePlayablePersistentData.mLicenseData);
            this.mOfflinePlayablePersistentData.resetPersistentError();
            this.mOfflineNrdpLogger.onFirstTimeLicenseReceived(this.mOfflinePlayablePersistentData);
        }
        else {
            this.mOfflineNrdpLogger.onFirstTimeLicenseError(this.mOfflinePlayablePersistentData, persistentError);
            this.mOfflinePlayablePersistentData.setPersistentError(persistentError);
        }
        this.mOfflinePlayableListener.onInitialized(this, persistentError);
    }
    
    private void handleFirstTimeManifestReceived(final NfManifest nfManifest, Status status) {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "handleFirstTimeManifestReceived status=" + status);
        }
        if (status.isSucces()) {
            final Status freshCdnUrlDownloadersFromManifest = this.createFreshCdnUrlDownloadersFromManifest(nfManifest);
            if (freshCdnUrlDownloadersFromManifest.isSucces()) {
                this.mOfflineNrdpLogger.onFirstTimeManifestReceived(this.mOfflinePlayablePersistentData);
                if (this.ensureEnoughDiskSpace()) {
                    this.setManifestNetwork();
                    if (nfManifest.hasDrm()) {
                        this.mOfflineLicenseManager.requestNewLicense(this.getPlayableId(), nfManifest.getDrmHeader(), nfManifest.getLicenseLinkJson().toString(), new OfflinePlayableImpl$1(this));
                        return;
                    }
                    this.handleFirstTimeClearContent();
                    return;
                }
                else {
                    status = new NetflixStatus(StatusCode.DL_NOT_ENOUGH_FREE_SPACE);
                    Log.e("nf_offlinePlayable", "handleFirstTimeManifestReceived not enough space");
                }
            }
            else {
                status = freshCdnUrlDownloadersFromManifest;
                if (Log.isLoggable()) {
                    Log.e("nf_offlinePlayable", "handleFirstTimeManifestReceived failed to create downloaders status=" + freshCdnUrlDownloadersFromManifest);
                    status = freshCdnUrlDownloadersFromManifest;
                }
            }
        }
        else {
            if (Log.isLoggable()) {
                Log.e("nf_offlinePlayable", "handleFirstTimeManifestReceived failed to get initial manifest status=" + status);
            }
            this.mOfflineNrdpLogger.onFirstTimeManifestFailed(this.mOfflinePlayablePersistentData, status);
        }
        Log.e("nf_offlinePlayable", "handleFirstTimeManifestReceived deleting downloads status=" + this.doDeleteDownload());
        this.mOfflinePlayableListener.onInitialized(this, status);
    }
    
    private void handleManifestForPlayback(final NfManifest nfManifest, final Status status, final OfflinePlayable$PlayableManifestCallBack offlinePlayable$PlayableManifestCallBack) {
        OfflinePlayableManifestImpl offlinePlayableManifestImpl = null;
        Status status2 = status;
        if (status.isSucces()) {
            offlinePlayableManifestImpl = new OfflinePlayableManifestImpl(this.mContext, this.mDirPathOfPlayable, nfManifest, OfflineUtils.getKeySetIdOrNull(this.mOfflinePlayablePersistentData), OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mAudioDownloadablePersistentList), OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mVideoDownloadablePersistentList), OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mSubtitleDownloadablePersistentList), OfflineUtils.getDownloadableList(this.mOfflinePlayablePersistentData.mTrickPlayDownloadablePersistentList), this.mOfflinePlayablePersistentData.mOxId, this.mOfflinePlayablePersistentData.mDxId, DownloadContext.createDownloadContext(this.mOfflinePlayablePersistentData));
            status2 = status;
            if (offlinePlayableManifestImpl.getMpd() == null) {
                offlinePlayableManifestImpl = null;
                status2 = new NetflixStatus(StatusCode.DL_MANIFEST_DATA_MISSING);
            }
        }
        offlinePlayable$PlayableManifestCallBack.onPlayableManifestReady(offlinePlayableManifestImpl, status2);
    }
    
    private void handleManifestRefreshedFromServer(final NfManifest nfManifest, final Status persistentError) {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "handleManifestRefreshedFromServer res=" + persistentError);
        }
        if (persistentError.isSucces()) {
            this.setManifestNetwork();
            this.handleManifestUpdated(nfManifest);
            return;
        }
        this.stopAndSendNetworkError(persistentError, StopReason.ManifestError);
        this.mOfflinePlayablePersistentData.setPersistentError(persistentError);
    }
    
    private void handleManifestUpdated(final NfManifest nfManifest) {
        if (!this.createCdnUrlDownloadersFromUpdatedManifest(nfManifest)) {
            this.stopAndSendEncodesNotAvailableError();
            return;
        }
        if (this.ensureEnoughDiskSpace()) {
            this.startCdnUrlDownloaders();
            this.mOfflinePlayableListener.requestSaveToRegistry();
            return;
        }
        this.stopAndSendStorageError(new NetflixStatus(StatusCode.DL_NOT_ENOUGH_FREE_SPACE), StopReason.NotEnoughSpace);
    }
    
    private void handleNetworkError(final CdnUrlDownloader cdnUrlDownloader, final Status status) {
        this.mOfflineNrdpLogger.onCdnUrlNetworkError(this.mOfflinePlayablePersistentData, status);
        final StopReason networkError = StopReason.NetworkError;
        this.updateActiveAndCompleteDownloadableCount();
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "handleNetworkError status=" + status + " playableId=" + this.getPlayableId() + " downloadableId=" + cdnUrlDownloader.getDownloadableId() + " active=" + this.mActiveDownloadableCount + " complete=" + this.mCompletedDownloadableCount);
        }
        StopReason noNetworkConnectivity;
        if (ConnectivityUtils.isConnectedOrConnecting(this.mContext)) {
            Log.i("nf_offlinePlayable", "handleNetworkError networkConnected");
            noNetworkConnectivity = networkError;
            if (this.mActiveDownloadableCount > 0) {
                if (Log.isLoggable()) {
                    Log.i("nf_offlinePlayable", "handleNetworkError networkConnected, waiting for mActiveDownloadableCount=" + this.mActiveDownloadableCount);
                }
                return;
            }
        }
        else {
            noNetworkConnectivity = StopReason.NoNetworkConnectivity;
            Log.i("nf_offlinePlayable", "handleNetworkError noNetwork");
        }
        this.stopAndSendNetworkError(status, noNetworkConnectivity);
    }
    
    private void handleRefreshLicenseResponse(final OfflineLicenseResponse offlineLicenseResponse, final Status persistentError, final OfflineAgentInterface$PlayableRefreshLicenseCallBack offlineAgentInterface$PlayableRefreshLicenseCallBack) {
        if (persistentError.isSucces()) {
            offlineLicenseResponse.populateLicenseData(this.mOfflinePlayablePersistentData.mLicenseData);
            this.mOfflinePlayablePersistentData.resetPersistentError();
        }
        else {
            Log.i("nf_offlinePlayable", "refreshLicense failed %s", persistentError);
            if (PlayabilityEnforcer.isLicenseExpired(this.mOfflinePlayablePersistentData.mLicenseData)) {
                this.mOfflinePlayablePersistentData.setPersistentError(persistentError);
            }
        }
        this.setLicenseRefreshing(false);
        this.mOfflinePlayableListener.requestSaveToRegistry();
        if (offlineAgentInterface$PlayableRefreshLicenseCallBack != null) {
            offlineAgentInterface$PlayableRefreshLicenseCallBack.onLicenseRefreshDone(persistentError);
        }
    }
    
    private void handleUrlDownloadDiskIOError() {
        this.mOfflineNrdpLogger.onUrlDownloadDiskIOError(this.mOfflinePlayablePersistentData);
        this.stopAndSendStorageError(new NetflixStatus(StatusCode.DL_URL_DOWNLOAD_DISK_IO_ERROR), StopReason.StorageError);
    }
    
    private Status hasAtLeastOneTrackForDownload() {
        if (this.mCdnUrlDownloaderList.size() > 0) {
            return CommonStatus.OK;
        }
        return new NetflixStatus(StatusCode.DL_MANIFEST_NO_TRACKS_TO_DOWNLOAD);
    }
    
    private boolean hasManifestNetworkChanged() {
        return false;
    }
    
    private boolean isIncompleteDownloadableCompletedByFileSize(final DownloadablePersistentData downloadablePersistentData, final File file) {
        return !downloadablePersistentData.mIsComplete && file.length() > 0L && file.length() == downloadablePersistentData.mSizeOfDownloadable;
    }
    
    private void onProgressWatchRunnable() {
        switch (OfflinePlayableImpl$13.$SwitchMap$com$netflix$mediaclient$servicemgr$interface_$offline$DownloadState[this.mOfflinePlayablePersistentData.getDownloadState().ordinal()]) {
            case 1: {
                this.mPlayableProgressInfo.updateProgressPercentage();
                this.scheduleNextProgressWatch();
                this.mOfflinePlayableListener.onProgress(this);
                break;
            }
        }
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "ProgressWatchRunnable playableId=" + this.getPlayableId() + " mPlayablePercentageProgress=" + this.mPlayableProgressInfo.getPercentageDownloaded());
        }
    }
    
    private void refreshLicense(final IBladeRunnerClient$OfflineRefreshInvoke bladeRunnerClient$OfflineRefreshInvoke, final NfManifest nfManifest, final OfflineAgentInterface$PlayableRefreshLicenseCallBack offlineAgentInterface$PlayableRefreshLicenseCallBack) {
        if (!this.canRefreshOrDeleteLicense() || !PlayabilityEnforcer.shouldRefreshLicense(this.mOfflinePlayablePersistentData.mLicenseData)) {
            if (offlineAgentInterface$PlayableRefreshLicenseCallBack != null) {
                offlineAgentInterface$PlayableRefreshLicenseCallBack.onLicenseRefreshDone(CommonStatus.OK);
            }
            return;
        }
        Log.d("nf_offlinePlayable", "refreshing license for %s", this.getPlayableId());
        this.setLicenseRefreshing(true);
        this.mOfflineLicenseManager.refreshLicense(bladeRunnerClient$OfflineRefreshInvoke, this.getPlayableId(), nfManifest.getDrmHeader(), OfflineUtils.getKeySetIdOrNull(this.mOfflinePlayablePersistentData), this.mOfflinePlayablePersistentData.mLicenseData.mLinkRefresh, new OfflinePlayableImpl$10(this, nfManifest, offlineAgentInterface$PlayableRefreshLicenseCallBack));
    }
    
    private void refreshManifestFromServerAndContinue() {
        Log.i("nf_offlinePlayable", "refreshManifestFromServerAndContinue");
        this.mOfflineNrdpLogger.onRequestingNewManifestFromServer(this.mOfflinePlayablePersistentData);
        this.mOfflineManifestManager.requestOfflineManifestRefreshFromServer(this.getPlayableId(), this.getOxId(), this.getDxId(), this.mDirPathOfPlayable, DownloadVideoQuality.create(this.mOfflinePlayablePersistentData.getDownloadVideoQuality()), new OfflinePlayableImpl$7(this));
    }
    
    private void removeProgressWatcher() {
        this.mBackGroundMessageHandler.removeCallbacks((Runnable)this.mProgressWatchRunnable);
    }
    
    private void scheduleFirstProgressWatch() {
        this.mBackGroundMessageHandler.postDelayed((Runnable)this.mProgressWatchRunnable, 0L);
    }
    
    private void scheduleNextProgressWatch() {
        this.mBackGroundMessageHandler.postDelayed((Runnable)this.mProgressWatchRunnable, 2000L);
    }
    
    private void sendMessageToHandler(final int n, final Object o) {
        if (this.mBackGroundMessageHandler != null) {
            this.mBackGroundMessageHandler.obtainMessage(n, o).sendToTarget();
            return;
        }
        Log.e("nf_offlinePlayable", "sendMessageToHandler after handler is gone");
    }
    
    private void sendNetworkError(final Status status, final StopReason downloadStateStopped) {
        if (this.getDownloadState() == DownloadState.Stopped) {
            Log.i("nf_offlinePlayable", "sendNetworkError already in stopped state.");
            return;
        }
        Log.i("nf_offlinePlayable", "sendNetworkError sending error to mOfflinePlayableListener.");
        this.mOfflinePlayablePersistentData.setDownloadStateStopped(downloadStateStopped);
        this.mOfflinePlayableListener.onNetworkError(this, status);
    }
    
    private void sendStorageError(Status status, StopReason notEnoughSpace) {
        if (10000000L > AndroidUtils.getFreeSpaceOnFileSystem(this.mDirPathOfPlayableFileObject)) {
            Log.e("nf_offlinePlayable", "sendStorageError overriding error to not enough space");
            status = new NetflixStatus(StatusCode.DL_NOT_ENOUGH_FREE_SPACE);
            notEnoughSpace = StopReason.NotEnoughSpace;
        }
        if (this.getDownloadState() == DownloadState.Stopped) {
            this.mOfflinePlayablePersistentData.setDownloadStateStopped(notEnoughSpace);
            Log.i("nf_offlinePlayable", "sendStorageError already in stopped state.");
            return;
        }
        this.mOfflinePlayablePersistentData.setDownloadStateStopped(notEnoughSpace);
        this.mOfflinePlayableListener.onStorageError(this, status);
    }
    
    private void setLicenseActivating(final boolean mLicenseActivating) {
        this.mLicenseActivating = mLicenseActivating;
    }
    
    private void setLicenseRefreshing(final boolean mLicenseRefreshing) {
        this.mLicenseRefreshing = mLicenseRefreshing;
    }
    
    private void setManifestNetwork() {
        final int activeNetworkTypeOrMinusOne = ConnectivityUtils.getActiveNetworkTypeOrMinusOne(this.mContext);
        this.mOfflinePlayablePersistentData.setManifestNetworkType(activeNetworkTypeOrMinusOne);
        String wiFiSsidOrNetworkOperatorName = "";
        if (activeNetworkTypeOrMinusOne != -1) {
            switch (activeNetworkTypeOrMinusOne) {
                default: {
                    wiFiSsidOrNetworkOperatorName = wiFiSsidOrNetworkOperatorName;
                    break;
                }
                case 1: {
                    wiFiSsidOrNetworkOperatorName = ConnectivityUtils.getSsidOrEmptyString((WifiManager)this.mContext.getSystemService("wifi"));
                    break;
                }
                case 0: {
                    wiFiSsidOrNetworkOperatorName = ConnectivityUtils.getCurrentOperatorNameOrEmptyString((TelephonyManager)this.mContext.getSystemService("phone"));
                    break;
                }
            }
        }
        this.mOfflinePlayablePersistentData.setWiFiSsidOrNetworkOperatorName(wiFiSsidOrNetworkOperatorName);
    }
    
    private void startCdnUrlDownloaders() {
        Log.i("nf_offlinePlayable", "startCdnUrlDownloaders");
        final Iterator<CdnUrlDownloader> iterator = this.mCdnUrlDownloaderList.iterator();
        boolean b = false;
        while (iterator.hasNext()) {
            final CdnUrlDownloader cdnUrlDownloader = iterator.next();
            if (!cdnUrlDownloader.isDownloadComplete()) {
                cdnUrlDownloader.startDownload();
                b = true;
            }
            else {
                Log.i("nf_offlinePlayable", "download was complete downloadableId=" + cdnUrlDownloader.getDownloadableId());
            }
        }
        if (b) {
            this.removeProgressWatcher();
            this.mOfflinePlayablePersistentData.setDownloadStateInProgress();
            this.scheduleFirstProgressWatch();
            return;
        }
        Log.e("nf_offlinePlayable", "startCdnUrlDownloaders not running progress watcher.");
    }
    
    private void stopAndSendEncodesNotAvailableError() {
        this.doStopDownload();
        this.mOfflinePlayablePersistentData.setDownloadStateStopped(StopReason.EncodesAreNotAvailableAnyMore);
        this.mOfflinePlayableListener.onUnRecoverableError(this, new NetflixStatus(StatusCode.DL_ENCODES_ARE_NOT_AVAILABLE));
    }
    
    private void stopAndSendNetworkError(final Status status, final StopReason stopReason) {
        this.doStopDownload();
        this.sendNetworkError(status, stopReason);
    }
    
    private void stopAndSendStorageError(final Status status, final StopReason stopReason) {
        this.doStopDownload();
        this.sendStorageError(status, stopReason);
    }
    
    private void updateActiveAndCompleteDownloadableCount() {
        this.mCompletedDownloadableCount = 0;
        this.mActiveDownloadableCount = 0;
        for (final CdnUrlDownloader cdnUrlDownloader : this.mCdnUrlDownloaderList) {
            if (cdnUrlDownloader.isDownloadComplete()) {
                ++this.mCompletedDownloadableCount;
            }
            if (cdnUrlDownloader.isDownloading()) {
                ++this.mActiveDownloadableCount;
            }
        }
    }
    
    @Override
    public boolean canResumeWithoutUserAction() {
        return this.mOfflinePlayablePersistentData.getStopReason().canResumeWithoutUserAction();
    }
    
    @Override
    public Status deleteDownload() {
        this.mOfflineNrdpLogger.onOfflinePlayableDownloadDelete(this.mOfflinePlayablePersistentData);
        ThreadUtils.assertNotOnMain();
        return this.doDeleteDownload();
    }
    
    @Override
    public void destroy() {
        if (this.mBackGroundMessageHandler != null) {
            this.mBackGroundMessageHandler.removeCallbacksAndMessages((Object)null);
            this.mBackGroundMessageHandler = null;
        }
        this.mOfflineManifestManager.abortAllRequestsForPlayable(this.getPlayableId());
    }
    
    @Override
    public void doMaintenanceWork(final OfflinePlayable$PlayableMaintenanceCallBack offlinePlayable$PlayableMaintenanceCallBack) {
        if (this.mOfflinePlayablePersistentData.mLicenseData.mShouldRefresh) {
            if (Log.isLoggable()) {
                Log.d("nf_offlinePlayable", "attempt auto refresh " + this.getPlayableId());
            }
            this.refreshLicenseIfNeeded(IBladeRunnerClient$OfflineRefreshInvoke.MAINTENANCE, new OfflinePlayableImpl$5(this, offlinePlayable$PlayableMaintenanceCallBack));
        }
        else {
            if (Log.isLoggable()) {
                Log.d("nf_offlinePlayable", "does not allow auto refresh " + this.getPlayableId());
            }
            if (offlinePlayable$PlayableMaintenanceCallBack != null) {
                offlinePlayable$PlayableMaintenanceCallBack.onMaintenanceJobDone(this);
            }
        }
    }
    
    @Override
    public long getCurrentEstimatedSpace() {
        if (this.getDownloadState() == DownloadState.Complete) {
            return this.mPlayableProgressInfo.getActualSizeOfPlayableOnDiskInCompleteState(this.mDirPathOfPlayableFileObject);
        }
        return this.mPlayableProgressInfo.getBytesDownloadedSoFar();
    }
    
    @Override
    public long getDownloadContextInitTimeMs() {
        return this.mOfflinePlayablePersistentData.mDownloadContextInitTimeMs;
    }
    
    @Override
    public int getDownloadContextListPos() {
        return this.mOfflinePlayablePersistentData.mDownloadContextListPos;
    }
    
    @Override
    public String getDownloadContextRequestId() {
        return this.mOfflinePlayablePersistentData.mDownloadContextRequestId;
    }
    
    @Override
    public int getDownloadContextTrackId() {
        return this.mOfflinePlayablePersistentData.mDownloadContextTrackId;
    }
    
    @Override
    public int getDownloadContextVideoPos() {
        return this.mOfflinePlayablePersistentData.mDownloadContextVideoPos;
    }
    
    @Override
    public DownloadState getDownloadState() {
        return this.mOfflinePlayablePersistentData.getDownloadState();
    }
    
    @Override
    public String getDxId() {
        return this.mOfflinePlayablePersistentData.mDxId;
    }
    
    @Override
    public long getExpiringInMillis() {
        if (this.mOfflinePlayablePersistentData.mLicenseData.mShouldUsePlayWindowLimits) {
            final long mPlayStartTime = this.mOfflinePlayablePersistentData.mPlayStartTime;
            final long mPlayWindowResetLimit = this.mOfflinePlayablePersistentData.mLicenseData.mPlayWindowResetLimit;
            final long mPlayableWindowInHour = this.mOfflinePlayablePersistentData.mLicenseData.mPlayableWindowInHour;
            if (mPlayStartTime > 0L && mPlayWindowResetLimit <= 0L && mPlayableWindowInHour > 0L) {
                return TimeUnit.HOURS.toMillis(mPlayableWindowInHour) - (System.currentTimeMillis() - mPlayStartTime);
            }
        }
        return -1L;
    }
    
    @Override
    public Status getLastPersistentErrorStatus() {
        if (this.mOfflinePlayablePersistentData.mErrorCode != 0) {
            final NetflixStatus netflixStatus = new NetflixStatus(StatusCode.getStatusCodeByValue(this.mOfflinePlayablePersistentData.mErrorCode));
            if (this.mOfflinePlayablePersistentData.mErrorString != null) {
                netflixStatus.setMessage(this.mOfflinePlayablePersistentData.mErrorString);
                netflixStatus.setDisplayMessage(true);
            }
            return netflixStatus;
        }
        return CommonStatus.OK;
    }
    
    @Override
    public OfflinePlayablePersistentData getOfflineViewablePersistentData() {
        return this.mOfflinePlayablePersistentData;
    }
    
    @Override
    public String getOxId() {
        return this.mOfflinePlayablePersistentData.mOxId;
    }
    
    @Override
    public int getPercentageDownloaded() {
        return this.mPlayableProgressInfo.getPercentageDownloaded();
    }
    
    @Override
    public String getPlayableId() {
        return this.mOfflinePlayablePersistentData.mPlayableId;
    }
    
    @Override
    public String getProfileGuidOfDownloadRequester() {
        if (this.mOfflinePlayablePersistentData.getProfileGuidList() != null && this.mOfflinePlayablePersistentData.getProfileGuidList().size() > 0) {
            return this.mOfflinePlayablePersistentData.getProfileGuidList().get(0);
        }
        Log.e("nf_offlinePlayable", "getProfileGuidOfDownloadRequester error");
        return "";
    }
    
    @Override
    public StopReason getStopReason() {
        return this.mOfflinePlayablePersistentData.getStopReason();
    }
    
    @Override
    public long getTotalEstimatedSpace() {
        if (this.getDownloadState() == DownloadState.Complete) {
            return this.mPlayableProgressInfo.getActualSizeOfPlayableOnDiskInCompleteState(this.mDirPathOfPlayableFileObject);
        }
        return this.mPlayableProgressInfo.getTotalBytesToDownload();
    }
    
    @Override
    public WatchState getWatchState() {
        if (this.mOfflinePlayablePersistentData.getDownloadState().equals(DownloadState.Complete)) {
            WatchState geo_BLOCKED;
            if (this.mOfflinePlayablePersistentData.isGeoBlocked() && ConnectivityUtils.isConnected(this.mContext)) {
                geo_BLOCKED = WatchState.GEO_BLOCKED;
            }
            else {
                final WatchState watching_ALLOWED = WatchState.WATCHING_ALLOWED;
                final OfflinePlayablePersistentData$LicenseData mLicenseData = this.mOfflinePlayablePersistentData.mLicenseData;
                geo_BLOCKED = watching_ALLOWED;
                if (mLicenseData != null) {
                    geo_BLOCKED = watching_ALLOWED;
                    if (mLicenseData.mKeySetId != null) {
                        WatchState watchState;
                        if (!PlayabilityEnforcer.isAllowedByViewWindow(mLicenseData)) {
                            watchState = WatchState.VIEW_WINDOW_EXPIRED;
                        }
                        else if (PlayabilityEnforcer.isLicenseExpired(mLicenseData)) {
                            watchState = WatchState.LICENSE_EXPIRED;
                        }
                        else {
                            final long mPlayWindowResetLimit = mLicenseData.mPlayWindowResetLimit;
                            if (!PlayabilityEnforcer.isAllowedByPlayWindow(this.mContext, this.mOfflinePlayablePersistentData, mLicenseData)) {
                                if (mLicenseData.mPwResettable && mLicenseData.mPlayWindowResetLimit > 0L) {
                                    watchState = WatchState.PLAY_WINDOW_EXPIRED_BUT_RENEWABLE;
                                }
                                else {
                                    watchState = WatchState.PLAY_WINDOW_EXPIRED_FINAL;
                                }
                            }
                            else {
                                final long mPlayWindowResetLimit2 = mLicenseData.mPlayWindowResetLimit;
                                watchState = watching_ALLOWED;
                                if (this.mOfflinePlayablePersistentData.mLicenseData.mShouldUsePlayWindowLimits) {
                                    watchState = watching_ALLOWED;
                                    if (mPlayWindowResetLimit2 != mPlayWindowResetLimit) {
                                        this.mBackGroundMessageHandler.post((Runnable)new OfflinePlayableImpl$6(this));
                                        watchState = watching_ALLOWED;
                                    }
                                }
                            }
                        }
                        geo_BLOCKED = watchState;
                        if (Log.isLoggable()) {
                            Log.i("nf_offlinePlayable", "new WatchState is " + watchState);
                            return watchState;
                        }
                    }
                }
            }
            return geo_BLOCKED;
        }
        return WatchState.NOT_WATCHABLE_DUE_TO_NOT_ENOUGH_DATA;
    }
    
    @Override
    public void initialize() {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "initialize " + this.mOfflinePlayablePersistentData.mPlayableId);
        }
        ThreadUtils.assertNotOnMain();
        if (!this.createPlayableDirectoryIfRequired()) {
            this.mOfflinePlayableListener.onInitialized(this, new NetflixStatus(StatusCode.DL_CANT_CREATE_VIEWABLE_DIRECTORY));
            return;
        }
        OfflineLogUtils.reportDownloadStart(this.mContext, this.mOfflinePlayablePersistentData.mDxId, false);
        this.mOfflineNrdpLogger.onOfflinePlayableInitialize(this.mOfflinePlayablePersistentData);
        this.mOfflineManifestManager.requestOfflineManifestFromServer(this.mOfflinePlayablePersistentData.mPlayableId, this.mOfflinePlayablePersistentData.mOxId, this.mOfflinePlayablePersistentData.mDxId, DownloadContext.createDownloadContext(this.mOfflinePlayablePersistentData), this.mDirPathOfPlayable, DownloadVideoQuality.create(this.mOfflinePlayablePersistentData.getDownloadVideoQuality()), new OfflinePlayableImpl$2(this));
    }
    
    @Override
    public void notifyOfflinePlayStarted() {
        if (PlayabilityEnforcer.hasRecentHomingAndConnectivity(this.mContext) || this.mOfflinePlayablePersistentData.mPlayStartTime > 0L) {
            return;
        }
        synchronized (this.mOfflinePlayablePersistentData) {
            this.mOfflinePlayablePersistentData.mPlayStartTime = System.currentTimeMillis();
            if (Log.isLoggable()) {
                Log.i("nf_offlinePlayable", "start playwindow " + this.mOfflinePlayablePersistentData.mPlayStartTime);
            }
            // monitorexit(this.mOfflinePlayablePersistentData)
            if (this.mOfflinePlayablePersistentData.mLicenseData.mShouldUsePlayWindowLimits) {
                this.mOfflinePlayableListener.requestSaveToRegistry();
            }
        }
    }
    
    @Override
    public void onCdnUrlDownloadSessionEnd(final CdnUrlDownloader cdnUrlDownloader) {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "onCdnUrlDownloadSessionEnd downloadableId=" + cdnUrlDownloader.getDownloadableId());
        }
        this.sendMessageToHandler(4, new OfflinePlayableImpl$CdnUrlDownloaderResponse(this, CommonStatus.OK, cdnUrlDownloader));
    }
    
    @Override
    public void onCdnUrlExpiredOrMoved(final CdnUrlDownloader cdnUrlDownloader, final Status status) {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "onCdnUrlExpiredOrMoved downloadableId=" + cdnUrlDownloader.getDownloadableId());
        }
        this.sendMessageToHandler(3, new OfflinePlayableImpl$CdnUrlDownloaderResponse(this, status, cdnUrlDownloader));
    }
    
    @Override
    public void onCdnUrlGeoCheckFailure(final CdnUrlDownloader cdnUrlDownloader, final Status status) {
        if (Log.isLoggable()) {
            Log.i("nf_offlinePlayable", "onCdnUrlExpiredOrMoved downloadableId=" + cdnUrlDownloader.getDownloadableId());
        }
        this.sendMessageToHandler(5, new OfflinePlayableImpl$CdnUrlDownloaderResponse(this, status, cdnUrlDownloader));
    }
    
    @Override
    public void onNetworkError(final CdnUrlDownloader cdnUrlDownloader, final Status status) {
        if (Log.isLoggable()) {
            Log.e("nf_offlinePlayable", "onNetworkError statusCode=" + status);
        }
        this.sendMessageToHandler(2, new OfflinePlayableImpl$CdnUrlDownloaderResponse(this, status, cdnUrlDownloader));
    }
    
    @Override
    public void onUrlDownloadDiskIOError(final CdnUrlDownloader cdnUrlDownloader) {
        Log.i("nf_offlinePlayable", "onUrlDownloadDiskIOError");
        this.sendMessageToHandler(1, new OfflinePlayableImpl$CdnUrlDownloaderResponse(this, new NetflixStatus(StatusCode.DL_URL_DOWNLOAD_DISK_IO_ERROR), cdnUrlDownloader));
    }
    
    @Override
    public void refreshLicenseIfNeeded(final IBladeRunnerClient$OfflineRefreshInvoke bladeRunnerClient$OfflineRefreshInvoke, final OfflineAgentInterface$PlayableRefreshLicenseCallBack offlineAgentInterface$PlayableRefreshLicenseCallBack) {
        if (PlayabilityEnforcer.shouldRefreshLicense(this.mOfflinePlayablePersistentData.mLicenseData) && this.canRefreshOrDeleteLicense()) {
            if (Log.isLoggable()) {
                Log.i("nf_offlinePlayable", "refreshLicenseIfNeeded " + this.getPlayableId());
            }
            this.mOfflineManifestManager.requestOfflineManifestFromCache(this.getPlayableId(), this.mDirPathOfPlayable, new OfflinePlayableImpl$9(this, bladeRunnerClient$OfflineRefreshInvoke, offlineAgentInterface$PlayableRefreshLicenseCallBack));
        }
        else {
            Log.i("nf_offlinePlayable", "refreshLicenseIfNeeded not refreshing");
            if (offlineAgentInterface$PlayableRefreshLicenseCallBack != null) {
                offlineAgentInterface$PlayableRefreshLicenseCallBack.onLicenseRefreshDone(CommonStatus.OK);
            }
        }
    }
    
    @Override
    public void requestManifestForPlayback(final OfflinePlayable$PlayableManifestCallBack offlinePlayable$PlayableManifestCallBack) {
        this.mOfflineManifestManager.requestOfflineManifestFromCache(this.getPlayableId(), this.mDirPathOfPlayable, new OfflinePlayableImpl$4(this, offlinePlayable$PlayableManifestCallBack));
    }
    
    @Override
    public void startDownload() {
        ThreadUtils.assertNotOnMain();
        Log.i("nf_offlinePlayable", "startDownload");
        this.mOfflineNrdpLogger.onOfflinePlayableDownloadStart(this.mOfflinePlayablePersistentData);
        if (this.mOfflinePlayablePersistentData.getDownloadState() == DownloadState.Complete) {
            Log.e("nf_offlinePlayable", "Download is already complete. This shouldn't be called.");
            return;
        }
        if (!this.canResumeWithoutUserAction()) {
            Log.e("nf_offlinePlayable", "Download is not resume-able without user action");
            return;
        }
        if (!this.createPlayableDirectoryIfRequired()) {
            this.sendStorageError(new NetflixStatus(StatusCode.DL_CANT_CREATE_VIEWABLE_DIRECTORY), StopReason.StorageError);
            return;
        }
        this.mOfflinePlayablePersistentData.setDownloadStateInProgress();
        this.doStopDownload();
        this.mOfflineManifestManager.requestOfflineManifestFromCache(this.getPlayableId(), this.mDirPathOfPlayable, new OfflinePlayableImpl$3(this));
    }
    
    @Override
    public void stopDownload(final StopReason downloadStateStopped) {
        ThreadUtils.assertNotOnMain();
        this.mOfflineNrdpLogger.onOfflinePlayableDownloadStop(this.mOfflinePlayablePersistentData, downloadStateStopped);
        this.doStopDownload();
        this.mOfflinePlayablePersistentData.setDownloadStateStopped(downloadStateStopped);
    }
}