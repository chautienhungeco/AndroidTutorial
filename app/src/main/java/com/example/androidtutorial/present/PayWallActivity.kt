package com.example.androidtutorial.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorial.R
import com.example.androidtutorial.databinding.ActivityPaywallDefaultBinding

class PayWallActivity: AppCompatActivity(){

    private lateinit var binding: ActivityPaywallDefaultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Ánh xạ layout bằng View Binding
        binding = ActivityPaywallDefaultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupInitialState()

        Handler(Looper.getMainLooper()).postDelayed({
            showLoadingState()
        }, 3000) // delay = 3 giây
    }

    //thiết lập trạng thái ban đầu: not found
    private fun setupInitialState() {
        binding.txtNotFound.visibility = View.VISIBLE
        binding.txtTryAgain.visibility = View.VISIBLE
    }

    //chuyể sang trạng thái loading
    private fun showLoadingState() {
        binding.txtNotFound.visibility = View.GONE
        binding.txtTryAgain.visibility = View.GONE

        binding.viewSubContainer.visibility = View.VISIBLE
        binding.btnLoading.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            showOfferState()
        }, 3000) // delay = 3 giây
    }

    //hiển thị đầy đủ thông tin
    private fun showOfferState() {
        binding.viewSubContainer.visibility = View.GONE
        binding.btnLoading.visibility = View.GONE

        binding.txtPaymentDetails.visibility = View.VISIBLE
        binding.btnClaimOffer.visibility = View.VISIBLE
        binding.txtNewPrice.visibility = View.VISIBLE
        binding.txtOFF50.visibility = View.VISIBLE
        binding.txtWeek.visibility = View.VISIBLE
        binding.txtPrice899.visibility = View.VISIBLE

        binding.headerPrice.visibility = View.VISIBLE
    }
}