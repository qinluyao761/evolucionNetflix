// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.react.shell;

import com.facebook.react.modules.intent.IntentModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.NativeModule;
import javax.inject.Provider;

class MainReactPackage$12 implements Provider<NativeModule>
{
    final /* synthetic */ MainReactPackage this$0;
    final /* synthetic */ ReactApplicationContext val$context;
    
    MainReactPackage$12(final MainReactPackage this$0, final ReactApplicationContext val$context) {
        this.this$0 = this$0;
        this.val$context = val$context;
    }
    
    public NativeModule get() {
        return new IntentModule(this.val$context);
    }
}
