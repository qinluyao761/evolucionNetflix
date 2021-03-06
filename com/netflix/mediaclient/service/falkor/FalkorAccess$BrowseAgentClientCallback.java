// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.falkor;

import com.netflix.model.leafs.advisory.ExpiringContentAdvisory$ContentAction;
import com.netflix.mediaclient.service.pushnotification.MessageData;
import com.netflix.mediaclient.servicemgr.Asset;
import com.netflix.model.leafs.social.IrisNotificationSummary;
import com.netflix.mediaclient.servicemgr.BillboardInteractionType;
import com.netflix.falkor.ModelProxy;
import com.netflix.falkor.task.CmpTaskDetails;
import com.netflix.mediaclient.ui.player.PostPlayRequestContext;
import java.io.File;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.service.browse.BrowseAgentCallbackWrapper;
import com.netflix.mediaclient.service.NetflixService$ClientCallbacks;
import com.netflix.mediaclient.servicemgr.IBrowseInterface;
import com.netflix.model.leafs.Video$Summary;
import com.netflix.mediaclient.servicemgr.interface_.RatingInfo;
import com.netflix.mediaclient.servicemgr.interface_.search.SearchVideoListProvider;
import com.netflix.mediaclient.servicemgr.interface_.details.ShowDetails;
import com.netflix.mediaclient.servicemgr.interface_.details.SeasonDetails;
import com.netflix.mediaclient.servicemgr.interface_.search.ISearchResults;
import com.netflix.mediaclient.servicemgr.interface_.details.PostPlayVideosProvider;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import java.util.Map;
import com.netflix.mediaclient.servicemgr.interface_.search.IrisNotificationsList;
import com.netflix.mediaclient.servicemgr.interface_.details.MovieDetails;
import com.netflix.mediaclient.servicemgr.interface_.LoMo;
import com.netflix.mediaclient.servicemgr.interface_.LoLoMo;
import com.netflix.mediaclient.servicemgr.interface_.details.KidsCharacterDetails;
import com.netflix.mediaclient.servicemgr.interface_.details.InteractiveMoments;
import com.netflix.mediaclient.servicemgr.interface_.genre.Genre;
import com.netflix.mediaclient.servicemgr.interface_.genre.GenreList;
import com.netflix.model.branches.FalkorVideo;
import com.netflix.mediaclient.servicemgr.interface_.details.EpisodeDetails;
import com.netflix.mediaclient.servicemgr.interface_.Discovery;
import com.netflix.mediaclient.servicemgr.interface_.CWVideo;
import com.netflix.mediaclient.service.logging.error.ErrorLoggingManager;
import com.netflix.mediaclient.servicemgr.interface_.Billboard;
import com.netflix.model.leafs.advisory.Advisory;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.INetflixServiceCallback;
import com.netflix.model.branches.FalkorActorStill;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.model.branches.MementoVideoSwatch;
import com.netflix.model.branches.FalkorPerson;
import java.util.List;
import com.netflix.mediaclient.service.browse.BrowseAgentCallback;

class FalkorAccess$BrowseAgentClientCallback implements BrowseAgentCallback
{
    private final int clientId;
    private final int requestId;
    final /* synthetic */ FalkorAccess this$0;
    
    FalkorAccess$BrowseAgentClientCallback(final FalkorAccess this$0, final int clientId, final int requestId) {
        this.this$0 = this$0;
        this.clientId = clientId;
        this.requestId = requestId;
    }
    
    @Override
    public void onActorDetailsAndRelatedFetched(final List<FalkorPerson> list, final List<MementoVideoSwatch> list2, final Status status, final List<FalkorActorStill> list3) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onActorDetailsAndRelatedFetched");
            return;
        }
        netflixServiceCallback.onActorDetailsAndRelatedFetched(this.requestId, list, list2, status, list3);
    }
    
    @Override
    public void onAdvisoriesFetched(final List<Advisory> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onAdvisoriesFetched");
            return;
        }
        netflixServiceCallback.onAdvisoriesFetched(this.requestId, list, status);
    }
    
    @Override
    public void onBBVideosFetched(final List<Billboard> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for oBBVideosFetched");
            return;
        }
        netflixServiceCallback.onBBVideosFetched(this.requestId, list, status);
    }
    
    @Override
    public void onBrowsePlaySessionEnd(final boolean b, final Status status) {
        if (Log.isLoggable()) {
            final String string = "SPY-8604 - No client callback found for onBrowsePlaySessionEnd: " + status;
            Log.w("FalkorAccess", string);
            ErrorLoggingManager.logHandledException(string);
        }
    }
    
    @Override
    public void onCWVideosFetched(final List<CWVideo> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onCWVideosFetched");
            return;
        }
        netflixServiceCallback.onCWVideosFetched(this.requestId, list, status);
    }
    
    @Override
    public void onDiscoveryVideosFetched(final List<Discovery> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onDiscoveryVideosFetched");
            return;
        }
        netflixServiceCallback.onDiscoveryVideosFetched(this.requestId, list, status);
    }
    
    @Override
    public void onEpisodeDetailsFetched(final EpisodeDetails episodeDetails, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onEpisodeDetailsFetched");
            return;
        }
        netflixServiceCallback.onEpisodeDetailsFetched(this.requestId, episodeDetails, status);
    }
    
    @Override
    public void onEpisodesFetched(final List<EpisodeDetails> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onEpisodesFetched");
            return;
        }
        netflixServiceCallback.onEpisodesFetched(this.requestId, list, status);
    }
    
    @Override
    public void onFalkorVideoFetched(final FalkorVideo falkorVideo, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onFalkorVideoFetched");
            return;
        }
        netflixServiceCallback.onFalkorVideoFetched(this.requestId, falkorVideo, status);
    }
    
    @Override
    public void onGenreListsFetched(final List<GenreList> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onGenreListsFetched");
            return;
        }
        netflixServiceCallback.onGenreListsFetched(this.requestId, list, status);
    }
    
    @Override
    public void onGenreLoLoMoPrefetched(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for client onGenreLoLoMoPrefetched");
            return;
        }
        netflixServiceCallback.onGenreLoLoMoPrefetched(this.requestId, status);
    }
    
    @Override
    public void onGenresFetched(final List<Genre> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onGenresFetched");
            return;
        }
        netflixServiceCallback.onGenresFetched(this.requestId, list, status);
    }
    
    @Override
    public void onInteractiveMomentsFetched(final InteractiveMoments interactiveMoments, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onInteractiveVideoMomentsFetched");
            return;
        }
        netflixServiceCallback.onInteractiveMomentsFetched(this.requestId, interactiveMoments, status);
    }
    
    @Override
    public void onIrisNotificationsMarkedAsRead(final Status status) {
        if (Log.isLoggable()) {
            Log.i("FalkorAccess", "onIrisNotificationsMarkedAsRead: " + status);
        }
    }
    
    @Override
    public void onKidsCharacterDetailsFetched(final KidsCharacterDetails kidsCharacterDetails, final Boolean b, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onKidsCharacterDetailsFetched");
            return;
        }
        netflixServiceCallback.onKidsCharacterDetailsFetched(this.requestId, kidsCharacterDetails, b, status);
    }
    
    @Override
    public void onLoLoMoPrefetched(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for client onLoLoMoPrefetched");
            return;
        }
        netflixServiceCallback.onLoLoMoPrefetched(this.requestId, status);
    }
    
    @Override
    public void onLoLoMoSummaryFetched(final LoLoMo loLoMo, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onLoLoMoSummaryFetched");
            return;
        }
        netflixServiceCallback.onLoLoMoSummaryFetched(this.requestId, loLoMo, status);
    }
    
    @Override
    public void onLoMosFetched(final List<LoMo> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onLoMosFetched");
            return;
        }
        netflixServiceCallback.onLoMosFetched(this.requestId, list, status);
    }
    
    @Override
    public void onMovieDetailsFetched(final MovieDetails movieDetails, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onMovieDetailsFetched");
            return;
        }
        netflixServiceCallback.onMovieDetailsFetched(this.requestId, movieDetails, status);
    }
    
    @Override
    public void onNotificationsListFetched(final IrisNotificationsList list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onIrisNotificationsListFetched");
            return;
        }
        netflixServiceCallback.onIrisNotificationsListFetched(this.requestId, list, status);
    }
    
    @Override
    public void onOfflineGeoPlayabilityReceived(final Map<String, Boolean> map, final Status status) {
        if (Log.isLoggable()) {
            final String string = "No client callback found for onOfflineGeoPlayabilityReceived: " + status;
            Log.w("FalkorAccess", string);
            ErrorLoggingManager.logHandledException(string);
        }
    }
    
    @Override
    public void onPersonDetailFetched(final FalkorPerson falkorPerson, final FalkorActorStill falkorActorStill, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onActorDetailsAndRelatedFetched");
            return;
        }
        netflixServiceCallback.onPersonDetailFetched(this.requestId, falkorPerson, falkorActorStill, status);
    }
    
    @Override
    public void onPersonRelatedFetched(final FalkorPerson falkorPerson, final List<Video> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onPersonRelatedFetched");
            return;
        }
        netflixServiceCallback.onPersonRelatedFetched(this.requestId, falkorPerson, list, status);
    }
    
    @Override
    public void onPostPlayImpressionLogged(final boolean b, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onPostPlayImpressionLogged");
            return;
        }
        netflixServiceCallback.onPostPlayImpressionLogged(this.requestId, b, status);
    }
    
    @Override
    public void onPostPlayVideosFetched(final PostPlayVideosProvider postPlayVideosProvider, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onPostPlayVideosFetched");
            return;
        }
        netflixServiceCallback.onPostPlayVideosFetched(this.requestId, postPlayVideosProvider, status);
    }
    
    @Override
    public void onQueueAdd(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onQueueAdd");
            return;
        }
        netflixServiceCallback.onQueueAdd(this.requestId, status);
    }
    
    @Override
    public void onQueueRemove(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onQueueRemove");
            return;
        }
        netflixServiceCallback.onQueueRemove(this.requestId, status);
    }
    
    @Override
    public void onScenePositionFetched(final int n, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onScenePositionFetched");
            return;
        }
        netflixServiceCallback.onScenePositionFetched(this.requestId, n, status);
    }
    
    @Override
    public void onSearchResultsFetched(final ISearchResults searchResults, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onSearchResultsFetched");
            return;
        }
        netflixServiceCallback.onSearchResultsFetched(this.requestId, searchResults, status);
    }
    
    @Override
    public void onSeasonDetailsFetched(final SeasonDetails seasonDetails, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onSeasonDetailsFetched");
            return;
        }
        netflixServiceCallback.onSeasonDetailsFetched(this.requestId, seasonDetails, status);
    }
    
    @Override
    public void onSeasonsFetched(final List<SeasonDetails> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onSeasonsFetched");
            return;
        }
        netflixServiceCallback.onSeasonsFetched(this.requestId, list, status);
    }
    
    @Override
    public void onShowDetailsAndSeasonsFetched(final ShowDetails showDetails, final List<SeasonDetails> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onShowDetailsAndSeasonsFetched");
            return;
        }
        netflixServiceCallback.onShowDetailsAndSeasonsFetched(this.requestId, showDetails, list, status);
    }
    
    @Override
    public void onShowDetailsFetched(final ShowDetails showDetails, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onShowDetailsFetched");
            return;
        }
        netflixServiceCallback.onShowDetailsFetched(this.requestId, showDetails, status);
    }
    
    @Override
    public void onSimilarVideosFetched(final SearchVideoListProvider searchVideoListProvider, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onSimilarVideosFetched");
            return;
        }
        netflixServiceCallback.onSimilarVideosFetched(this.requestId, searchVideoListProvider, status);
    }
    
    @Override
    public void onVideoHide(final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onVideoHide");
            return;
        }
        netflixServiceCallback.onVideoHide(this.requestId, status);
    }
    
    @Override
    public void onVideoRatingSet(final RatingInfo ratingInfo, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onVideoRatingSet");
            return;
        }
        netflixServiceCallback.onVideoRatingSet(this.requestId, ratingInfo, status);
    }
    
    @Override
    public void onVideoSummaryFetched(final Video$Summary video$Summary, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onVideoSummaryFetched");
            return;
        }
        netflixServiceCallback.onVideoSummaryFetched(this.requestId, video$Summary, status);
    }
    
    @Override
    public void onVideosFetched(final List<Video> list, final Status status) {
        final INetflixServiceCallback netflixServiceCallback = (INetflixServiceCallback)this.this$0.mClientCallbacks.get(this.clientId);
        if (netflixServiceCallback == null) {
            Log.w("FalkorAccess", "No client callback found for onVideosFetched");
            return;
        }
        netflixServiceCallback.onVideosFetched(this.requestId, list, status);
    }
}
