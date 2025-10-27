package com.eco.musicplayer.audioplayer.music.present

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.auth.LoginActivity

class HomeEarthMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_map)

        val btnGoToLogin = findViewById<Button>(R.id.btnGoToLogin)
        val imgSetting = findViewById<ImageView>(R.id.btnSetting)
//
        btnGoToLogin.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        imgSetting.setOnClickListener {
            val settingIntent = Intent(this, SettingActivity::class.java)
            startActivity(settingIntent)
        }
    }
}