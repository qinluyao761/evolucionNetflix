// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.ui.launch;

import com.netflix.mediaclient.android.activity.ServiceErrorsHandler;
import android.support.v7.app.ActionBar;
import android.view.View;
import com.netflix.mediaclient.android.fragment.LoadingView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.auth.api.credentials.CredentialRequest$Builder;
import android.os.Bundle;
import com.netflix.mediaclient.util.LoginUtils;
import com.netflix.mediaclient.ui.login.AccountActivity;
import com.netflix.mediaclient.service.logging.perf.Sessions;
import com.netflix.mediaclient.service.logging.perf.PerformanceProfiler;
import com.netflix.mediaclient.servicemgr.ManagerStatusListener;
import com.netflix.mediaclient.ui.ums.EndOfGrandfatheringActivity;
import com.netflix.mediaclient.ui.ums.EogUtils;
import com.netflix.mediaclient.ui.home.HomeActivity;
import com.netflix.mediaclient.ui.offline.OfflineActivity;
import com.netflix.mediaclient.ui.profiles.ProfileSelectionActivity;
import com.netflix.mediaclient.util.ConnectivityUtils;
import android.content.IntentSender$SendIntentException;
import android.app.Activity;
import com.netflix.mediaclient.util.IntentUtils;
import com.netflix.mediaclient.service.webclient.model.leafs.SignInConfigData;
import com.netflix.mediaclient.ui.signup.SignupActivity;
import com.netflix.mediaclient.ui.signup.ReactSignupActivity;
import com.netflix.mediaclient.service.configuration.SignInConfiguration;
import com.netflix.mediaclient.service.user.UserAgentBroadcastIntents;
import com.google.android.gms.common.api.Api$ApiOptions$NotRequiredOptions;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient$Builder;
import com.netflix.mediaclient.servicemgr.ManagerCallback;
import com.netflix.mediaclient.servicemgr.SignInLogging$CredentialService;
import com.netflix.mediaclient.service.logging.apm.model.Display;
import com.netflix.mediaclient.util.StringUtils;
import com.netflix.mediaclient.ui.login.LoginActivity;
import com.netflix.mediaclient.service.logging.apm.model.DeepLink;
import com.netflix.mediaclient.service.logging.apm.model.UIBrowseStartupSessionCustomData;
import com.netflix.mediaclient.servicemgr.ApplicationPerformanceMetricsLogging$UiStartupTrigger;
import com.netflix.mediaclient.util.log.ConsolidatedLoggingUtils;
import com.netflix.mediaclient.servicemgr.ApplicationPerformanceMetricsLogging;
import com.netflix.mediaclient.util.PreferenceUtils;
import com.netflix.mediaclient.servicemgr.IClientLogging$ModalView;
import com.netflix.mediaclient.util.DeviceUtils;
import com.netflix.mediaclient.service.logging.client.model.Error;
import com.netflix.mediaclient.util.log.SignInLogUtils;
import com.netflix.mediaclient.servicemgr.IClientLogging$CompletionReason;
import com.netflix.mediaclient.servicemgr.SignInLogging$SignInType;
import com.netflix.mediaclient.StatusCode;
import android.content.Intent;
import com.netflix.mediaclient.Log;
import android.content.Context;
import com.netflix.mediaclient.protocol.nflx.NflxHandlerFactory;
import com.netflix.mediaclient.protocol.netflixcom.NetflixComHandlerFactory;
import com.netflix.mediaclient.protocol.nflx.NflxHandler$Response;
import com.google.android.gms.auth.api.credentials.Credential;
import com.netflix.mediaclient.servicemgr.ServiceManager;
import android.content.BroadcastReceiver;
import com.netflix.mediaclient.android.app.Status;
import java.util.concurrent.atomic.AtomicBoolean;
import com.google.android.gms.common.api.GoogleApiClient;
import com.netflix.mediaclient.servicemgr.interface_.Video;
import com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient$ConnectionCallbacks;
import com.netflix.mediaclient.android.activity.NetflixActivity;

public class LaunchActivity extends NetflixActivity implements GoogleApiClient$ConnectionCallbacks, GoogleApiClient$OnConnectionFailedListener
{
    private static final boolean HANG_ON_LOADING_SCREEN = false;
    private static final String PREFERENCE_NON_MEMBER_PLAYBACK = "prefs_non_member_playback";
    private static final int PROFILE_GATE_SHOULD_BE_SHOWN_TIMES = 2;
    private static final int RC_READ = 2;
    private static final int SMARTLOCK_TIMEOUT_MS = 30000;
    private static final boolean START_DETAILS_ACTIVITY_ON_LAUNCH = false;
    private static final String TAG = "LaunchActivity";
    private static final Video sampleVideo;
    private boolean isLoading;
    private GoogleApiClient mCredentialsApiClient;
    private String mHint;
    private boolean mIsErrorDialogVisible;
    private AtomicBoolean mLoginWorkflowInProgress;
    private Status mManagerStatus;
    private final Runnable mSmartLockTimeoutTask;
    private long mSplashScreenStarted;
    private long mStarted;
    private final BroadcastReceiver nflxBroadcastReceiver;
    
    static {
        sampleVideo = (Video)new LaunchActivity$6();
    }
    
    public LaunchActivity() {
        this.isLoading = true;
        this.mLoginWorkflowInProgress = new AtomicBoolean(false);
        this.mSmartLockTimeoutTask = (Runnable)new LaunchActivity$2(this);
        this.nflxBroadcastReceiver = (BroadcastReceiver)new LaunchActivity$3(this);
    }
    
    private NflxHandler$Response canHandleIntent() {
        final Intent intent = this.getIntent();
        if (NetflixComHandlerFactory.canHandle(intent)) {
            return NetflixComHandlerFactory.handle(this, intent);
        }
        try {
            final NflxHandler$Response handle = NflxHandlerFactory.getHandlerForIntent(this, intent, this.mStarted).handle();
            NflxHandlerFactory.endCommandSessions((Context)this, intent);
            return handle;
        }
        catch (Throwable t) {
            Log.e("LaunchActivity", "Failed to parse nflx url ", t);
            return NflxHandler$Response.NOT_HANDLING;
        }
    }
    
    private void handleLoginComplete(final Status status, final Credential credential) {
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "Login Complete - Status: " + status);
        }
        this.setRequestedOrientation(-1);
        if (status.isSucces() || status.getStatusCode() == StatusCode.NRD_REGISTRATION_EXISTS) {
            this.showDebugToast(this.getString(2131296789));
            SignInLogUtils.reportSignInRequestSessionEnded((Context)this, SignInLogging$SignInType.smartLock, IClientLogging$CompletionReason.success, (Error)null);
            return;
        }
        Log.e("LaunchActivity", "Login failed, redirect to LoginActivity with credential and status");
        this.handleUserLoginWithError(this.getServiceManager(), credential, status);
        SignInLogUtils.reportSignInRequestSessionEnded((Context)this, SignInLogging$SignInType.smartLock, IClientLogging$CompletionReason.failed, status.getError());
    }
    
    private void handleManagerReady(final ServiceManager serviceManager) {
        if (this.isFinishing()) {
            return;
        }
        Log.d("LaunchActivity", "LaunchActivity::handleManagerReady: starts ");
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging();
        final boolean userLoggedIn = serviceManager.isUserLoggedIn();
        if (this.mSplashScreenStarted > 0L) {
            Log.d("LaunchActivity", "Splash screen was displayed, reporting");
            applicationPerformanceMetricsLogging.uiViewChanged(DeviceUtils.isPortrait((Context)this), IClientLogging$ModalView.appLoading, this.mSplashScreenStarted);
        }
        if (!userLoggedIn || PreferenceUtils.getBooleanPref((Context)this, "prefs_non_member_playback", false)) {
            Log.d("LaunchActivity", "LaunchActivity::handleManagerReady: user not logged in ");
            this.handleUserNotSignedIn(serviceManager);
            return;
        }
        Log.d("LaunchActivity", "LaunchActivity::handleManagerReady: user logged in ");
        this.handleUserSignedIn(serviceManager);
    }
    
    private void handleUserLogin(final ServiceManager serviceManager) {
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging();
        final Display display = ConsolidatedLoggingUtils.getDisplay((Context)this);
        Log.d("LaunchActivity", "User is NOT logged in, redirect to Login screen");
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiStartupSession(ApplicationPerformanceMetricsLogging$UiStartupTrigger.touchGesture, IClientLogging$ModalView.login, this.mStarted, display, null, UIBrowseStartupSessionCustomData.create((Context)this));
        }
        final Intent startIntent = LoginActivity.createStartIntent((Context)this);
        if (StringUtils.isNotEmpty(this.mHint)) {
            startIntent.putExtra("email", this.mHint);
        }
        this.startNextActivity(startIntent);
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiBrowseStartupSession(this.mStarted);
        }
        this.finish();
    }
    
    private void handleUserLoginWithCredentials(final Credential credential) {
        SignInLogUtils.reportCredentialRetrievalSessionEnded((Context)this, SignInLogging$CredentialService.GooglePlayService, IClientLogging$CompletionReason.success, (Error)null);
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = this.getServiceManager().getClientLogging().getApplicationPerformanceMetricsLogging();
        final Display display = ConsolidatedLoggingUtils.getDisplay((Context)this);
        Log.d("LaunchActivity", "User is NOT logged in, redirect to Login screen with login credentials");
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiStartupSession(ApplicationPerformanceMetricsLogging$UiStartupTrigger.touchGesture, IClientLogging$ModalView.login, this.mStarted, display, null, UIBrowseStartupSessionCustomData.create((Context)this));
        }
        this.getServiceManager().loginUser(credential.getId(), credential.getPassword(), (ManagerCallback)new LaunchActivity$5(this, credential));
    }
    
    private void handleUserLoginWithError(final ServiceManager serviceManager, final Credential credential, final Status status) {
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging();
        final Display display = ConsolidatedLoggingUtils.getDisplay((Context)this);
        Log.d("LaunchActivity", "User is NOT logged in, redirect to Login screen");
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiStartupSession(ApplicationPerformanceMetricsLogging$UiStartupTrigger.touchGesture, IClientLogging$ModalView.login, this.mStarted, display, null, UIBrowseStartupSessionCustomData.create((Context)this));
        }
        this.startNextActivity(LoginActivity.createStartIntent((Context)this, credential, status));
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiBrowseStartupSession(this.mStarted);
        }
        this.finish();
    }
    
    private void handleUserNotSignedIn(final ServiceManager serviceManager) {
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "handleUserNotSignedIn starts");
        }
        if (this.isAutoLoginEnabled()) {
            if (Log.isLoggable()) {
                Log.d("LaunchActivity", "Google Play Services are available, try to retrieve credentials");
            }
            SignInLogUtils.reportSignInRequestSessionStarted((Context)this, SignInLogging$SignInType.smartLock);
            SignInLogUtils.reportCredentialRetrievalSessionStarted((Context)this, SignInLogging$CredentialService.GooglePlayService);
            (this.mCredentialsApiClient = new GoogleApiClient$Builder((Context)this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Auth.CREDENTIALS_API).build()).connect();
            this.handler.postDelayed(this.mSmartLockTimeoutTask, 30000L);
            return;
        }
        Log.d("LaunchActivity", "Google Play Services are not available, go with regular workflow");
        this.handleUserNotSignedInWithoutCredentials(serviceManager);
    }
    
    private void handleUserNotSignedInWithoutCredentials(final ServiceManager serviceManager) {
        this.resetTimeout();
        final boolean booleanPref = PreferenceUtils.getBooleanPref((Context)this, "prefs_non_member_playback", false);
        if ((isSignUpEnabled(serviceManager) && !this.getNetflixApplication().hasSignedUpOnce()) || booleanPref) {
            this.handleUserSignUp(serviceManager);
        }
        else {
            this.handleUserLogin(serviceManager);
        }
        UserAgentBroadcastIntents.signalUserAccountNotLoggedIn((Context)this);
    }
    
    private void handleUserSignUp(final ServiceManager serviceManager) {
        Log.d("LaunchActivity", "User has not signed up, redirect to Signup screen");
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging();
        final Display display = ConsolidatedLoggingUtils.getDisplay((Context)this);
        final SignInConfigData signInConfigData = new SignInConfiguration((Context)this).getSignInConfigData();
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiStartupSession(ApplicationPerformanceMetricsLogging$UiStartupTrigger.touchGesture, IClientLogging$ModalView.signupPrompt, this.mStarted, display, null, UIBrowseStartupSessionCustomData.create((Context)this));
        }
        if (signInConfigData != null && signInConfigData.getReactNativeMode() != null) {
            this.startNextActivity(ReactSignupActivity.createStartIntent((Context)this, this.getIntent()));
        }
        else {
            this.startNextActivity(SignupActivity.createStartIntent((Context)this, this.getIntent()));
        }
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiBrowseStartupSession(this.mStarted);
        }
        this.finish();
    }
    
    private void handleUserSignedIn(final ServiceManager serviceManager) {
        NflxHandler$Response canHandleIntent = null;
        if (serviceManager.getCurrentProfile() != null) {
            canHandleIntent = this.canHandleIntent();
        }
        if (canHandleIntent != null && canHandleIntent == NflxHandler$Response.HANDLING) {
            Log.d("LaunchActivity", "Handled by nflx workflow");
            this.finish();
            return;
        }
        if (canHandleIntent != null && canHandleIntent == NflxHandler$Response.HANDLING_WITH_DELAY) {
            Log.d("LaunchActivity", "Handled by nflx workflow with delay. Stay on splash screen...");
            return;
        }
        if (serviceManager.getCurrentProfile() == null || this.shouldProfileGateBeShown(serviceManager)) {
            this.showProfileGate(serviceManager);
            return;
        }
        this.showStartPageForSignedInUser(serviceManager);
    }
    
    public static boolean isSignUpEnabled(final ServiceManager serviceManager) {
        return serviceManager != null && serviceManager.isReady() && serviceManager.getSignUpParams() != null && serviceManager.getSignUpParams().isSignUpEnabled();
    }
    
    private void onCredentialRetrieved(final Credential credential) {
        this.resetTimeout();
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "Google Play Services: Credential Retrieved: " + credential.getId());
            Log.d("LaunchActivity", "Google Play Services: Credential name: " + credential.getName());
            Log.d("LaunchActivity", "Google Play Services: Credential account type: " + credential.getAccountType());
        }
        this.showDebugToast("Google Play Services: Credential Retrieved");
        this.handleUserLoginWithCredentials(credential);
    }
    
    private void registerNflxReceiver() {
        Log.d("LaunchActivity", "Register receiver");
        IntentUtils.registerSafelyLocalBroadcastReceiver((Context)this, this.nflxBroadcastReceiver, "LocalIntentNflxUi", "com.netflix.mediaclient.intent.action.HANDLER_RESULT");
    }
    
    private void resetTimeout() {
        this.mLoginWorkflowInProgress.set(true);
        this.handler.removeCallbacks(this.mSmartLockTimeoutTask);
    }
    
    private void resolveResult(com.google.android.gms.common.api.Status credentialRequestResultToError) {
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "Google Play Services: Resolving: " + credentialRequestResultToError);
        }
        int n = 0;
        while (true) {
            Label_0125: {
                if (credentialRequestResultToError == null || !credentialRequestResultToError.hasResolution()) {
                    break Label_0125;
                }
                Log.d("LaunchActivity", "Google Play Services: STATUS: RESOLVING");
                try {
                    credentialRequestResultToError.startResolutionForResult(this, 2);
                    if (n != 0) {
                        Log.d("LaunchActivity", "Failed to initiate resolve, start regular user not signed in workflow");
                        credentialRequestResultToError = (com.google.android.gms.common.api.Status)SignInLogUtils.credentialRequestResultToError(credentialRequestResultToError);
                        SignInLogUtils.reportCredentialRetrievalSessionEnded((Context)this, SignInLogging$CredentialService.GooglePlayService, IClientLogging$CompletionReason.failed, (Error)credentialRequestResultToError);
                        SignInLogUtils.reportSignInRequestSessionEnded((Context)this, SignInLogging$SignInType.smartLock, IClientLogging$CompletionReason.failed, (Error)credentialRequestResultToError);
                        this.handleUserNotSignedInWithoutCredentials(this.getServiceManager());
                    }
                    return;
                }
                catch (IntentSender$SendIntentException ex) {
                    Log.e("LaunchActivity", "Google Play Services: STATUS: Failed to send resolution.", (Throwable)ex);
                    n = 1;
                    continue;
                }
            }
            Log.e("LaunchActivity", "Google Play Services: STATUS: FAIL");
            this.showDebugToast("Google Play Services: Could Not Resolve Error");
            n = 1;
            continue;
        }
    }
    
    private boolean shouldDisplayOfflineContent(final ServiceManager serviceManager) {
        if (ConnectivityUtils.isConnectedOrConnecting((Context)this)) {
            Log.d("LaunchActivity", "Network connectivity exist, go to LOLOMO");
            return false;
        }
        if (!serviceManager.isOfflineFeatureAvailable()) {
            Log.d("LaunchActivity", "Offline feature not available!");
            return false;
        }
        if (serviceManager.getOfflineAgent() != null && serviceManager.getOfflineAgent().getLatestOfflinePlayableList() != null && serviceManager.getOfflineAgent().getLatestOfflinePlayableList().size() > 0) {
            Log.d("LaunchActivity", "Network connectivity do NOT exist, we have %d offline titles available, load Offline UI...", serviceManager.getOfflineAgent().getLatestOfflinePlayableList().size());
            return true;
        }
        Log.d("LaunchActivity", "Network connectivity do NOT exist, we do NOT have any offline titles available, load LOLOMO...");
        return false;
    }
    
    private boolean shouldProfileGateBeShown(final ServiceManager serviceManager) {
        final boolean b = false;
        final boolean b2 = false;
        boolean b3;
        if (serviceManager == null) {
            Log.e("LaunchActivity", "shouldProfileGateBeShown was called with null manager");
            b3 = b2;
        }
        else {
            int n;
            if (serviceManager.getAllProfiles().size() == 1 && !(this instanceof RelaunchActivity)) {
                n = 1;
            }
            else {
                n = 0;
            }
            b3 = b2;
            if (!this.shouldDisplayOfflineContent(serviceManager)) {
                b3 = b2;
                if (n != 0) {
                    final int intPref = PreferenceUtils.getIntPref((Context)this, "user_saw_profile_gate", 0);
                    boolean b4 = b;
                    if (intPref < 2) {
                        b4 = true;
                    }
                    b3 = b4;
                    if (b4) {
                        PreferenceUtils.putIntPref((Context)this, "user_saw_profile_gate", intPref + 1);
                        return b4;
                    }
                }
            }
        }
        return b3;
    }
    
    private void showProfileGate(final ServiceManager serviceManager) {
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging();
        final Display display = ConsolidatedLoggingUtils.getDisplay((Context)this);
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiStartupSession(ApplicationPerformanceMetricsLogging$UiStartupTrigger.touchGesture, IClientLogging$ModalView.profilesGate, this.mStarted, display, null, UIBrowseStartupSessionCustomData.create((Context)this));
            applicationPerformanceMetricsLogging.startUiBrowseStartupSession(this.mStarted);
        }
        this.startNextActivity(ProfileSelectionActivity.createStartIntentForAppRestart((Context)this));
        this.finish();
    }
    
    private void showStartPageForSignedInUser(final ServiceManager serviceManager) {
        final ApplicationPerformanceMetricsLogging applicationPerformanceMetricsLogging = serviceManager.getClientLogging().getApplicationPerformanceMetricsLogging();
        final Display display = ConsolidatedLoggingUtils.getDisplay((Context)this);
        if (this.shouldDisplayOfflineContent(serviceManager)) {
            Log.d("LaunchActivity", "Redirect to offline activity with profile %s, %s", serviceManager.getCurrentProfile().getProfileName(), serviceManager.getCurrentProfile().getProfileGuid());
            this.startNextActivity(OfflineActivity.showAllDownloads((Activity)this).putExtra("com.netflix.mediaclient._TRANSITION_ANIMATION", false));
        }
        else {
            Log.d("LaunchActivity", "Redirect to home with profile %s, %s", serviceManager.getCurrentProfile().getProfileName(), serviceManager.getCurrentProfile().getProfileGuid());
            this.startNextActivity(HomeActivity.createStartIntent((NetflixActivity)this).putExtra("com.netflix.mediaclient._TRANSITION_ANIMATION", false));
        }
        if (EogUtils.shouldShowEogAlert(serviceManager)) {
            this.startNextActivity(EndOfGrandfatheringActivity.createStartIntent((NetflixActivity)this, EndOfGrandfatheringActivity.shouldBlockUser(serviceManager.getEndOfGrandfatheringAlert().isBlocking())));
        }
        if (this.shouldCreateUiSessions()) {
            applicationPerformanceMetricsLogging.startUiStartupSession(ApplicationPerformanceMetricsLogging$UiStartupTrigger.touchGesture, IClientLogging$ModalView.homeScreen, this.mStarted, display, null, UIBrowseStartupSessionCustomData.create((Context)this));
            applicationPerformanceMetricsLogging.startUiBrowseStartupSession(this.mStarted);
        }
        this.finish();
    }
    
    private void startNextActivity(final Intent intent) {
        this.startActivity(intent);
        this.overridePendingTransition(0, 0);
    }
    
    private void unregisterNflxReceiver() {
        Log.d("LaunchActivity", "Unregistering Nflx receiver");
        IntentUtils.unregisterSafelyLocalBroadcastReceiver((Context)this, this.nflxBroadcastReceiver);
    }
    
    @Override
    protected boolean allowTransitionAnimation() {
        return false;
    }
    
    @Override
    protected ManagerStatusListener createManagerStatusListener() {
        PerformanceProfiler.getInstance().startSession(Sessions.LAUNCH_ACTIVITY_MANAGER_LOAD);
        return (ManagerStatusListener)new LaunchActivity$1(this);
    }
    
    @Override
    public void finish() {
        PerformanceProfiler.getInstance().endSession(Sessions.LAUNCH_ACTIVITY_LIFE);
        super.finish();
    }
    
    @Override
    public IClientLogging$ModalView getUiScreen() {
        return IClientLogging$ModalView.appLoading;
    }
    
    @Override
    protected void handleAccountDeactivated() {
        Log.i("LaunchActivity", "Account deactivated ...");
    }
    
    @Override
    protected void handleProfileReadyToSelect() {
        Log.i("LaunchActivity", "New profile requested - starting profile selection activity...");
        this.startActivity(ProfileSelectionActivity.createStartIntent((Context)this));
        AccountActivity.finishAllAccountActivities((Context)this);
    }
    
    protected boolean isAutoLoginEnabled() {
        return LoginUtils.shouldUseAutoLogin((Context)this);
    }
    
    @Override
    public boolean isLoadingData() {
        return this.isLoading;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "onActivityResult:" + n + ":" + n2 + ":" + intent);
        }
        if (n != 2) {
            Log.e("LaunchActivity", "onActivityResult: uknown request code" + n);
            final Error credentialRequestResultToError = SignInLogUtils.credentialRequestResultToError(n);
            SignInLogUtils.reportCredentialRetrievalSessionEnded((Context)this, SignInLogging$CredentialService.GooglePlayService, IClientLogging$CompletionReason.failed, credentialRequestResultToError);
            SignInLogUtils.reportSignInRequestSessionEnded((Context)this, SignInLogging$SignInType.smartLock, IClientLogging$CompletionReason.failed, credentialRequestResultToError);
            this.handleUserNotSignedInWithoutCredentials(this.getServiceManager());
            return;
        }
        if (n2 == -1) {
            Log.d("LaunchActivity", "onActivityResult: conflict resolved");
            this.onCredentialRetrieved((Credential)intent.getParcelableExtra("com.google.android.gms.credentials.Credential"));
            return;
        }
        Log.e("LaunchActivity", "Credential Read: NOT OK");
        this.showDebugToast("Google Play Services: Credential Read Failed");
        final Error credentialRequestResultToError2 = SignInLogUtils.credentialRequestResultToError(n2);
        SignInLogUtils.reportCredentialRetrievalSessionEnded((Context)this, SignInLogging$CredentialService.GooglePlayService, IClientLogging$CompletionReason.failed, credentialRequestResultToError2);
        SignInLogUtils.reportSignInRequestSessionEnded((Context)this, SignInLogging$SignInType.smartLock, IClientLogging$CompletionReason.failed, credentialRequestResultToError2);
        this.handleUserNotSignedInWithoutCredentials(this.getServiceManager());
    }
    
    @Override
    public void onConnected(final Bundle bundle) {
        Log.d("LaunchActivity", "onConnected, retrieve credentials if any");
        Auth.CredentialsApi.request(this.mCredentialsApiClient, new CredentialRequest$Builder().setSupportsPasswordLogin(true).build()).setResultCallback((ResultCallback<? super CredentialRequestResult>)new LaunchActivity$4(this));
    }
    
    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "onConnectionFailed:" + connectionResult);
        }
        this.handleUserNotSignedInWithoutCredentials(this.getServiceManager());
    }
    
    @Override
    public void onConnectionSuspended(final int n) {
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", "onConnectionSuspended:" + n);
        }
        if (this.mCredentialsApiClient != null) {
            this.mCredentialsApiClient.reconnect();
        }
    }
    
    @Override
    protected void onCreate(final Bundle bundle) {
        this.mStarted = System.currentTimeMillis();
        super.onCreate(bundle);
        if (bundle == null) {
            if (this.shouldStartPerformanceLogging()) {
                PerformanceProfiler.getInstance().clear();
                PerformanceProfiler.getInstance().startSession(Sessions.TTI);
                PerformanceProfiler.getInstance().startSession(Sessions.TTR);
                PerformanceProfiler.getInstance().startSession(Sessions.NON_MEMBER_TTI);
            }
            PerformanceProfiler.getInstance().startSession(Sessions.LAUNCH_ACTIVITY_LIFE);
        }
        if (Log.isLoggable()) {
            Log.d("LaunchActivity", this.getIntent());
            Log.d("LaunchActivity", "Time: " + System.nanoTime());
        }
        this.registerNflxReceiver();
        if (this.getNetflixApplication().isReady()) {
            Log.d("LaunchActivity", "Service is ready, just use loading view...");
            this.setContentView((View)new LoadingView((Context)this));
            return;
        }
        Log.d("LaunchActivity", "Service is NOT ready, use splash screen... nf_config: splashscreen");
        this.mSplashScreenStarted = System.currentTimeMillis();
        this.setContentView(2130903308);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterNflxReceiver();
        if (this.mCredentialsApiClient != null) {
            this.mCredentialsApiClient.disconnect();
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        this.mIsErrorDialogVisible = false;
    }
    
    @Override
    protected void onPostCreate(final Bundle bundle) {
        super.onPostCreate(bundle);
        final ActionBar supportActionBar = this.getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.hide();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (this.mManagerStatus != null && this.mManagerStatus.isError() && !this.mIsErrorDialogVisible) {
            this.mIsErrorDialogVisible = ServiceErrorsHandler.handleManagerResponse(this, this.mManagerStatus);
        }
    }
    
    protected boolean shouldCreateUiSessions() {
        return true;
    }
    
    @Override
    protected boolean shouldFinishOnManagerError() {
        return false;
    }
    
    protected boolean shouldStartPerformanceLogging() {
        return true;
    }
    
    @Override
    protected boolean showMdxInMenu() {
        return false;
    }
}
