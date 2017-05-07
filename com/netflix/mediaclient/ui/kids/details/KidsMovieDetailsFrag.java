// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.kids.details;

import com.netflix.mediaclient.servicemgr.LoggingManagerCallback;
import com.netflix.mediaclient.android.widget.ErrorWrapper;
import android.content.Context;
import com.netflix.mediaclient.ui.kids.KidsUtils;
import android.view.LayoutInflater;
import android.widget.ListAdapter;
import com.netflix.mediaclient.util.gfx.AnimationUtils;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import android.widget.TextView;
import android.view.ViewGroup;
import android.os.Bundle;
import android.app.Fragment;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.MovieDetails;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.widget.ListView;
import com.netflix.mediaclient.android.widget.LoadingAndErrorWrapper;
import android.view.View;
import com.netflix.mediaclient.android.fragment.NetflixFrag;

public class KidsMovieDetailsFrag extends NetflixFrag
{
    private static final String EXTRA_MOVIE_ID = "extra_movie_id";
    private static final String TAG = "KidsMovieDetailsFrag";
    private KidsMovieDetailsAdapter adapter;
    private View content;
    private KidsDetailsViewGroup detailsViewGroup;
    private boolean isLoading;
    private LoadingAndErrorWrapper leWrapper;
    private ListView listView;
    private ServiceManager manager;
    private String movieId;
    private long requestId;
    
    private void completeInitIfPossible() {
        if (this.getActivity() == null) {
            Log.v("KidsMovieDetailsFrag", "Can't complete init yet - activity is null");
            return;
        }
        if (this.manager == null) {
            Log.v("KidsMovieDetailsFrag", "Can't complete init yet - manager is null");
            return;
        }
        if (this.content == null) {
            Log.v("KidsMovieDetailsFrag", "Can't complete init yet - details view is null");
            return;
        }
        if (this.movieId == null) {
            Log.v("KidsMovieDetailsFrag", "Can't complete init yet - movieId is null");
            return;
        }
        Log.v("KidsMovieDetailsFrag", "All clear - completing init process...");
        this.fetchMovieDetails();
    }
    
    public static Fragment create(final String s) {
        final KidsMovieDetailsFrag kidsMovieDetailsFrag = new KidsMovieDetailsFrag();
        final Bundle arguments = new Bundle();
        arguments.putString("extra_movie_id", s);
        kidsMovieDetailsFrag.setArguments(arguments);
        return kidsMovieDetailsFrag;
    }
    
    private View createSimilarMoviesHeader() {
        final View inflate = this.getActivity().getLayoutInflater().inflate(2130903103, (ViewGroup)null);
        ((TextView)inflate.findViewById(2131165411)).setText(2131492954);
        return inflate;
    }
    
    private void fetchMovieDetails() {
        if (this.manager == null) {
            Log.w("KidsMovieDetailsFrag", "Manager is null - can't get movie details");
            return;
        }
        this.showLoadingView();
        this.isLoading = true;
        this.requestId = System.nanoTime();
        Log.v("KidsMovieDetailsFrag", "Fetching data for show ID: " + this.movieId);
        this.manager.fetchMovieDetails(this.movieId, new FetchMovieDetailsCallback(this.requestId));
    }
    
    private void showErrorView() {
        this.leWrapper.showErrorView(true);
        AnimationUtils.hideView((View)this.listView, true);
    }
    
    private void showLoadingView() {
        this.leWrapper.showLoadingView(true);
        AnimationUtils.hideView((View)this.listView, true);
    }
    
    private void showStandardViews() {
        this.leWrapper.hide(true);
        AnimationUtils.showView((View)this.listView, true);
    }
    
    private void updateMovieDetails(final MovieDetails movieDetails) {
        Log.v("KidsMovieDetailsFrag", "Updating movie details: " + movieDetails.getTitle());
        this.detailsViewGroup.updateDetails(movieDetails);
        this.adapter = new KidsMovieDetailsAdapter(this, movieDetails);
        this.listView.setAdapter((ListAdapter)this.adapter);
        this.showStandardViews();
    }
    
    public boolean isLoadingData() {
        return this.isLoading;
    }
    
    public void onActivityCreated(final Bundle bundle) {
        Log.v("KidsMovieDetailsFrag", "onActivityCreated");
        super.onActivityCreated(bundle);
        this.completeInitIfPossible();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        this.movieId = this.getArguments().getString("extra_movie_id");
        super.onCreate(bundle);
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.content = layoutInflater.inflate(2130903109, (ViewGroup)null);
        this.listView = (ListView)this.content.findViewById(16908298);
        KidsUtils.configureListViewForKids(this.getNetflixActivity(), this.listView);
        this.detailsViewGroup = new KidsDetailsViewGroup((Context)this.getActivity());
        this.listView.addHeaderView((View)this.detailsViewGroup, (Object)null, false);
        this.listView.addHeaderView(this.createSimilarMoviesHeader(), (Object)null, false);
        this.leWrapper = new LoadingAndErrorWrapper(this.content, new ErrorWrapper.Callback() {
            @Override
            public void onRetryRequested() {
                KidsMovieDetailsFrag.this.showLoadingView();
                KidsMovieDetailsFrag.this.fetchMovieDetails();
            }
        });
        return this.content;
    }
    
    @Override
    public void onManagerReady(final ServiceManager manager, final int n) {
        Log.v("KidsMovieDetailsFrag", "onManagerReady");
        super.onManagerReady(manager, n);
        this.manager = manager;
        this.completeInitIfPossible();
    }
    
    @Override
    public void onManagerUnavailable(final ServiceManager serviceManager, final int n) {
        super.onManagerUnavailable(serviceManager, n);
        this.manager = null;
    }
    
    private class FetchMovieDetailsCallback extends LoggingManagerCallback
    {
        private final long requestId;
        
        public FetchMovieDetailsCallback(final long requestId) {
            super("KidsMovieDetailsFrag");
            this.requestId = requestId;
        }
        
        @Override
        public void onMovieDetailsFetched(final MovieDetails movieDetails, final int n) {
            super.onMovieDetailsFetched(movieDetails, n);
            if (this.requestId != KidsMovieDetailsFrag.this.requestId) {
                Log.v("KidsMovieDetailsFrag", "Ignoring stale callback");
                return;
            }
            if (n != 0) {
                Log.w("KidsMovieDetailsFrag", "Invalid status code");
                KidsMovieDetailsFrag.this.showErrorView();
                return;
            }
            if (movieDetails == null) {
                Log.v("KidsMovieDetailsFrag", "No details in response");
                KidsMovieDetailsFrag.this.showErrorView();
                return;
            }
            KidsMovieDetailsFrag.this.updateMovieDetails(movieDetails);
        }
    }
}