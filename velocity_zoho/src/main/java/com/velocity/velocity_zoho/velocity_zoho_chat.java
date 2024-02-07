package com.velocity.velocity_zoho;

import android.app.Application;
import android.util.Log;

import com.zoho.commons.InitConfig;
import com.zoho.livechat.android.listeners.InitListener;
import com.zoho.salesiqembed.ZohoSalesIQ;
import com.zoho.commons.Fonts;

public class velocity_zoho_chat extends Application {
    public String message = "Hello, world! MotherF*****";
    private final Application application;

    public velocity_zoho_chat(Application application) {
        this.application = application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            // Initialize Zoho SalesIQ on the UI thread
            InitConfig initConfig = new InitConfig();
            initConfig.setFont(Fonts.REGULAR, ""); // Specify your font here

            String accessKey = "LbO1ugc6oWJuhb2lMBuvgCbX8ePG4Vah1oUY2SbehdSV7ijMF6zKKqyBa90tFrc8eLCb6%2F6tylPKHmQ9qwrgNYjBx4SjLDClQ2kmhRKuTsxF5taRliRJYyGz2avGobCKBASPR7MbSJT3JJSvtk9%2B6RPCANC9LnQa";
            String appKey = "0YTMmRaFQkdKEEb6NxhloyN7WcoYThP9naVmXju0PF%2FC1sCwmNlvVw%3D%3D_eu";
            ZohoSalesIQ.init(application, appKey, accessKey, initConfig, new InitListener() {
                @Override
                public void onInitSuccess() {
                    // Show the Zoho SalesIQ launcher
                    Log.d("Hi Mom", "Yo");

                    ZohoSalesIQ.Launcher.show(ZohoSalesIQ.Launcher.VisibilityMode.ALWAYS);


                }

                @Override
                public void onInitError(int errorCode, String errorMessage) {
                    // Handle initialization error (your code)
                    Log.d("Hi Mom", "No No");

                }

            });
            Log.d("Hi Mom", "Yo Yo Yo");
        } catch (Exception e) {
            Log.d("Hi Mom", "Error during Zoho SalesIQ initialization: " + e.getMessage());
        }
    }

    public String getHelloWorld() {
        return message;
    }
}