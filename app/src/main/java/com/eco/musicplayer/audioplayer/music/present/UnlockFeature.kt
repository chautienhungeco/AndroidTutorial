package com.eco.musicplayer.audioplayer.music.present

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.databinding.ActivityUnlockFeatureBinding

class UnlockFeature : AppCompatActivity() {

    private lateinit var binding: ActivityUnlockFeatureBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUnlockFeatureBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoadingSate()
    }

    private fun startLoadingAnimation() {
        try {
            val animatedDrawable = ContextCompat.getDrawable(
                this,
                com.eco.musicplayer.audioplayer.music.R.drawable.avd_loading
            )
            if (animatedDrawable is Animatable) {
                binding.btnLoadingContinue.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    animatedDrawable,
                    null
                )
                animatedDrawable.start()
            }
        } catch (e: Exception) {
            Log.e("Animation", "Error starting animation: ${e.message}")
        }
    }

    private fun stopLoadingAnimation() {
        try {
            val drawable = binding.btnLoadingContinue.compoundDrawables[2] // drawableEnd
            if (drawable is Animatable) {
                drawable.stop()
            }
        } catch (e: Exception) {
            Log.e("Animation", "Error stopping animation: ${e.message}")
        }
    }

    private fun showLoadingSate() {
        binding.btnLoadingContinue.visibility = View.VISIBLE
        binding.txtTrialFree.visibility = View.INVISIBLE
        binding.btnTryFree.visibility = View.INVISIBLE

        binding.txtTrialFreePay.visibility = View.INVISIBLE
        binding.btnContinue.visibility = View.INVISIBLE

        startLoadingAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            showTrailFreeState()
        }, 3000)
    }

    private fun showTrailFreeState() {
        stopLoadingAnimation()
        
        binding.txtTrialFree.visibility = View.INVISIBLE
        binding.btnTryFree.visibility = View.INVISIBLE
        binding.btnLoadingContinue.visibility = View.INVISIBLE

        binding.txtTrialFreePay.visibility = View.VISIBLE
        binding.btnContinue.visibility = View.VISIBLE

        binding.btnContinue.setOnClickListener {
            showPayState()
        }
    }

    private fun showPayState() {
        binding.txtTrialFree.visibility = View.VISIBLE
        binding.btnTryFree.visibility = View.VISIBLE

        binding.txtTrialFreePay.visibility = View.INVISIBLE
        binding.btnContinue.visibility = View.INVISIBLE
        binding.btnLoadingContinue.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLoadingAnimation()
    }
}
