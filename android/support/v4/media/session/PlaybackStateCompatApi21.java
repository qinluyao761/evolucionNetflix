// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.media.session;

import java.util.Iterator;
import android.media.session.PlaybackState$CustomAction;
import android.media.session.PlaybackState$Builder;
import java.util.List;
import android.media.session.PlaybackState;
import android.annotation.TargetApi;

@TargetApi(21)
class PlaybackStateCompatApi21
{
    public static long getActions(final Object o) {
        return ((PlaybackState)o).getActions();
    }
    
    public static long getActiveQueueItemId(final Object o) {
        return ((PlaybackState)o).getActiveQueueItemId();
    }
    
    public static long getBufferedPosition(final Object o) {
        return ((PlaybackState)o).getBufferedPosition();
    }
    
    public static List<Object> getCustomActions(final Object o) {
        return (List<Object>)((PlaybackState)o).getCustomActions();
    }
    
    public static CharSequence getErrorMessage(final Object o) {
        return ((PlaybackState)o).getErrorMessage();
    }
    
    public static long getLastPositionUpdateTime(final Object o) {
        return ((PlaybackState)o).getLastPositionUpdateTime();
    }
    
    public static float getPlaybackSpeed(final Object o) {
        return ((PlaybackState)o).getPlaybackSpeed();
    }
    
    public static long getPosition(final Object o) {
        return ((PlaybackState)o).getPosition();
    }
    
    public static int getState(final Object o) {
        return ((PlaybackState)o).getState();
    }
    
    public static Object newInstance(final int n, final long n2, final long bufferedPosition, final float n3, final long actions, final CharSequence errorMessage, final long n4, final List<Object> list, final long activeQueueItemId) {
        final PlaybackState$Builder playbackState$Builder = new PlaybackState$Builder();
        playbackState$Builder.setState(n, n2, n3, n4);
        playbackState$Builder.setBufferedPosition(bufferedPosition);
        playbackState$Builder.setActions(actions);
        playbackState$Builder.setErrorMessage(errorMessage);
        final Iterator<PlaybackState$CustomAction> iterator = list.iterator();
        while (iterator.hasNext()) {
            playbackState$Builder.addCustomAction((PlaybackState$CustomAction)iterator.next());
        }
        playbackState$Builder.setActiveQueueItemId(activeQueueItemId);
        return playbackState$Builder.build();
    }
}
