// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.browse;

import com.netflix.mediaclient.service.pushnotification.MessageData;
import com.netflix.mediaclient.servicemgr.model.LoMoType;
import com.netflix.mediaclient.servicemgr.model.Billboard;
import com.netflix.mediaclient.service.webclient.model.SeasonDetails;
import com.netflix.mediaclient.servicemgr.model.VideoType;
import com.netflix.mediaclient.service.webclient.model.CWVideo;
import com.netflix.mediaclient.service.user.UserAgentBroadcastIntents;
import android.content.IntentFilter;
import com.netflix.mediaclient.service.webclient.model.MovieDetails;
import com.netflix.mediaclient.util.SocialNotificationsUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import com.netflix.mediaclient.android.app.BackgroundTask;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import com.netflix.mediaclient.Log;
import java.util.Random;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.model.details.KidsCharacterDetails;
import com.netflix.mediaclient.ui.Asset;
import com.netflix.mediaclient.service.NetflixService;
import com.netflix.model.leafs.social.SocialNotificationSummary;
import com.netflix.mediaclient.servicemgr.model.details.EpisodeDetails;
import com.netflix.model.leafs.social.SocialNotificationsList;
import com.netflix.mediaclient.service.ServiceAgent$UserAgentInterface;
import com.netflix.mediaclient.servicemgr.model.details.ShowDetails;
import android.content.Context;
import android.content.BroadcastReceiver;
import com.netflix.mediaclient.service.browse.cache.BrowseWebClientCache;
import com.netflix.mediaclient.service.ServiceAgent$BrowseAgentInterface;
import com.netflix.mediaclient.service.ServiceAgent;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.servicemgr.model.LoMo;
import com.netflix.mediaclient.servicemgr.model.Video;
import java.util.List;

class BrowseAgent$FetchVideosTask extends BrowseAgent$CachedFetchTask<List<Video>>
{
    private final boolean includeKubrick;
    private final LoMo lomo;
    final /* synthetic */ BrowseAgent this$0;
    private final boolean useCacheOnly;
    private final BrowseAgentCallback webClientCallback;
    
    public BrowseAgent$FetchVideosTask(final BrowseAgent this$0, final LoMo lomo, final int n, final int n2, final boolean useCacheOnly, final boolean includeKubrick, final BrowseAgentCallback browseAgentCallback, final boolean b) {
        this.this$0 = this$0;
        super(this$0, lomo.getId(), n, n2, browseAgentCallback, b);
        this.webClientCallback = new BrowseAgent$FetchVideosTask$1(this);
        this.lomo = lomo;
        this.useCacheOnly = useCacheOnly;
        this.includeKubrick = includeKubrick;
    }
    
    @Override
    public void run() {
        final List<Video> list = this.getCachedValue();
        if (list == null && !this.useCacheOnly) {
            this.this$0.mBrowseWebClient.fetchVideos(this.lomo, this.getFromIndex(), this.getToIndex(), this.includeKubrick, this.webClientCallback);
            return;
        }
        this.webClientCallback.onVideosFetched(list, CommonStatus.OK);
    }
}
