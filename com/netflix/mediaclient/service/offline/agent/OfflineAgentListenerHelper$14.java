// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.offline.agent;

import com.netflix.mediaclient.servicemgr.interface_.offline.StopReason;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflinePlayableViewData;
import com.netflix.mediaclient.android.app.Status;
import android.os.Handler;
import java.util.Iterator;
import com.netflix.mediaclient.service.NetflixPowerManager$PartialWakeLockReason;
import java.util.ArrayList;
import java.util.List;
import com.netflix.mediaclient.service.NetflixPowerManager;
import com.netflix.mediaclient.Log;

class OfflineAgentListenerHelper$14 implements Runnable
{
    final /* synthetic */ OfflineAgentListenerHelper this$0;
    final /* synthetic */ OfflineAgentListener val$listener;
    
    OfflineAgentListenerHelper$14(final OfflineAgentListenerHelper this$0, final OfflineAgentListener val$listener) {
        this.this$0 = this$0;
        this.val$listener = val$listener;
    }
    
    @Override
    public void run() {
        this.this$0.mOfflineAgentListeners.remove(this.val$listener);
        this.this$0.removeDeadListeners();
        Log.i("nf_offlineAgent", "removeOfflineAgentListener after count=%d", this.this$0.mOfflineAgentListeners.size());
    }
}
