// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.servicemgr.interface_.offline.realm;

import java.util.ListIterator;
import java.util.Iterator;
import io.realm.internal.Collection;
import io.realm.BaseRealm;
import io.realm.OrderedRealmCollectionImpl;
import io.realm.RealmModel;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflineImageUtils;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.service.logging.error.ErrorLoggingManager;
import io.realm.Realm;
import android.content.Context;
import io.realm.Realm$Transaction;

final class RealmUtils$1 implements Realm$Transaction
{
    final /* synthetic */ Context val$context;
    final /* synthetic */ String val$playableId;
    
    RealmUtils$1(final String val$playableId, final Context val$context) {
        this.val$playableId = val$playableId;
        this.val$context = val$context;
    }
    
    public void execute(final Realm realm) {
        final RealmVideoDetails realmVideoDetails = (RealmVideoDetails)realm.where(RealmVideoDetails.class).equalTo("id", this.val$playableId).findFirst();
        if (realmVideoDetails == null) {
            ErrorLoggingManager.logHandledException("SPY-10597: videoRecord to delete is null (playableId= " + this.val$playableId + ")");
            return;
        }
        while (true) {
            Label_0272: {
                if (realmVideoDetails.getType() != VideoType.EPISODE) {
                    break Label_0272;
                }
                if (realm.where(RealmVideoDetails.class).equalTo("playable.parentId", realmVideoDetails.getPlayable().getTopLevelId()).equalTo("videoType", VideoType.EPISODE.getKey()).findAll().size() == 1) {
                    OfflineImageUtils.deleteVideoDetailsImage(this.val$context, realmVideoDetails.getPlayable().getTopLevelId());
                    realm.where(RealmVideoDetails.class).equalTo("id", realmVideoDetails.getPlayable().getTopLevelId()).findAll().deleteAllFromRealm();
                    realm.where(RealmPlayable.class).equalTo("parentId", realmVideoDetails.getPlayable().getTopLevelId()).findAll().deleteAllFromRealm();
                }
                if (realm.where(RealmVideoDetails.class).equalTo("playable.playableId", this.val$playableId).findAll().size() <= 1) {
                    break Label_0272;
                }
                final int n = 0;
                if (n != 0) {
                    realm.where(RealmPlayable.class).equalTo("playableId", this.val$playableId).findAll().deleteAllFromRealm();
                }
                OfflineImageUtils.deleteVideoDetailsImage(this.val$context, this.val$playableId);
                realm.where(RealmVideoDetails.class).equalTo("id", this.val$playableId).findAll().deleteAllFromRealm();
                return;
            }
            final int n = 1;
            continue;
        }
    }
}
