// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.react.uimanager.layoutanimation;

import android.view.animation.ScaleAnimation;
import com.facebook.react.uimanager.IllegalViewOperationException;
import android.view.animation.Animation;
import android.view.View;

abstract class BaseLayoutAnimation extends AbstractLayoutAnimation
{
    @Override
    Animation createAnimationImpl(final View view, final int n, final int n2, final int n3, final int n4) {
        final float n5 = 0.0f;
        float alpha = 0.0f;
        if (this.mAnimatedProperty == null) {
            throw new IllegalViewOperationException("Missing animated property from animation config");
        }
        switch (BaseLayoutAnimation$1.$SwitchMap$com$facebook$react$uimanager$layoutanimation$AnimatedPropertyType[this.mAnimatedProperty.ordinal()]) {
            default: {
                throw new IllegalViewOperationException("Missing animation for property : " + this.mAnimatedProperty);
            }
            case 1: {
                float alpha2;
                if (this.isReverse()) {
                    alpha2 = view.getAlpha();
                }
                else {
                    alpha2 = 0.0f;
                }
                if (!this.isReverse()) {
                    alpha = view.getAlpha();
                }
                return new OpacityAnimation(view, alpha2, alpha);
            }
            case 2: {
                float n6;
                if (this.isReverse()) {
                    n6 = 1.0f;
                }
                else {
                    n6 = 0.0f;
                }
                float n7;
                if (this.isReverse()) {
                    n7 = n5;
                }
                else {
                    n7 = 1.0f;
                }
                return (Animation)new ScaleAnimation(n6, n7, n6, n7, 1, 0.5f, 1, 0.5f);
            }
        }
    }
    
    abstract boolean isReverse();
    
    @Override
    boolean isValid() {
        return this.mDurationMs > 0 && this.mAnimatedProperty != null;
    }
}
