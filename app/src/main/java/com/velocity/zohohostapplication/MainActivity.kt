package com.velocity.zohohostapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.velocity.velocity_zoho.velocity_zoho_chat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myButton: Button = findViewById(R.id.myButton)
        val velocityZohoChat = velocity_zoho_chat(this.application)

        val oncreate = velocityZohoChat.onCreate()
        Log.d("Hi Mom: ", oncreate.toString())

        myButton.setOnClickListener {
            Toast.makeText(
                applicationContext,
                velocityZohoChat.getHelloWorld(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}