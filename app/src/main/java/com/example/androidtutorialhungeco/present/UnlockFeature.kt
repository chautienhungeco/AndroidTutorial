package com.example.androidtutorialhungeco.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtutorialhungeco.R
import com.example.androidtutorialhungeco.databinding.ActivityUnlockFeatureBinding

class UnlockFeature : AppCompatActivity(){

    private lateinit var binding: ActivityUnlockFeatureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnlockFeatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showTrialFreeState()
    }

    private fun showTrialFreeState() {

        binding.txtTrialFree.visibility = View.VISIBLE
        binding.btnTryFree.visibility = View.VISIBLE

        binding.txtTrialFreePay.visibility = View.GONE
        binding.btnContinue.visibility = View.GONE
        binding.btnLoadingContinue.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
            showLoadingState()
        }, 3000)
    }

    private fun showLoadingState() {
        binding.txtTrialFree.visibility = View.GONE
        binding.btnTryFree.visibility = View.GONE

        binding.txtTrialFreePay.visibility = View.GONE
        binding.btnContinue.visibility = View.GONE

        binding.btnLoadingContinue.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            showPayState()
        }, 3000)
    }

    private fun showPayState() {
        binding.txtTrialFree.visibility = View.GONE
        binding.btnTryFree.visibility = View.GONE
        binding.btnLoadingContinue.visibility = View.GONE

        binding.txtTrialFreePay.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.VISIBLE
    }
}