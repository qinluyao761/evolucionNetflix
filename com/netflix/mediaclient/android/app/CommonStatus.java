// 
// Decompiled by Procyon v0.5.30
// 

package com.netflix.mediaclient.android.app;

import com.netflix.mediaclient.StatusCode;

public interface CommonStatus
{
    public static final NetflixImmutableStatus ALREADY_IN_QUEUE = new NetflixImmutableStatus(StatusCode.ALREADY_IN_QUEUE);
    public static final NetflixImmutableStatus BLADERUNNER_FAILURE = new NetflixImmutableStatus(StatusCode.BLADERUNNER_FAILURE);
    public static final NetflixImmutableStatus CONFIG_DOWNLOAD_FAILED = new NetflixImmutableStatus(StatusCode.CONFIG_DOWNLOAD_FAILED);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM_EXCEPTION = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM_EXCEPTION);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM_KEY_SET_EMPTY = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM_KEY_SET_EMPTY);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM_LICENSE_EMPTY = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM_LICENSE_EMPTY);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM_NOT_PROVISIONED = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM_NOT_PROVISIONED);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM_RESOURCE_BUSY = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM_RESOURCE_BUSY);
    public static final NetflixImmutableStatus DRM_FAILURE_CDM_SERVER_DENIED = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_CDM_SERVER_DENIED);
    public static final NetflixImmutableStatus DRM_FAILURE_GOOGLE_CDM_PROVISIONG_DENIED = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_GOOGLE_CDM_PROVISIONG_DENIED);
    public static final NetflixImmutableStatus DRM_FAILURE_GOOGLE_DECLINED_PROVISIONING = new NetflixImmutableStatus(StatusCode.DRM_FAILURE_GOOGLE_DECLINED_PROVISIONING);
    public static final NetflixImmutableStatus FATAL_CONFIG_DOWNLOAD_FAILED = new NetflixImmutableStatus(StatusCode.FATAL_CONFIG_DOWNLOAD_FAILED);
    public static final NetflixImmutableStatus FORCED_TESTING_ERROR = new NetflixImmutableStatus(StatusCode.FORCED_TESTING_ERROR);
    public static final NetflixImmutableStatus HTTP_SSL_DATE_TIME_ERROR = new NetflixImmutableStatus(StatusCode.HTTP_SSL_DATE_TIME_ERROR);
    public static final NetflixImmutableStatus HTTP_SSL_ERROR = new NetflixImmutableStatus(StatusCode.HTTP_SSL_ERROR);
    public static final NetflixImmutableStatus HTTP_SSL_NO_VALID_CERT = new NetflixImmutableStatus(StatusCode.HTTP_SSL_NO_VALID_CERT);
    public static final NetflixImmutableStatus INIT_SERVICE_TIMEOUT = new NetflixImmutableStatus(StatusCode.INIT_SERVICE_TIMEOUT);
    public static final NetflixImmutableStatus INTERNAL_ERROR = new NetflixImmutableStatus(StatusCode.INTERNAL_ERROR);
    public static final NetflixImmutableStatus INT_ERR_ADVIS_VIDEO_ID_NULL = new NetflixImmutableStatus(StatusCode.INT_ERR_ADVIS_VIDEO_ID_NULL);
    public static final NetflixImmutableStatus INT_ERR_BAD_COOKIES = new NetflixImmutableStatus(StatusCode.INT_ERR_BAD_COOKIES);
    public static final NetflixImmutableStatus INT_ERR_CB_NULL = new NetflixImmutableStatus(StatusCode.INT_ERR_CB_NULL);
    public static final NetflixImmutableStatus INT_ERR_CMP_RESP_NULL = new NetflixImmutableStatus(StatusCode.INT_ERR_CMP_RESP_NULL);
    public static final NetflixImmutableStatus INT_ERR_MDX_INIT = new NetflixImmutableStatus(StatusCode.INT_ERR_MDX_INIT);
    public static final NetflixImmutableStatus INT_ERR_MNGR_NOT_READY = new NetflixImmutableStatus(StatusCode.INT_ERR_MNGR_NOT_READY);
    public static final NetflixImmutableStatus INT_ERR_REALM_DETAILS_NULL = new NetflixImmutableStatus(StatusCode.INT_ERR_REALM_DETAILS_NULL);
    public static final NetflixImmutableStatus INT_ERR_RESOURCE_URL_NULL = new NetflixImmutableStatus(StatusCode.INT_ERR_RESOURCE_URL_NULL);
    public static final NetflixImmutableStatus MSL_FAILED_TO_CREATE_CLIENT = new NetflixImmutableStatus(StatusCode.MSL_FAILED_TO_CREATE_CLIENT);
    public static final NetflixImmutableStatus MSL_LEGACY_CRYPTO = new NetflixImmutableStatus(StatusCode.MSL_LEGACY_CRYPTO);
    public static final NetflixImmutableStatus MSL_LEGACY_CRYPTO_BUT_USED_WIDEVINE_BEFORE = new NetflixImmutableStatus(StatusCode.MSL_LEGACY_CRYPTO_BUT_USED_WIDEVINE_BEFORE);
    public static final NetflixImmutableStatus NON_RECOMMENDED_APP_VERSION = new NetflixImmutableStatus(StatusCode.NON_RECOMMENDED_APP_VERSION);
    public static final NetflixImmutableStatus NON_SUPPORTED_LOCALE = new NetflixImmutableStatus(StatusCode.NON_SUPPORTED_LOCALE);
    public static final NetflixImmutableStatus NOT_IMPLEMENTED = new NetflixImmutableStatus(StatusCode.NOT_IMPLEMENTED);
    public static final NetflixImmutableStatus NOT_IN_QUEUE = new NetflixImmutableStatus(StatusCode.NOT_IN_QUEUE);
    public static final NetflixImmutableStatus NOT_VALID = new NetflixImmutableStatus(StatusCode.NOT_VALID);
    public static final NetflixImmutableStatus NO_CONNECTIVITY = new NetflixImmutableStatus(StatusCode.NO_CONNECTIVITY);
    public static final NetflixImmutableStatus NRD_ERROR = new NetflixImmutableStatus(StatusCode.NRD_ERROR);
    public static final NetflixImmutableStatus OBSOLETE_APP_VERSION = new NetflixImmutableStatus(StatusCode.OBSOLETE_APP_VERSION);
    public static final NetflixImmutableStatus OK = new NetflixImmutableStatus(StatusCode.OK);
    public static final NetflixImmutableStatus SERVICE_DISCONNECTED = new NetflixImmutableStatus(StatusCode.INT_ERR_SERVICE_DISCONNECTED);
    public static final NetflixImmutableStatus SIGNIN_FAILURE = new NetflixImmutableStatus(StatusCode.USER_SIGNIN_FAILURE);
    public static final NetflixImmutableStatus UNKNOWN = new NetflixImmutableStatus(StatusCode.UNKNOWN);
    public static final NetflixImmutableStatus VOIP_CONFIG_DOWNLOAD_FAILED = new NetflixImmutableStatus(StatusCode.VOIP_CONFIG_DOWNLOAD_FAILED);
}
