package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.eco.musicplayer.audioplayer.music.databinding.ActivityFocusYearlyBinding

class FocusYearlyPrice : AppCompatActivity() {

    private lateinit var binding: ActivityFocusYearlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFocusYearlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        setDefaultState()
    }

    private fun setupClickListeners() {
        binding.imgBtnClose.setOnClickListener {
            finish()
        }

        binding.cvYearlyPlan.setOnClickListener {
            selectYearlyPlan()
        }

        binding.cvYearlyPlanHint.setOnClickListener {
            selectYearlyPlan()
        }

        binding.cvWeeklyPlanHint.setOnClickListener {
            selectWeeklyPlan()
        }

        binding.cvWeeklyPlan.setOnClickListener {
            selectWeeklyPlan()
        }
    }

    private fun setDefaultState() {
        selectYearlyPlan()
    }

    private fun selectYearlyPlan() {
        binding.cvYearlyPlan.visibility = View.VISIBLE

        binding.cvYearlyPlanHint.visibility = View.INVISIBLE

        binding.cvWeeklyPlan.visibility = View.INVISIBLE

        binding.cvWeeklyPlanHint.visibility = View.VISIBLE

        binding.txtRenewalInfo.visibility = View.VISIBLE

        binding.txtRenewalInfoWeek.visibility = View.INVISIBLE
    }

    private fun selectWeeklyPlan() {
        binding.cvYearlyPlan.visibility = View.INVISIBLE

        binding.cvYearlyPlanHint.visibility = View.VISIBLE

        binding.cvWeeklyPlan.visibility = View.VISIBLE

        binding.cvWeeklyPlanHint.visibility = View.INVISIBLE

        binding.txtRenewalInfo.visibility = View.INVISIBLE

        binding.txtRenewalInfoWeek.visibility = View.VISIBLE
    }
}