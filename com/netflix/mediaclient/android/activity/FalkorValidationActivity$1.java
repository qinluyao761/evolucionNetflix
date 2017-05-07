// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.android.activity;

import com.netflix.mediaclient.servicemgr.model.details.PostPlayContext;
import com.netflix.mediaclient.service.webclient.model.PostPlayVideo$PostPlayContext;
import com.netflix.mediaclient.service.webclient.model.PostPlayVideo;
import com.netflix.mediaclient.service.webclient.model.PostPlayVideosProvider;
import com.netflix.mediaclient.servicemgr.model.LoLoMo;
import com.netflix.mediaclient.service.webclient.model.leafs.LoLoMoSummary;
import com.netflix.mediaclient.service.webclient.model.KidsCharacterDetails;
import com.netflix.mediaclient.android.app.NetflixImmutableStatus;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.android.app.NetflixStatus;
import com.netflix.mediaclient.servicemgr.model.UserRating;
import com.netflix.mediaclient.service.webclient.model.branches.Video$UserRating;
import com.netflix.mediaclient.servicemgr.model.search.SearchVideoListProvider;
import com.netflix.mediaclient.service.webclient.model.SearchVideoList;
import com.netflix.mediaclient.service.webclient.model.SearchSuggestion;
import com.netflix.mediaclient.service.webclient.model.SearchPerson;
import com.netflix.mediaclient.service.webclient.model.SearchVideo;
import com.netflix.mediaclient.servicemgr.model.trackable.SearchTrackable;
import com.netflix.mediaclient.service.webclient.model.leafs.SearchTrackableListSummary;
import com.netflix.mediaclient.servicemgr.model.search.ISearchResults;
import com.netflix.mediaclient.service.webclient.model.SearchResults;
import com.netflix.mediaclient.service.webclient.model.EpisodeDetails;
import com.netflix.mediaclient.service.webclient.model.SeasonDetails;
import com.netflix.mediaclient.servicemgr.model.details.KubrickShowDetails;
import com.netflix.mediaclient.service.webclient.model.ShowDetails;
import com.netflix.mediaclient.servicemgr.model.Playable;
import com.netflix.mediaclient.service.webclient.model.MovieDetails;
import com.netflix.mediaclient.servicemgr.model.genre.GenreList;
import com.netflix.mediaclient.service.webclient.model.leafs.ListOfGenreSummary;
import com.netflix.mediaclient.service.webclient.model.leafs.social.SocialGroupPlaceholder;
import com.netflix.mediaclient.service.webclient.model.leafs.social.SocialFriendPlaceholder;
import com.netflix.mediaclient.servicemgr.model.FriendProfilesProvider;
import com.netflix.mediaclient.service.webclient.model.leafs.social.SocialConnectPlaceholder;
import com.netflix.mediaclient.service.webclient.model.branches.KubrickVideo;
import com.netflix.mediaclient.servicemgr.model.Video;
import com.netflix.mediaclient.service.webclient.model.branches.Video$Summary;
import com.netflix.mediaclient.servicemgr.model.Billboard;
import com.netflix.mediaclient.service.webclient.model.BillboardDetails;
import com.netflix.mediaclient.service.webclient.model.CWVideo;
import com.netflix.mediaclient.servicemgr.model.LoMo;
import com.netflix.mediaclient.service.webclient.model.leafs.ListOfMoviesSummary;
import java.util.HashMap;

final class FalkorValidationActivity$1 extends HashMap<Class<?>, Class<?>[]>
{
    FalkorValidationActivity$1() {
        ((HashMap<Class<String>, Class[]>)this).put(String.class, new Class[] { CharSequence.class });
        ((HashMap<Class<ListOfMoviesSummary>, Class[]>)this).put(ListOfMoviesSummary.class, new Class[] { LoMo.class });
        ((HashMap<Class<CWVideo>, Class[]>)this).put(CWVideo.class, new Class[] { com.netflix.mediaclient.servicemgr.model.CWVideo.class });
        ((HashMap<Class<BillboardDetails>, Class[]>)this).put(BillboardDetails.class, new Class[] { Billboard.class });
        ((HashMap<Class<Video$Summary>, Class[]>)this).put(Video$Summary.class, new Class[] { Video.class });
        ((HashMap<Class<KubrickVideo>, Class[]>)this).put(KubrickVideo.class, new Class[] { com.netflix.mediaclient.servicemgr.model.KubrickVideo.class });
        ((HashMap<Class<SocialConnectPlaceholder>, Class[]>)this).put(SocialConnectPlaceholder.class, new Class[] { Video.class, FriendProfilesProvider.class });
        ((HashMap<Class<SocialFriendPlaceholder>, Class[]>)this).put(SocialFriendPlaceholder.class, new Class[] { Video.class, FriendProfilesProvider.class });
        ((HashMap<Class<SocialGroupPlaceholder>, Class[]>)this).put(SocialGroupPlaceholder.class, new Class[] { Video.class, FriendProfilesProvider.class });
        ((HashMap<Class<ListOfGenreSummary>, Class[]>)this).put(ListOfGenreSummary.class, new Class[] { GenreList.class });
        ((HashMap<Class<MovieDetails>, Class[]>)this).put(MovieDetails.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.MovieDetails.class, Playable.class });
        ((HashMap<Class<ShowDetails>, Class[]>)this).put(ShowDetails.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.ShowDetails.class, KubrickShowDetails.class, Playable.class });
        ((HashMap<Class<SeasonDetails>, Class[]>)this).put(SeasonDetails.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.SeasonDetails.class });
        ((HashMap<Class<EpisodeDetails>, Class[]>)this).put(EpisodeDetails.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.EpisodeDetails.class, Playable.class });
        ((HashMap<Class<SearchResults>, Class[]>)this).put(SearchResults.class, new Class[] { ISearchResults.class });
        ((HashMap<Class<SearchTrackableListSummary>, Class[]>)this).put(SearchTrackableListSummary.class, new Class[] { SearchTrackable.class });
        ((HashMap<Class<SearchVideo>, Class[]>)this).put(SearchVideo.class, new Class[] { com.netflix.mediaclient.servicemgr.model.search.SearchVideo.class });
        ((HashMap<Class<SearchPerson>, Class[]>)this).put(SearchPerson.class, new Class[] { com.netflix.mediaclient.servicemgr.model.search.SearchPerson.class });
        ((HashMap<Class<SearchSuggestion>, Class[]>)this).put(SearchSuggestion.class, new Class[] { com.netflix.mediaclient.servicemgr.model.search.SearchSuggestion.class });
        ((HashMap<Class<SearchVideoList>, Class[]>)this).put(SearchVideoList.class, new Class[] { SearchVideoListProvider.class });
        ((HashMap<Class<Video$UserRating>, Class[]>)this).put(Video$UserRating.class, new Class[] { UserRating.class });
        ((HashMap<Class<NetflixStatus>, Class[]>)this).put(NetflixStatus.class, new Class[] { Status.class });
        ((HashMap<Class<NetflixImmutableStatus>, Class[]>)this).put(NetflixImmutableStatus.class, new Class[] { Status.class });
        ((HashMap<Class<KidsCharacterDetails>, Class[]>)this).put(KidsCharacterDetails.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.KidsCharacterDetails.class, Playable.class });
        ((HashMap<Class<LoLoMoSummary>, Class[]>)this).put(LoLoMoSummary.class, new Class[] { LoLoMo.class });
        ((HashMap<Class<PostPlayVideosProvider>, Class[]>)this).put(PostPlayVideosProvider.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.PostPlayVideosProvider.class });
        ((HashMap<Class<PostPlayVideo>, Class[]>)this).put(PostPlayVideo.class, new Class[] { com.netflix.mediaclient.servicemgr.model.details.PostPlayVideo.class, Playable.class });
        ((HashMap<Class<PostPlayVideo$PostPlayContext>, Class[]>)this).put(PostPlayVideo$PostPlayContext.class, new Class[] { PostPlayContext.class });
    }
}
