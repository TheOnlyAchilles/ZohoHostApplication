package com.velocity.zohohostapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.velocity.velocity_zoho.velocity_zoho_chat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appKey = "{{appKey}}"
        val accessKey = "{{accessKey}}"
        velocity_zoho_chat().initZoho(application, appKey, accessKey)
    }
}