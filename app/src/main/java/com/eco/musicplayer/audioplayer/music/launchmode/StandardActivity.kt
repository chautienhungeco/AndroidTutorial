package com.eco.musicplayer.audioplayer.music.launchmode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.mainui.setupActivity

class StandardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this, "tạo bơi Standard")
    }
}