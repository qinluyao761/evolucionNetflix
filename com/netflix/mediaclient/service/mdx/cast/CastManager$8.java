// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.mdx.cast;

import com.netflix.mediaclient.service.configuration.SettingsConfiguration;
import android.support.v7.media.MediaRouter$ProviderInfo;
import com.netflix.mediaclient.util.StringUtils;
import org.json.JSONException;
import com.netflix.mediaclient.Log;
import org.json.JSONObject;
import com.google.android.gms.cast.CastDevice;
import java.util.ArrayList;
import org.json.JSONArray;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouteSelector;
import com.netflix.mediaclient.service.mdx.MdxNrdpLogger;
import android.os.Handler;
import android.support.v7.media.MediaRouter$RouteInfo;
import java.util.List;
import android.content.Context;
import android.support.v7.media.MediaRouter$Callback;

class CastManager$8 implements Runnable
{
    final /* synthetic */ CastManager this$0;
    final /* synthetic */ boolean val$success;
    final /* synthetic */ String val$uuid;
    
    CastManager$8(final CastManager this$0, final boolean val$success, final String val$uuid) {
        this.this$0 = this$0;
        this.val$success = val$success;
        this.val$uuid = val$uuid;
    }
    
    @Override
    public void run() {
        this.this$0.nativeSendMessageResult(this.val$success, this.val$uuid);
    }
}