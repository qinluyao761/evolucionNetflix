// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.widget;

import android.widget.TextView;
import android.annotation.TargetApi;

@TargetApi(23)
class TextViewCompatApi23
{
    public static void setTextAppearance(final TextView textView, final int textAppearance) {
        textView.setTextAppearance(textAppearance);
    }
}
