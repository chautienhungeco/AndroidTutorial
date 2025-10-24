package com.eco.musicplayer.audioplayer.music.launchmode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.mainui.setupActivity

class SingleTopActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this, "tạo bởi SingleTop")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        setupActivity(this, "tạo bởi SingleTop - dùng lại")
        Log.d("LAUNCH_MODE", "SingleTop đang dùng pt: onNewIntent")
        Toast.makeText(this, "tái sử dụng Instance!", Toast.LENGTH_SHORT).show()
    }
}