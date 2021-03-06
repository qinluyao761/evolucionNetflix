// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.barker.details;

import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.interface_.details.SeasonDetails;
import com.netflix.mediaclient.android.app.CommonStatus;
import java.util.Collection;
import android.support.v7.widget.GridLayoutManager$SpanSizeLookup;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView$ItemDecoration;
import android.annotation.SuppressLint;
import com.netflix.mediaclient.ui.experience.BrowseExperience;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import android.view.View$OnClickListener;
import android.support.v7.widget.RecyclerView$OnScrollListener;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView$Adapter;
import android.support.v7.widget.RecyclerView$LayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.FrameLayout$LayoutParams;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.interface_.details.Similarable;
import android.widget.AdapterView$OnItemSelectedListener;
import com.netflix.mediaclient.ui.barker.BarkerUtils;
import android.graphics.drawable.Drawable;
import com.netflix.mediaclient.util.api.Api16Util;
import android.os.Parcelable;
import com.netflix.mediaclient.android.fragment.NetflixDialogFrag;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import com.netflix.mediaclient.servicemgr.interface_.details.ShowDetails;
import com.netflix.mediaclient.android.app.Status;
import android.view.ViewGroup;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup;
import com.netflix.mediaclient.android.widget.LoadingAndErrorWrapper;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter$IViewCreator;
import android.support.v7.widget.RecyclerView;
import java.util.Stack;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener;
import com.netflix.mediaclient.util.ItemDecorationBarkerGrid;
import android.view.View;
import com.netflix.mediaclient.servicemgr.interface_.details.EpisodeDetails;
import java.util.List;
import com.netflix.mediaclient.ui.details.CopyrightView;
import com.netflix.mediaclient.ui.mdx.CastPlayerHelper$CastPlayerDialog;
import com.netflix.mediaclient.ui.details.ServiceManagerProvider;
import com.netflix.mediaclient.ui.details.IHandleBackPress;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;
import com.netflix.mediaclient.android.widget.ErrorWrapper$Callback;
import com.netflix.mediaclient.ui.details.EpisodesFrag;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import com.netflix.mediaclient.ui.details.DetailsActivity;
import android.content.Context;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.ui.common.PlayContextProvider;
import com.netflix.mediaclient.android.activity.NetflixActivity;

public class BarkerShowDetailsFrag$BarkerRelatedVideoDetailsClickListener extends BarkerVideoDetailsClickListener
{
    final /* synthetic */ BarkerShowDetailsFrag this$0;
    
    public BarkerShowDetailsFrag$BarkerRelatedVideoDetailsClickListener(final BarkerShowDetailsFrag this$0, final NetflixActivity netflixActivity, final PlayContextProvider playContextProvider) {
        this.this$0 = this$0;
        super(netflixActivity, playContextProvider);
    }
    
    private void saveCurrentTitleState() {
        this.this$0.relatedTitlesHistory.push(new RelatedTitleState(this.this$0.showId, this.this$0.recyclerView.getLayoutManager().onSaveInstanceState(), this.this$0.currSeasonIndex, DeviceUtils.getBasicScreenOrientation((Context)this.this$0.getActivity()), ((DetailsActivity)this.this$0.getActivity()).getPlayContext()));
    }
    
    @Override
    protected void launchDetailsActivity(final NetflixActivity netflixActivity, final Video video, final PlayContext playContext) {
        if (video.getType() == VideoType.SHOW) {
            this.this$0.leWrapper.showLoadingView(false);
            this.saveCurrentTitleState();
            this.this$0.heroSlideshow.stop(true);
            ((BarkerVideoDetailsViewGroup)this.this$0.detailsViewGroup).clearHeroImages();
            this.this$0.showId = video.getId();
            this.this$0.currSeasonIndex = -1;
            this.this$0.fetchShowDetailsAndSeasons();
        }
        else {
            super.launchDetailsActivity(netflixActivity, video, playContext);
        }
        this.this$0.isFromRelated = true;
    }
}
