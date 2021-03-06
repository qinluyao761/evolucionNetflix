// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.android.widget;

import android.animation.Animator$AnimatorListener;
import java.io.Serializable;
import java.security.InvalidParameterException;
import android.view.View$OnClickListener;
import com.netflix.mediaclient.Log;
import com.netflix.mediaclient.util.AndroidUtils;
import android.view.View;

class PressedStateHandler$DelayedOnClickListener$1 implements PressedStateHandler$Listener
{
    final /* synthetic */ PressedStateHandler$DelayedOnClickListener this$0;
    final /* synthetic */ View val$v;
    
    PressedStateHandler$DelayedOnClickListener$1(final PressedStateHandler$DelayedOnClickListener this$0, final View val$v) {
        this.this$0 = this$0;
        this.val$v = val$v;
    }
    
    @Override
    public void onPressedAnimationComplete() {
        this.this$0.pressedStateHandler.listener = null;
        if (AndroidUtils.isActivityFinishedOrDestroyed(this.val$v.getContext())) {
            Log.d("PressedStateHandler", "Activity is dead - skipping onClick callback");
            return;
        }
        if (Log.isLoggable()) {
            Log.v("PressedStateHandler", "Pressed animation complete - calling onClick callback");
        }
        this.this$0.onClickListener.onClick(this.val$v);
    }
}
