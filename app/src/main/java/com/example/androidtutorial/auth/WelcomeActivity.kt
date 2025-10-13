package com.example.androidtutorial.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.R
import com.example.androidtutorial.common.Constants
import com.example.androidtutorial.mainui.MainActivity

class WelcomeActivity : AppCompatActivity() {

    private val TAG = "ANDROID_TUTORIAL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        Log.d(TAG, "onCreate: WelcomeActivity được TẠO.")

        val tvWelcomeMessage = findViewById<TextView>(R.id.tvWelcomeMessage)
        val username = intent.getStringExtra(Constants.EXTRA_USERNAME)
        val btnBackLogin = findViewById<Button>(R.id.btnBacklogin)
        val edtResultMessage = findViewById<EditText>(R.id.edtResultMessage)
        val btnLaunchMode = findViewById<Button>(R.id.btnLaunchMode)

        if (username != null && username.isNotEmpty()) {
            tvWelcomeMessage.text = "Xin Chao, ${username.uppercase()}!"
        } else {
            tvWelcomeMessage.text = "Xin Chào Bạn!"
        }

        btnBackLogin.setOnClickListener {
            val messageToReturn = edtResultMessage.text.toString().trim()
            val resultIntent = Intent()
            resultIntent.putExtra(Constants.EXTRA_RETURN_MESSAGE, messageToReturn)
            setResult(RESULT_OK, resultIntent)
            finish()
//            val loginIntent = Intent(this, LoginActivity::class.java)
//            startActivity(loginIntent)
        }
        btnLaunchMode.setOnClickListener {
            val launchModeIntent = Intent(this, MainActivity::class.java)
            startActivity(launchModeIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: WelcomeActivity SẮP HIỂN THỊ, chưa tương tác.")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: WelcomeActivity ĐANG TƯƠNG TÁC")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: WelcomeActivity SẮP BỊ CHE KHUẤT")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: WelcomeActivity KHÔNG CÒN HIỂN THỊ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: WelcomeActivity ĐƯỢC KHỞI ĐỘNG LẠI")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: WelcomeActivity BỊ HỦY HOÀN TOÀN")
    }
}