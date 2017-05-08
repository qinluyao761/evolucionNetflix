// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.offline.agent;

import com.netflix.mediaclient.util.UserVisibleErrorCodeGenerator;
import com.netflix.mediaclient.servicemgr.interface_.offline.StopReason;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.util.ThreadUtils;
import com.netflix.mediaclient.ui.player.PlayerActivity;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.ui.offline.OfflineActivity;
import android.support.v4.app.NotificationCompat$Style;
import android.support.v4.app.NotificationCompat$BigTextStyle;
import android.support.v4.app.NotificationCompat$Action;
import android.app.Notification$Style;
import android.app.Notification$BigTextStyle;
import com.netflix.mediaclient.util.StringUtils;
import android.text.format.Formatter;
import com.netflix.mediaclient.servicemgr.interface_.Playable;
import com.netflix.mediaclient.servicemgr.interface_.offline.realm.RealmVideoDetails;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.util.l10n.LocalizationUtils;
import com.netflix.mediaclient.servicemgr.interface_.offline.realm.RealmUtils;
import com.netflix.mediaclient.service.NetflixService;
import android.app.PendingIntent;
import android.content.Intent;
import android.app.NotificationManager;
import android.support.v4.content.ContextCompat;
import android.os.Build$VERSION;
import android.app.Notification;
import android.app.Notification$Builder;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflinePlayableViewData;
import android.os.Handler;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.servicemgr.INetflixService;
import android.support.v4.app.NotificationCompat$Builder;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import android.content.Context;
import com.netflix.mediaclient.Log;
import android.graphics.Bitmap;
import com.netflix.mediaclient.util.gfx.ImageLoader$ImageLoaderListener;

class DownloadNotificationManager$2 implements ImageLoader$ImageLoaderListener
{
    final /* synthetic */ DownloadNotificationManager this$0;
    
    DownloadNotificationManager$2(final DownloadNotificationManager this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void onErrorResponse(final String s) {
        this.this$0.mBoxShot = null;
    }
    
    @Override
    public void onResponse(final Bitmap bitmap, final String s) {
        boolean b = true;
        if (bitmap == null) {
            b = false;
        }
        Log.i("nf_downloadNotification", "fetchImage onResponse gotImage=%b", b);
        this.this$0.mBoxShot = bitmap;
    }
}