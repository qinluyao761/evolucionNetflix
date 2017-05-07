// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.logging.uiaction.model;

import org.json.JSONException;
import com.netflix.mediaclient.util.JsonUtils;
import org.json.JSONObject;
import com.netflix.mediaclient.service.logging.client.model.UIError;
import com.netflix.mediaclient.servicemgr.UserActionLogging;
import com.netflix.mediaclient.service.logging.client.model.DeviceUniqueId;
import com.netflix.mediaclient.servicemgr.IClientLogging;

public class NavigationEndedEvent extends BaseUIActionSessionEndedEvent
{
    public static final String DESTINATION_VIEW = "destinationView";
    public static final String START_VIEW = "sourceModalView";
    public static final String UIA_SESSION_NAME = "navigate";
    private IClientLogging.ModalView mDestinationView;
    private IClientLogging.ModalView mStartView;
    
    public NavigationEndedEvent(final DeviceUniqueId deviceUniqueId, final long n, final UserActionLogging.CommandName commandName, final IClientLogging.CompletionReason completionReason, final UIError uiError, final IClientLogging.ModalView mStartView, final IClientLogging.ModalView mDestinationView) {
        super("navigate", deviceUniqueId, n, mDestinationView, commandName, completionReason, uiError);
        this.mStartView = mStartView;
        this.mDestinationView = mDestinationView;
    }
    
    public NavigationEndedEvent(final JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        this.mStartView = IClientLogging.ModalView.valueOf(JsonUtils.getString(jsonObject, "sourceModalView", null));
        this.mDestinationView = IClientLogging.ModalView.valueOf(JsonUtils.getString(jsonObject, "destinationView", null));
    }
    
    @Override
    protected JSONObject getData() throws JSONException {
        final JSONObject data = super.getData();
        if (this.mDestinationView != null) {
            data.put("destinationView", (Object)this.mDestinationView.name());
        }
        if (this.mStartView != null) {
            data.put("sourceModalView", (Object)this.mStartView.name());
        }
        return data;
    }
    
    @Override
    public boolean isMemberEvent() {
        return true;
    }
}
