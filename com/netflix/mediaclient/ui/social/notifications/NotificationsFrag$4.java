// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.social.notifications;

import android.os.Parcelable;
import android.widget.ListAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.netflix.mediaclient.util.SocialUtils;
import android.os.Bundle;
import android.app.Activity;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import com.netflix.mediaclient.ui.player.PlayerActivity;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.StatusCode;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import java.util.HashSet;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.model.leafs.social.SocialNotificationSummary;
import java.util.Set;
import com.netflix.mediaclient.android.widget.StaticListView;
import com.netflix.mediaclient.servicemgr.interface_.search.SocialNotificationsList;
import com.netflix.mediaclient.android.widget.ErrorWrapper$Callback;
import com.netflix.mediaclient.android.fragment.NetflixFrag;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

class NotificationsFrag$4 extends BroadcastReceiver
{
    final /* synthetic */ NotificationsFrag this$0;
    
    NotificationsFrag$4(final NotificationsFrag this$0) {
        this.this$0 = this$0;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        if (this.this$0.mNotificationsList != null && this.this$0.mNotificationsList.getFirstVisiblePosition() == 0) {
            this.this$0.fetchNotificationsList(false);
            return;
        }
        this.this$0.mWasRefreshForTopViewScheduled = true;
    }
}