package com.example.androidtutorial.launchmode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.mainui.setupActivity

class StandardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this, "tạo bơi Standard")
    }
}