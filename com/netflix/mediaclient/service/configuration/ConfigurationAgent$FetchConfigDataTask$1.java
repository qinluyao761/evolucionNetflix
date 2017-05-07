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
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.util.DeviceCategory;
import com.netflix.mediaclient.media.PlayerType;
import com.netflix.mediaclient.service.webclient.model.leafs.ConsolidatedLoggingSessionSpecification;
import org.json.JSONArray;
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
import com.netflix.mediaclient.util.api.Api19Util;
import com.netflix.mediaclient.util.AndroidUtils;
import com.netflix.mediaclient.android.app.CommonStatus;
import java.util.Locale;
import java.io.IOException;
import com.netflix.mediaclient.service.configuration.volley.FetchConfigDataRequest;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.service.NetflixService;
import android.os.Handler;
import com.netflix.mediaclient.service.configuration.esn.EsnProvider;
import com.netflix.mediaclient.service.configuration.drm.DrmManager;
import java.util.ArrayList;
import android.annotation.SuppressLint;
import com.netflix.mediaclient.service.ServiceAgent$ConfigurationAgentInterface;
import com.netflix.mediaclient.service.ServiceAgent;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.service.webclient.model.leafs.ConfigData;

class ConfigurationAgent$FetchConfigDataTask$1 extends SimpleConfigurationAgentWebCallback
{
    final /* synthetic */ ConfigurationAgent$FetchConfigDataTask this$1;
    
    ConfigurationAgent$FetchConfigDataTask$1(final ConfigurationAgent$FetchConfigDataTask this$1) {
        this.this$1 = this$1;
    }
    
    @Override
    public void onConfigDataFetched(final ConfigData configData, final Status status) {
        if (Log.isLoggable("nf_configurationagent", 3)) {
            Log.d("nf_configurationagent", String.format("onConfigDataFetched statusCode=%d", status.getStatusCode().getValue()));
        }
        this.this$1.this$0.mConfigRefreshStatus = status;
        if (status.isSucces() && configData != null) {
            this.this$1.this$0.persistConfigOverride(configData);
        }
        this.this$1.this$0.mIsConfigRefreshInBackground = false;
        this.this$1.this$0.refreshHandler.postDelayed(this.this$1.this$0.refreshRunnable, 28800000L);
        this.this$1.this$0.notifyConfigRefreshed();
        if (this.this$1.getCallback() != null) {
            this.this$1.getCallback().onConfigDataFetched(configData, this.this$1.this$0.mConfigRefreshStatus);
        }
    }
}