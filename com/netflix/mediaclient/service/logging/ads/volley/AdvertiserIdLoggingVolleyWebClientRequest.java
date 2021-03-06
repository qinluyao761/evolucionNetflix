// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.logging.ads.volley;

import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.android.app.Status;
import java.util.Map;
import com.netflix.mediaclient.service.logging.ads.AdvertiserIdLoggingCallback;
import com.netflix.mediaclient.service.logging.client.volley.ClientLoggingVolleyWebClientRequest;

public final class AdvertiserIdLoggingVolleyWebClientRequest extends ClientLoggingVolleyWebClientRequest<String>
{
    private static final String TAG = "nf_volleyrequest";
    private AdvertiserIdLoggingCallback mCallback;
    private String mPayload;
    
    protected AdvertiserIdLoggingVolleyWebClientRequest(final String mPayload, final AdvertiserIdLoggingCallback mCallback) {
        this.mPayload = mPayload;
        this.mCallback = mCallback;
    }
    
    @Override
    public Map<String, String> getHeaders() {
        final Map<String, String> headers = super.getHeaders();
        headers.put("X-Netflix.ichnaea.request.type", "IchnaeaRequest");
        return headers;
    }
    
    @Override
    protected String getPayload() {
        return this.mPayload;
    }
    
    @Override
    protected void onFailure(final Status status) {
        if (Log.isLoggable()) {
            Log.d("nf_volleyrequest", "Advertiser ID and opt in startus failed to be delivered. Status code returned" + status);
        }
        if (this.mCallback != null) {
            this.mCallback.onFailure();
        }
    }
    
    @Override
    protected void onSuccess(final String s) {
        if (Log.isLoggable()) {
            Log.d("nf_volleyrequest", "Advertiser ID and opt in startus delivered " + s);
        }
        if (this.mCallback != null) {
            this.mCallback.onSuccess();
        }
    }
    
    @Override
    protected String parseResponse(final String s) {
        if (Log.isLoggable()) {
            Log.v("nf_volleyrequest", "String response to parse = " + s);
        }
        return "OK";
    }
}
