// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service;

import com.netflix.mediaclient.service.webclient.model.leafs.social.FriendForRecommendation;
import java.util.Set;
import android.os.Process;
import com.netflix.mediaclient.javabridge.ui.ActivationTokens;
import com.netflix.mediaclient.servicemgr.SignUpParams;
import com.netflix.mediaclient.servicemgr.IPushNotification;
import com.netflix.mediaclient.repository.SecurityRepository;
import com.netflix.mediaclient.servicemgr.NrdpComponent;
import com.netflix.mediaclient.servicemgr.IPlayer;
import com.netflix.mediaclient.servicemgr.IMdx;
import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.service.configuration.esn.EsnProvider;
import com.netflix.mediaclient.servicemgr.IDiagnosis;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.servicemgr.IBrowseInterface;
import com.netflix.mediaclient.servicemgr.model.user.UserProfile;
import java.util.List;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcherCallback;
import com.netflix.mediaclient.servicemgr.IClientLogging$AssetType;
import com.netflix.mediaclient.service.user.UserAgent$UserAgentCallback;
import android.os.SystemClock;
import com.netflix.mediaclient.servicemgr.ApplicationPerformanceMetricsLogging$Trigger;
import com.netflix.mediaclient.servicemgr.INetflixServiceCallback;
import java.util.Iterator;
import android.support.v4.content.LocalBroadcastManager;
import java.io.Serializable;
import android.content.IntentFilter;
import com.netflix.mediaclient.util.ThreadUtils;
import com.netflix.mediaclient.util.ConnectivityUtils;
import com.netflix.mediaclient.util.AndroidUtils;
import com.netflix.mediaclient.util.StringUtils;
import android.content.Context;
import android.app.PendingIntent;
import com.netflix.mediaclient.Log;
import android.app.AlarmManager;
import android.content.Intent;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.service.user.UserAgent;
import com.netflix.mediaclient.service.pushnotification.PushNotificationAgent;
import com.netflix.mediaclient.service.preapp.PreAppAgent;
import com.netflix.mediaclient.service.player.PlayerAgent;
import android.content.BroadcastReceiver;
import com.netflix.mediaclient.service.mdx.MdxAgent;
import com.netflix.mediaclient.android.app.Status;
import java.util.ArrayList;
import com.netflix.mediaclient.service.falkor.FalkorAgent;
import com.netflix.mediaclient.service.falkor.FalkorAccess;
import com.netflix.mediaclient.service.diagnostics.DiagnosisAgent;
import com.netflix.mediaclient.service.configuration.ConfigurationAgent;
import com.netflix.mediaclient.service.logging.LoggingAgent;
import android.os.IBinder;
import android.os.Handler;
import com.netflix.mediaclient.servicemgr.INetflixService;
import android.app.Service;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcher;
import com.netflix.mediaclient.service.browse.BrowseAgent;
import com.netflix.mediaclient.NetflixApplication;

class NetflixService$3 implements ServiceAgent$AgentContext
{
    final /* synthetic */ NetflixService this$0;
    
    NetflixService$3(final NetflixService this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public NetflixApplication getApplication() {
        return (NetflixApplication)this.this$0.getApplication();
    }
    
    @Override
    public ServiceAgent$BrowseAgentInterface getBrowseAgent() {
        ServiceAgent$BrowseAgentInterface serviceAgent$BrowseAgentInterface;
        if (this.this$0.mConfigurationAgent.shouldUseLegacyBrowseVolleyClient()) {
            serviceAgent$BrowseAgentInterface = this.this$0.mBrowseAgent;
        }
        else {
            serviceAgent$BrowseAgentInterface = this.this$0.mFalkorAgent;
        }
        return serviceAgent$BrowseAgentInterface;
    }
    
    @Override
    public ServiceAgent$ConfigurationAgentInterface getConfigurationAgent() {
        return this.this$0.mConfigurationAgent;
    }
    
    @Override
    public NrdController getNrdController() {
        return this.this$0.mNrdController;
    }
    
    @Override
    public ServiceAgent$PreAppAgentInterface getPreAppAgent() {
        return this.this$0.mPreAppAgent;
    }
    
    @Override
    public ResourceFetcher getResourceFetcher() {
        return this.this$0.mResourceFetcher;
    }
    
    @Override
    public NetflixService getService() {
        return this.this$0;
    }
    
    @Override
    public ServiceAgent$UserAgentInterface getUserAgent() {
        return this.this$0.mUserAgent;
    }
}
