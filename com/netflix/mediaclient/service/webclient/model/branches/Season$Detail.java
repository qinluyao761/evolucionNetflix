// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.webclient.model.branches;

public class Season$Detail extends Video$Summary
{
    public int currentEpisodeNumber;
    private int episodeCount;
    private int number;
    private int year;
    
    public int getCurrentEpisodeNumber() {
        return this.currentEpisodeNumber;
    }
    
    public int getEpisodeCount() {
        return this.episodeCount;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public int getYear() {
        return this.year;
    }
}