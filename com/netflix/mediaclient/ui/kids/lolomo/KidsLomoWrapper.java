// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kids.lolomo;

import android.os.Parcel;
import com.netflix.mediaclient.servicemgr.model.LoMoType;
import com.netflix.mediaclient.servicemgr.model.user.FriendProfile;
import java.util.List;
import com.netflix.mediaclient.servicemgr.model.BasicLoMo;
import com.netflix.mediaclient.servicemgr.model.LoMo;

public class KidsLomoWrapper implements LoMo
{
    private final BasicLoMo lomo;
    
    public KidsLomoWrapper(final BasicLoMo lomo) {
        this.lomo = lomo;
    }
    
    public int describeContents() {
        throw new UnsupportedOperationException("Can't describe contents");
    }
    
    public List<FriendProfile> getFacebookFriends() {
        return null;
    }
    
    public int getHeroTrackId() {
        return this.lomo.getHeroTrackId();
    }
    
    public String getId() {
        return this.lomo.getId();
    }
    
    public int getListPos() {
        return this.lomo.getListPos();
    }
    
    @Override
    public List<String> getMoreImages() {
        return null;
    }
    
    @Override
    public int getNumVideos() {
        return -1;
    }
    
    public String getRequestId() {
        return this.lomo.getRequestId();
    }
    
    public String getTitle() {
        return this.lomo.getTitle();
    }
    
    public int getTrackId() {
        return this.lomo.getTrackId();
    }
    
    public LoMoType getType() {
        return this.lomo.getType();
    }
    
    @Override
    public boolean isBillboard() {
        return false;
    }
    
    public boolean isHero() {
        return this.lomo.isHero();
    }
    
    @Override
    public void setId(final String s) {
    }
    
    @Override
    public void setListPos(final int n) {
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        throw new UnsupportedOperationException("Can't write to parcel");
    }
}
