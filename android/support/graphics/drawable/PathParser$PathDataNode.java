// 
// Decompiled by Procyon v0.5.30
// 

package android.support.graphics.drawable;

import android.util.Log;
import android.graphics.Path;

public class PathParser$PathDataNode
{
    float[] params;
    char type;
    
    PathParser$PathDataNode(final char type, final float[] params) {
        this.type = type;
        this.params = params;
    }
    
    PathParser$PathDataNode(final PathParser$PathDataNode pathParser$PathDataNode) {
        this.type = pathParser$PathDataNode.type;
        this.params = PathParser.copyOfRange(pathParser$PathDataNode.params, 0, pathParser$PathDataNode.params.length);
    }
    
    private static void addCommand(final Path path, final float[] array, final char c, final char c2, final float[] array2) {
        float n = array[0];
        float n2 = array[1];
        float n3 = array[2];
        float n4 = array[3];
        float n5 = array[4];
        float n6 = array[5];
        int n7 = 0;
        switch (c2) {
            default: {
                n7 = 2;
                break;
            }
            case 'Z':
            case 'z': {
                path.close();
                path.moveTo(n5, n6);
                n4 = n6;
                n3 = n5;
                n2 = n6;
                n = n5;
                n7 = 2;
                break;
            }
            case 'L':
            case 'M':
            case 'T':
            case 'l':
            case 'm':
            case 't': {
                n7 = 2;
                break;
            }
            case 'H':
            case 'V':
            case 'h':
            case 'v': {
                n7 = 1;
                break;
            }
            case 'C':
            case 'c': {
                n7 = 6;
                break;
            }
            case 'Q':
            case 'S':
            case 'q':
            case 's': {
                n7 = 4;
                break;
            }
            case 'A':
            case 'a': {
                n7 = 7;
                break;
            }
        }
        final int n8 = 0;
        char c3 = c;
        float n10 = 0.0f;
        float n11 = 0.0f;
        float n12 = 0.0f;
        float n71;
        for (int i = n8; i < array2.length; i += n7, n71 = n11, c3 = c2, n4 = n10, n6 = n12, n5 = n71) {
            switch (c2) {
                default: {
                    final float n9 = n6;
                    n10 = n4;
                    n11 = n5;
                    n12 = n9;
                    break;
                }
                case 'm': {
                    n += array2[i + 0];
                    n2 += array2[i + 1];
                    if (i > 0) {
                        path.rLineTo(array2[i + 0], array2[i + 1]);
                        final float n13 = n5;
                        n12 = n6;
                        n10 = n4;
                        n11 = n13;
                        break;
                    }
                    path.rMoveTo(array2[i + 0], array2[i + 1]);
                    final float n14 = n2;
                    final float n15 = n;
                    n10 = n4;
                    n12 = n2;
                    n11 = n;
                    n2 = n14;
                    n = n15;
                    break;
                }
                case 'M': {
                    final float n16 = array2[i + 0];
                    final float n17 = array2[i + 1];
                    if (i > 0) {
                        path.lineTo(array2[i + 0], array2[i + 1]);
                        final float n18 = n16;
                        final float n19 = n5;
                        n12 = n6;
                        n10 = n4;
                        n11 = n19;
                        n2 = n17;
                        n = n18;
                        break;
                    }
                    path.moveTo(array2[i + 0], array2[i + 1]);
                    final float n20 = n17;
                    final float n21 = n16;
                    n10 = n4;
                    n12 = n17;
                    n11 = n16;
                    n2 = n20;
                    n = n21;
                    break;
                }
                case 'l': {
                    path.rLineTo(array2[i + 0], array2[i + 1]);
                    final float n22 = array2[i + 0];
                    final float n23 = array2[i + 1] + n2;
                    n += n22;
                    final float n24 = n5;
                    n12 = n6;
                    n10 = n4;
                    n11 = n24;
                    n2 = n23;
                    break;
                }
                case 'L': {
                    path.lineTo(array2[i + 0], array2[i + 1]);
                    n = array2[i + 0];
                    n2 = array2[i + 1];
                    final float n25 = n5;
                    n12 = n6;
                    n10 = n4;
                    n11 = n25;
                    break;
                }
                case 'h': {
                    path.rLineTo(array2[i + 0], 0.0f);
                    final float n26 = n + array2[i + 0];
                    final float n27 = n5;
                    n12 = n6;
                    n10 = n4;
                    n11 = n27;
                    n = n26;
                    break;
                }
                case 'H': {
                    path.lineTo(array2[i + 0], n2);
                    n = array2[i + 0];
                    final float n28 = n5;
                    n12 = n6;
                    n10 = n4;
                    n11 = n28;
                    break;
                }
                case 'v': {
                    path.rLineTo(0.0f, array2[i + 0]);
                    final float n29 = array2[i + 0];
                    final float n30 = n5;
                    n2 += n29;
                    n12 = n6;
                    n10 = n4;
                    n11 = n30;
                    break;
                }
                case 'V': {
                    path.lineTo(n, array2[i + 0]);
                    final float n31 = array2[i + 0];
                    final float n32 = n5;
                    n12 = n6;
                    n10 = n4;
                    n11 = n32;
                    n2 = n31;
                    break;
                }
                case 'c': {
                    path.rCubicTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3], array2[i + 4], array2[i + 5]);
                    final float n33 = array2[i + 2];
                    final float n34 = array2[i + 3];
                    final float n35 = array2[i + 4];
                    final float n36 = array2[i + 5];
                    n3 = n + n33;
                    final float n37 = n36 + n2;
                    n += n35;
                    n11 = n5;
                    final float n38 = n34 + n2;
                    n12 = n6;
                    n10 = n38;
                    n2 = n37;
                    break;
                }
                case 'C': {
                    path.cubicTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3], array2[i + 4], array2[i + 5]);
                    n = array2[i + 4];
                    n2 = array2[i + 5];
                    n3 = array2[i + 2];
                    final float n39 = array2[i + 3];
                    n11 = n5;
                    n12 = n6;
                    n10 = n39;
                    break;
                }
                case 's': {
                    float n41;
                    float n42;
                    if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                        final float n40 = n - n3;
                        n41 = n2 - n4;
                        n42 = n40;
                    }
                    else {
                        n41 = 0.0f;
                        n42 = 0.0f;
                    }
                    path.rCubicTo(n42, n41, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                    final float n43 = array2[i + 0];
                    final float n44 = array2[i + 1];
                    final float n45 = array2[i + 2];
                    final float n46 = array2[i + 3];
                    n3 = n + n43;
                    final float n47 = n46 + n2;
                    n += n45;
                    n11 = n5;
                    final float n48 = n44 + n2;
                    n12 = n6;
                    n10 = n48;
                    n2 = n47;
                    break;
                }
                case 'S': {
                    float n50;
                    if (c3 == 'c' || c3 == 's' || c3 == 'C' || c3 == 'S') {
                        final float n49 = 2.0f * n - n3;
                        n2 = 2.0f * n2 - n4;
                        n50 = n49;
                    }
                    else {
                        n50 = n;
                    }
                    path.cubicTo(n50, n2, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                    n3 = array2[i + 0];
                    final float n51 = array2[i + 1];
                    n = array2[i + 2];
                    n2 = array2[i + 3];
                    n11 = n5;
                    n12 = n6;
                    n10 = n51;
                    break;
                }
                case 'q': {
                    path.rQuadTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                    final float n52 = array2[i + 0];
                    final float n53 = array2[i + 1];
                    final float n54 = array2[i + 2];
                    final float n55 = array2[i + 3];
                    n3 = n + n52;
                    final float n56 = n55 + n2;
                    n += n54;
                    n11 = n5;
                    final float n57 = n53 + n2;
                    n12 = n6;
                    n10 = n57;
                    n2 = n56;
                    break;
                }
                case 'Q': {
                    path.quadTo(array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3]);
                    n3 = array2[i + 0];
                    final float n58 = array2[i + 1];
                    n = array2[i + 2];
                    n2 = array2[i + 3];
                    n11 = n5;
                    n12 = n6;
                    n10 = n58;
                    break;
                }
                case 't': {
                    float n59;
                    float n60;
                    if (c3 == 'q' || c3 == 't' || c3 == 'Q' || c3 == 'T') {
                        n59 = n - n3;
                        n60 = n2 - n4;
                    }
                    else {
                        n60 = 0.0f;
                        n59 = 0.0f;
                    }
                    path.rQuadTo(n59, n60, array2[i + 0], array2[i + 1]);
                    final float n61 = array2[i + 0];
                    final float n62 = array2[i + 1];
                    final float n63 = n + n59;
                    final float n64 = n62 + n2;
                    n += n61;
                    final float n65 = n5;
                    final float n66 = n60 + n2;
                    n12 = n6;
                    n10 = n66;
                    n11 = n65;
                    n3 = n63;
                    n2 = n64;
                    break;
                }
                case 'T': {
                    float n67 = 0.0f;
                    float n68 = 0.0f;
                    Label_1864: {
                        if (c3 != 'q' && c3 != 't' && c3 != 'Q') {
                            n67 = n2;
                            n68 = n;
                            if (c3 != 'T') {
                                break Label_1864;
                            }
                        }
                        n68 = 2.0f * n - n3;
                        n67 = 2.0f * n2 - n4;
                    }
                    path.quadTo(n68, n67, array2[i + 0], array2[i + 1]);
                    n = array2[i + 0];
                    n2 = array2[i + 1];
                    n3 = n68;
                    n11 = n5;
                    n12 = n6;
                    n10 = n67;
                    break;
                }
                case 'a': {
                    drawArc(path, n, n2, array2[i + 5] + n, array2[i + 6] + n2, array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                    n += array2[i + 5];
                    final float n69 = array2[i + 6] + n2;
                    n11 = n5;
                    n3 = n;
                    n2 = n69;
                    n12 = n6;
                    n10 = n69;
                    break;
                }
                case 'A': {
                    drawArc(path, n, n2, array2[i + 5], array2[i + 6], array2[i + 0], array2[i + 1], array2[i + 2], array2[i + 3] != 0.0f, array2[i + 4] != 0.0f);
                    n = array2[i + 5];
                    final float n70 = array2[i + 6];
                    n11 = n5;
                    n3 = n;
                    n2 = n70;
                    n12 = n6;
                    n10 = n70;
                    break;
                }
            }
        }
        array[0] = n;
        array[1] = n2;
        array[2] = n3;
        array[3] = n4;
        array[4] = n5;
        array[5] = n6;
    }
    
    private static void arcToBezier(final Path path, final double n, final double n2, final double n3, final double n4, double n5, double n6, double cos, double n7, double sin) {
        final int n8 = (int)Math.ceil(Math.abs(4.0 * sin / 3.141592653589793));
        final double cos2 = Math.cos(cos);
        final double sin2 = Math.sin(cos);
        cos = Math.cos(n7);
        final double sin3 = Math.sin(n7);
        final double n9 = -n3;
        final double n10 = -n3;
        final double n11 = sin / n8;
        int i = 0;
        final double n12 = sin3 * (n10 * sin2) + cos * (n4 * cos2);
        final double n13 = n9 * cos2 * sin3 - n4 * sin2 * cos;
        sin = n7;
        n7 = n6;
        cos = n5;
        n6 = n13;
        n5 = n12;
        while (i < n8) {
            final double n14 = sin + n11;
            final double sin4 = Math.sin(n14);
            final double cos3 = Math.cos(n14);
            final double n15 = n3 * cos2 * cos3 + n - n4 * sin2 * sin4;
            final double n16 = n4 * cos2 * sin4 + (n3 * sin2 * cos3 + n2);
            final double n17 = -n3 * cos2 * sin4 - n4 * sin2 * cos3;
            final double n18 = cos3 * (n4 * cos2) + sin4 * (-n3 * sin2);
            final double tan = Math.tan((n14 - sin) / 2.0);
            sin = Math.sin(n14 - sin);
            sin = (Math.sqrt(tan * (3.0 * tan) + 4.0) - 1.0) * sin / 3.0;
            path.rCubicTo((float)(n6 * sin + cos) - (float)cos, (float)(n7 + n5 * sin) - (float)n7, (float)(n15 - sin * n17) - (float)cos, (float)(n16 - sin * n18) - (float)n7, (float)n15 - (float)cos, (float)n16 - (float)n7);
            ++i;
            n6 = n17;
            sin = n14;
            n7 = n16;
            cos = n15;
            n5 = n18;
        }
    }
    
    private static void drawArc(final Path path, final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2) {
        final double radians = Math.toRadians(n7);
        final double cos = Math.cos(radians);
        final double sin = Math.sin(radians);
        final double n8 = (n * cos + n2 * sin) / n5;
        final double n9 = (-n * sin + n2 * cos) / n6;
        final double n10 = (n3 * cos + n4 * sin) / n5;
        final double n11 = (-n3 * sin + n4 * cos) / n6;
        final double n12 = n8 - n10;
        final double n13 = n9 - n11;
        final double n14 = (n8 + n10) / 2.0;
        final double n15 = (n9 + n11) / 2.0;
        final double n16 = n12 * n12 + n13 * n13;
        if (n16 == 0.0) {
            Log.w("PathParser", " Points are coincident");
            return;
        }
        final double n17 = 1.0 / n16 - 0.25;
        if (n17 < 0.0) {
            Log.w("PathParser", "Points are too far apart " + n16);
            final float n18 = (float)(Math.sqrt(n16) / 1.99999);
            drawArc(path, n, n2, n3, n4, n5 * n18, n6 * n18, n7, b, b2);
            return;
        }
        final double sqrt = Math.sqrt(n17);
        final double n19 = n12 * sqrt;
        final double n20 = n13 * sqrt;
        double n21;
        double n22;
        if (b == b2) {
            n21 = n14 - n20;
            n22 = n19 + n15;
        }
        else {
            n21 = n20 + n14;
            n22 = n15 - n19;
        }
        final double atan2 = Math.atan2(n9 - n22, n8 - n21);
        final double n23 = Math.atan2(n11 - n22, n10 - n21) - atan2;
        final boolean b3 = n23 >= 0.0;
        double n24 = n23;
        if (b2 != b3) {
            if (n23 > 0.0) {
                n24 = n23 - 6.283185307179586;
            }
            else {
                n24 = n23 + 6.283185307179586;
            }
        }
        final double n25 = n5 * n21;
        final double n26 = n22 * n6;
        arcToBezier(path, n25 * cos - n26 * sin, n25 * sin + n26 * cos, n5, n6, n, n2, radians, atan2, n24);
    }
    
    public static void nodesToPath(final PathParser$PathDataNode[] array, final Path path) {
        final float[] array2 = new float[6];
        char type = 'm';
        for (int i = 0; i < array.length; ++i) {
            addCommand(path, array2, type, array[i].type, array[i].params);
            type = array[i].type;
        }
    }
}
