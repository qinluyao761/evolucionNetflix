// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lomo;

import com.netflix.mediaclient.util.DataUtil;
import com.netflix.mediaclient.servicemgr.interface_.trackable.Trackable;
import java.util.List;
import android.widget.LinearLayout$LayoutParams;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.l10n.LocalizationUtils;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.view.ViewGroup$LayoutParams;
import android.widget.AbsListView$LayoutParams;
import android.content.Context;
import android.widget.LinearLayout;
import com.netflix.mediaclient.servicemgr.interface_.Video;

public abstract class VideoViewGroup<T extends Video, V extends View> extends LinearLayout
{
    protected static final String TAG = "VideoViewGroup";
    protected boolean isRtlLocale;
    
    public VideoViewGroup(final Context context, final boolean b) {
        super(context);
        this.setId(2131689500);
        this.setLayoutParams((ViewGroup$LayoutParams)new AbsListView$LayoutParams(-1, -2));
        this.setOrientation(0);
        if (b) {
            LoMoUtils.applyContentOverlapPadding((NetflixActivity)this.getContext(), (View)this, this.getLomoWidthType());
        }
        this.isRtlLocale = LocalizationUtils.isCurrentLocaleRTL();
    }
    
    private int calculatePosition(final int n, final int n2) {
        int n3 = n;
        if (this.isRtlLocale) {
            n3 = n2 - n - 1;
            if (Log.isLoggable()) {
                Log.v("VideoViewGroup", "Calculate position for RTL: " + n3 + ", for original position " + n + ", items per page: " + n2);
            }
        }
        return n3;
    }
    
    protected abstract V createChildView(final Context p0);
    
    protected int getChildPaddingDimenResId() {
        return 2131362223;
    }
    
    protected LoMoUtils$LoMoWidthType getLomoWidthType() {
        return LoMoUtils$LoMoWidthType.STANDARD;
    }
    
    public void init(final int n) {
        final LinearLayout$LayoutParams linearLayout$LayoutParams = new LinearLayout$LayoutParams(-1, -1, 1.0f);
        final int dimensionPixelOffset = this.getResources().getDimensionPixelOffset(this.getChildPaddingDimenResId());
        for (int i = 0; i < n; ++i) {
            final View childView = this.createChildView(this.getContext());
            if (this.shouldApplyPaddingToChildren()) {
                childView.setPadding(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            }
            this.addView(childView, (ViewGroup$LayoutParams)linearLayout$LayoutParams);
        }
    }
    
    protected abstract boolean shouldApplyPaddingToChildren();
    
    public void updateDataThenViews(final List<T> list, final int n, final int n2, final int n3, final Trackable trackable) {
        if (Log.isLoggable()) {
            Log.v("VideoViewGroup", "Setting data, first: " + DataUtil.getFirstItemSafely(list) + ", hash: " + this.hashCode() + ", num per page: " + n + ", page: " + n2 + ", listViewPos: " + n3);
        }
        final int n4 = n2 * n;
        for (int i = 0; i < n; ++i) {
            final View child = this.getChildAt(i);
            if (child == null) {
                Log.w("VideoViewGroup", "Expected valid child but child is null, index: " + i);
            }
            else {
                this.updateViewIds((V)child, n3, n4, i);
                if (list != null && i < list.size()) {
                    final int calculatePosition = this.calculatePosition(i, n);
                    if (calculatePosition < list.size()) {
                        final Video video = (T)list.get(calculatePosition);
                        if (video != null) {
                            ((VideoViewGroup$IVideoView<Video>)child).update(video, trackable, n4 + i, n2 == 0, false);
                        }
                        else {
                            ((VideoViewGroup$IVideoView<Video>)child).hide();
                        }
                    }
                    else {
                        ((VideoViewGroup$IVideoView<Video>)child).hide();
                    }
                }
                else {
                    ((VideoViewGroup$IVideoView<Video>)child).hide();
                }
            }
        }
    }
    
    protected abstract void updateViewIds(final V p0, final int p1, final int p2, final int p3);
}
