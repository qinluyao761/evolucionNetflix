// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.voip;

import com.netflix.mediaclient.android.widget.AlertDialogFactory$AlertDialogDescriptor;
import com.netflix.mediaclient.android.widget.AlertDialogFactory$TwoButtonAlertDialogDescriptor;
import com.netflix.mediaclient.service.error.ErrorDescriptor;
import android.content.Context;

public class VoipErrorDialogDescriptorFactory
{
    protected static final String TAG = "ErrorManager";
    
    private static ErrorDescriptor getHandle(final Context context, final String s, final String s2, final Runnable runnable) {
        return new VoipErrorDescriptor(new AlertDialogFactory$TwoButtonAlertDialogDescriptor(s, s2, context.getString(2131231174), new VoipErrorDialogDescriptorFactory$1(context), context.getString(2131230993), runnable));
    }
    
    public static ErrorDescriptor getHandlerForCallFailed(final Context context, final Runnable runnable) {
        return getHandle(context, context.getString(2131231014), context.getString(2131231013), runnable);
    }
    
    public static ErrorDescriptor getHandlerForCallFailed(final Context context, String string, final int n) {
        string = context.getString(2131231013);
        return getHandle(context, context.getString(2131231014), string, null);
    }
    
    public static ErrorDescriptor getHandlerForEngineFailed(final Context context, final Runnable runnable) {
        return getHandle(context, context.getString(2131231014), context.getString(2131231013), runnable);
    }
    
    public static ErrorDescriptor getHandlerForNoLineAvailable(final Context context) {
        return getHandle(context, context.getString(2131231014), context.getString(2131231013), null);
    }
}
