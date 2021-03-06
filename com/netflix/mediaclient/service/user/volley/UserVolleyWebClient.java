// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.service.user.volley;

import com.netflix.mediaclient.ui.verifyplay.PinVerifier$PinType;
import com.netflix.mediaclient.service.webclient.model.leafs.User;
import com.netflix.model.leafs.OnRampEligibility$Action;
import com.netflix.mediaclient.service.webclient.volley.FalkorVolleyWebClientRequest;
import com.netflix.mediaclient.service.user.UserAgentWebCallback;
import com.netflix.mediaclient.service.webclient.volley.FalkorVolleyWebClient;
import com.netflix.mediaclient.service.NetflixService;
import com.netflix.mediaclient.service.user.UserWebClient;

public final class UserVolleyWebClient implements UserWebClient
{
    private final NetflixService service;
    private final FalkorVolleyWebClient webclient;
    
    public UserVolleyWebClient(final NetflixService service, final FalkorVolleyWebClient webclient) {
        this.webclient = webclient;
        this.service = service;
    }
    
    @Override
    public void addWebUserProfile(final String s, final boolean b, final String s2, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new AddUserProfileRequest(this.service.getApplicationContext(), s, b, s2, userAgentWebCallback));
    }
    
    @Override
    public void autoLogin(final String s, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new AutoLoginRequest(this.service.getApplicationContext(), s, userAgentWebCallback));
    }
    
    @Override
    public void createAutoLoginToken(final long n, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new CreateAutoLoginTokenRequest(this.service.getApplicationContext(), n, userAgentWebCallback));
    }
    
    @Override
    public void doDummyWebCall(final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new FetchDummyWebRequest(this.service.getApplicationContext(), userAgentWebCallback));
    }
    
    @Override
    public void doOnRampEligibilityAction(final OnRampEligibility$Action onRampEligibility$Action, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new DoOnRampEligibilityActionRequest(this.service.getApplicationContext(), onRampEligibility$Action, userAgentWebCallback));
    }
    
    @Override
    public void editWebUserProfile(final String s, final String s2, final boolean b, final String s3, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new EditUserProfileRequest(this.service.getApplicationContext(), s, s2, b, s3, userAgentWebCallback));
    }
    
    @Override
    public void fetchAccountData(final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new FetchAccountDataRequest(this.service.getApplicationContext(), userAgentWebCallback));
    }
    
    @Override
    public void fetchAvailableAvatarsList(final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new FetchAvailableAvatarsRequest(this.service.getApplicationContext(), userAgentWebCallback));
    }
    
    @Override
    public void fetchProfileData(final String s, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new FetchProfileDataRequest(this.service.getApplicationContext(), s, userAgentWebCallback));
    }
    
    @Override
    public void fetchSurvey(final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new FetchSurveyRequest(this.service.getApplicationContext(), userAgentWebCallback));
    }
    
    @Override
    public void fetchUserData(final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new FetchUserDataRequest(this.service.getApplicationContext(), userAgentWebCallback));
    }
    
    @Override
    public final boolean isSynchronous() {
        return this.webclient.isSynchronous();
    }
    
    @Override
    public void markSurveysAsRead() {
        this.webclient.sendRequest(new MarkSurveyReadRequest(this.service.getApplicationContext()));
    }
    
    @Override
    public void recordPlanSelection(final String s, final String s2) {
        this.webclient.sendRequest(new RecordPlanSelection(this.service.getApplicationContext(), s, s2));
    }
    
    @Override
    public void recordThumbRatingWelcomeSeen() {
        this.webclient.sendRequest(new RecordThumbRatingWelcomeSeen(this.service.getApplicationContext()));
    }
    
    @Override
    public void recordUmsImpression(final String s, final String s2) {
        this.webclient.sendRequest(new RecordUmsImpression(this.service.getApplicationContext(), s, s2));
    }
    
    @Override
    public void refreshUserMessage(final User user) {
        this.webclient.sendRequest(new RefreshUserMessageRequest(this.service.getApplicationContext(), user));
    }
    
    @Override
    public void removeWebUserProfile(final String s, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new RemoveUserProfileRequest(this.service.getApplicationContext(), s, userAgentWebCallback));
    }
    
    @Override
    public void switchWebUserProfile(final String s, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new SwitchUserProfileRequest(this.service.getApplicationContext(), s, userAgentWebCallback));
    }
    
    @Override
    public void verifyPin(final String s, final PinVerifier$PinType pinVerifier$PinType, final String s2, final UserAgentWebCallback userAgentWebCallback) {
        this.webclient.sendRequest(new VerifyPinRequest(this.service.getApplicationContext(), s, pinVerifier$PinType, s2, userAgentWebCallback));
    }
}
