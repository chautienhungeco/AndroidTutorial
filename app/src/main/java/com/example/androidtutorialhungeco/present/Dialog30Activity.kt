package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.R
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.AppCompatImageView

class Dialog30Activity : AppCompatActivity() {

    private lateinit var txtNotFound30: AppCompatTextView
    private lateinit var txtTryAgain30: AppCompatTextView
    private lateinit var btnClaimLoading: AppCompatButton
    private lateinit var btnClaimOffer: AppCompatButton
    private lateinit var txtTrialOffer: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sale_weekly)

        txtNotFound30 = findViewById(R.id.txtNotFound30)
        txtTryAgain30 = findViewById(R.id.txtTryAgain30)
        btnClaimLoading = findViewById(R.id.btnClaimLoading)
        btnClaimOffer = findViewById(R.id.btnClaimOffer)
        txtTrialOffer = findViewById(R.id.txtTrialOffer)

        btnClaimOffer.visibility = View.INVISIBLE
        txtTrialOffer.visibility = View.GONE

        txtNotFound30.visibility = View.VISIBLE
        txtTryAgain30.visibility = View.VISIBLE
        btnClaimLoading.visibility = View.GONE


        txtTryAgain30.setOnClickListener {
            txtNotFound30.visibility = View.GONE
            txtTryAgain30.visibility = View.GONE

            btnClaimLoading.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                btnClaimLoading.visibility = View.GONE

                btnClaimOffer.visibility = View.VISIBLE
                txtTrialOffer.visibility = View.VISIBLE

            }, 3000)
        }
    }
}