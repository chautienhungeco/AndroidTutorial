package com.eco.musicplayer.audioplayer.music.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.eco.musicplayer.audioplayer.music.present.HomeEarthMapActivity
import com.eco.musicplayer.audioplayer.music.R
import com.eco.musicplayer.audioplayer.music.common.Constants
import com.eco.musicplayer.audioplayer.music.phonebook.PhoneBookActivity
import com.eco.musicplayer.audioplayer.music.present.Dialog30Activity
import com.eco.musicplayer.audioplayer.music.present.Dialog30ActivityYear
import com.eco.musicplayer.audioplayer.music.present.FocusYearlyPrice
import com.eco.musicplayer.audioplayer.music.present.PayWallActivity
import com.eco.musicplayer.audioplayer.music.present.PaywallOnboard
import com.eco.musicplayer.audioplayer.music.present.UnlockFeature

class LoginActivity : AppCompatActivity() {

    private val TAG = "ANDROID_TUTOLRIAL"
    private val VALID_USERNAME = "hungne"
    private val VALID_PASSWORD = "123456"
    private lateinit var tvMessageOut: TextView

    private val welcomeActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val returnMessage = data?.getStringExtra(Constants.EXTRA_RETURN_MESSAGE)
            if (!returnMessage.isNullOrEmpty()) {
                tvMessageOut.text = "Tin nhắn tử Wellcome \"$returnMessage\""

                Toast.makeText(this, "Đã nhận kết quả!", Toast.LENGTH_SHORT).show()
            } else {
                tvMessageOut.text = "Welcome Đã bị hủy, Không có message trả về"
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            tvMessageOut.text = "Welcome Activity đã bị huủy"
        }
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
        tvMessageOut = findViewById(R.id.tvMessageOutLogin)
        val btnDemoLayout = findViewById<Button>(R.id.btnDemoLayout)
        val btnPhoneBook = findViewById<Button>(R.id.btnPhone)
        val btnPayWall = findViewById<Button>(R.id.btnPaywall)
        val btnDialog30 = findViewById<Button>(R.id.btnDialog30)
        val btnDialog30Year = findViewById<Button>(R.id.btnDialog30Year)
        val btnUnlockFeature = findViewById<Button>(R.id.btnUnlockFeature)
        val btnPaywallOnboard = findViewById<Button>(R.id.btnPaywallOnboard)
        val btnFocusYearlyPrice = findViewById<Button>(R.id.btnFocusYearlyPrice)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username == VALID_USERNAME && password == VALID_PASSWORD) {
                tvError.text = ""
                val welcomeIntent = Intent(this, WelcomeActivity::class.java)
                welcomeIntent.putExtra(Constants.EXTRA_USERNAME, username)
                welcomeActivityResultLauncher.launch(welcomeIntent)
//                startActivity(welcomeIntent)
            } else {
                tvError.text = "Tên đăng nhập hoặc mật khẩu không đúng!"
            }
        }

        btnDemoLayout.setOnClickListener {
            val demoLayoutIntent = Intent(this, HomeEarthMapActivity::class.java)
            startActivity(demoLayoutIntent)
        }

        btnPhoneBook.setOnClickListener {
            val phoneBookIntent = Intent(this, PhoneBookActivity::class.java)
            startActivity(phoneBookIntent)
        }

        btnPayWall.setOnClickListener {
            val payWallIntent = Intent(this, PayWallActivity::class.java)
            startActivity(payWallIntent)
        }

        btnDialog30.setOnClickListener {
            val dialog30Intent = Intent(this, Dialog30Activity::class.java)
            startActivity(dialog30Intent)
        }

        btnDialog30Year.setOnClickListener {
            val dialog30IntentYear = Intent(this, Dialog30ActivityYear::class.java)
            startActivity(dialog30IntentYear)
        }

        btnUnlockFeature.setOnClickListener {
            val unlockFeatureIntent = Intent(this, UnlockFeature::class.java)
            startActivity(unlockFeatureIntent)
        }

        btnPaywallOnboard.setOnClickListener {
            val paywallOnboardIntent = Intent(this, PaywallOnboard::class.java)
            startActivity(paywallOnboardIntent)
        }

        btnFocusYearlyPrice.setOnClickListener {
            val focusYearlyPriceIntent = Intent(this, FocusYearlyPrice::class.java)
            startActivity(focusYearlyPriceIntent)
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