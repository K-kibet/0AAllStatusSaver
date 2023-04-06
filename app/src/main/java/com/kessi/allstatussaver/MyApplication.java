package com.kessi.allstatussaver;

import static com.onesignal.OneSignal.*;

import android.app.Application;

import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kessi.allstatussaver.utils.AdController;
import com.kessi.allstatussaver.utils.AppOpenManager;
import com.onesignal.OneSignal;

public class MyApplication extends Application {
    AppOpenManager appOpenManager;
    //    private static final String ONESIGNAL_APP_ID = "809d778f-ded6-4ac5-b9f1-a02ad943feee";
    private static final String ONESIGNAL_APP_ID = "7a630d6c-9c0a-4d49-9010-c5ddc3ef7edc";
    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        //StartAppSDK.init(this, getResources().getString(R.string.startappid), false);
        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        MobileAds.initialize(
                this,
                new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
        AppLovinSdk.getInstance(this).setMediationProvider("max");
        AppLovinSdk.initializeSdk(this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(AppLovinSdkConfiguration config) {
                //Toast.makeText(app.this, "Welcome", Toast.LENGTH_SHORT).show();

            }
        });

        AdController.initAd(this);
        appOpenManager = new AppOpenManager(this);
    }

}
