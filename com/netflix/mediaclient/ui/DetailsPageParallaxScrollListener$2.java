// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui;

import android.view.animation.Animation$AnimationListener;
import android.view.animation.AlphaAnimation;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable$Orientation;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.util.AndroidUtils;
import android.graphics.drawable.Drawable;
import com.netflix.mediaclient.ui.details.SeasonsSpinner;
import android.view.View;
import android.widget.AbsListView;
import android.view.animation.Animation;
import android.widget.AbsListView$OnScrollListener;
import android.util.Log;

class DetailsPageParallaxScrollListener$2 implements Runnable
{
    final /* synthetic */ DetailsPageParallaxScrollListener this$0;
    final /* synthetic */ int val$seasonNumber;
    
    DetailsPageParallaxScrollListener$2(final DetailsPageParallaxScrollListener this$0, final int val$seasonNumber) {
        this.this$0 = this$0;
        this.val$seasonNumber = val$seasonNumber;
    }
    
    @Override
    public void run() {
        final int seasonIndexBySeasonNumber = this.this$0.seasonsSpinner.getSeasonIndexBySeasonNumber(this.val$seasonNumber);
        if (seasonIndexBySeasonNumber < 0) {
            Log.v("detailsScroller", "No valid season index found");
            return;
        }
        if (Log.isLoggable("detailsScroller", 2)) {
            Log.v("detailsScroller", "Setting current season to: " + seasonIndexBySeasonNumber);
        }
        this.this$0.seasonsSpinner.setSelection(seasonIndexBySeasonNumber, true);
    }
}
