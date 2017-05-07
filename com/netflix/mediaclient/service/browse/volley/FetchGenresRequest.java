// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.browse.volley;

import com.netflix.mediaclient.service.webclient.model.leafs.ListOfMoviesSummary;
import com.google.gson.JsonObject;
import com.netflix.mediaclient.service.webclient.volley.FalcorParseUtils;
import java.util.ArrayList;
import com.netflix.mediaclient.service.webclient.volley.FalcorServerException;
import com.netflix.mediaclient.service.webclient.volley.FalcorParseException;
import com.netflix.mediaclient.android.app.CommonStatus;
import java.util.Collections;
import com.netflix.mediaclient.android.app.Status;
import java.util.Arrays;
import com.netflix.mediaclient.Log;
import android.content.Context;
import com.netflix.mediaclient.service.browse.BrowseAgentCallback;
import com.netflix.mediaclient.service.browse.cache.BrowseWebClientCache;
import com.netflix.mediaclient.servicemgr.model.genre.Genre;
import java.util.List;
import com.netflix.mediaclient.service.webclient.volley.FalcorVolleyWebClientRequest;

public class FetchGenresRequest extends FalcorVolleyWebClientRequest<List<Genre>>
{
    private static final String FIELD_GENRE_LOLOMO = "genreLolomo";
    private static final String FIELD_TOP_GENRE = "topGenre";
    private static final String TAG = "nf_service_browse_fetchgenresrequest";
    private final BrowseWebClientCache browseCache;
    private final int fromGenre;
    private final String genreId;
    private final String lolomoId;
    private final boolean lolomoIdInCache;
    private final String pqlQuery;
    private final BrowseAgentCallback responseCallback;
    private final int toGenre;
    
    public FetchGenresRequest(final Context context, final BrowseWebClientCache browseCache, final String genreId, final int fromGenre, final int toGenre, final BrowseAgentCallback responseCallback) {
        super(context);
        this.responseCallback = responseCallback;
        this.genreId = genreId;
        this.fromGenre = fromGenre;
        this.toGenre = toGenre;
        this.browseCache = browseCache;
        this.lolomoId = browseCache.getGenreLoLoMoId(genreId);
        this.lolomoIdInCache = (this.lolomoId != null);
        if (this.lolomoIdInCache) {
            this.pqlQuery = String.format("['genreLolomo', '%s', {'from':%d,'to':%d}, 'summary']", this.lolomoId, fromGenre, toGenre);
        }
        else {
            this.pqlQuery = String.format("['topGenre', '%s', {'from':%d,'to':%d}, 'summary']", genreId, fromGenre, toGenre);
        }
        if (Log.isLoggable("nf_service_browse_fetchgenresrequest", 2)) {
            Log.v("nf_service_browse_fetchgenresrequest", "PQL = " + this.pqlQuery);
        }
    }
    
    @Override
    protected List<String> getPQLQueries() {
        return Arrays.asList(this.pqlQuery);
    }
    
    @Override
    protected void onFailure(final Status status) {
        if (this.responseCallback != null) {
            this.responseCallback.onGenresFetched(Collections.emptyList(), status);
        }
    }
    
    @Override
    protected void onSuccess(final List<Genre> list) {
        if (this.responseCallback != null) {
            this.responseCallback.onGenresFetched(list, CommonStatus.OK);
        }
    }
    
    @Override
    protected List<Genre> parseFalcorResponse(String o) throws FalcorParseException, FalcorServerException {
        if (Log.isLoggable("nf_service_browse_fetchgenresrequest", 2)) {}
        final ArrayList<ListOfMoviesSummary> list = (ArrayList<ListOfMoviesSummary>)new ArrayList<Genre>();
        final JsonObject dataObj = FalcorParseUtils.getDataObj("nf_service_browse_fetchgenresrequest", (String)o);
        if (!FalcorParseUtils.isEmpty(dataObj)) {
            try {
                if (this.lolomoIdInCache) {
                    o = dataObj.getAsJsonObject("genreLolomo").getAsJsonObject(this.lolomoId);
                }
                else {
                    o = dataObj.getAsJsonObject("topGenre").getAsJsonObject(this.genreId);
                }
                if (!this.lolomoIdInCache) {
                    PrefetchGenreLoLoMoRequest.putGenreLoLoMoIdInBrowseCache(this.browseCache, this.genreId, (JsonObject)o);
                }
                for (int i = this.fromGenre; i <= this.toGenre; ++i) {
                    final String string = Integer.toString(i);
                    if (((JsonObject)o).has(string)) {
                        final ListOfMoviesSummary listOfMoviesSummary = FalcorParseUtils.getPropertyObject(((JsonObject)o).getAsJsonObject(string), "summary", ListOfMoviesSummary.class);
                        if (listOfMoviesSummary != null) {
                            listOfMoviesSummary.setListPos(i);
                        }
                        list.add(listOfMoviesSummary);
                    }
                }
            }
            catch (Exception ex) {
                Log.v("nf_service_browse_fetchgenresrequest", "String response to parse = " + (String)o);
                throw new FalcorParseException("response missing expected json objects", ex);
            }
        }
        return (List<Genre>)list;
    }
}
