package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.databinding.ActivityUnlockFeatureBinding

class UnlockFeature : AppCompatActivity() {

    private lateinit var binding: ActivityUnlockFeatureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnlockFeatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoadingSate()
    }

    private fun showLoadingSate() {
        binding.txtTrialFree.visibility = View.INVISIBLE
        binding.btnTryFree.visibility = View.INVISIBLE

        binding.txtTrialFreePay.visibility = View.INVISIBLE
        binding.btnContinue.visibility = View.INVISIBLE

        binding.btnLoadingContinue.visibility = View.VISIBLE


        binding.btnLoadingContinue.setOnClickListener {
            showTrailFreeStae()
        }
    }

    private fun showTrailFreeStae(){
        binding.txtTrialFree.visibility = View.INVISIBLE
        binding.btnTryFree.visibility = View.INVISIBLE
        binding.btnLoadingContinue.visibility = View.INVISIBLE

        binding.txtTrialFreePay.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.VISIBLE

        binding.btnContinue.setOnClickListener{
            showPayState()
        }
    }
    private fun showPayState(){
        binding.txtTrialFree.visibility = View.VISIBLE
        binding.btnTryFree.visibility = View.VISIBLE

        binding.txtTrialFreePay.visibility = View.INVISIBLE
        binding.btnContinue.visibility = View.INVISIBLE
        binding.btnLoadingContinue.visibility = View.INVISIBLE
    }
}
