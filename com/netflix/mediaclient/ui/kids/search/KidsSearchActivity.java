// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kids.search;

import com.netflix.mediaclient.util.ViewUtils;
import android.os.Bundle;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.ui.search.SearchActivity;

public class KidsSearchActivity extends SearchActivity
{
    @Override
    protected NetflixActionBar createActionBar() {
        return new KidsSearchActionBar(this);
    }
    
    @Override
    protected int getInitMessageStringId() {
        return 2131492965;
    }
    
    @Override
    public boolean isForKids() {
        return true;
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setBackgroundDrawableResource(2130837716);
        this.leWrapper.getErrorMessageTextView().setTextColor(this.getResources().getColor(2131296358));
        ViewUtils.clearShadow(this.leWrapper.getErrorMessageTextView());
    }
}
