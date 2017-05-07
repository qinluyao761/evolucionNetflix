// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.mdx;

import android.text.TextUtils;
import com.netflix.mediaclient.servicemgr.PostPlayVideo;
import java.util.List;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;
import android.content.IntentFilter;
import com.netflix.mediaclient.ui.pin.PinDialogVault;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.util.StringUtils;
import android.content.Intent;
import com.netflix.mediaclient.util.WebApiUtils;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.MdxPostplayState;
import com.netflix.mediaclient.ui.player.MDXControllerActivity;
import android.content.Context;
import com.netflix.mediaclient.ui.pin.PinVerifier;
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
        PinVerifier.getInstance().dismissPinVerification();
    }
    
    private void hideMdxController(final Context context) {
        MDXControllerActivity.finishMDXController((NetflixActivity)context);
    }
    
    private void showFirstEpisodeInNextSeries(final MdxPostplayState mdxPostplayState) {
        final WebApiUtils.VideoIds videoIds = this.mActivity.getServiceManager().getMdx().getVideoIds();
        if (videoIds != null && videoIds.episodeId > 0) {
            this.mActivity.getServiceManager().fetchPostPlayVideos(String.valueOf(videoIds.episodeId), new FetchVideoDetailsForMdxCallback("nf_mdx", this.mActivity));
        }
    }
    
    private void showMdxController(final Intent intent, final Context context) {
        final String string = intent.getExtras().getString("postplayState");
        if (!StringUtils.isEmpty(string)) {
            final MdxPostplayState mdxPostplayState = new MdxPostplayState(string);
            if (mdxPostplayState.isInCountdown()) {
                this.showNextEpisodeInSeries(mdxPostplayState);
            }
            else if (mdxPostplayState.isInPrompt()) {
                this.showFirstEpisodeInNextSeries(mdxPostplayState);
            }
        }
    }
    
    private void showNextEpisodeInSeries(final MdxPostplayState mdxPostplayState) {
        final WebApiUtils.VideoIds videoIds = this.mActivity.getServiceManager().getMdx().getVideoIds();
        if (videoIds != null && videoIds.episodeId > 0) {
            MDXControllerActivity.showMDXController(this.mActivity, videoIds.episodeId, PlayContext.DFLT_MDX_CONTEXT);
        }
    }
    
    private void verifyPinAndNotify(final Intent intent) {
        PinVerifier.getInstance().verify(this.mActivity, true, new PinDialogVault(PinDialogVault.PinInvokedFrom.MDX.getValue(), intent.getExtras().getString("uuid")));
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
        intentFilter.addCategory("com.netflix.mediaclient.intent.category.MDX");
        return intentFilter;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (Log.isLoggable("nf_mdx", 2)) {
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
                    return;
                }
                if (intent.getAction().equals("com.netflix.mediaclient.intent.action.MDXUPDATE_PLAYBACKSTART")) {
                    this.hideMdxController(context);
                }
            }
        }
    }
    
    private static class FetchVideoDetailsForMdxCallback extends LoggingManagerCallback
    {
        private final NetflixActivity mActivity;
        
        public FetchVideoDetailsForMdxCallback(final String s, final NetflixActivity mActivity) {
            super(s);
            this.mActivity = mActivity;
        }
        
        @Override
        public void onPostPlayVideosFetched(final List<PostPlayVideo> list, final int n) {
            super.onPostPlayVideosFetched(list, n);
            if (this.mActivity != null && n == 0 && list.size() > 0) {
                final String id = list.get(0).getId();
                if (!TextUtils.isEmpty((CharSequence)id)) {
                    MDXControllerActivity.showMDXController(this.mActivity, id, PlayContext.DFLT_MDX_CONTEXT);
                }
            }
        }
    }
}
