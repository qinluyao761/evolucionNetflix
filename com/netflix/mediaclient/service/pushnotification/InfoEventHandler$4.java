// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.pushnotification;

import com.netflix.mediaclient.util.ParcelUtils;
import com.netflix.mediaclient.util.StringUtils;
import android.content.Intent;
import com.netflix.mediaclient.service.logging.client.model.Error;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import com.netflix.mediaclient.servicemgr.UserActionLogging$CommandName;
import com.netflix.mediaclient.util.log.UserActionLogUtils;
import com.netflix.mediaclient.service.NetflixService;
import com.netflix.mediaclient.Log;

class InfoEventHandler$4 implements Runnable
{
    final /* synthetic */ InfoEventHandler this$0;
    
    InfoEventHandler$4(final InfoEventHandler this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void run() {
        Log.i("nf_push_info", "Refreshing socialNotifications via runnable");
        if (InfoEventHandler.mService != null) {
            InfoEventHandler.mService.getBrowse().refreshSocialNotifications(true, false, null);
        }
    }
}
