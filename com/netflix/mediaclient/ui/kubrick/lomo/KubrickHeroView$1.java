// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kubrick.lomo;

import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.servicemgr.model.Video;
import com.netflix.mediaclient.ui.common.PlayContextImp;
import com.netflix.mediaclient.servicemgr.model.trackable.Trackable;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import android.content.res.Resources;
import com.netflix.mediaclient.util.TimeUtils;
import com.netflix.mediaclient.servicemgr.model.VideoType;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.model.HdEnabledProvider;
import com.netflix.mediaclient.servicemgr.model.Ratable;
import com.netflix.mediaclient.servicemgr.model.LoMoUtils;
import com.netflix.mediaclient.ui.common.PlayContextProvider;
import com.netflix.mediaclient.util.DeviceUtils;
import android.widget.RelativeLayout$LayoutParams;
import android.view.ViewGroup;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.util.ViewUtils;
import android.util.AttributeSet;
import android.content.Context;
import com.netflix.mediaclient.android.widget.AdvancedImageView;
import android.content.BroadcastReceiver;
import com.netflix.mediaclient.ui.details.NetflixRatingBar;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.android.widget.TopCropImageView;
import android.view.View;
import com.netflix.mediaclient.android.widget.VideoDetailsClickListener;
import android.widget.TextView;
import com.netflix.mediaclient.servicemgr.model.KubrickVideo;
import com.netflix.mediaclient.ui.lomo.VideoViewGroup$IVideoView;
import android.widget.RelativeLayout;
import com.netflix.mediaclient.Log;
import android.view.ViewTreeObserver$OnGlobalLayoutListener;

class KubrickHeroView$1 implements ViewTreeObserver$OnGlobalLayoutListener
{
    final /* synthetic */ KubrickHeroView this$0;
    
    KubrickHeroView$1(final KubrickHeroView this$0) {
        this.this$0 = this$0;
    }
    
    public void onGlobalLayout() {
        final int height = this.this$0.infoGroup.getHeight();
        if (Log.isLoggable("KubrickHeroView", 2)) {
            Log.v("KubrickHeroView", "Setting shadow height to: " + height);
        }
        this.this$0.shadow.getLayoutParams().height = height;
    }
}
