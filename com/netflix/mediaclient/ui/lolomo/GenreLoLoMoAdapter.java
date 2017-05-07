// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lolomo;

import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;
import com.netflix.mediaclient.ui.lomo.PaginatedLoMoAdapter;
import android.content.Context;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.model.genre.Genre;

public class GenreLoLoMoAdapter extends BasePaginatedLoLoMoAdapter<Genre>
{
    private static final String TAG = "GenreLoLoMoAdapter";
    
    public GenreLoLoMoAdapter(final LoLoMoFrag loLoMoFrag, final String s) {
        super(loLoMoFrag, s);
    }
    
    private void handlePrefetchComplete() {
        super.refreshData();
    }
    
    @Override
    protected boolean isGenreList() {
        return true;
    }
    
    @Override
    protected void makeFetchRequest(final String s, final int n, final int n2, final ManagerCallback managerCallback) {
        this.getServiceManager().getBrowse().fetchGenres(s, n, n2, managerCallback);
    }
    
    @Override
    public void refreshData() {
        final ServiceManager serviceManager = this.getServiceManager();
        if (serviceManager == null) {
            Log.w("GenreLoLoMoAdapter", "Service man is null");
            return;
        }
        serviceManager.getBrowse().prefetchGenreLoLoMo(this.getGenreId(), 0, 19, 0, PaginatedLoMoAdapter.computeNumVideosToFetchPerBatch(this.activity, DeviceUtils.getScreenSizeCategory((Context)this.activity)) - 1, false, new LoggingManagerCallback("GenreLoLoMoAdapter") {
            @Override
            public void onGenreLoLoMoPrefetched(final Status status) {
                super.onGenreLoLoMoPrefetched(status);
                GenreLoLoMoAdapter.this.handlePrefetchComplete();
            }
        });
    }
}
