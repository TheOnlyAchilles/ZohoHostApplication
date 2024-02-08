package com.velocity.velocity_zoho;

import android.app.Application;
import android.util.Log;
import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;

public class velocity_zoho_chat extends Application {

    public void initZoho(Application application, String appKey, String accessKey){
        try {
            InitConfig initConfig = new InitConfig();
            ZohoSalesIQ.init(application, appKey, accessKey, initConfig, new InitListener() {
                @Override
                public void onInitSuccess() {
                    Log.d("LOGS", "SUCCESS");
                    ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS);
                }

                @Override
                public void onInitError(int errorCode, String errorMessage) {
                    Log.d("LOGS", "ERROR: " + errorMessage);
                }

            });
        } catch (Exception e) {
            Log.d("LOGS", "CATCH: " + e.getMessage());
        }
    }

}