// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.falkor;

import com.netflix.model.leafs.SearchTrackableListSummary;
import com.netflix.falkor.Ref;
import com.netflix.model.branches.SummarizedList;
import com.netflix.falkor.Func;

final class Falkor$Creator$8 implements Func<SummarizedList<Ref, SearchTrackableListSummary>>
{
    @Override
    public SummarizedList<Ref, SearchTrackableListSummary> call() {
        return (SummarizedList<Ref, SearchTrackableListSummary>)new SummarizedList((Func)Falkor$Creator.Ref, (Func)Falkor$Creator.SearchTrackableListSummary);
    }
}
