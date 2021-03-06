// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service;

import com.netflix.mediaclient.ui.verifyplay.PinVerifier$PinType;
import android.app.Notification;
import com.netflix.mediaclient.service.logging.perf.Events;
import android.os.Process;
import com.netflix.mediaclient.service.error.crypto.CryptoErrorManager;
import com.netflix.mediaclient.service.player.OfflinePlaybackInterface;
import com.netflix.mediaclient.service.job.NetflixJobSchedulerSelector;
import com.netflix.mediaclient.util.AndroidManifestUtils;
import com.netflix.mediaclient.service.logging.perf.Sessions;
import com.netflix.mediaclient.service.logging.perf.PerformanceProfiler;
import com.netflix.mediaclient.javabridge.ui.ActivationTokens;
import com.netflix.mediaclient.servicemgr.IVoip;
import com.netflix.mediaclient.service.webclient.model.leafs.UmaAlert;
import com.netflix.mediaclient.service.webclient.model.leafs.ThumbMessaging;
import com.netflix.mediaclient.servicemgr.SignUpParams;
import com.netflix.mediaclient.servicemgr.IPushNotification;
import com.netflix.mediaclient.service.offline.agent.OfflineAgentInterface;
import com.netflix.mediaclient.repository.SecurityRepository;
import com.netflix.mediaclient.servicemgr.NrdpComponent;
import com.netflix.mediaclient.servicemgr.IPlayer;
import com.netflix.mediaclient.servicemgr.IMdx;
import com.netflix.mediaclient.servicemgr.IMSLClient;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.servicemgr.IErrorHandler;
import com.netflix.mediaclient.service.webclient.model.leafs.EogAlert;
import com.netflix.mediaclient.service.configuration.esn.EsnProvider;
import com.netflix.mediaclient.servicemgr.IDiagnosis;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.servicemgr.IBrowseInterface;
import com.netflix.mediaclient.servicemgr.interface_.user.UserProfile;
import java.util.List;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcherCallback;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.model.leafs.OnRampEligibility$Action;
import com.netflix.mediaclient.service.user.UserAgent$UserAgentCallback;
import com.netflix.mediaclient.servicemgr.IMSLClient$NetworkRequestInspector;
import android.os.SystemClock;
import com.netflix.mediaclient.servicemgr.ApplicationPerformanceMetricsLogging$Trigger;
import com.netflix.mediaclient.service.logging.error.ErrorLoggingManager;
import com.netflix.mediaclient.service.pservice.logging.PServiceLogging;
import com.netflix.mediaclient.servicemgr.INetflixServiceCallback;
import java.util.Iterator;
import com.crittercism.app.Crittercism;
import com.netflix.mediaclient.ui.lolomo.PrefetchLolomoABTestUtils;
import android.support.v4.content.LocalBroadcastManager;
import java.io.Serializable;
import android.content.IntentFilter;
import com.netflix.mediaclient.util.ThreadUtils;
import com.netflix.mediaclient.util.AndroidUtils;
import android.app.PendingIntent;
import android.app.AlarmManager;
import java.util.HashSet;
import com.netflix.mediaclient.android.app.CommonStatus;
import java.util.HashMap;
import com.netflix.mediaclient.service.voip.WhistleVoipAgent;
import com.netflix.mediaclient.service.user.UserAgent;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcher;
import com.netflix.mediaclient.service.pushnotification.PushNotificationAgent;
import com.netflix.mediaclient.service.preapp.PreAppAgent;
import java.util.Set;
import com.netflix.mediaclient.service.player.PlayerAgent;
import com.netflix.mediaclient.service.pdslogging.PdsAgent;
import com.netflix.mediaclient.service.player.exoplayback.ExoPlayback;
import com.netflix.mediaclient.service.offline.agent.OfflineAgent;
import com.netflix.mediaclient.service.job.NetflixJobScheduler;
import com.netflix.mediaclient.service.job.NetflixJobExecutor;
import com.netflix.mediaclient.service.job.NetflixJob$NetflixJobId;
import java.util.Map;
import com.netflix.mediaclient.service.msl.MSLAgent;
import com.netflix.mediaclient.service.mdx.MdxAgent;
import com.netflix.mediaclient.android.app.Status;
import java.util.ArrayList;
import com.netflix.mediaclient.service.falkor.FalkorAgent;
import com.netflix.mediaclient.service.falkor.FalkorAccess;
import com.netflix.mediaclient.service.error.ErrorAgent;
import com.netflix.mediaclient.service.diagnostics.DiagnosisAgent;
import com.netflix.mediaclient.service.configuration.ConfigurationAgent;
import com.netflix.mediaclient.service.logging.LoggingAgent;
import com.netflix.mediaclient.media.BookmarkStore;
import android.os.IBinder;
import android.os.Handler;
import com.netflix.mediaclient.servicemgr.INetflixService;
import android.app.Service;
import com.netflix.mediaclient.ui.verifyplay.PinVerifier;
import com.netflix.mediaclient.Log;
import android.content.Intent;
import android.content.Context;
import com.netflix.mediaclient.servicemgr.interface_.Playable;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.Asset;
import android.content.BroadcastReceiver;

class NetflixService$6 extends BroadcastReceiver
{
    final /* synthetic */ NetflixService this$0;
    
    NetflixService$6(final NetflixService this$0) {
        this.this$0 = this$0;
    }
    
    private Asset getMdxAgentVideoAsset() {
        final Asset asset = null;
        Playable playable;
        if (this.this$0.mMdxAgent.getVideoDetail() != null) {
            playable = this.this$0.mMdxAgent.getVideoDetail().getPlayable();
        }
        else {
            playable = null;
        }
        Asset create = asset;
        if (playable != null) {
            create = asset;
            if (StringUtils.isNotEmpty(playable.getPlayableId())) {
                create = Asset.create(playable, PlayContext.EMPTY_CONTEXT, true);
            }
        }
        return create;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        boolean previewProtected = false;
        final String action = intent.getAction();
        if ("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKEND".equals(action)) {
            Log.d("NetflixService", "mdx exit, stop service in %sms", 28800000L);
            this.this$0.stopSelfInMs(28800000L);
            final Asset mdxAgentVideoAsset = this.getMdxAgentVideoAsset();
            boolean pinProtected;
            boolean previewProtected2;
            if (mdxAgentVideoAsset != null) {
                pinProtected = mdxAgentVideoAsset.isPinProtected();
                previewProtected2 = mdxAgentVideoAsset.isPreviewProtected();
            }
            else {
                previewProtected2 = false;
                pinProtected = false;
            }
            PinVerifier.getInstance().registerPlayEvent(pinProtected, previewProtected2);
            if (intent.getBooleanExtra("updateCW", true)) {
                Log.d("NetflixService", "Refreshing CW for MDXUPDATE_PLAYBACKEND...");
                this.this$0.getBrowse().refreshCw(false);
            }
        }
        else if ("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKSTART".equals(action)) {
            if (this.this$0.mMdxAgent == null || !this.this$0.mMdxAgent.hasActiveSession()) {
                Log.e("NetflixService", "false MDXUPDATE_PLAYBACKSTART");
                return;
            }
            Log.i("NetflixService", "start mdx notification");
            this.this$0.cancelPendingSelfStop();
            final Asset mdxAgentVideoAsset2 = this.getMdxAgentVideoAsset();
            if (mdxAgentVideoAsset2 != null) {
                Log.d("NetflixService", "refreshing episodes data on play start");
                this.this$0.getBrowse().refreshEpisodeData(mdxAgentVideoAsset2);
            }
        }
        else if ("com.netflix.mediaclient.intent.action.MDXUPDATE_STATE".equals(action)) {
            final int intExtra = intent.getIntExtra("time", -1);
            Log.v("NetflixService", "on MDX state update - received updated mdx position: " + intExtra);
            final Asset mdxAgentVideoAsset3 = this.getMdxAgentVideoAsset();
            boolean pinProtected2;
            if (mdxAgentVideoAsset3 != null) {
                mdxAgentVideoAsset3.setPlaybackBookmark(intExtra);
                Log.v("NetflixService", "updating cached video position");
                this.this$0.getBrowse().updateCachedVideoPosition(mdxAgentVideoAsset3);
                pinProtected2 = mdxAgentVideoAsset3.isPinProtected();
                previewProtected = mdxAgentVideoAsset3.isPreviewProtected();
            }
            else {
                pinProtected2 = false;
            }
            PinVerifier.getInstance().registerPlayEvent(pinProtected2, previewProtected);
        }
    }
}
