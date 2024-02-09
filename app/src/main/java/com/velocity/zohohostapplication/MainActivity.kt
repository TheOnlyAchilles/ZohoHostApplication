package com.velocity.zohohostapplication

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.velocity.velocity_zoho.velocity_zoho_chat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton: Button = findViewById(R.id.myButton)
        myButton.setOnClickListener {
            Toast.makeText(this, "Button clicked!", Toast.LENGTH_SHORT).show()
            velocity_zoho_chat().openChat();
        }

        val appKey =  BuildConfig.APP_KEY
        val accessKey = BuildConfig.ACCESS_KEY
        val languageCode = "en"
        val countryCode = "za"
        val testMode = false
        velocity_zoho_chat(appKey, accessKey,application)

    }
}