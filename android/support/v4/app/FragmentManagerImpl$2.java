// 
// Decompiled by Procyon v0.5.30
// 

package android.support.v4.app;

class FragmentManagerImpl$2 implements Runnable
{
    final /* synthetic */ FragmentManagerImpl this$0;
    
    FragmentManagerImpl$2(final FragmentManagerImpl this$0) {
        this.this$0 = this$0;
    }
    
    @Override
    public void run() {
        this.this$0.popBackStackState(this.this$0.mHost.getHandler(), null, -1, 0);
    }
}