// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.webclient.model;

import com.netflix.mediaclient.servicemgr.model.VideoType;
import android.content.Context;
import com.netflix.mediaclient.service.webclient.model.branches.Season$Detail;

public class SeasonDetails implements com.netflix.mediaclient.servicemgr.model.details.SeasonDetails
{
    public Season$Detail detail;
    
    @Override
    public int getCurrentEpisodeNumber() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.getCurrentEpisodeNumber();
    }
    
    @Override
    public String getId() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.getId();
    }
    
    @Override
    public int getNumOfEpisodes() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.getEpisodeCount();
    }
    
    @Override
    public int getSeasonNumber() {
        if (this.detail == null) {
            return -1;
        }
        return this.detail.getNumber();
    }
    
    @Override
    public String getSeasonNumberTitle(final Context context) {
        return String.format(context.getString(2131493157), this.getSeasonNumber());
    }
    
    @Override
    public String getTitle() {
        if (this.detail == null) {
            return null;
        }
        return this.detail.getTitle();
    }
    
    @Override
    public VideoType getType() {
        return this.detail.getType();
    }
    
    @Override
    public int getYear() {
        if (this.detail == null) {
            return 0;
        }
        return this.detail.getYear();
    }
}
