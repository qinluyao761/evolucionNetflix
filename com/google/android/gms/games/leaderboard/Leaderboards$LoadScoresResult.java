// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.games.leaderboard;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Releasable;

public interface Leaderboards$LoadScoresResult extends Releasable, Result
{
    Leaderboard getLeaderboard();
    
    LeaderboardScoreBuffer getScores();
}