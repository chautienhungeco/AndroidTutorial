package com.example.androidtutorial.launchmode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class StandardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupActivity(this, "tạo bơi Standard")
    }
}