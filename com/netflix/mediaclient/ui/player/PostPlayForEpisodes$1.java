// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.player;

import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import android.content.Context;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.servicemgr.model.details.InterestingVideoDetails;
import java.util.List;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.ui.common.PlayContextImp;
import com.netflix.mediaclient.servicemgr.model.details.PostPlayVideo;
import com.netflix.mediaclient.util.ViewUtils;
import com.netflix.mediaclient.util.ViewUtils$Visibility;
import com.netflix.mediaclient.android.widget.AdvancedImageView;
import android.widget.TextView;
import android.view.View;
import com.netflix.mediaclient.Log;

class PostPlayForEpisodes$1 implements Runnable
{
    final /* synthetic */ PostPlayForEpisodes this$0;
    
    PostPlayForEpisodes$1(final PostPlayForEpisodes this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void run() {
        if (!this.this$0.mInPostPlay || this.this$0.mContext.destroyed()) {
            Log.d("nf_postplay", "post play timer exit or activity is destroyed");
            return;
        }
        this.this$0.mTimer--;
        if (this.this$0.mTimerView != null) {
            this.this$0.mTimerView.setText((CharSequence)String.valueOf(this.this$0.mTimer));
        }
        if (this.this$0.mTimer <= 0) {
            Log.d("nf_postplay", "Timer counter to 0, play next episode");
            this.this$0.onTimerEnd();
            return;
        }
        this.this$0.mContext.getHandler().postDelayed((Runnable)this, 1000L);
    }
}