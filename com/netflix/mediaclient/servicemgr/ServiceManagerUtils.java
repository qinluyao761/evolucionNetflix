// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.servicemgr;

import android.util.Pair;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.model.Playable;

public class ServiceManagerUtils
{
    private static final String TAG = "ServiceManagerUtils";
    
    public static void cacheManifestIfEnabled(final ServiceManager serviceManager, final Playable playable) {
        if (!serviceManager.getPlayer().isManifestCacheEnabled()) {
            return;
        }
        if (Log.isLoggable("ServiceManagerUtils", 3)) {
            Log.d("ServiceManagerUtils", "schedule manifest pre-fectiion for " + playable);
        }
        try {
            serviceManager.getPlayer().getManifestCache().cacheSchedule(new IManifestCache$CacheScheduleRequest[] { new IManifestCache$CacheScheduleRequest(Integer.parseInt(playable.getPlayableId()), 0L, 1L) });
        }
        catch (NumberFormatException ex) {
            Log.w("ServiceManagerUtils", "schedule manifest pre-fectiion gets invalid playableId, ignored");
        }
    }
    
    public static String getCurrentDeviceFriendlyName(final ServiceManager serviceManager) {
        if (!isMdxAgentAvailable(serviceManager)) {
            Log.e("ServiceManagerUtils", "Service manager is not available!");
            return "";
        }
        final String currentTarget = serviceManager.getMdx().getCurrentTarget();
        final Pair<String, String>[] targetList = serviceManager.getMdx().getTargetList();
        if (targetList == null || targetList.length < 1 || currentTarget == null) {
            if (Log.isLoggable("ServiceManagerUtils", 6)) {
                Log.e("ServiceManagerUtils", "No devices, current device " + currentTarget);
            }
            return "";
        }
        for (int i = 0; i < targetList.length; ++i) {
            if (currentTarget.equals(targetList[i].first)) {
                if (Log.isLoggable("ServiceManagerUtils", 3)) {
                    Log.d("ServiceManagerUtils", "Friendly name " + (String)targetList[i].second + " found for current device " + currentTarget);
                }
                return (String)targetList[i].second;
            }
        }
        if (Log.isLoggable("ServiceManagerUtils", 6)) {
            Log.e("ServiceManagerUtils", "No match found for current device " + currentTarget);
        }
        return "";
    }
    
    public static boolean isMdxAgentAvailable(final ServiceManager serviceManager) {
        return serviceManager != null && serviceManager.isReady() && serviceManager.getMdx() != null;
    }
}
