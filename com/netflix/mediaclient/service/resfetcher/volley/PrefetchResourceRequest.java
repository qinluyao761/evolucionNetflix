// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.resfetcher.volley;

import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.Response;
import com.android.volley.NetworkResponse;
import com.android.volley.Request$Priority;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.android.volley.RetryPolicy;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response$ErrorListener;
import com.netflix.mediaclient.service.resfetcher.ResourceFetcherCallback;
import com.android.volley.Request;

public class PrefetchResourceRequest extends Request<Integer>
{
    private static final float IMAGE_BACKOFF_MULT = 2.0f;
    private static final int IMAGE_MAX_RETRIES = 2;
    private final ResourceFetcherCallback mCallback;
    
    public PrefetchResourceRequest(final String s, final ResourceFetcherCallback mCallback, final Response$ErrorListener response$ErrorListener, final int n) {
        super(0, s, response$ErrorListener);
        this.mCallback = mCallback;
        this.setShouldCache(true);
        this.setRetryPolicy(new DefaultRetryPolicy(n, 2, 2.0f));
    }
    
    @Override
    protected void deliverResponse(final Integer n) {
        if (this.mCallback != null) {
            this.mCallback.onResourcePrefetched(this.getUrl(), n, CommonStatus.OK);
        }
    }
    
    @Override
    public String getCacheKey() {
        return ImageLoader.getCacheKey(this.getUrl());
    }
    
    @Override
    public Request$Priority getPriority() {
        return Request$Priority.LOW;
    }
    
    @Override
    protected Response<Integer> parseNetworkResponse(final NetworkResponse networkResponse) {
        return Response.success(networkResponse.data.length, HttpHeaderParser.parseCacheHeaders(networkResponse));
    }
}
