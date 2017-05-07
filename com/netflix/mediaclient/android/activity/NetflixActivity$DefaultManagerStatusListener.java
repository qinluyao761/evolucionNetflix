// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.android.activity;

import com.netflix.mediaclient.ui.RelaunchActivity;
import com.netflix.mediaclient.util.WebApiUtils$VideoIds;
import com.netflix.mediaclient.ui.player.MDXControllerActivity;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.service.mdx.MdxAgent;
import android.text.TextUtils;
import com.netflix.mediaclient.servicemgr.ServiceManagerUtils;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.content.IntentFilter;
import com.netflix.mediaclient.ui.home.HomeActivity;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.servicemgr.UserActionLogging$CommandName;
import com.netflix.mediaclient.util.log.UserActionLogUtils;
import android.view.View;
import com.netflix.mediaclient.ui.pin.PinDialogVault;
import android.view.MenuItem;
import com.netflix.mediaclient.util.DeviceUtils;
import java.util.Iterator;
import android.support.v4.content.LocalBroadcastManager;
import com.netflix.mediaclient.ui.settings.SettingsActivity;
import com.netflix.mediaclient.ui.common.DebugMenuItems;
import com.netflix.mediaclient.util.ViewUtils;
import android.os.Bundle;
import com.netflix.mediaclient.util.log.UIViewLogUtils;
import com.netflix.mediaclient.servicemgr.UIViewLogging$UIViewCommandName;
import com.netflix.mediaclient.ui.ServiceErrorsHandler;
import com.netflix.mediaclient.android.app.CommonStatus;
import android.app.Activity;
import com.netflix.mediaclient.ui.profiles.ProfileSelectionActivity;
import com.netflix.mediaclient.ui.login.LogoutActivity;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import com.netflix.mediaclient.servicemgr.IMdxSharedState;
import com.netflix.mediaclient.ui.details.EpisodeRowView$EpisodeRowListener;
import com.netflix.mediaclient.service.logging.client.model.DataContext;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.servicemgr.ApplicationPerformanceMetricsLogging;
import java.io.Serializable;
import com.netflix.mediaclient.android.widget.AlertDialogFactory;
import com.netflix.mediaclient.android.widget.AlertDialogFactory$AlertDialogDescriptor;
import com.netflix.mediaclient.android.widget.UpdateDialog;
import com.netflix.mediaclient.android.widget.UpdateDialog$Builder;
import android.app.AlertDialog;
import android.app.AlertDialog$Builder;
import android.view.MotionEvent;
import com.netflix.mediaclient.util.MdxUtils;
import android.view.KeyEvent;
import com.netflix.mediaclient.ui.kids.NetflixKidsActionBar;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import android.content.Intent;
import android.content.Context;
import com.netflix.mediaclient.service.user.UserAgentBroadcastIntents;
import com.netflix.mediaclient.ui.mdx.MdxReceiver;
import android.view.MenuItem$OnMenuItemClickListener;
import android.view.Menu;
import android.net.Uri;
import com.netflix.mediaclient.StatusCode;
import java.util.HashSet;
import android.app.Dialog;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout$PanelSlideListener;
import com.netflix.mediaclient.android.widget.NetflixActionBar;
import com.netflix.mediaclient.ui.mdx.MdxMiniPlayerFrag;
import com.netflix.mediaclient.android.app.LoadingStatus$LoadingStatusCallback;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicBoolean;
import android.os.Handler;
import android.content.BroadcastReceiver;
import java.util.Set;
import android.annotation.SuppressLint;
import com.netflix.mediaclient.ui.pin.PinVerifier$PinVerificationCallback;
import com.netflix.mediaclient.ui.mdx.ShowMessageDialogFrag$MessageResponseProvider;
import com.netflix.mediaclient.ui.details.EpisodeRowView$EpisodeRowListenerProvider;
import com.netflix.mediaclient.android.app.LoadingStatus;
import android.support.v7.app.ActionBarActivity;
import android.app.DialogFragment;
import com.netflix.mediaclient.ui.search.SearchUtils;
import com.netflix.mediaclient.service.logging.client.model.UIError;
import com.netflix.mediaclient.ui.LaunchActivity;
import com.netflix.mediaclient.NetflixApplication;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;

class NetflixActivity$DefaultManagerStatusListener implements ManagerStatusListener
{
    private boolean isFrombackground;
    private final ManagerStatusListener listener;
    final /* synthetic */ NetflixActivity this$0;
    
    public NetflixActivity$DefaultManagerStatusListener(final NetflixActivity this$0, final ManagerStatusListener listener, final boolean isFrombackground) {
        this.this$0 = this$0;
        this.isFrombackground = false;
        this.listener = listener;
        this.isFrombackground = isFrombackground;
    }
    
    @Override
    public void onManagerReady(final ServiceManager serviceManager, final Status status) {
        if (Log.isLoggable("NetflixActivity", 3)) {
            Log.d("NetflixActivity", "onManagerReady, status: " + status.getStatusCode());
        }
        this.this$0.mIsTablet = serviceManager.isTablet();
        if (status.isError()) {
            this.this$0.startLaunchActivityIfVisible();
        }
        ((NetflixApplication)this.this$0.getApplication()).refreshLocale(serviceManager.getCurrentAppLocale());
        if (this.this$0.netflixActionBar != null) {
            this.this$0.netflixActionBar.onManagerReady();
        }
        if (this.this$0.mdxFrag != null) {
            this.this$0.mdxFrag.onManagerReady(serviceManager, status);
            if (this.this$0.shouldExpandMiniPlayer) {
                this.this$0.shouldExpandMiniPlayer = false;
                this.this$0.handler.postDelayed((Runnable)new NetflixActivity$DefaultManagerStatusListener$1(this), 400L);
            }
        }
        final DialogFragment dialogFragment = this.this$0.getDialogFragment();
        if (dialogFragment instanceof ManagerStatusListener) {
            ((ManagerStatusListener)dialogFragment).onManagerReady(serviceManager, status);
        }
        this.this$0.addMdxReceiver();
        this.this$0.addUserAgentUpdateReceiver();
        if (this.this$0.showMdxInMenu()) {
            this.this$0.invalidateOptionsMenu();
        }
        if (this.listener != null) {
            this.listener.onManagerReady(serviceManager, status);
        }
        if (!(this.this$0 instanceof LaunchActivity)) {
            serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging().endUiStartupSession(true, null, serviceManager.getConfiguration().getCurrentPlayerType());
        }
        serviceManager.getClientLogging().setDataContext(this.this$0.getDataContext());
        this.this$0.reportUiViewChanged(this.this$0.getUiScreen());
        if (this.isFrombackground) {
            this.this$0.showMDXPostPlayOnResume();
        }
        SearchUtils.setTestCell(serviceManager.getConfiguration().getSearchTest());
    }
    
    @Override
    public void onManagerUnavailable(final ServiceManager serviceManager, final Status status) {
        if (Log.isLoggable("NetflixActivity", 3)) {
            Log.d("NetflixActivity", "onManagerUnavailable, status: " + status.getStatusCode());
        }
        if (this.this$0.mdxFrag != null) {
            this.this$0.mdxFrag.onManagerUnavailable(serviceManager, status);
        }
        final DialogFragment dialogFragment = this.this$0.getDialogFragment();
        if (dialogFragment instanceof ManagerStatusListener) {
            ((ManagerStatusListener)dialogFragment).onManagerUnavailable(serviceManager, status);
        }
        if (this.listener != null) {
            this.listener.onManagerUnavailable(serviceManager, status);
        }
        this.this$0.startLaunchActivityIfVisible();
        if (this.this$0.shouldFinishOnManagerError()) {
            if (Log.isLoggable("NetflixActivity", 3)) {
                Log.d("NetflixActivity", this.this$0.getClass().getSimpleName() + ": Finishing activity because manager error occured...");
            }
            this.this$0.finish();
        }
    }
}