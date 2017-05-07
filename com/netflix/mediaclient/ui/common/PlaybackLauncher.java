// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.common;

import com.netflix.mediaclient.servicemgr.ShowDetails;
import com.netflix.mediaclient.servicemgr.MovieDetails;
import android.widget.Toast;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;
import android.os.Handler;
import com.netflix.mediaclient.service.mdx.MdxAgent;
import com.netflix.mediaclient.servicemgr.Playable;
import com.netflix.mediaclient.util.StringUtils;
import android.util.Pair;
import com.netflix.mediaclient.servicemgr.IMdx;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.servicemgr.VideoDetails;
import com.netflix.mediaclient.ui.player.PlayerActivity;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.VideoType;
import android.content.Context;
import com.netflix.mediaclient.ui.mdx.MdxMiniPlayerFrag;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.service.webclient.model.BillboardDetails;
import com.netflix.mediaclient.ui.Asset;
import com.netflix.mediaclient.android.activity.NetflixActivity;

public final class PlaybackLauncher
{
    private static final String TAG = "nf_play";
    
    public static void getDetailsAndStartPlayback(final NetflixActivity netflixActivity, final BillboardDetails billboardDetails, final PlayContext playContext) {
        if (shouldPlayOnRemoteTarget(netflixActivity.getServiceManager())) {
            final ServiceManager serviceManager = netflixActivity.getServiceManager();
            if (serviceManager != null) {
                Log.d("nf_play", "Getting video details for mdx playback");
                MdxMiniPlayerFrag.sendShowAndDisableIntent((Context)netflixActivity);
                if (billboardDetails.getType() == VideoType.MOVIE) {
                    serviceManager.fetchMovieDetails(billboardDetails.getId(), new FetchVideoDetailsForMdxCallback(netflixActivity, playContext));
                }
                else {
                    if (billboardDetails.getType() == VideoType.SHOW) {
                        serviceManager.fetchShowDetails(billboardDetails.getId(), null, new FetchVideoDetailsForMdxCallback(netflixActivity, playContext));
                        return;
                    }
                    throw new IllegalStateException("Invalid billboard video type: " + billboardDetails.getType());
                }
            }
            return;
        }
        Log.d("nf_play", "Starting local playback, asking for video details first");
        PlayerActivity.getDetailsAndPlayVideo(netflixActivity, billboardDetails, playContext);
    }
    
    private static boolean isExisitingMdxTargetAvailable(final IMdx mdx, final String s) {
        if (Log.isLoggable("nf_play", 3)) {
            Log.d("nf_play", "Check if MDX remote target exist in target list: " + s);
        }
        if (!mdx.isReady()) {
            Log.w("nf_play", "MDX service is NOT ready");
            return false;
        }
        final Pair<String, String>[] targetList = mdx.getTargetList();
        if (targetList == null || targetList.length < 1) {
            Log.w("nf_play", "No MDX remote targets found");
            return false;
        }
        for (int i = 0; i < targetList.length; ++i) {
            if (s.equals(targetList[i].first)) {
                Log.d("nf_play", "Target found");
                return true;
            }
        }
        Log.w("nf_play", "Target NOT found!");
        return false;
    }
    
    private static void logMdx(final IMdx mdx) {
        if (Log.isLoggable("nf_play", 3)) {
            Log.d("nf_play", "MDX is ready " + mdx.isReady());
            if (mdx.getTargetList() != null) {
                Log.d("nf_play", "MDX found targets: " + mdx.getTargetList().length);
            }
            else {
                Log.d("nf_play", "MDX found no targets ");
            }
            Log.d("nf_play", "MDX current target '" + mdx.getCurrentTarget() + "'");
        }
    }
    
    public static boolean shouldPlayOnRemoteTarget(final ServiceManager serviceManager) {
        if (serviceManager == null || !serviceManager.isReady() || serviceManager.getMdx() == null) {
            Log.e("nf_play", "MDX or service manager are null! That should NOT happen. Default to local.");
            return false;
        }
        final IMdx mdx = serviceManager.getMdx();
        logMdx(mdx);
        final String currentTarget = mdx.getCurrentTarget();
        if (StringUtils.isEmpty(currentTarget)) {
            Log.d("nf_play", "Local target, play on device");
            return false;
        }
        return isExisitingMdxTargetAvailable(mdx, currentTarget);
    }
    
    public static void startPlaybackAfterPIN(final NetflixActivity netflixActivity, final Playable playable, final PlayContext playContext) {
        startPlaybackAfterPIN(netflixActivity, Asset.create(playable, playContext, PlayerActivity.PIN_VERIFIED));
    }
    
    public static void startPlaybackAfterPIN(final NetflixActivity netflixActivity, final Asset asset) {
        verifyPinAndPlay(netflixActivity, asset, shouldPlayOnRemoteTarget(netflixActivity.getServiceManager()));
    }
    
    public static void startPlaybackForceLocal(final NetflixActivity netflixActivity, final Asset asset) {
        verifyPinAndPlay(netflixActivity, asset, false);
    }
    
    public static void startPlaybackForceRemote(final NetflixActivity netflixActivity, final Asset asset) {
        verifyPinAndPlay(netflixActivity, asset, true);
    }
    
    private static void startPlaybackInternal(final NetflixActivity netflixActivity, final Asset asset, final boolean b) {
        if (!b) {
            Log.d("nf_play", "Start local playback");
            PlayerActivity.playVideo(netflixActivity, asset);
            return;
        }
        Log.d("nf_play", "Starting MDX remote playback");
        if (!MdxAgent.Utils.playVideo(netflixActivity, asset, false)) {
            return;
        }
        new Handler(netflixActivity.getMainLooper()).postDelayed((Runnable)new Runnable() {
            final /* synthetic */ Context val$context = netflixActivity.getApplication();
            
            @Override
            public void run() {
                MdxMiniPlayerFrag.sendShowAndDisableIntent(this.val$context);
            }
        }, 250L);
    }
    
    private static void verifyPinAndPlay(final NetflixActivity netflixActivity, final Asset asset, final boolean b) {
        Log.d("nf_play", String.format("nf_pin verifyPinAndPlay - %s protected:%b", asset.getPlayableId(), asset.isPinProtected()));
        PinVerifier.getInstance().verify(netflixActivity, asset.isPinProtected(), (PinVerifier.PinVerificationCallback)new PinVerifier.PinVerificationCallback() {
            @Override
            public void onPinCancelled() {
                Log.d("nf_play", "nf_pin pinVerification skipped - not starting playback");
            }
            
            @Override
            public void onPinVerification(final boolean b) {
                if (!b) {
                    Log.d("nf_play", "nf_pin pinVerification failed - not starting playback");
                    return;
                }
                startPlaybackInternal(netflixActivity, asset, b);
            }
        });
    }
    
    private static class FetchVideoDetailsForMdxCallback extends LoggingManagerCallback
    {
        private final NetflixActivity activity;
        private final PlayContext playContext;
        
        public FetchVideoDetailsForMdxCallback(final NetflixActivity activity, final PlayContext playContext) {
            super("nf_play");
            this.activity = activity;
            this.playContext = playContext;
        }
        
        private void handleResponse(final VideoDetails videoDetails, final int n) {
            if (this.activity.destroyed()) {
                return;
            }
            if (n != 0 || videoDetails == null) {
                Log.w("nf_play", "Error loading video details for MDX launch - hiding mini player");
                Toast.makeText((Context)this.activity, 2131493117, 1).show();
                this.activity.hideMdxMiniPlayer();
                return;
            }
            PlaybackLauncher.startPlaybackAfterPIN(this.activity, videoDetails, this.playContext);
        }
        
        @Override
        public void onMovieDetailsFetched(final MovieDetails movieDetails, final int n) {
            super.onMovieDetailsFetched(movieDetails, n);
            this.handleResponse(movieDetails, n);
        }
        
        @Override
        public void onShowDetailsFetched(final ShowDetails showDetails, final int n) {
            super.onShowDetailsFetched(showDetails, n);
            this.handleResponse(showDetails, n);
        }
    }
}
