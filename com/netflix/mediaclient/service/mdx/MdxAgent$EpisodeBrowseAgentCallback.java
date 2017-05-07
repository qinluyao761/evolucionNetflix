// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.mdx;

import com.netflix.mediaclient.ui.player.MDXControllerActivity;
import android.app.Service;
import com.netflix.mediaclient.service.user.UserAgentBroadcastIntents;
import java.util.Collection;
import com.netflix.mediaclient.servicemgr.IMdxSharedState;
import android.text.TextUtils;
import android.app.Notification;
import android.util.Pair;
import java.nio.ByteBuffer;
import com.netflix.mediaclient.service.ServiceAgent$UserAgentInterface;
import com.netflix.mediaclient.javabridge.ui.mdxcontroller.TransactionId;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.servicemgr.model.VideoType;
import android.support.v4.content.LocalBroadcastManager;
import android.content.IntentFilter;
import android.annotation.SuppressLint;
import android.os.PowerManager;
import android.net.wifi.WifiManager;
import java.util.Iterator;
import com.netflix.mediaclient.service.ServiceAgent$BrowseAgentInterface;
import com.netflix.mediaclient.service.browse.BrowseAgentCallback;
import com.netflix.mediaclient.service.mdx.notification.MdxNotificationManagerFactory;
import com.netflix.mediaclient.util.AndroidUtils;
import android.app.PendingIntent;
import android.content.Intent;
import com.netflix.mediaclient.javabridge.ui.Mdx$Events;
import com.netflix.mediaclient.service.NetflixService;
import android.content.Context;
import android.net.wifi.WifiManager$WifiLock;
import com.netflix.mediaclient.javabridge.ui.mdxcontroller.RemoteDevice;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.os.PowerManager$WakeLock;
import com.netflix.mediaclient.service.mdx.notification.IMdxNotificationManager;
import java.util.concurrent.atomic.AtomicBoolean;
import com.netflix.mediaclient.javabridge.ui.mdxcontroller.MdxController;
import android.os.HandlerThread;
import android.os.Handler;
import com.netflix.mediaclient.javabridge.ui.EventListener;
import com.netflix.mediaclient.service.mdx.cast.CastManager;
import android.graphics.Bitmap;
import com.netflix.mediaclient.media.BifManager;
import com.netflix.mediaclient.servicemgr.IMdx;
import com.netflix.mediaclient.service.mdx.notification.MdxNotificationManager$MdxNotificationIntentRetriever;
import com.netflix.mediaclient.javabridge.ui.mdxcontroller.MdxController$PropertyUpdateListener;
import com.netflix.mediaclient.service.ServiceAgent;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.model.details.EpisodeDetails;
import com.netflix.mediaclient.util.WebApiUtils$VideoIds;
import com.netflix.mediaclient.servicemgr.model.details.VideoDetails;
import com.netflix.mediaclient.service.browse.SimpleBrowseAgentCallback;

class MdxAgent$EpisodeBrowseAgentCallback extends SimpleBrowseAgentCallback
{
    private final boolean isPostPlay;
    final /* synthetic */ MdxAgent this$0;
    private final boolean triggeredByCommand;
    VideoDetails vidDetails;
    WebApiUtils$VideoIds vidIds;
    
    MdxAgent$EpisodeBrowseAgentCallback(final MdxAgent this$0, final boolean triggeredByCommand, final boolean isPostPlay) {
        this.this$0 = this$0;
        this.triggeredByCommand = triggeredByCommand;
        this.isPostPlay = isPostPlay;
    }
    
    private void assignVideoDetails(final VideoDetails vidDetails) {
        this.vidDetails = vidDetails;
        if (!this.isPostPlay) {
            this.this$0.mVideoDetails = vidDetails;
            return;
        }
        this.this$0.mVideoDetailsPostplay = vidDetails;
    }
    
    private void assignVideoIds(final WebApiUtils$VideoIds vidIds) {
        this.vidIds = vidIds;
        if (this.isPostPlay) {
            this.this$0.mVideoIdsPostplay = vidIds;
            return;
        }
        this.this$0.mVideoIds = vidIds;
    }
    
    @Override
    public void onEpisodeDetailsFetched(final EpisodeDetails episodeDetails, final Status status) {
        if (Log.isLoggable("nf_mdx_agent", 2)) {
            Log.v("nf_mdx_agent", "onEpisodeDetailsFetched, res: " + status);
        }
        if (status.isError()) {
            return;
        }
        this.assignVideoDetails(episodeDetails);
        final String highResolutionPortraitBoxArtUrl = episodeDetails.getHighResolutionPortraitBoxArtUrl();
        if (this.this$0.mMdxBoxartLoader != null) {
            this.this$0.mMdxBoxartLoader.fetchImage(highResolutionPortraitBoxArtUrl);
        }
        final String bifUrl = episodeDetails.getBifUrl();
        if (StringUtils.isNotEmpty(bifUrl)) {
            this.this$0.createBifManager(bifUrl);
        }
        this.this$0.mNotifier.movieMetaDataAvailable(this.this$0.mCurrentTargetUuid);
        if (this.triggeredByCommand) {
            this.assignVideoIds(new WebApiUtils$VideoIds(episodeDetails.getPlayable().isPlayableEpisode(), episodeDetails.getEpisodeIdUrl(), episodeDetails.getCatalogIdUrl(), Integer.parseInt(episodeDetails.getId()), Integer.parseInt(episodeDetails.getShowId())));
            this.this$0.mTargetManager.playerPlay(this.this$0.mCurrentTargetUuid, this.vidIds.catalogIdUrl, this.this$0.mTrackId, this.vidIds.episodeIdUrl, this.this$0.mStartTime);
            this.this$0.logPlaystart(false);
        }
        this.this$0.updateMdxRemoteClient(this.isPostPlay);
        this.this$0.updateMdxNotification(true, this.vidDetails.getPlayable().getParentTitle(), this.this$0.getContext().getString(2131493227, new Object[] { this.vidDetails.getPlayable().getSeasonNumber(), this.vidDetails.getPlayable().getEpisodeNumber(), this.vidDetails.getTitle() }), this.isPostPlay);
    }
}
