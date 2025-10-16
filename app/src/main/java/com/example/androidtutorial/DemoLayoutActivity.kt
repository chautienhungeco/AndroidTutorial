package com.example.androidtutorial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.auth.LoginActivity

class DemoLayoutActivity : AppCompatActivity() {
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
            val settingIntent = Intent(this, DemoSettingActivity::class.java)
            startActivity(settingIntent)
        }
    }
}