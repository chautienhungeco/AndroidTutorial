package com.example.androidtutorialhungeco.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.example.androidtutorialhungeco.R

class Dialog30ActivityYear : AppCompatActivity() {

    private lateinit var txtNotFound30Year: AppCompatTextView
    private lateinit var txtTryAgain30Year: AppCompatTextView
    private lateinit var btnClaimLoadingYear: AppCompatButton
    private lateinit var btnClaimOfferYear: AppCompatButton
    private lateinit var txtTrialOfferYear: AppCompatTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sale_yearly)

        txtNotFound30Year = findViewById(R.id.txtNotFoundYearly)
        txtTryAgain30Year = findViewById(R.id.txtTryAgain30Year)
        btnClaimLoadingYear = findViewById(R.id.btnLoadClaimOfferYear)
        btnClaimOfferYear = findViewById(R.id.btnClaimOfferYear)
        txtTrialOfferYear = findViewById(R.id.txtTrialOfferYear)

        btnClaimOfferYear.visibility = View.INVISIBLE
        txtTrialOfferYear.visibility = View.INVISIBLE

        txtTryAgain30Year.setOnClickListener {
            txtNotFound30Year.visibility = View.GONE
            txtTryAgain30Year.visibility = View.GONE

            btnClaimLoadingYear.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                btnClaimLoadingYear.visibility = View.INVISIBLE
                btnClaimOfferYear.visibility = View.VISIBLE
                txtTrialOfferYear.visibility = View.VISIBLE
            }, 3000)
        }

    }
}