// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.internal.safeparcel.a;
import com.google.android.gms.common.internal.safeparcel.b;
import android.os.Parcel;
import android.os.Parcelable$Creator;

public class an implements Parcelable$Creator<am>
{
    static void a(final am am, final Parcel parcel, int d) {
        d = b.D(parcel);
        b.c(parcel, 1, am.versionCode);
        b.a(parcel, 2, am.packageName, false);
        b.a(parcel, 3, am.label, false);
        b.a(parcel, 4, am.avC);
        b.H(parcel, d);
    }
    
    public am ee(final Parcel parcel) {
        String o = null;
        final int c = a.C(parcel);
        int g = 0;
        long i = 0L;
        String o2 = null;
        while (parcel.dataPosition() < c) {
            final int b = a.B(parcel);
            switch (a.aD(b)) {
                default: {
                    a.b(parcel, b);
                    continue;
                }
                case 1: {
                    g = a.g(parcel, b);
                    continue;
                }
                case 2: {
                    o2 = a.o(parcel, b);
                    continue;
                }
                case 3: {
                    o = a.o(parcel, b);
                    continue;
                }
                case 4: {
                    i = a.i(parcel, b);
                    continue;
                }
            }
        }
        if (parcel.dataPosition() != c) {
            throw new a.a("Overread allowed size end=" + c, parcel);
        }
        return new am(g, o2, o, i);
    }
    
    public am[] gg(final int n) {
        return new am[n];
    }
}