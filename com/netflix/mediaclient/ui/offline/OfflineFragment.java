// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.offline;

import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.android.activity.NetflixActivity$ServiceManagerRunnable;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.servicemgr.interface_.offline.StopReason;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflinePlayableViewData;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.util.AndroidUtils;
import android.content.Intent;
import com.netflix.mediaclient.util.LogUtils;
import com.netflix.mediaclient.servicemgr.interface_.offline.realm.RealmUtils;
import android.content.Context;
import com.netflix.mediaclient.ui.common.PlayContext;
import android.support.v7.widget.RecyclerView$LayoutManager;
import android.view.View$OnClickListener;
import com.netflix.mediaclient.ui.experience.BrowseExperience;
import android.support.v7.widget.RecyclerView$Adapter;
import android.support.v7.widget.RecyclerView$AdapterDataObserver;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflineAdapterData$ViewType;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflinePlayableUiList;
import com.netflix.mediaclient.servicemgr.interface_.offline.realm.RealmVideoDetails;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflineAdapterData;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import com.netflix.mediaclient.service.offline.agent.OfflineAgentInterface;
import android.support.v7.widget.LinearLayoutManager;
import com.netflix.mediaclient.android.widget.NetflixTextButton;
import android.view.View;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;
import com.netflix.mediaclient.service.offline.agent.OfflineAgentListener;
import com.netflix.mediaclient.android.fragment.NetflixFrag;

public class OfflineFragment extends NetflixFrag implements OfflineAgentListener, ManagerStatusListener
{
    private static final String TAG = "OfflineFragment";
    private View mEmptyState;
    private NetflixTextButton mFindSomethingToDownload;
    private LinearLayoutManager mLayoutManager;
    private OfflineAgentInterface mOfflineAgentInterface;
    private OfflineBaseAdapter mOfflinePlayableAdapter;
    private RecyclerView mRecyclerView;
    private boolean mWasInitPerformed;
    private String selectedProfileId;
    private String selectedTitleId;
    
    public static Fragment create() {
        return new OfflineFragment();
    }
    
    static String getFirstEpisodeProfileId(final OfflineAdapterData offlineAdapterData) {
        if (offlineAdapterData.getEpisodes() != null && offlineAdapterData.getEpisodes().length > 0) {
            final RealmVideoDetails[] episodes = offlineAdapterData.getEpisodes();
            for (int length = episodes.length, i = 0; i < length; ++i) {
                final RealmVideoDetails realmVideoDetails = episodes[i];
                if (realmVideoDetails.getType() == VideoType.EPISODE) {
                    return realmVideoDetails.getProfileId();
                }
            }
        }
        return null;
    }
    
    static String getFirstEpisodeProfileId(final OfflinePlayableUiList list, final int n) {
        if (list.size() > n) {
            return getFirstEpisodeProfileId(list.get(n));
        }
        return null;
    }
    
    static OfflineAdapterData getShowUIData(final OfflinePlayableUiList list, final String s, final String s2) {
        if (s2 != null) {
            for (int i = 0; i < list.size(); ++i) {
                final OfflineAdapterData offlineAdapterData = list.get(i);
                if (OfflineAdapterData$ViewType.SHOW.equals(offlineAdapterData.getVideoAndProfileData().type) && offlineAdapterData.getVideoAndProfileData().video.getId().equalsIgnoreCase(s)) {
                    final OfflineAdapterData offlineAdapterData2 = offlineAdapterData;
                    if (s2.equals(getFirstEpisodeProfileId(offlineAdapterData))) {
                        return offlineAdapterData2;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    private void handleInitIfReady() {
        if (this.mWasInitPerformed) {
            return;
        }
        if (this.getActivity() == null) {
            Log.d("OfflineFragment", "Activity is null - can't continue init");
            return;
        }
        if (this.getServiceManager() == null) {
            Log.d("OfflineFragment", "Manager not available - can't continue init");
            return;
        }
        if (this.mRecyclerView == null) {
            Log.d("OfflineFragment", "Views are not initialized - can't continue init");
            return;
        }
        (this.mOfflineAgentInterface = this.getNetflixActivity().getServiceManager().getOfflineAgent()).addOfflineAgentListener(this);
        this.refreshAdapter();
        this.mWasInitPerformed = true;
        this.setupClicks(this.getView());
    }
    
    private void initAdapter() {
        final OfflineFragment$2 offlineFragment$2 = new OfflineFragment$2(this);
        RealmVideoDetails[] episodes;
        final RealmVideoDetails[] array = episodes = null;
        if (this.selectedTitleId != null) {
            final OfflineAdapterData showUIData = getShowUIData(this.mOfflineAgentInterface.getLatestOfflinePlayableList(), this.selectedTitleId, this.selectedProfileId);
            episodes = array;
            if (showUIData != null) {
                episodes = showUIData.getEpisodes();
            }
        }
        if (Log.isLoggable() && this.selectedTitleId != null && (episodes == null || episodes.length < 1)) {
            Log.w("OfflineFragment", "Nothing was found among shows for current playableID: " + this.selectedTitleId + "; navigating into profiles and videos...");
        }
        if (episodes != null && episodes.length > 0) {
            this.mOfflinePlayableAdapter = new OfflineEpisodesAdapter(this.getNetflixActivity(), this.mOfflineAgentInterface, offlineFragment$2, this.selectedTitleId, this.selectedProfileId);
        }
        else {
            this.mOfflinePlayableAdapter = new OfflineVideosAdapter(this.getNetflixActivity(), this.mOfflineAgentInterface, offlineFragment$2);
        }
        this.mOfflinePlayableAdapter.registerAdapterDataObserver(new OfflineFragment$3(this));
        this.mRecyclerView.setAdapter(this.mOfflinePlayableAdapter);
        this.updateEmptyState();
    }
    
    private boolean isKidsProfile() {
        return this.getServiceManager() != null && this.getServiceManager().getCurrentProfile() != null && this.getServiceManager().getCurrentProfile().isKidsProfile();
    }
    
    private void setupClicks(final View view) {
        final boolean b = this.mOfflinePlayableAdapter instanceof OfflineEpisodesAdapter;
        this.mFindSomethingToDownload = (NetflixTextButton)view.findViewById(2131689858);
        if (this.mFindSomethingToDownload != null) {
            if (BrowseExperience.showKidsExperience()) {
                this.mFindSomethingToDownload.applyFrom(2131427594);
            }
            if (b) {
                this.mFindSomethingToDownload.setText((CharSequence)this.getResources().getString(2131231312));
            }
            else {
                final NetflixTextButton mFindSomethingToDownload = this.mFindSomethingToDownload;
                int text;
                if (this.mOfflinePlayableAdapter.getItemCount() > 0) {
                    text = 2131231313;
                }
                else {
                    text = 2131231322;
                }
                mFindSomethingToDownload.setText(text);
            }
            this.mFindSomethingToDownload.setOnClickListener((View$OnClickListener)new OfflineFragment$4(this, b));
        }
    }
    
    private void setupMainView(final View view) {
        this.mEmptyState = view.findViewById(2131689859);
        this.mRecyclerView = (RecyclerView)view.findViewById(2131689860);
        this.mLayoutManager = new LinearLayoutManager(this.mRecyclerView.getContext());
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);
    }
    
    private void startPlayerActivity(final String s, final int n) {
        if (Log.isLoggable()) {
            Log.d("OfflineFragment", "startPlayerActivity playableId=" + s + "position=" + n);
        }
        OfflineUiHelper.startOfflinePlayback((Context)this.getActivity(), s, this.mOfflinePlayableAdapter.getVideoType(n), PlayContext.OFFLINE_MY_DOWNLOADS_CONTEXT);
    }
    
    private void updateCurrentShowIdIfFound() {
        if (this.isActivityValid()) {
            final Intent intent = this.getActivity().getIntent();
            if (intent.hasExtra("title_id")) {
                this.selectedTitleId = intent.getStringExtra("title_id");
            }
            if (intent.hasExtra("profile_id")) {
                this.selectedProfileId = intent.getStringExtra("profile_id");
            }
            if (intent.hasExtra("playable_id")) {
                final String stringExtra = intent.getStringExtra("playable_id");
                final RealmVideoDetails offlineVideoDetails = RealmUtils.getOfflineVideoDetails(stringExtra);
                if (offlineVideoDetails == null) {
                    final String string = "Video details not in realm, finish the activity : " + stringExtra;
                    Log.w("OfflineFragment", string);
                    LogUtils.reportErrorSafely(string);
                    this.getActivity().finish();
                    return;
                }
                if (offlineVideoDetails.getType() == VideoType.EPISODE) {
                    this.selectedTitleId = offlineVideoDetails.getPlayable().getParentId();
                    this.selectedProfileId = offlineVideoDetails.getProfileId();
                }
                else if (offlineVideoDetails.getType() == VideoType.SHOW) {
                    Log.w("OfflineFragment", "updateCurrentShowIdIfFound() found showId inside PLAYABLE_ID which should never happen - use TITLE_ID instead");
                    this.selectedTitleId = stringExtra;
                    this.selectedProfileId = offlineVideoDetails.getProfileId();
                }
                else {
                    this.selectedTitleId = stringExtra;
                }
            }
            if (this.isEpisodesLevel() && !intent.hasExtra("title_id") && !intent.hasExtra("playable_id")) {
                if (!this.getActivity().isTaskRoot()) {
                    Log.i("OfflineFragment", "Since there is some activity underneath (it should be videos one), we are just closing this instance of OfflineActivity to expose \"videos\" level");
                    this.getActivity().finish();
                    return;
                }
                this.selectedTitleId = null;
                this.selectedProfileId = null;
                Log.i("OfflineFragment", "Since there is no activity underneath, we are just transforming the UI of current OfflineActivity to show \"videos\" level");
            }
        }
    }
    
    private void updateEmptyState() {
        final int itemCount = this.mOfflinePlayableAdapter.getItemCount();
        if (this.isKidsProfile()) {
            if (this.mOfflinePlayableAdapter instanceof OfflineVideosAdapter) {
                int n;
                if (((OfflineVideosAdapter)this.mOfflinePlayableAdapter).isShowingAdultContent()) {
                    n = 2;
                }
                else {
                    n = 1;
                }
                final View mEmptyState = this.mEmptyState;
                int visibility;
                if (itemCount > n) {
                    visibility = 8;
                }
                else {
                    visibility = 0;
                }
                mEmptyState.setVisibility(visibility);
                this.mRecyclerView.setVisibility(0);
            }
            return;
        }
        if (itemCount == 0) {
            this.mEmptyState.setVisibility(0);
            this.mRecyclerView.setVisibility(8);
            return;
        }
        this.mEmptyState.setVisibility(8);
        this.mRecyclerView.setVisibility(0);
    }
    
    private void updatePlayableList() {
        int n;
        if (!this.areDownloadsPreset()) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.mOfflinePlayableAdapter.updatePlayableList();
        if (n != 0) {
            this.getActivity().invalidateOptionsMenu();
        }
        this.updateEmptyState();
    }
    
    public boolean areDownloadsPreset() {
        return this.mOfflinePlayableAdapter != null && this.mOfflinePlayableAdapter.getItemCount() > 0;
    }
    
    public void deleteSelected() {
        if (this.mOfflinePlayableAdapter != null) {
            this.mOfflinePlayableAdapter.deleteSelected();
        }
    }
    
    public String generateDeleteDlgString() {
        if (this.mOfflinePlayableAdapter != null) {
            return this.mOfflinePlayableAdapter.generateDeleteDlgString();
        }
        return "";
    }
    
    protected int getLayoutId() {
        return 2130903138;
    }
    
    public int getSelectedItemsCount() {
        if (this.mOfflinePlayableAdapter != null) {
            return this.mOfflinePlayableAdapter.getItemsCheckedCount();
        }
        return 0;
    }
    
    public String getSelectedItemsDiskSpace() {
        if (this.mOfflinePlayableAdapter != null) {
            return this.mOfflinePlayableAdapter.getSelectedItemsDiskSpace();
        }
        return "";
    }
    
    public boolean isEditMode() {
        return this.mOfflinePlayableAdapter != null && this.mOfflinePlayableAdapter.isSelectionMode();
    }
    
    public boolean isEpisodesLevel() {
        return this.mOfflinePlayableAdapter instanceof OfflineEpisodesAdapter;
    }
    
    @Override
    public boolean isListenerDestroyed() {
        return AndroidUtils.isActivityFinishedOrDestroyed((Context)this.getNetflixActivity());
    }
    
    public boolean isLoadingData() {
        return false;
    }
    
    @Override
    public void onAllPlayablesDeleted(final Status status) {
        this.updatePlayableList();
    }
    
    @Override
    public void onCreateRequestResponse(final String s, final Status status) {
        if (Log.isLoggable()) {
            Log.e("OfflineFragment", "onCreateRequestResponse error=" + status);
        }
        this.updatePlayableList();
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        Log.v("OfflineFragment", "Creating frag view");
        final View inflate = layoutInflater.inflate(this.getLayoutId(), viewGroup, false);
        this.setupMainView(inflate);
        this.handleInitIfReady();
        return inflate;
    }
    
    @Override
    public void onDownloadCompleted(final OfflinePlayableViewData offlinePlayableViewData) {
        this.updatePlayableList();
    }
    
    @Override
    public void onDownloadResumedByUser(final OfflinePlayableViewData offlinePlayableViewData) {
    }
    
    @Override
    public void onDownloadStopped(final OfflinePlayableViewData offlinePlayableViewData, final StopReason stopReason) {
        this.updatePlayableList();
    }
    
    @Override
    public void onError(final Status status) {
        this.updatePlayableList();
    }
    
    @Override
    public void onLicenseRefreshDone(final OfflinePlayableViewData offlinePlayableViewData, final Status status) {
        this.updatePlayableList();
    }
    
    @Override
    public void onManagerReady(final ServiceManager serviceManager, final Status status) {
        Log.d("OfflineFragment", "onManagerReady");
        if (status.isError()) {
            Log.w("OfflineFragment", "Manager status code not okay");
            return;
        }
        this.handleInitIfReady();
    }
    
    @Override
    public void onManagerUnavailable(final ServiceManager serviceManager, final Status status) {
    }
    
    @Override
    public void onOfflinePlayableDeleted(final String s, final Status status) {
        this.updatePlayableList();
    }
    
    @Override
    public void onOfflinePlayableProgress(final OfflinePlayableViewData offlinePlayableViewData, int i) {
        final String playableId = offlinePlayableViewData.getPlayableId();
        Log.i("OfflineFragment", "onOfflinePlayableProgress playableId=" + playableId + " percentageDownloaded=" + i);
        i = this.mLayoutManager.findFirstVisibleItemPosition();
        if (i >= 0) {
            while (i <= this.mLayoutManager.findLastVisibleItemPosition()) {
                if (this.mOfflinePlayableAdapter.containsPlayableId(i, playableId)) {
                    this.mRecyclerView.getAdapter().notifyItemChanged(i, OfflineFragment$Payload.All);
                    return;
                }
                ++i;
            }
        }
    }
    
    public void onPause() {
        if (this.mOfflineAgentInterface != null) {
            this.mOfflineAgentInterface.removeOfflineAgentListener(this);
        }
        super.onPause();
    }
    
    @Override
    public void onPlayWindowRenewDone(final OfflinePlayableViewData offlinePlayableViewData, final Status status) {
        this.updatePlayableList();
    }
    
    public void onResume() {
        super.onResume();
        if (this.mOfflineAgentInterface != null) {
            this.mOfflineAgentInterface.addOfflineAgentListener(this);
        }
        if (this.mWasInitPerformed) {
            this.updatePlayableList();
        }
    }
    
    public void refreshAdapter() {
        final NetflixActivity netflixActivity = this.getNetflixActivity();
        if (netflixActivity != null) {
            netflixActivity.runWhenManagerIsReady(new OfflineFragment$1(this, netflixActivity));
        }
    }
    
    public void switchToEditMode(final boolean selectionMode) {
        if (this.mOfflinePlayableAdapter != null) {
            this.mOfflinePlayableAdapter.setSelectionMode(selectionMode);
        }
    }
}