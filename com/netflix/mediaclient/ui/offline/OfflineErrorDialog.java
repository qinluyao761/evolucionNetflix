// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.offline;

import com.netflix.mediaclient.util.LogUtils;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflinePlayableUiList;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflineAdapterData$ViewType;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflineAdapterData;
import com.netflix.mediaclient.ui.common.PlayContextProvider;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import com.netflix.mediaclient.util.ConnectivityUtils;
import android.os.Bundle;
import com.netflix.mediaclient.android.app.Status;
import com.netflix.mediaclient.servicemgr.interface_.offline.OfflinePlayableViewData;
import com.netflix.mediaclient.util.UserVisibleErrorCodeGenerator;
import com.netflix.mediaclient.StatusCode;
import com.netflix.mediaclient.Log;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog$Builder;
import android.app.Dialog;
import com.netflix.mediaclient.android.activity.NetflixActivity;
import com.netflix.mediaclient.android.app.CommonStatus;
import com.netflix.mediaclient.servicemgr.interface_.offline.WatchState;
import com.netflix.mediaclient.servicemgr.interface_.VideoType;
import com.netflix.mediaclient.servicemgr.interface_.offline.StopReason;
import com.netflix.mediaclient.ui.common.PlayContext;
import com.netflix.mediaclient.service.offline.agent.OfflineAgentInterface;
import com.netflix.mediaclient.servicemgr.interface_.offline.DownloadState;
import android.content.DialogInterface$OnClickListener;
import com.netflix.mediaclient.android.fragment.NetflixDialogFrag;

public class OfflineErrorDialog extends NetflixDialogFrag
{
    private static final String CHANGE_PLAN_URL = "https://www.netflix.com/changeplan";
    private static final long MIN_SPACE_CONSUMED_FOR_DELETE_OPTION = 50000000L;
    private static final String PARAMS_HAS_SOME_DOWNLOADED_DATA = "hasNetflixDownloadedData";
    private static final String PARAMS_USER_REQUIRES_WIFI_CONNECTION = "requiresWiFiConnection";
    private static final String PARAM_DOWNLOAD_STATE = "downloadState";
    private static final String PARAM_LAST_PERSISTENT_STATUS = "status_bundle";
    private static final String PARAM_PLAYABLE_ID = "playableId";
    private static final String PARAM_STATUS_CODE_INT_VALUE = "status_code_int_value";
    private static final String PARAM_STATUS_DISPLAYABLE_MESSAGE = "status_displayable_message";
    private static final String PARAM_STATUS_IS_ERROR_OR_WARNING = "status_is_error_or_warning";
    private static final String PARAM_STATUS_SHOW_MESSAGE = "status_show_message";
    private static final String PARAM_STOP_REASON = "stopReason";
    private static final String PARAM_VIDEO_TYPE = "videoType";
    private static final String PARAM_WATCH_STATE = "watchState";
    private static final String TAG = "offlineErrorDialog";
    private static final boolean TESTING = false;
    private final DialogInterface$OnClickListener mDeleteAction;
    private final DialogInterface$OnClickListener mDeleteAndCreateAction;
    private final DialogInterface$OnClickListener mDownloadResumeAction;
    private DownloadState mDownloadState;
    private int mErrorStatusCodeIntValue;
    private String mErrorStatusDisplayMessage;
    private boolean mErrorStatusShowMessage;
    private final DialogInterface$OnClickListener mMyDownloadAction;
    private final DialogInterface$OnClickListener mNoAction;
    private OfflineAgentInterface mOfflineAgent;
    private PlayContext mPlayContext;
    private String mPlayableId;
    private OfflineErrorDialog$DeleteAndTryAgainHelper mRetryHelper;
    private final DialogInterface$OnClickListener mSeePlanOptionsAction;
    private boolean mStatusIsErrorOrWarning;
    private StopReason mStopReason;
    private VideoType mVideoType;
    private WatchState mWatchState;
    
    public OfflineErrorDialog() {
        this.mStatusIsErrorOrWarning = false;
        this.mErrorStatusShowMessage = false;
        this.mErrorStatusDisplayMessage = "";
        this.mErrorStatusCodeIntValue = CommonStatus.OK.getStatusCode().getValue();
        this.mDeleteAction = (DialogInterface$OnClickListener)new OfflineErrorDialog$1(this);
        this.mNoAction = (DialogInterface$OnClickListener)new OfflineErrorDialog$2(this);
        this.mDownloadResumeAction = (DialogInterface$OnClickListener)new OfflineErrorDialog$3(this);
        this.mDeleteAndCreateAction = (DialogInterface$OnClickListener)new OfflineErrorDialog$4(this);
        this.mMyDownloadAction = (DialogInterface$OnClickListener)new OfflineErrorDialog$5(this);
        this.mSeePlanOptionsAction = (DialogInterface$OnClickListener)new OfflineErrorDialog$6(this);
    }
    
    private Dialog createDefaultDialogFromOfflineStates(final String s, final boolean b, final boolean b2) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle(2131231333).setMessage(this.getNetflixActivity().getString(2131231332, new Object[] { s }));
        if (b) {
            alertDialog$Builder.setNegativeButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction);
        }
        if (b2) {
            alertDialog$Builder.setPositiveButton(2131231224, this.mDownloadResumeAction);
        }
        alertDialog$Builder.setNeutralButton(2131231167, this.mNoAction);
        return alertDialog$Builder.create();
    }
    
    private AlertDialog createDialogFromStatusCode(final boolean b) {
        if (Log.isLoggable()) {
            Log.i("offlineErrorDialog", "createDialogFromStatusCode statusCode=" + this.mErrorStatusCodeIntValue);
        }
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        if (this.mErrorStatusCodeIntValue == StatusCode.DL_NOT_ENOUGH_FREE_SPACE.getValue()) {
            return this.createNotEnoughSpaceDialog(b);
        }
        String s;
        if (this.mErrorStatusShowMessage) {
            s = this.mErrorStatusDisplayMessage;
        }
        else {
            s = this.getNetflixActivity().getString(2131231332, new Object[] { UserVisibleErrorCodeGenerator.addParenthesisWithPrefixSpace(UserVisibleErrorCodeGenerator.getOfflineErrorCodeFromStatusIntValue(this.mErrorStatusCodeIntValue)) });
        }
        alertDialog$Builder.setMessage(s);
        if (this.mErrorStatusCodeIntValue == StatusCode.DL_LIMIT_CANT_DOWNLOAD_TILL_DATE.getValue()) {
            alertDialog$Builder.setTitle(2131231335).setPositiveButton(2131231167, this.mDeleteAction);
        }
        else if (this.mErrorStatusCodeIntValue == StatusCode.DL_LIMIT_TOO_MANY_DOWNLOADED_DELETE_SOME.getValue()) {
            alertDialog$Builder.setTitle(2131231335).setPositiveButton(2131231167, this.mNoAction);
            if (this.notInMyDownloadScreen()) {
                alertDialog$Builder.setNegativeButton(2131231314, this.mMyDownloadAction);
            }
        }
        else if (this.mErrorStatusCodeIntValue == StatusCode.DL_LIMIT_TOO_MANY_DEVICES_PLAN_OPTION.getValue()) {
            alertDialog$Builder.setTitle(2131231335).setNegativeButton(2131231321, this.mSeePlanOptionsAction).setPositiveButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction);
        }
        else if (this.mErrorStatusCodeIntValue == StatusCode.DL_WARNING_DL_N_TIMES_BEFORE_DATE.getValue()) {
            alertDialog$Builder.setNegativeButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction).setPositiveButton(2131230926, this.mNoAction);
        }
        else {
            alertDialog$Builder.setTitle(2131231333).setMessage(s);
            alertDialog$Builder.setNegativeButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction).setPositiveButton(2131231224, this.mDeleteAndCreateAction);
        }
        return alertDialog$Builder.create();
    }
    
    private static OfflineErrorDialog createDialogInternal(final VideoType videoType, final OfflinePlayableViewData offlinePlayableViewData, final OfflineAgentInterface offlineAgentInterface, final Status status) {
        final OfflineErrorDialog offlineErrorDialog = new OfflineErrorDialog();
        final Bundle arguments = new Bundle();
        arguments.putString("playableId", offlinePlayableViewData.getPlayableId());
        arguments.putString("videoType", videoType.toString());
        arguments.putInt("watchState", offlinePlayableViewData.getWatchState().getIntValue());
        arguments.putInt("downloadState", offlinePlayableViewData.getDownloadState().getIntValue());
        arguments.putBundle("status_bundle", getBundleForLastPersistentStatus(status));
        final StopReason stopReason = offlinePlayableViewData.getStopReason();
        int n;
        if (stopReason != null) {
            n = stopReason.getIntValue();
        }
        else {
            n = StopReason.Unknown.getIntValue();
        }
        arguments.putInt("stopReason", n);
        arguments.putBoolean("hasNetflixDownloadedData", hasSomeNetflixDownloadData(offlineAgentInterface));
        arguments.putBoolean("requiresWiFiConnection", offlineAgentInterface.getRequiresUnmeteredNetwork());
        offlineErrorDialog.setArguments(arguments);
        return offlineErrorDialog;
    }
    
    private AlertDialog createGeoNotPlayableDialog() {
        Log.i("offlineErrorDialog", "createGeoNotPlayableDialog");
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle(2131231341).setMessage(2131231340).setNegativeButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction).setPositiveButton(2131231167, this.mNoAction);
        return alertDialog$Builder.create();
    }
    
    private Dialog createLicenseExpiredDialog() {
        Log.i("offlineErrorDialog", "createLicenseExpiredDialog");
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle(2131231336);
        alertDialog$Builder.setNegativeButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction);
        if (ConnectivityUtils.isConnected((Context)this.getNetflixActivity())) {
            alertDialog$Builder.setMessage(2131231339);
            alertDialog$Builder.setPositiveButton(2131231318, (DialogInterface$OnClickListener)new OfflineErrorDialog$7(this));
        }
        else {
            alertDialog$Builder.setMessage(2131231338);
            alertDialog$Builder.setPositiveButton(2131231319, this.mNoAction);
        }
        return alertDialog$Builder.create();
    }
    
    private AlertDialog createNotEnoughSpaceDialog(final boolean b) {
        Log.i("offlineErrorDialog", "createNotEnoughSpaceDialog");
        boolean b2 = false;
        final AlertDialog$Builder setTitle = new AlertDialog$Builder((Context)this.getNetflixActivity()).setTitle(2131231347);
        if (b) {
            setTitle.setMessage(2131231345);
            if (this.notInMyDownloadScreen()) {
                b2 = true;
                setTitle.setNegativeButton(2131231314, this.mMyDownloadAction);
            }
        }
        else {
            setTitle.setMessage(2131231346);
        }
        setTitle.setPositiveButton(2131231167, this.mNoAction);
        if (this.mDownloadState == DownloadState.CreateFailed) {
            if (b2) {
                setTitle.setNeutralButton(2131231224, this.mDeleteAndCreateAction);
            }
            else {
                setTitle.setNegativeButton(2131231224, this.mDeleteAndCreateAction);
            }
        }
        else if (b2) {
            setTitle.setNeutralButton(2131231224, this.mDownloadResumeAction);
        }
        else {
            setTitle.setNegativeButton(2131231224, this.mDownloadResumeAction);
        }
        return setTitle.create();
    }
    
    static OfflineErrorDialog createOfflineErrorStateDialog(final VideoType videoType, final OfflinePlayableViewData offlinePlayableViewData, final OfflineAgentInterface offlineAgentInterface) {
        return createDialogInternal(videoType, offlinePlayableViewData, offlineAgentInterface, offlinePlayableViewData.getLastPersistentErrorStatus());
    }
    
    static OfflineErrorDialog createOfflineWarningDialog(final VideoType videoType, final OfflinePlayableViewData offlinePlayableViewData, final OfflineAgentInterface offlineAgentInterface, final Status status) {
        return createDialogInternal(videoType, offlinePlayableViewData, offlineAgentInterface, status);
    }
    
    private AlertDialog createPlayWindowExpiredButRenewableDialog() {
        Log.i("offlineErrorDialog", "createPlayWindowExpiredButRenewableDialog");
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle(2131231336);
        alertDialog$Builder.setNegativeButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction);
        if (ConnectivityUtils.isConnected((Context)this.getNetflixActivity())) {
            alertDialog$Builder.setMessage(2131231339);
            alertDialog$Builder.setPositiveButton(2131231318, (DialogInterface$OnClickListener)new OfflineErrorDialog$8(this));
        }
        else {
            alertDialog$Builder.setMessage(2131231338);
            alertDialog$Builder.setPositiveButton(2131231319, this.mNoAction);
        }
        return alertDialog$Builder.create();
    }
    
    private AlertDialog createPlayWindowFinalExpiredDialog() {
        Log.i("offlineErrorDialog", "createPlayWindowFinalExpiredDialog");
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setTitle(2131231336).setMessage(2131231337).setPositiveButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction);
        return alertDialog$Builder.create();
    }
    
    private Dialog createViewWindowExpiredDialog() {
        Log.i("offlineErrorDialog", "createViewWindowExpiredDialog");
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this.getActivity());
        alertDialog$Builder.setMessage(2131231342).setPositiveButton(this.getStringResourceIdForCancelOrDelete(), this.mDeleteAction);
        return alertDialog$Builder.create();
    }
    
    private Dialog genericErrorDialogNoAction() {
        return new AlertDialog$Builder((Context)this.getNetflixActivity()).setTitle(2131231333).setMessage(this.getNetflixActivity().getResources().getString(2131231332, new Object[] { "" })).setNegativeButton(2131231167, this.mNoAction).create();
    }
    
    private static Bundle getBundleForLastPersistentStatus(final Status status) {
        final Bundle bundle = new Bundle();
        bundle.putBoolean("status_is_error_or_warning", status.isError() || StatusCode.isWarning(status.getStatusCode().getValue()));
        bundle.putBoolean("status_show_message", status.shouldDisplayMessage());
        bundle.putString("status_displayable_message", status.getMessage());
        bundle.putInt("status_code_int_value", status.getStatusCode().getValue());
        return bundle;
    }
    
    private OfflineErrorDialog$DeleteAndTryAgainHelper getDeleteAndRetryHelper() {
        return this.mRetryHelper;
    }
    
    private OfflineAgentInterface getOfflineAgent() {
        if (this.mOfflineAgent == null) {
            final ServiceManager serviceManager = this.getServiceManager();
            if (serviceManager != null) {
                this.mOfflineAgent = serviceManager.getOfflineAgent();
            }
        }
        return this.mOfflineAgent;
    }
    
    private PlayContext getPlayContextSafely() {
        if (this.mPlayContext == null) {
            if (this.getNetflixActivity() instanceof PlayContextProvider) {
                this.mPlayContext = ((PlayContextProvider)this.getNetflixActivity()).getPlayContext();
            }
            if (this.mPlayContext == null) {
                this.mPlayContext = PlayContext.EMPTY_CONTEXT;
            }
        }
        return this.mPlayContext;
    }
    
    private String getPlayableId() {
        return this.mPlayableId;
    }
    
    private int getStringResourceIdForCancelOrDelete() {
        if (this.mDownloadState == DownloadState.Complete) {
            return 2131231309;
        }
        return 2131231307;
    }
    
    private VideoType getVideoType() {
        return this.mVideoType;
    }
    
    private static boolean hasSomeNetflixDownloadData(final OfflineAgentInterface offlineAgentInterface) {
        boolean b = false;
        final OfflinePlayableUiList latestOfflinePlayableList = offlineAgentInterface.getLatestOfflinePlayableList();
        long n = 0L;
        long n2;
        for (int i = 0; i < latestOfflinePlayableList.size(); ++i, n = n2) {
            final OfflineAdapterData$ViewType type = latestOfflinePlayableList.get(i).getVideoAndProfileData().type;
            if (type != OfflineAdapterData$ViewType.SHOW) {
                n2 = n;
                if (type != OfflineAdapterData$ViewType.MOVIE) {
                    continue;
                }
            }
            n2 = n + latestOfflinePlayableList.getCurrentSpace(i);
        }
        if (n > 50000000L) {
            b = true;
        }
        return b;
    }
    
    private boolean notInMyDownloadScreen() {
        return !(this.getNetflixActivity() instanceof OfflineActivity);
    }
    
    private void populateErrorStatusDataFromBundle(final Bundle bundle) {
        if (bundle != null) {
            this.mStatusIsErrorOrWarning = bundle.getBoolean("status_is_error_or_warning", false);
            this.mErrorStatusShowMessage = bundle.getBoolean("status_show_message", false);
            this.mErrorStatusDisplayMessage = bundle.getString("status_displayable_message", "");
            this.mErrorStatusCodeIntValue = bundle.getInt("status_code_int_value", CommonStatus.OK.getStatusCode().getValue());
            return;
        }
        this.mStatusIsErrorOrWarning = false;
        this.mErrorStatusShowMessage = false;
        this.mErrorStatusDisplayMessage = "";
        this.mErrorStatusCodeIntValue = CommonStatus.OK.getStatusCode().getValue();
    }
    
    public Dialog onCreateDialog(Bundle arguments) {
        final boolean b = false;
        final boolean b2 = true;
        super.onCreate(arguments);
        arguments = this.getArguments();
        this.mPlayableId = arguments.getString("playableId");
        this.mVideoType = VideoType.valueOf(arguments.getString("videoType"));
        this.mWatchState = WatchState.getStateByValue(arguments.getInt("watchState"));
        this.mDownloadState = DownloadState.getStateByValue(arguments.getInt("downloadState"));
        this.mStopReason = StopReason.getStopReasonByValue(arguments.getInt("stopReason"));
        this.populateErrorStatusDataFromBundle(arguments.getBundle("status_bundle"));
        final boolean boolean1 = arguments.getBoolean("hasNetflixDownloadedData", false);
        final boolean boolean2 = arguments.getBoolean("requiresWiFiConnection", true);
        if (this.mPlayableId == null || this.mVideoType == null || this.mVideoType == VideoType.UNKNOWN) {
            return this.genericErrorDialogNoAction();
        }
        this.mRetryHelper = new OfflineErrorDialog$DeleteAndTryAgainHelper(this);
        if (Log.isLoggable()) {
            Log.i("offlineErrorDialog", "downloadState=" + this.mDownloadState + " watchState=" + this.mWatchState + " stopReason=" + this.mStopReason);
        }
        String addParenthesisWithPrefixSpace = "";
        boolean b3 = false;
        boolean b4 = false;
        Label_0299: {
            switch (OfflineErrorDialog$9.$SwitchMap$com$netflix$mediaclient$servicemgr$interface_$offline$DownloadState[this.mDownloadState.ordinal()]) {
                default: {
                    LogUtils.reportErrorSafely("OfflineErrorDialog unhandled downloadState" + this.mDownloadState);
                    b3 = false;
                    b4 = b;
                    break;
                }
                case 1: {
                    b3 = false;
                    b4 = b;
                    break;
                }
                case 2: {
                    b3 = false;
                    b4 = b;
                    break;
                }
                case 3: {
                    b3 = false;
                    b4 = b;
                    break;
                }
                case 4: {
                    addParenthesisWithPrefixSpace = UserVisibleErrorCodeGenerator.addParenthesisWithPrefixSpace(UserVisibleErrorCodeGenerator.getOfflineErrorCodeForStoppedDownload(this.mStopReason));
                    switch (OfflineErrorDialog$9.$SwitchMap$com$netflix$mediaclient$servicemgr$interface_$offline$StopReason[this.mStopReason.ordinal()]) {
                        default: {
                            LogUtils.reportErrorSafely("OfflineErrorDialog unhandled stopReason" + this.mStopReason);
                            b4 = true;
                            b3 = b2;
                            break Label_0299;
                        }
                        case 1: {
                            b3 = false;
                            b4 = b;
                            break Label_0299;
                        }
                        case 2: {
                            b3 = false;
                            b4 = b;
                            break Label_0299;
                        }
                        case 3: {
                            b4 = true;
                            b3 = b2;
                            break Label_0299;
                        }
                        case 4: {
                            b4 = true;
                            b3 = b2;
                            break Label_0299;
                        }
                        case 5: {
                            return this.createNotEnoughSpaceDialog(boolean1);
                        }
                        case 6: {
                            b3 = false;
                            b4 = b;
                            break Label_0299;
                        }
                        case 7: {
                            if (boolean2) {
                                return DownloadButtonDialogHelper.createNoWifiDialog((Context)this.getNetflixActivity(), this.getPlayableId(), this.mVideoType, true);
                            }
                            return DownloadButtonDialogHelper.createNoInternetDialog((Context)this.getNetflixActivity(), this.getPlayableId(), true);
                        }
                        case 8: {
                            return DownloadButtonDialogHelper.createNoInternetDialog((Context)this.getNetflixActivity(), this.getPlayableId(), true);
                        }
                        case 9: {
                            b3 = false;
                            b4 = b;
                            break Label_0299;
                        }
                        case 10: {
                            b3 = false;
                            b4 = b;
                            break Label_0299;
                        }
                        case 11:
                        case 12:
                        case 13: {
                            b4 = true;
                            b3 = b2;
                            break Label_0299;
                        }
                    }
                    break;
                }
                case 5: {
                    final String addParenthesisWithPrefixSpace2 = UserVisibleErrorCodeGenerator.addParenthesisWithPrefixSpace(UserVisibleErrorCodeGenerator.getOfflineErrorCodeForCompleteDownload(this.mWatchState));
                    b4 = b;
                    b3 = b2;
                    addParenthesisWithPrefixSpace = addParenthesisWithPrefixSpace2;
                    switch (OfflineErrorDialog$9.$SwitchMap$com$netflix$mediaclient$servicemgr$interface_$offline$WatchState[this.mWatchState.ordinal()]) {
                        case 2: {
                            b3 = false;
                            b4 = b;
                            addParenthesisWithPrefixSpace = addParenthesisWithPrefixSpace2;
                        }
                        case 1: {
                            break Label_0299;
                        }
                        default: {
                            LogUtils.reportErrorSafely("OfflineErrorDialog unhandled watchState" + this.mWatchState);
                            b4 = true;
                            b3 = b2;
                            addParenthesisWithPrefixSpace = addParenthesisWithPrefixSpace2;
                            break Label_0299;
                        }
                        case 3: {
                            return this.createLicenseExpiredDialog();
                        }
                        case 4: {
                            return this.createPlayWindowExpiredButRenewableDialog();
                        }
                        case 5: {
                            return this.createPlayWindowFinalExpiredDialog();
                        }
                        case 6: {
                            return this.createViewWindowExpiredDialog();
                        }
                        case 7: {
                            return this.createGeoNotPlayableDialog();
                        }
                    }
                    break;
                }
                case 6: {
                    b3 = false;
                    b4 = b;
                    break;
                }
                case 7: {
                    b3 = false;
                    b4 = b;
                    break;
                }
                case 8: {
                    b3 = false;
                    b4 = b;
                    break;
                }
            }
        }
        if (this.mStatusIsErrorOrWarning) {
            return this.createDialogFromStatusCode(boolean1);
        }
        return this.createDefaultDialogFromOfflineStates(addParenthesisWithPrefixSpace, b3, b4);
    }
    
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mRetryHelper != null) {
            this.mRetryHelper.cleanUp();
        }
    }
}