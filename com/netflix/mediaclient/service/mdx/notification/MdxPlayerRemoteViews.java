// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.mdx.notification;

import android.widget.RemoteViews;
import android.content.Context;

public final class MdxPlayerRemoteViews extends MdxRemoteViews
{
    private static final int SKIPBACK_SECONDS = -30;
    private String headerTitle;
    
    public MdxPlayerRemoteViews(final String s, final boolean b, final MdxNotificationManager.MdxNotificationIntentRetriever mdxNotificationIntentRetriever, final Context headerTitle) {
        super(s, b, mdxNotificationIntentRetriever, headerTitle);
        this.setHeaderTitle(headerTitle);
    }
    
    private void setHeaderTitle(final Context context) {
        if (context == null) {
            return;
        }
        this.headerTitle = context.getResources().getString(2131493242);
    }
    
    @Override
    protected RemoteViews createViewForEpisodes(final boolean b) {
        if (b) {
            return new RemoteViews(this.mPackageName, 2130903134);
        }
        return new RemoteViews(this.mPackageName, 2130903133);
    }
    
    @Override
    protected String getHeader() {
        return this.headerTitle;
    }
    
    protected void setSkipActive(final RemoteViews remoteViews) {
        if (remoteViews == null || this.mIntentRetriever == null) {
            return;
        }
        remoteViews.setImageViewResource(2131165502, 2130837808);
        remoteViews.setOnClickPendingIntent(2131165502, this.mIntentRetriever.getSkipbackIntent(-30));
    }
    
    protected void setSkipInactive(final RemoteViews remoteViews) {
        if (remoteViews == null || this.mIntentRetriever == null) {
            return;
        }
        remoteViews.setImageViewResource(2131165502, 2130837809);
        remoteViews.setOnClickPendingIntent(2131165502, this.mIntentRetriever.getNoActionIntent());
    }
    
    @Override
    public void setState(final boolean mPaused, final boolean mInTransition) {
        this.mInTransition = mInTransition;
        this.mPaused = mPaused;
        if (this.mNormalRemoteView != null) {
            this.updateControl(this.mNormalRemoteView);
        }
        if (this.mExpandedRemoteView != null) {
            this.updateControl(this.mExpandedRemoteView);
        }
    }
    
    @Override
    protected void updateControlAsActive(final RemoteViews skipActive) {
        super.updateControlAsActive(skipActive);
        this.setSkipActive(skipActive);
    }
    
    @Override
    protected void updateControlsAsInactive(final RemoteViews skipInactive) {
        super.updateControlsAsInactive(skipInactive);
        this.setSkipInactive(skipInactive);
    }
}
