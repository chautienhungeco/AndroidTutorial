package com.example.androidtutorial.launchmode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.mainui.setupActivity

class SingleInstanceActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this, "tạo bởi SingleInstance")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        setupActivity(this, "Tạo bởi SingleInstance - tài sử dụng")
        Log.d("LAUNCH_MODE","SingleInstance đang sử dụng pt: onNewIntent")
        Toast.makeText(this,"Tái sử dụng Instance", Toast.LENGTH_SHORT).show()
    }
}