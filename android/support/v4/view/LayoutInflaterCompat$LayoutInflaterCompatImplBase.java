// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.view;

import android.view.LayoutInflater;

class LayoutInflaterCompat$LayoutInflaterCompatImplBase implements LayoutInflaterCompat$LayoutInflaterCompatImpl
{
    @Override
    public LayoutInflaterFactory getFactory(final LayoutInflater layoutInflater) {
        return LayoutInflaterCompatBase.getFactory(layoutInflater);
    }
    
    @Override
    public void setFactory(final LayoutInflater layoutInflater, final LayoutInflaterFactory layoutInflaterFactory) {
        LayoutInflaterCompatBase.setFactory(layoutInflater, layoutInflaterFactory);
    }
}
