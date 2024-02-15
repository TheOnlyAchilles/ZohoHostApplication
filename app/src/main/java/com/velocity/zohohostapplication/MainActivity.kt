package com.velocity.zohohostapplication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.velocity.velocity_zoho.VelocityZohoChat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.myButton)
        val appKey =  BuildConfig.APP_KEY
        val accessKey = BuildConfig.ACCESS_KEY
        val languageCode = "nl"
        val countryCode = "nl"
        val testMode = true
        val environment = "staging"
        val title = "ICAS Hub"
        val companyCode = "Meyer&Muller"
        val serviceName = null


        VelocityZohoChat(
            appKey,
            accessKey,
            application
        )
        VelocityZohoChat().showZohoLauncher()
        myButton.setOnClickListener {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()
            VelocityZohoChat()
                .startChat(companyCode, serviceName, title, this)
            VelocityZohoChat()
                .openChat(languageCode, countryCode, testMode, environment, applicationContext)
        }
    }
}