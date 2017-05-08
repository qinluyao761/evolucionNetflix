// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.mdx;

import com.netflix.mediaclient.servicemgr.IMdxSharedState;
import com.netflix.mediaclient.servicemgr.IMdxSharedState$MdxPlaybackState;
import android.content.IntentFilter;
import com.netflix.mediaclient.ui.verifyplay.PlayVerifierVault;
import com.netflix.mediaclient.ui.verifyplay.PlayVerifierVault$PlayInvokedFrom;
import com.netflix.mediaclient.service.mdx.MdxAgent;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.util.WebApiUtils$VideoIds;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.ui.player.PostPlayRequestContext;
import com.netflix.mediaclient.servicemgr.MdxPostplayState;
import com.netflix.mediaclient.ui.player.MDXControllerActivity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import com.netflix.mediaclient.ui.experience.BrowseExperience;
import android.content.Context;
import com.netflix.mediaclient.ui.verifyplay.PinVerifier;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.content.BroadcastReceiver;

public final class MdxReceiver extends BroadcastReceiver
{
    private static final String TAG = "nf_mdx";
    private final NetflixActivity mActivity;
    
    public MdxReceiver(final NetflixActivity mActivity) {
        this.mActivity = mActivity;
        Log.v("nf_mdx", "Receiver created");
    }
    
    private void cancelPin() {
        Log.d("nf_pin", "cancelPin on PIN_VERIFICATION_NOT_REQUIRED");
        PinVerifier.getInstance().dismissPinVerification();
    }
    
    private void hideMdxController(final Context context) {
        if (BrowseExperience.shouldShowMemento(context)) {
            LocalBroadcastManager.getInstance((Context)this.mActivity).sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.MINI_PLAYER_POST_HIDE"));
            return;
        }
        MDXControllerActivity.finishMDXController(context);
    }
    
    private void showFirstEpisodeInNextSeries(final MdxPostplayState mdxPostplayState) {
        final WebApiUtils$VideoIds videoIds = this.mActivity.getServiceManager().getMdx().getVideoIds();
        if (videoIds != null && videoIds.episodeId > 0) {
            this.mActivity.getServiceManager().getBrowse().fetchPostPlayVideos(String.valueOf(videoIds.episodeId), videoIds.getVideoType(), PostPlayRequestContext.MDX, new MdxReceiver$FetchNextSeriesEpisodeVideoDetailsForMdxCallback("nf_mdx", this.mActivity));
        }
    }
    
    private void showMdxController(final Intent intent, final Context context) {
        final String string = intent.getExtras().getString("postplayState");
        if (!StringUtils.isEmpty(string)) {
            final MdxPostplayState mdxPostplayState = new MdxPostplayState(string);
            if (BrowseExperience.shouldShowMemento((Context)this.mActivity)) {
                if (mdxPostplayState.isInCountdown()) {
                    this.showNextEpisodeInSeries(mdxPostplayState);
                }
                else if (mdxPostplayState.isInPrompt()) {
                    this.showFirstEpisodeInNextSeries(mdxPostplayState);
                }
            }
            else {
                final WebApiUtils$VideoIds videoIds = this.mActivity.getServiceManager().getMdx().getVideoIds();
                if (videoIds != null && videoIds.episodeId > 0) {
                    MDXControllerActivity.showMDXController(this.mActivity, String.valueOf(videoIds.episodeId), true, PlayContext.DFLT_MDX_CONTEXT);
                }
            }
        }
    }
    
    private void showNextEpisodeInSeries(final MdxPostplayState mdxPostplayState) {
        final WebApiUtils$VideoIds videoIdsPostplay = ((MdxAgent)this.mActivity.getServiceManager().getMdx()).getVideoIdsPostplay();
        if (videoIdsPostplay != null && videoIdsPostplay.episodeId > 0) {
            this.mActivity.getServiceManager().getBrowse().fetchEpisodeDetails(String.valueOf(videoIdsPostplay.episodeId), null, new MdxReceiver$FetchPostPlayForPlaybackCallback("nf_mdx", this.mActivity));
        }
    }
    
    private void verifyPinAndNotify(final Intent intent) {
        final String string = intent.getExtras().getString("uuid");
        Log.d("nf_pin", "verifyPinAndNotify on PIN_VERIFICATION_SHOW");
        PinVerifier.getInstance().verify(this.mActivity, true, new PlayVerifierVault(PlayVerifierVault$PlayInvokedFrom.MDX.getValue(), string));
    }
    
    public IntentFilter getFilter() {
        Log.v("nf_mdx", "Get filter called");
        final IntentFilter intentFilter = new IntentFilter("com.netflix.mediaclient.intent.action.MDXUPDATE_READY");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.MDXUPDATE_NOTREADY");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.MDXUPDATE_TARGETLIST");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.PIN_VERIFICATION_SHOW");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.PIN_VERIFICATION_NOT_REQUIRED");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.MDXUPDATE_POSTPLAY");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKSTART");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.MDXUPDATE_STATE");
        intentFilter.addAction("com.netflix.mediaclient.intent.action.MDXUPDATE_CAPABILITY");
        intentFilter.addCategory("com.netflix.mediaclient.intent.category.MDX");
        return intentFilter;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (Log.isLoggable()) {
            Log.v("nf_mdx", "MDX broadcast " + intent);
        }
        if (!this.mActivity.isFinishing()) {
            final String action = intent.getAction();
            if (action != null) {
                if (!this.mActivity.shouldAddMdxToMenu()) {
                    Log.d("nf_mdx", "Ignore MDX broadcast");
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.MDXUPDATE_NOTREADY".equals(action)) {
                    Log.d("nf_mdx", "MDX is NOT ready, invalidate action bar");
                    this.mActivity.invalidateOptionsMenu();
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.MDXUPDATE_READY".equals(action)) {
                    Log.d("nf_mdx", "MDX is ready, invalidate action bar");
                    this.mActivity.invalidateOptionsMenu();
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.MDXUPDATE_TARGETLIST".equals(action)) {
                    Log.d("nf_mdx", "MDX is ready, got target list update, invalidate action bar");
                    this.mActivity.invalidateOptionsMenu();
                    this.mActivity.updateTargetSelectionDialog();
                    this.mActivity.setConnectingToTarget(false);
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.PIN_VERIFICATION_SHOW".equals(action)) {
                    Log.d("nf_mdx", "MDX PIN show dialog");
                    this.verifyPinAndNotify(intent);
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.PIN_VERIFICATION_NOT_REQUIRED".equals(action)) {
                    Log.d("nf_mdx", "MDX cancel pin dialog - verified on other controller");
                    this.cancelPin();
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.MDXUPDATE_POSTPLAY".equals(action)) {
                    this.showMdxController(intent, context);
                    this.abortBroadcast();
                    return;
                }
                if ("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKSTART".equals(action)) {
                    this.hideMdxController(context);
                    return;
                }
                if (intent.getAction().equals("com.netflix.mediaclient.intent.action.MDXUPDATE_STATE")) {
                    final IMdxSharedState sharedState = this.mActivity.getServiceManager().getMdx().getSharedState();
                    if (sharedState != null && sharedState.getMdxPlaybackState() == IMdxSharedState$MdxPlaybackState.Transitioning) {
                        this.hideMdxController(context);
                    }
                }
                else if ("com.netflix.mediaclient.intent.action.MDXUPDATE_CAPABILITY".equals(action)) {
                    Log.d("nf_mdx", "MDX is connected, invalidate action bar to finish animation");
                    this.mActivity.setConnectingToTarget(false);
                    this.mActivity.invalidateOptionsMenu();
                    LocalBroadcastManager.getInstance((Context)this.mActivity).sendBroadcast(new Intent("com.netflix.mediaclient.intent.action.UPDATE_CAPABILITIES_BADGES"));
                }
            }
        }
    }
}
