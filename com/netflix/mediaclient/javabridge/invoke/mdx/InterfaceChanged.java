// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.javabridge.invoke.mdx;

import android.net.wifi.WifiInfo;
import org.json.JSONException;
import com.netflix.mediaclient.Log;
import android.net.wifi.WifiManager;
import com.netflix.mediaclient.util.ConnectivityUtils;
import org.json.JSONObject;
import android.content.Context;
import com.netflix.mediaclient.javabridge.invoke.BaseInvoke;

public class InterfaceChanged extends BaseInvoke
{
    private static final String METHOD = "InterfaceChanged";
    private static final String PROPERTY_CONNECTED = "connected";
    private static final String PROPERTY_IPADDRESS = "ipaddress";
    private static final String PROPERTY_NEW_INTERFACE = "newInterface";
    private static final String PROPERTY_SSID = "ssid";
    private static final String TAG = "nf_invoke";
    private static final String TARGET = "mdx";
    
    public InterfaceChanged(final Context arguments) {
        super("mdx", "InterfaceChanged");
        if (arguments == null) {
            throw new IllegalArgumentException("Context is null!");
        }
        this.setArguments(arguments);
    }
    
    public InterfaceChanged(final boolean b, final boolean b2, final String s, final String s2) {
        super("mdx", "InterfaceChanged");
        this.setArguments(b, b2, s, s2);
    }
    
    private void setArguments(final Context context) {
        JSONObject jsonObject;
        String networkType;
        WifiManager wifiManager;
        WifiInfo connectionInfo;
        String ssid = null;
        String localIP4Address;
        String s;
        Block_8_Outer:Label_0122_Outer:
        while (true) {
            while (true) {
                Label_0216: {
                    Label_0214: {
                        try {
                            jsonObject = new JSONObject();
                            networkType = ConnectivityUtils.getNetworkType(context);
                            jsonObject.put("newInterface", (Object)networkType);
                            jsonObject.put("connected", (Object)String.valueOf(ConnectivityUtils.isConnected(context)));
                            if (!"WIFI".equals(networkType)) {
                                break Label_0214;
                            }
                            wifiManager = (WifiManager)context.getSystemService("wifi");
                            if (wifiManager == null) {
                                break Label_0214;
                            }
                            connectionInfo = wifiManager.getConnectionInfo();
                            if (connectionInfo != null) {
                                if (Log.isLoggable()) {
                                    Log.d("nf_invoke", connectionInfo.toString());
                                    Log.d("nf_invoke", "" + connectionInfo.getSSID());
                                }
                                ssid = connectionInfo.getSSID();
                                break Label_0216;
                            }
                            break Label_0214;
                            // iftrue(Label_0190:, localIP4Address == null)
                            while (true) {
                                Block_7: {
                                    Label_0180: {
                                        while (true) {
                                            jsonObject.put("ipaddress", (Object)localIP4Address);
                                            break Label_0180;
                                            Label_0190: {
                                                jsonObject.put("ipaddress", (Object)"");
                                            }
                                            break Label_0180;
                                            Log.d("nf_invoke", "LocalIPAddress:" + localIP4Address);
                                            continue Block_8_Outer;
                                        }
                                        jsonObject.put("ssid", (Object)s);
                                        localIP4Address = ConnectivityUtils.getLocalIP4Address(context);
                                        break Block_7;
                                    }
                                    this.arguments = jsonObject.toString();
                                    return;
                                }
                                continue Label_0122_Outer;
                            }
                        }
                        // iftrue(Label_0171:, !Log.isLoggable())
                        catch (JSONException ex) {
                            Log.e("nf_invoke", "Failed to create JSON object", (Throwable)ex);
                            return;
                        }
                    }
                    ssid = null;
                }
                s = ssid;
                if (ssid == null) {
                    s = "";
                    continue;
                }
                continue;
            }
        }
    }
    
    private void setArguments(final boolean b, final boolean b2, final String s, final String s2) {
        while (true) {
        Label_0097_Outer:
            while (true) {
                Label_0157: {
                    while (true) {
                        JSONObject jsonObject = null;
                        Label_0131: {
                            try {
                                jsonObject = new JSONObject();
                                if (b) {
                                    jsonObject.put("newInterface", (Object)"MOBILE");
                                }
                                else {
                                    jsonObject.put("newInterface", (Object)"WIFI");
                                }
                                if (b2) {
                                    jsonObject.put("connected", (Object)"true");
                                    break Label_0157;
                                }
                                break Label_0131;
                                final String s3;
                                jsonObject.put("ssid", (Object)s3);
                                // iftrue(Label_0144:, s2 == null)
                                // iftrue(Label_0087:, !Log.isLoggable())
                                while (true) {
                                    Block_6: {
                                        break Block_6;
                                        jsonObject.put("ipaddress", (Object)s2);
                                        this.arguments = jsonObject.toString();
                                        return;
                                    }
                                    Log.d("nf_invoke", "LocalIPAddress:" + s2);
                                    continue Label_0097_Outer;
                                }
                            }
                            catch (JSONException ex) {
                                Log.e("nf_invoke", "Failed to create JSON object", (Throwable)ex);
                                return;
                            }
                        }
                        jsonObject.put("connected", (Object)"false");
                        break Label_0157;
                        Label_0144: {
                            jsonObject.put("ipaddress", (Object)"");
                        }
                        continue;
                    }
                }
                String s3 = s;
                if (s == null) {
                    s3 = "";
                    continue;
                }
                continue;
            }
        }
    }
}
