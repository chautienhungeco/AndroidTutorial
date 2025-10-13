package com.example.androidtutorial.launchmode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.mainui.setupActivity

class SingleTaskActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this,"Tạo bởi SingleTask")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        setupActivity(this,"tạo bởi SingleTask - tài sử dụng")
        Log.d("LAUNCH_MODE", "SingleTask đang sử dụng pt: onNewIntent")
        Toast.makeText(this,"Tái sử dụng Instance", Toast.LENGTH_SHORT).show()
    }
}