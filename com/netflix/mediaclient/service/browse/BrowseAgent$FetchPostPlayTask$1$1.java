// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.browse;

import com.netflix.mediaclient.servicemgr.model.details.PostPlayVideo;
import com.netflix.mediaclient.android.app.Status;
import java.util.List;

class BrowseAgent$FetchPostPlayTask$1$1 implements Runnable
{
    final /* synthetic */ BrowseAgent$FetchPostPlayTask$1 this$2;
    final /* synthetic */ List val$postPlayVideos;
    final /* synthetic */ Status val$res;
    
    BrowseAgent$FetchPostPlayTask$1$1(final BrowseAgent$FetchPostPlayTask$1 this$2, final List val$postPlayVideos, final Status val$res) {
        this.this$2 = this$2;
        this.val$postPlayVideos = val$postPlayVideos;
        this.val$res = val$res;
    }
    
    @Override
    public void run() {
        this.this$2.this$1.getCallback().onPostPlayVideosFetched(this.val$postPlayVideos, this.val$res);
    }
}