// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.games.internal.api;

import com.google.android.gms.games.GamesMetadata$LoadExtendedGamesResult;
import com.google.android.gms.common.api.BaseImplementation$b;
import com.google.android.gms.games.internal.GamesClientImpl;
import com.google.android.gms.common.api.Api$a;

class GamesMetadataImpl$17 extends GamesMetadataImpl$LoadExtendedGamesImpl
{
    final /* synthetic */ boolean XU;
    final /* synthetic */ int Yl;
    final /* synthetic */ String Yn;
    
    @Override
    protected void a(final GamesClientImpl gamesClientImpl) {
        gamesClientImpl.e((BaseImplementation$b<GamesMetadata$LoadExtendedGamesResult>)this, this.Yn, this.Yl, false, this.XU);
    }
}