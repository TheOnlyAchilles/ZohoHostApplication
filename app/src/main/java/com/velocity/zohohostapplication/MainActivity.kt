package com.velocity.zohohostapplication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.velocity.velocity_zoho.velocity_zoho_chat
import java.util.Map


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.myButton)
        val appKey =  BuildConfig.APP_KEY
        val accessKey = BuildConfig.ACCESS_KEY
        val languageCode = "nl"
        val countryCode = "nl"
        val testMode = false
        val title = "ICAS Hub"
        val additionalInfo = Map.of(
            "Company Name", "Acme Corp",
            "Page", "Services",
            "Primary Need", "Customer Support",
            "Potential Risk", "No"
        )


        velocity_zoho_chat(appKey, accessKey, application)
        myButton.setOnClickListener {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()
            velocity_zoho_chat().startChat(additionalInfo, title, this)
            velocity_zoho_chat().openChat(countryCode, languageCode, applicationContext)
        }
    }
}