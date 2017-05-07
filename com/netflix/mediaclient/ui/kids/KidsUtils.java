// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kids;

import android.view.View;
import android.view.View$OnClickListener;
import java.io.Serializable;
import com.netflix.mediaclient.service.ServiceAgent;
import android.view.MenuItem;
import android.view.Menu;
import com.netflix.mediaclient.ui.profiles.ProfileSelectionActivity;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.service.configuration.KidsOnPhoneConfiguration;
import com.netflix.mediaclient.ui.lomo.BasePaginatedAdapter;
import java.util.Iterator;
import com.netflix.mediaclient.servicemgr.UserProfile;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.content.Intent;
import com.netflix.mediaclient.servicemgr.UIViewLogging;
import android.app.Activity;

public class KidsUtils
{
    private static final String TAG = "KidsUtils";
    
    private static boolean accountHasKidsProfile(final NetflixActivity netflixActivity) {
        if (netflixActivity == null) {
            Log.w("KidsUtils", "Null activity - can't get profiles");
        }
        else {
            if (netflixActivity.getServiceManager() == null) {
                Log.w("KidsUtils", "Null service man - can't get profiles");
                return false;
            }
            if (netflixActivity.getServiceManager().getAllProfiles() == null) {
                Log.w("KidsUtils", "Null profiles list - can't get profiles");
                return false;
            }
            final Iterator<? extends UserProfile> iterator = netflixActivity.getServiceManager().getAllProfiles().iterator();
            while (iterator.hasNext()) {
                if (((UserProfile)iterator.next()).isKidsProfile()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static int computeHorizontalRowHeight(final NetflixActivity netflixActivity, final boolean b) {
        return (int)(BasePaginatedAdapter.computeViewPagerWidth(netflixActivity, b) * 0.5625f);
    }
    
    public static int computeRowHeight(final NetflixActivity netflixActivity, final boolean b) {
        final int computeViewPagerWidth;
        final int n = computeViewPagerWidth = BasePaginatedAdapter.computeViewPagerWidth(netflixActivity, b);
        final ServiceManager serviceManager = netflixActivity.getServiceManager();
        if (serviceManager == null) {
            Log.w("KidsUtils", "Null service manager in computeRowHeight()");
            return computeViewPagerWidth;
        }
        int n2 = computeViewPagerWidth;
        if (serviceManager.getConfiguration().getKidsOnPhoneConfiguration().getLolomoImageType() == KidsOnPhoneConfiguration.LolomoImageType.HORIZONTAL) {
            n2 = (int)(computeViewPagerWidth * 0.5625f);
        }
        if (Log.isLoggable("KidsUtils", 2)) {
            Log.v("KidsUtils", "Computed row height as: " + n2 + ", from width of: " + n);
        }
        return n2;
    }
    
    public static Intent createExitKidsIntent(final Activity activity, final UIViewLogging.UIViewCommandName uiViewCommandName) {
        return ProfileSelectionActivity.createSwitchFromKidsIntent(activity, uiViewCommandName);
    }
    
    public static MenuItem createKidsMenuItem(final NetflixActivity netflixActivity, final Menu menu) {
        final MenuItem add = menu.add(0, 2131165242, 0, 2131492962);
        updateKidsMenuItem(netflixActivity, add);
        return add;
    }
    
    private static Intent createSwitchToKidsIntent(final Activity activity, final UIViewLogging.UIViewCommandName uiViewCommandName) {
        return ProfileSelectionActivity.createSwitchToKidsIntent(activity, uiViewCommandName);
    }
    
    public static boolean isKidsProfile(final UserProfile userProfile) {
        return userProfile != null && userProfile.isKidsProfile();
    }
    
    public static boolean isKidsWithUpDownScrolling(final NetflixActivity netflixActivity) {
        return netflixActivity.isForKids() && netflixActivity.getServiceManager().getConfiguration().getKidsOnPhoneConfiguration().getScrollBehavior() == KidsOnPhoneConfiguration.ScrollBehavior.UP_DOWN;
    }
    
    public static boolean isKoPExperience(final ServiceAgent.ConfigurationAgentInterface configurationAgentInterface, final UserProfile userProfile) {
        return isKidsProfile(userProfile) && isUserInKopExperience(configurationAgentInterface);
    }
    
    public static boolean isUserInKopExperience(final ServiceAgent.ConfigurationAgentInterface configurationAgentInterface) {
        return configurationAgentInterface != null && configurationAgentInterface.getKidsOnPhoneConfiguration() != null && configurationAgentInterface.getKidsOnPhoneConfiguration().isKidsOnPhoneEnabled();
    }
    
    public static boolean shouldShowHorizontalImages(final NetflixActivity netflixActivity) {
        return netflixActivity.getServiceManager().getConfiguration().getKidsOnPhoneConfiguration().getLolomoImageType() == KidsOnPhoneConfiguration.LolomoImageType.HORIZONTAL;
    }
    
    private static boolean shouldShowKidsEntryInActionBar(final NetflixActivity netflixActivity) {
        return accountHasKidsProfile(netflixActivity) && netflixActivity.getServiceManager().getConfiguration().getKidsOnPhoneConfiguration().shouldShowKidsEntryInActionBar();
    }
    
    public static boolean shouldShowKidsEntryInGenreLomo(final NetflixActivity netflixActivity) {
        return accountHasKidsProfile(netflixActivity) && netflixActivity.getServiceManager().getConfiguration().getKidsOnPhoneConfiguration().shouldShowKidsEntryInGenreLomo();
    }
    
    public static boolean shouldShowKidsEntryInMenu(final NetflixActivity netflixActivity) {
        return accountHasKidsProfile(netflixActivity) && netflixActivity.getServiceManager().getConfiguration().getKidsOnPhoneConfiguration().shouldShowKidsEntryInMenu();
    }
    
    public static boolean shouldShowKidsExperience(final NetflixActivity netflixActivity) {
        if (netflixActivity == null) {
            Log.w("KidsUtils", "Activity is null - should not happen");
            return false;
        }
        if (netflixActivity.isForKids()) {
            Log.v("KidsUtils", "Should show kids experience because we're in a kids activity");
            return true;
        }
        final ServiceManager serviceManager = netflixActivity.getServiceManager();
        final boolean b = serviceManager != null && serviceManager.getCurrentProfile() != null && serviceManager.getCurrentProfile().isKidsProfile() && serviceManager.getConfiguration() != null && serviceManager.getConfiguration().getKidsOnPhoneConfiguration() != null && serviceManager.getConfiguration().getKidsOnPhoneConfiguration().isKidsOnPhoneEnabled();
        if (Log.isLoggable("KidsUtils", 2)) {
            Serializable value;
            if (serviceManager == null) {
                value = "null service";
            }
            else if (serviceManager.getCurrentProfile() == null) {
                value = "null profile";
            }
            else {
                value = serviceManager.getCurrentProfile().isKidsProfile();
            }
            Serializable value2;
            if (serviceManager.getConfiguration() == null) {
                value2 = "null config";
            }
            else {
                value2 = serviceManager.getConfiguration().getKidsOnPhoneConfiguration().isKidsOnPhoneEnabled();
            }
            Log.v("KidsUtils", String.format("Should show kids experience - isKidsProfile: %s, KoP enabled: %s, rtn: %s", value, value2, b));
        }
        return b;
    }
    
    public static void updateKidsMenuItem(final NetflixActivity netflixActivity, final MenuItem menuItem) {
        if (menuItem == null) {
            return;
        }
        if (!netflixActivity.getServiceManager().isReady() || !shouldShowKidsEntryInActionBar(netflixActivity)) {
            menuItem.setVisible(false).setEnabled(false);
            return;
        }
        menuItem.setVisible(true).setEnabled(true);
        if (netflixActivity.isForKids()) {
            menuItem.setTitle(2131492967).setIcon(2130837694).setIntent(createExitKidsIntent(netflixActivity, UIViewLogging.UIViewCommandName.actionBarKidsExit)).setShowAsAction(2);
            return;
        }
        menuItem.setTitle(2131492948).setIcon(2130837732).setIntent(createSwitchToKidsIntent(netflixActivity, UIViewLogging.UIViewCommandName.actionBarKidsEntry)).setShowAsAction(2);
    }
    
    public static class OnSwitchToKidsClickListener implements View$OnClickListener
    {
        private final Activity activity;
        private final UIViewLogging.UIViewCommandName entryName;
        
        public OnSwitchToKidsClickListener(final Activity activity, final UIViewLogging.UIViewCommandName entryName) {
            this.activity = activity;
            this.entryName = entryName;
        }
        
        public void onClick(final View view) {
            this.activity.startActivity(createSwitchToKidsIntent(this.activity, this.entryName));
        }
    }
}
