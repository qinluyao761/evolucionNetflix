// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.ArrayList;
import android.app.Notification;

class NotificationCompat$NotificationCompatImplBase implements NotificationCompat$NotificationCompatImpl
{
    @Override
    public Notification build(final NotificationCompat$Builder notificationCompat$Builder, final NotificationCompat$BuilderExtender notificationCompat$BuilderExtender) {
        final Notification add = NotificationCompatBase.add(notificationCompat$Builder.mNotification, notificationCompat$Builder.mContext, notificationCompat$Builder.resolveTitle(), notificationCompat$Builder.resolveText(), notificationCompat$Builder.mContentIntent, notificationCompat$Builder.mFullScreenIntent);
        if (notificationCompat$Builder.mPriority > 0) {
            add.flags |= 0x80;
        }
        if (notificationCompat$Builder.mContentView != null) {
            add.contentView = notificationCompat$Builder.mContentView;
        }
        return add;
    }
    
    @Override
    public NotificationCompat$Action getAction(final Notification notification, final int n) {
        return null;
    }
    
    @Override
    public int getActionCount(final Notification notification) {
        return 0;
    }
    
    @Override
    public NotificationCompat$Action[] getActionsFromParcelableArrayList(final ArrayList<Parcelable> list) {
        return null;
    }
    
    @Override
    public Bundle getBundleForUnreadConversation(final NotificationCompatBase$UnreadConversation notificationCompatBase$UnreadConversation) {
        return null;
    }
    
    @Override
    public String getCategory(final Notification notification) {
        return null;
    }
    
    @Override
    public Bundle getExtras(final Notification notification) {
        return null;
    }
    
    @Override
    public String getGroup(final Notification notification) {
        return null;
    }
    
    @Override
    public boolean getLocalOnly(final Notification notification) {
        return false;
    }
    
    @Override
    public ArrayList<Parcelable> getParcelableArrayListForActions(final NotificationCompat$Action[] array) {
        return null;
    }
    
    @Override
    public String getSortKey(final Notification notification) {
        return null;
    }
    
    @Override
    public NotificationCompatBase$UnreadConversation getUnreadConversationFromBundle(final Bundle bundle, final NotificationCompatBase$UnreadConversation$Factory notificationCompatBase$UnreadConversation$Factory, final RemoteInputCompatBase$RemoteInput$Factory remoteInputCompatBase$RemoteInput$Factory) {
        return null;
    }
    
    @Override
    public boolean isGroupSummary(final Notification notification) {
        return false;
    }
}
