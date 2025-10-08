package com.example.androidtutorial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private val TAG = "ANDROID_TUTOLRIAL"
    private val VALID_USERNAME = "hungne"
    private val VALID_PASSWORD = "123456"

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }

    //check vòng đời với log
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG, "onCreate: LoginActivity được TẠO.")

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvError = findViewById<TextView>(R.id.tvError)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username == VALID_USERNAME && password == VALID_PASSWORD) {
                tvError.text = ""
                val welcomeIntent = Intent(this, WelcomeActivity::class.java)
                welcomeIntent.putExtra(EXTRA_USERNAME, username)
                startActivity(welcomeIntent)
            } else {
                tvError.text = "Tên đăng nhập hoặc mật khẩu không đúng!"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: LoginActivity SẮP HIỂN THỊ, chưa tương tác")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: LoginActivity ĐANG TƯƠNG TÁC")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: LoginActivity SẮP BỊ CHE KHUẤT")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: LoginActivity KHÔNG CÒN HIỂN THỊ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: LoginActivity ĐƯỢC KHỞI ĐỘNG LẠI")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: LoginActivity BỊ HỦY HOÀN TOÀN")
    }
}