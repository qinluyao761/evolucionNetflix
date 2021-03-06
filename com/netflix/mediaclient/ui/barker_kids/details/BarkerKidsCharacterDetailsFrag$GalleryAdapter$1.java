// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.barker_kids.details;

import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.servicemgr.interface_.details.SeasonDetails;
import java.util.List;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import android.support.v7.widget.RecyclerView$Adapter;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup$DetailsStringProvider;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import java.util.Collection;
import com.netflix.mediaclient.servicemgr.interface_.details.VideoDetails;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter;
import com.netflix.mediaclient.ui.details.SeasonsSpinnerAdapter;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener$IScrollStateChanged;
import android.support.v7.widget.RecyclerView$OnScrollListener;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.servicemgr.interface_.details.ShowDetails;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.Log;
import android.os.Bundle;
import android.app.Fragment;
import com.netflix.mediaclient.ui.details.VideoDetailsViewGroup;
import android.view.ViewGroup;
import com.netflix.mediaclient.android.app.Status;
import android.support.v7.widget.RecyclerView;
import com.netflix.mediaclient.ui.details.DetailsPageParallaxScrollListener;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.ui.details.SeasonsSpinner;
import com.netflix.mediaclient.ui.barker.details.BarkerShowDetailsFrag$HeroSlideshow;
import com.netflix.mediaclient.servicemgr.interface_.details.KidsCharacterDetails;
import android.annotation.SuppressLint;
import android.view.ViewGroup$LayoutParams;
import android.widget.AbsListView$LayoutParams;
import android.widget.ImageView$ScaleType;
import com.netflix.mediaclient.android.widget.VideoView;
import android.view.View;
import android.content.Context;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter$IViewCreator;

class BarkerKidsCharacterDetailsFrag$GalleryAdapter$1 implements RecyclerViewHeaderAdapter$IViewCreator
{
    final /* synthetic */ BarkerKidsCharacterDetailsFrag val$this$0;
    
    BarkerKidsCharacterDetailsFrag$GalleryAdapter$1(final BarkerKidsCharacterDetailsFrag val$this$0) {
        this.val$this$0 = val$this$0;
    }
    
    private int getImageHeight() {
        final float n = this.val$this$0.getActivity().getResources().getDimensionPixelOffset(2131427767);
        final float n2 = this.val$this$0.getNumColumns();
        float n3;
        if (this.val$this$0.shouldRenderAsSDP) {
            n3 = 0.5625f;
        }
        else {
            n3 = 1.43f;
        }
        return (int)(n3 * ((KidsUtils.getDetailsPageContentWidth((Context)this.val$this$0.getActivity()) - (n2 + 1.0f) * n) / this.val$this$0.getNumColumns()));
    }
    
    @Override
    public View createItemView() {
        final VideoView videoView = new VideoView(this.val$this$0.recyclerView.getContext());
        videoView.setAdjustViewBounds(true);
        videoView.setScaleType(ImageView$ScaleType.FIT_XY);
        videoView.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, this.getImageHeight()));
        videoView.setIsHorizontal(this.val$this$0.shouldRenderAsSDP);
        return (View)videoView;
    }
}
