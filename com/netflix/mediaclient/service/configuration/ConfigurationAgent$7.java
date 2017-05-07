// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.configuration;

import android.view.Display;
import android.util.DisplayMetrics;
import android.hardware.display.DisplayManager;
import com.netflix.mediaclient.media.VideoResolutionRange;
import org.json.JSONObject;
import com.netflix.mediaclient.net.IpConnectivityPolicy;
import com.netflix.mediaclient.service.webclient.model.leafs.ErrorLoggingSpecification;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.media.PlayerType;
import com.netflix.mediaclient.service.webclient.model.leafs.ConsolidatedLoggingSessionSpecification;
import com.netflix.mediaclient.service.webclient.model.leafs.BreadcrumbLoggingSpecification;
import com.netflix.mediaclient.service.webclient.ApiEndpointRegistry;
import com.netflix.mediaclient.service.configuration.esn.EsnProviderRegistry;
import com.netflix.mediaclient.service.configuration.drm.DrmManager$DrmReadyCallback;
import com.netflix.mediaclient.service.configuration.drm.DrmManagerRegistry;
import com.netflix.mediaclient.nccp.NccpKeyStore;
import com.netflix.mediaclient.util.AndroidManifestUtils;
import com.netflix.mediaclient.util.PreferenceUtils;
import com.netflix.mediaclient.android.app.NetflixImmutableStatus;
import com.netflix.mediaclient.android.app.BackgroundTask;
import com.netflix.mediaclient.android.app.CommonStatus;
import java.util.Locale;
import java.io.IOException;
import com.netflix.mediaclient.service.configuration.volley.FetchConfigDataRequest;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.util.api.Api19Util;
import com.netflix.mediaclient.util.AndroidUtils;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.Log;
import android.content.Context;
import com.netflix.mediaclient.service.webclient.model.leafs.ConfigData;
import com.netflix.mediaclient.service.NetflixService;
import android.os.Handler;
import com.netflix.mediaclient.service.configuration.esn.EsnProvider;
import com.netflix.mediaclient.service.configuration.drm.DrmManager;
import com.netflix.mediaclient.android.app.Status;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import com.netflix.mediaclient.service.ServiceAgent$ConfigurationAgentInterface;
import com.netflix.mediaclient.service.ServiceAgent;

class ConfigurationAgent$7 implements PlaybackConfiguration
{
    final /* synthetic */ ConfigurationAgent this$0;
    
    ConfigurationAgent$7(final ConfigurationAgent this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public boolean isLocalPlaybackEnabled() {
        return this.this$0.mDeviceConfigOverride.isLocalPlaybackEnabled();
    }
    
    @Override
    public boolean isSuspendPlaybackEnabled() {
        return !this.this$0.mAccountConfigOverride.toDisableSuspendPlay();
    }
}
