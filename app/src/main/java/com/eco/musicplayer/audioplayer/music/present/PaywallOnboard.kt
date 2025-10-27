package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.databinding.ActivityPaywallOnboardBinding

//trạng thái ở màn hình chính
private enum class ScreenState {
    LOADING,
    NO_TRIAL,
    TRIAL_AVAILABLE,
    DEFAULT
}

class PaywallOnboard : AppCompatActivity() {

    private lateinit var binding: ActivityPaywallOnboardBinding
    private val handler = Handler(Looper.getMainLooper())

    private var isYearlySelected: Boolean = false

    private var isBillingLoaded: Boolean = false
    private var isTrialAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaywallOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // mặc định ban đầu là Weekly (chưa load billing)
        setToggleState(false)
        setScreenState(ScreenState.LOADING)

        setupListeners()

        // Để chuyển màn sau 3s
        handler.postDelayed({
            isBillingLoaded = true
            setScreenState(ScreenState.NO_TRIAL)
        }, 3000)
    }

    //hủy các callback
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    private fun setupListeners() {
        // Xử lý Yearly Toggle
        binding.yearlyToggle.setOnClickListener {
            if (!isYearlySelected) {
                setToggleState(true)
                updateVisibilityBasedOnState()
            }
        }
        binding.yearlyToggleNon.setOnClickListener {
            if (!isYearlySelected) {
                setToggleState(true)
                updateVisibilityBasedOnState()
            }
        }

        // 1. Xử lý Weekly Toggle
        binding.weeklyToggle.setOnClickListener {
            if (isYearlySelected) {
                setToggleState(false)
                updateVisibilityBasedOnState()
            }
        }
        binding.weeklyToggleNon.setOnClickListener {
            if (isYearlySelected) {
                setToggleState(false)
                updateVisibilityBasedOnState()
            }
        }

        // ấn Switch
        binding.scTrialSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setScreenState(ScreenState.DEFAULT)
            } else {
                setScreenState(ScreenState.TRIAL_AVAILABLE)
            }
        }

        //
        binding.btnContinue.setOnClickListener {
            // test chuyển màn ở trạng thái (Không đủ điều kiện trial)
            if (!isTrialAvailable) {
                isTrialAvailable = true
                setScreenState(ScreenState.TRIAL_AVAILABLE)
            }
        }

        binding.btnTryForFree.setOnClickListener {}
    }

    // hiện 1 trong 2 Yearly/Weekly
    private fun setToggleState(isYearly: Boolean) {
        isYearlySelected = isYearly
        if (isYearly) {
            binding.yearlyToggle.visibility = View.VISIBLE
            binding.yearlyToggleNon.visibility = View.INVISIBLE
            binding.weeklyToggle.visibility = View.VISIBLE
            binding.weeklyToggleNon.visibility = View.INVISIBLE
        } else {
            binding.yearlyToggle.visibility = View.INVISIBLE
            binding.yearlyToggleNon.visibility = View.VISIBLE
            binding.weeklyToggle.visibility = View.INVISIBLE
            binding.weeklyToggleNon.visibility = View.VISIBLE
        }
    }

    private fun setScreenState(state: ScreenState) {
        binding.btnTryForFree.visibility = View.INVISIBLE
        binding.btnContinue.visibility = View.INVISIBLE
        binding.btnContinueLoad.visibility = View.INVISIBLE
        binding.freeTrialContainer.visibility = View.VISIBLE

        binding.txtPriceYear.visibility = View.INVISIBLE
        binding.txtPriceWeekly.visibility = View.INVISIBLE
        binding.txtResultYearly.visibility = View.INVISIBLE
        binding.txtResultWeekly.visibility = View.INVISIBLE

        when (state) {
            ScreenState.LOADING -> {
                binding.btnContinueLoad.visibility = View.VISIBLE
                binding.freeTrialContainer.visibility = View.VISIBLE
                binding.scTrialSwitch.isChecked = true
            }

            ScreenState.NO_TRIAL -> {
                binding.btnContinue.visibility = View.VISIBLE
                binding.freeTrialContainer.visibility = View.GONE
                updatePriceResultVisibility(isYearlySelected, isTrialEnabled = false)
            }

            ScreenState.TRIAL_AVAILABLE -> {
                binding.btnContinue.visibility = View.VISIBLE
                binding.freeTrialContainer.visibility = View.VISIBLE
                binding.scTrialSwitch.isChecked = false
                updatePriceResultVisibility(isYearlySelected, isTrialEnabled = false)
            }

            ScreenState.DEFAULT -> {
                binding.btnContinue.visibility = View.INVISIBLE
                binding.btnTryForFree.visibility = View.VISIBLE
                binding.freeTrialContainer.visibility = View.VISIBLE
                binding.scTrialSwitch.isChecked = true
                updatePriceResultVisibility(isYearlySelected, isTrialEnabled = true)
            }
        }
    }

    private fun updatePriceResultVisibility(isYearly: Boolean, isTrialEnabled: Boolean) {
        if (isTrialEnabled) {
            if (isYearly) {
                binding.txtPriceYear.visibility = View.VISIBLE
            } else {
                binding.txtPriceWeekly.visibility = View.VISIBLE
            }
        } else {
            if (isYearly) {
                binding.txtResultYearly.visibility = View.VISIBLE
            } else {
                binding.txtResultWeekly.visibility = View.VISIBLE
            }
        }
    }

    private fun updateVisibilityBasedOnState() {
        if (!isBillingLoaded) {
            setScreenState(ScreenState.LOADING)
        } else if (!isTrialAvailable) {
            setScreenState(ScreenState.NO_TRIAL)
        } else if (binding.scTrialSwitch.isChecked) {
            setScreenState(ScreenState.DEFAULT)
        } else {
            setScreenState(ScreenState.TRIAL_AVAILABLE)
        }
    }
}