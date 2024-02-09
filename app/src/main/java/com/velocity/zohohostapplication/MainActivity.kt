package com.velocity.zohohostapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.velocity.velocity_zoho.velocity_zoho_chat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appKey =  BuildConfig.APP_KEY
        val accessKey = BuildConfig.ACCESS_KEY
        val languageCode = "en"
        val countryCode = "za"
        val testMode = false
        velocity_zoho_chat(appKey, accessKey,languageCode,countryCode,application,testMode)
    }
}