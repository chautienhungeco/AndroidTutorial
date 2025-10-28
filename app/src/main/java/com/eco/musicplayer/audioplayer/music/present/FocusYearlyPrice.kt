package com.eco.musicplayer.audioplayer.music.present

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.eco.musicplayer.audioplayer.music.databinding.ActivityFocusYearlyBinding

class FocusYearlyPrice : AppCompatActivity() {

    private lateinit var binding: ActivityFocusYearlyBinding
    private var isYearlySelected = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusYearlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInitialState()
        setupClickListeners()
        startLoadingSequence()
    }

    private fun setupInitialState() {
        binding.cvYearlyPlan.visibility = View.INVISIBLE
        binding.cvYearlyPlanHint.visibility = View.INVISIBLE
        binding.cvWeeklyPlan.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHint.visibility = View.INVISIBLE

        binding.cvYearlyPlanLoad.visibility = View.INVISIBLE
        binding.cvYearlyPlanHintLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHintLoad.visibility = View.INVISIBLE

        binding.btnTryForFree.visibility = View.INVISIBLE
        binding.btnContinueFocus.visibility = View.INVISIBLE

        binding.txtRenewalInfo.visibility = View.INVISIBLE
        binding.txtRenewalInfoWeek.visibility = View.INVISIBLE
        binding.txtRenewalYearContinue.visibility = View.INVISIBLE
        binding.txtRenewalWeekContinue.visibility = View.INVISIBLE

        selectYearlyPlanLoad()
    }

    private fun setupClickListeners() {
        binding.imgBtnClose.setOnClickListener {
            finish()
        }

        // Yearly plan selection
        binding.cvYearlyPlan.setOnClickListener {
            selectYearlyPlan()
        }

        binding.cvYearlyPlanHint.setOnClickListener {
            selectYearlyPlan()
        }

        // Weekly plan selection
        binding.cvWeeklyPlanHint.setOnClickListener {
            selectWeeklyPlan()
        }

        binding.cvWeeklyPlan.setOnClickListener {
            selectWeeklyPlan()
        }

        // Loading state plan selection
        binding.cvYearlyPlanLoad.setOnClickListener {
            selectYearlyPlanLoad()
        }

        binding.cvYearlyPlanHintLoad.setOnClickListener {
            selectYearlyPlanLoad()
        }

        binding.cvWeeklyPlanLoad.setOnClickListener {
            selectWeeklyPlanLoad()
        }

        binding.cvWeeklyPlanHintLoad.setOnClickListener {
            selectWeeklyPlanLoad()
        }

        // Button interactions
        binding.btnTryForFree.setOnClickListener {
            showContinueState()
        }

        binding.btnContinueFocus.setOnClickListener {
            showTryForFreeState()
        }
    }

    private fun startLoadingSequence() {
        binding.btnLoadingBar.visibility = View.VISIBLE
        binding.btnTryForFree.visibility = View.INVISIBLE

        startLoadingAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            showLoadingPlans()
        }, 3000)
    }

    private fun showLoadingPlans() {
        binding.btnLoadingBar.visibility = View.INVISIBLE
        stopLoadingAnimation()

        binding.btnTryForFree.visibility = View.VISIBLE

        selectYearlyPlan()
    }

    private fun selectYearlyPlan() {
        isYearlySelected = true

        binding.cvYearlyPlanLoad.visibility = View.INVISIBLE
        binding.cvYearlyPlanHintLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHintLoad.visibility = View.INVISIBLE

        binding.cvYearlyPlan.visibility = View.VISIBLE
        binding.cvYearlyPlanHint.visibility = View.INVISIBLE

        binding.cvWeeklyPlan.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHint.visibility = View.VISIBLE

        binding.txtRenewalInfo.visibility = View.VISIBLE
        binding.txtRenewalInfoWeek.visibility = View.INVISIBLE

        binding.txtRenewalYearContinue.visibility = View.INVISIBLE
        binding.txtRenewalWeekContinue.visibility = View.INVISIBLE
    }

    private fun selectWeeklyPlan() {
        isYearlySelected = false

        binding.cvYearlyPlanLoad.visibility = View.INVISIBLE
        binding.cvYearlyPlanHintLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHintLoad.visibility = View.INVISIBLE

        binding.cvYearlyPlan.visibility = View.INVISIBLE
        binding.cvYearlyPlanHint.visibility = View.VISIBLE

        binding.cvWeeklyPlan.visibility = View.VISIBLE
        binding.cvWeeklyPlanHint.visibility = View.INVISIBLE

        binding.txtRenewalInfo.visibility = View.INVISIBLE
        binding.txtRenewalInfoWeek.visibility = View.VISIBLE

        binding.txtRenewalYearContinue.visibility = View.INVISIBLE
        binding.txtRenewalWeekContinue.visibility = View.INVISIBLE
    }

    private fun selectYearlyPlanLoad() {
        isYearlySelected = true

        binding.cvYearlyPlan.visibility = View.INVISIBLE
        binding.cvYearlyPlanHint.visibility = View.INVISIBLE
        binding.cvWeeklyPlan.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHint.visibility = View.INVISIBLE

        binding.cvYearlyPlanLoad.visibility = View.VISIBLE
        binding.cvYearlyPlanHintLoad.visibility = View.INVISIBLE

        binding.cvWeeklyPlanLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHintLoad.visibility = View.VISIBLE

        binding.txtRenewalInfo.visibility = View.INVISIBLE
        binding.txtRenewalInfoWeek.visibility = View.INVISIBLE
        binding.txtRenewalYearContinue.visibility = View.INVISIBLE
        binding.txtRenewalWeekContinue.visibility = View.INVISIBLE
    }

    private fun selectWeeklyPlanLoad() {
        isYearlySelected = false

        binding.cvYearlyPlan.visibility = View.INVISIBLE
        binding.cvYearlyPlanHint.visibility = View.INVISIBLE
        binding.cvWeeklyPlan.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHint.visibility = View.INVISIBLE

        binding.cvYearlyPlanLoad.visibility = View.INVISIBLE
        binding.cvYearlyPlanHintLoad.visibility = View.VISIBLE

        binding.cvWeeklyPlanLoad.visibility = View.VISIBLE
        binding.cvWeeklyPlanHintLoad.visibility = View.INVISIBLE

        binding.txtRenewalInfo.visibility = View.INVISIBLE
        binding.txtRenewalInfoWeek.visibility = View.INVISIBLE
        binding.txtRenewalYearContinue.visibility = View.INVISIBLE
        binding.txtRenewalWeekContinue.visibility = View.INVISIBLE
    }

    private fun showContinueState() {
        binding.btnTryForFree.visibility = View.INVISIBLE

        binding.btnContinueFocus.visibility = View.VISIBLE

        binding.cvYearlyPlanLoad.visibility = View.INVISIBLE
        binding.cvYearlyPlanHintLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanLoad.visibility = View.INVISIBLE
        binding.cvWeeklyPlanHintLoad.visibility = View.INVISIBLE

        binding.txtRenewalInfo.visibility = View.INVISIBLE
        binding.txtRenewalInfoWeek.visibility = View.INVISIBLE

        if (isYearlySelected) {
            binding.cvYearlyPlan.visibility = View.VISIBLE
            binding.cvYearlyPlanHint.visibility = View.INVISIBLE

            binding.cvWeeklyPlan.visibility = View.INVISIBLE
            binding.cvWeeklyPlanHint.visibility = View.VISIBLE

            binding.txtRenewalYearContinue.visibility = View.VISIBLE
            binding.txtRenewalWeekContinue.visibility = View.INVISIBLE
        } else {
            binding.cvYearlyPlan.visibility = View.INVISIBLE
            binding.cvYearlyPlanHint.visibility = View.VISIBLE

            binding.cvWeeklyPlan.visibility = View.VISIBLE
            binding.cvWeeklyPlanHint.visibility = View.INVISIBLE

            binding.txtRenewalYearContinue.visibility = View.INVISIBLE
            binding.txtRenewalWeekContinue.visibility = View.VISIBLE
        }
    }

    private fun showTryForFreeState() {
        binding.btnContinueFocus.visibility = View.INVISIBLE

        binding.btnTryForFree.visibility = View.VISIBLE

        binding.txtRenewalYearContinue.visibility = View.INVISIBLE
        binding.txtRenewalWeekContinue.visibility = View.INVISIBLE
    }

    private fun startLoadingAnimation() {
        try {
            val animatedDrawable = ContextCompat.getDrawable(
                this,
                com.eco.musicplayer.audioplayer.music.R.drawable.avd_loading
            )
            if (animatedDrawable is Animatable) {
                binding.btnLoadingBar.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    animatedDrawable,
                    null
                )
                animatedDrawable.start()
            }

            startLoadingIconAnimation(binding.txtYearlyPriceLoad)
            startLoadingIconAnimation(binding.txtWeeklyPriceHintLoad)
        } catch (e: Exception) {
            // error
        }
    }

    private fun startLoadingIconAnimation(textView: android.widget.TextView) {
        try {
            val animatedDrawable = ContextCompat.getDrawable(
                this,
                com.eco.musicplayer.audioplayer.music.R.drawable.ic_loading_black
            )
            if (animatedDrawable is Animatable) {
                textView.setCompoundDrawablesWithIntrinsicBounds(null, null, animatedDrawable, null)
                animatedDrawable.start()
            }
        } catch (e: Exception) {
            // error
        }
    }

    private fun stopLoadingAnimation() {
        try {
            val drawable = binding.btnLoadingBar.compoundDrawables[2]
            if (drawable is Animatable) {
                drawable.stop()
            }

            stopLoadingIconAnimation(binding.txtYearlyPriceLoad)
            stopLoadingIconAnimation(binding.txtWeeklyPriceHintLoad)
        } catch (e: Exception) {
        }
    }

    private fun stopLoadingIconAnimation(textView: android.widget.TextView) {
        try {
            val drawable = textView.compoundDrawables[2]
            if (drawable is Animatable) {
                drawable.stop()
            }
        } catch (e: Exception) {
            // error
        }
    }
}