// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.pushnotification;

import android.net.Uri;
import android.app.Notification$BigTextStyle;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.util.StringUtils;
import android.app.Notification$BigPictureStyle;
import android.annotation.TargetApi;
import com.netflix.mediaclient.util.ViewUtils;
import com.netflix.mediaclient.util.AndroidUtils;
import android.graphics.Bitmap;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.servicemgr.ErrorLogging;
import android.content.Context;
import android.app.Notification$Builder;
import com.netflix.mediaclient.util.gfx.ImageLoader$ImageLoaderListener;

final class NotificationBuilderJellyBean$1 implements ImageLoader$ImageLoaderListener
{
    final /* synthetic */ Notification$Builder val$builder;
    final /* synthetic */ Context val$context;
    final /* synthetic */ ErrorLogging val$errorLogger;
    final /* synthetic */ ImageLoader val$imageLoader;
    final /* synthetic */ int val$msgId;
    final /* synthetic */ Payload val$payload;
    
    NotificationBuilderJellyBean$1(final Payload val$payload, final Notification$Builder val$builder, final Context val$context, final int val$msgId, final ImageLoader val$imageLoader, final ErrorLogging val$errorLogger) {
        this.val$payload = val$payload;
        this.val$builder = val$builder;
        this.val$context = val$context;
        this.val$msgId = val$msgId;
        this.val$imageLoader = val$imageLoader;
        this.val$errorLogger = val$errorLogger;
    }
    
    @Override
    public void onErrorResponse(final String s) {
        if (Log.isLoggable("nf_push", 6)) {
            Log.e("nf_push", "Failed to downlod " + this.val$payload.largeIcon + ". Reason: " + s);
        }
        addBigView(this.val$context, this.val$payload, this.val$builder, this.val$msgId, this.val$imageLoader, this.val$errorLogger);
    }
    
    @Override
    public void onResponse(Bitmap squaredBitmap, final String s) {
        if (Log.isLoggable("nf_push", 3)) {
            Log.d("nf_push", "Image is downloaded " + this.val$payload.largeIcon + " from " + s);
        }
        if (squaredBitmap != null) {
            if (AndroidUtils.getAndroidVersion() >= 21) {
                squaredBitmap = ViewUtils.createSquaredBitmap(squaredBitmap);
                this.val$builder.setLargeIcon(squaredBitmap);
                this.val$builder.setColor(this.val$context.getResources().getColor(2131296356));
            }
            else {
                this.val$builder.setLargeIcon(squaredBitmap);
            }
        }
        addBigView(this.val$context, this.val$payload, this.val$builder, this.val$msgId, this.val$imageLoader, this.val$errorLogger);
        Log.d("nf_push", "Large icon image set!");
    }
}