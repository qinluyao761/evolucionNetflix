// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.details;

import com.netflix.mediaclient.android.widget.RecyclerViewHeaderAdapter;
import android.support.v7.widget.GridLayoutManager$SpanSizeLookup;

class MovieDetailsFrag$5 extends GridLayoutManager$SpanSizeLookup
{
    final /* synthetic */ MovieDetailsFrag this$0;
    
    MovieDetailsFrag$5(final MovieDetailsFrag this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public int getSpanSize(final int n) {
        if (((RecyclerViewHeaderAdapter)this.this$0.recyclerView.getAdapter()).isPositionFooter(n)) {
            return this.this$0.numColumns;
        }
        if (n == 0) {
            return this.this$0.numColumns;
        }
        return 1;
    }
}
