// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.falkor;

import com.netflix.model.branches.FalkorBillboardData;
import com.netflix.falkor.Func;

final class Falkor$Creator$23 implements Func<FalkorBillboardData>
{
    @Override
    public FalkorBillboardData call() {
        return new FalkorBillboardData((Func)Falkor$Creator.BillboardSummary);
    }
}
