// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.lomo;

import com.netflix.mediaclient.util.gfx.ImageLoader;
import com.netflix.mediaclient.servicemgr.model.Video;
import com.netflix.mediaclient.ui.common.PlaybackLauncher;
import android.view.View$OnClickListener;
import com.netflix.mediaclient.servicemgr.model.VideoType;
import com.netflix.mediaclient.ui.common.PlayContextImp;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.servicemgr.model.trackable.Trackable;
import android.view.View;
import com.netflix.mediaclient.servicemgr.IClientLogging;
import com.netflix.mediaclient.ui.common.PlayContextProvider;
import android.view.ViewGroup;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.ProgressBar;
import com.netflix.mediaclient.ui.common.PlayContext;
import android.widget.ImageView;
import com.netflix.mediaclient.android.widget.AdvancedImageView;
import com.netflix.mediaclient.android.widget.VideoDetailsClickListener;
import com.netflix.mediaclient.servicemgr.model.CWVideo;
import android.widget.RelativeLayout;

public class CwView extends RelativeLayout implements IVideoView<CWVideo>
{
    public static final float CW_IMG_HEIGHT_TO_WIDTH_RATIO = 0.5625f;
    private static final String TAG = "CwView";
    private VideoDetailsClickListener clicker;
    private AdvancedImageView img;
    private ImageView info;
    private PlayContext playContext;
    private ProgressBar progress;
    private TextView title;
    
    public CwView(final Context context) {
        super(context);
        this.init();
    }
    
    public CwView(final Context context, final AttributeSet set) {
        super(context, set);
        this.init();
    }
    
    public CwView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.init();
    }
    
    private void init() {
        this.setFocusable(true);
        this.setBackgroundResource(2130837876);
        this.playContext = PlayContext.EMPTY_CONTEXT;
        final NetflixActivity netflixActivity = (NetflixActivity)this.getContext();
        netflixActivity.getLayoutInflater().inflate(2130903082, (ViewGroup)this);
        this.title = (TextView)this.findViewById(2131165333);
        this.img = (AdvancedImageView)this.findViewById(2131165330);
        this.progress = (ProgressBar)this.findViewById(2131165335);
        this.info = (ImageView)this.findViewById(2131165334);
        this.clicker = new VideoDetailsClickListener(netflixActivity, this);
    }
    
    public PlayContext getPlayContext() {
        return this.playContext;
    }
    
    public void hide() {
        NetflixActivity.getImageLoader(this.getContext()).showImg(this.img, null, IClientLogging.AssetType.bif, null, false, false);
        this.setVisibility(4);
        this.clicker.remove((View)this);
    }
    
    public void setInfoViewId(final int id) {
        this.info.setId(id);
    }
    
    public void update(final CWVideo cwVideo, final Trackable trackable, int progress, final boolean b) {
        if (Log.isLoggable("CwView", 2)) {
            Log.v("CwView", "Updating for video: " + cwVideo.toString());
        }
        this.playContext = new PlayContextImp(trackable, progress);
        this.setVisibility(0);
        final String format = String.format(this.getResources().getString(2131493187), cwVideo.getTitle());
        this.setContentDescription((CharSequence)format);
        if (VideoType.SHOW.equals(cwVideo.getType())) {
            this.title.setText((CharSequence)this.getContext().getString(2131493252, new Object[] { cwVideo.getTitle(), cwVideo.getPlayable().getSeasonNumber(), cwVideo.getPlayable().getEpisodeNumber() }));
        }
        else {
            this.title.setText((CharSequence)cwVideo.getTitle());
        }
        final ImageLoader imageLoader = NetflixActivity.getImageLoader(this.getContext());
        final AdvancedImageView img = this.img;
        final String stillUrl = cwVideo.getStillUrl();
        final IClientLogging.AssetType bif = IClientLogging.AssetType.bif;
        if (b) {
            progress = 1;
        }
        else {
            progress = 0;
        }
        imageLoader.showImg(img, stillUrl, bif, format, true, true, progress);
        if (cwVideo.getPlayable().getRuntime() > 0) {
            progress = cwVideo.getPlayable().getPlayableBookmarkPosition() * 100 / cwVideo.getPlayable().getRuntime();
        }
        else {
            progress = 0;
        }
        this.progress.setProgress(progress);
        this.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                PlaybackLauncher.startPlaybackAfterPIN((NetflixActivity)CwView.this.getContext(), cwVideo.getPlayable(), CwView.this.playContext);
            }
        });
        this.info.setContentDescription((CharSequence)String.format(this.getResources().getString(2131493229), cwVideo.getTitle()));
        this.clicker.update((View)this.info, cwVideo);
    }
}
