package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.databinding.ActivityPaywallDefaultBinding

class PayWallActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaywallDefaultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaywallDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInitialState()

        setClickListeners()

    }

    private fun setClickListeners() {
        binding.txtTryAgain.setOnClickListener {
            showLoadingState()

            Handler(Looper.getMainLooper()).postDelayed({
                showOfferState()
            }, 3000)
        }

        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnClaimOffer.setOnClickListener {
        }
    }

    private fun setupInitialState() {
        binding.viewSubContainer.visibility = View.INVISIBLE
        binding.btnLoading.visibility = View.INVISIBLE
        binding.txtPaymentDetails.visibility = View.INVISIBLE
        binding.btnClaimOffer.visibility = View.INVISIBLE
        binding.txtNewPrice.visibility = View.GONE
        binding.txtOFF50.visibility = View.GONE
        binding.txtWeek.visibility = View.GONE
        binding.txtPrice899.visibility = View.INVISIBLE
        binding.headerPrice.visibility = View.INVISIBLE

        binding.txtNotFound.visibility = View.VISIBLE
        binding.txtTryAgain.visibility = View.VISIBLE
    }

    private fun showLoadingState() {
        binding.txtNotFound.visibility = View.INVISIBLE
        binding.txtTryAgain.visibility = View.INVISIBLE
        binding.btnClaimOffer.visibility = View.INVISIBLE

        binding.viewSubContainer.visibility = View.VISIBLE
        binding.btnLoading.visibility = View.VISIBLE

    }

    private fun showOfferState() {
        binding.viewSubContainer.visibility = View.INVISIBLE
        binding.btnLoading.visibility = View.INVISIBLE

        binding.txtPaymentDetails.visibility = View.VISIBLE
        binding.btnClaimOffer.visibility = View.VISIBLE
        binding.txtNewPrice.visibility = View.VISIBLE
        binding.txtOFF50.visibility = View.VISIBLE
        binding.txtWeek.visibility = View.VISIBLE
        binding.txtPrice899.visibility = View.VISIBLE

        binding.headerPrice.visibility = View.VISIBLE
    }
}