package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.*
import com.eco.musicplayer.audioplayer.music.databinding.DialogSaleYearlyBinding

class Dialog30ActivityYear : AppCompatActivity() {

    private lateinit var binding: DialogSaleYearlyBinding

    private lateinit var billingClient: BillingClient

    companion object {
        private const val TAG = "Dialog30ActivityYear"

        private const val PRODUCT_ID = "free_123"
        private const val OFFER_ID = "3days"
    }

    private val productId = PRODUCT_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSaleYearlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCloseButton()

        initializeBillingClient()
    }

    private fun setupCloseButton() {
        binding.btnCloseYear.setOnClickListener {
            finish()
        }
    }

    private fun initializeBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener { billingResult, purchases ->
                handlePurchasesUpdated(billingResult, purchases)
            }
            .enablePendingPurchases()
            .build()

        connectToBillingService()
    }

    private fun connectToBillingService() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished successfully")
                    Log.d(TAG, "=== SUBSCRIPTION PRODUCTS ===")
                    queryProductDetails()
                } else {
                    Log.e(TAG, "Billing setup failed: ${billingResult.debugMessage}")
                    showErrorState()
                }
            }

            override fun onBillingServiceDisconnected() {
                Log.w(TAG, "Billing service disconnected")
            }
        })
    }

    private fun queryProductDetails() {
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Query product details successful")

                if (productDetailsList.isNotEmpty()) {
                    val productDetails = productDetailsList[0]
                    Log.d(TAG, "Product ID: ${productDetails.productId}")
                    Log.d(TAG, "Product Type: ${productDetails.productType}")
                    Log.d(TAG, "Name: ${productDetails.name}")
                    Log.d(TAG, "Title: ${productDetails.title}")
                    Log.d(TAG, "Description: ${productDetails.description}")

                    displayOfferDetails(productDetails)

                    updateUI(productDetails)
                } else {
                    Log.e(TAG, "No product details found")
                    showErrorState()
                }
            } else {
                Log.e(TAG, "Failed to query product details: ${billingResult.debugMessage}")
                showErrorState()
            }
        }
    }

    private fun displayOfferDetails(productDetails: ProductDetails) {
        Log.d(TAG, "=== OFFER DETAILS for ${productDetails.productId} ===")

        productDetails.subscriptionOfferDetails?.forEach { offer ->
            Log.d(TAG, "Offer ID: ${offer.offerId}")
            Log.d(TAG, "Offer Token: ${offer.basePlanId}")

            offer.pricingPhases.pricingPhaseList.forEach { phase ->
                Log.d(TAG, "Phase: ${phase.billingCycleCount} cycles")
                Log.d(TAG, "Billing Period: ${phase.billingPeriod}")
                Log.d(TAG, "Price: ${phase.formattedPrice}")
                Log.d(TAG, "Price Amount: ${phase.priceAmountMicros}")
                Log.d(TAG, "Price Currency: ${phase.priceCurrencyCode}")
                Log.d(TAG, "---")
            }
        }
    }

    private fun updateUI(productDetails: ProductDetails) {
        runOnUiThread {
            val offers = productDetails.subscriptionOfferDetails
            if (!offers.isNullOrEmpty()) {
                val offer = findOfferById(offers, OFFER_ID) ?: offers[0]

                val pricingPhases = offer.pricingPhases.pricingPhaseList

                if (pricingPhases.isNotEmpty()) {
                    val trialPhase = pricingPhases.first()
                    
                    val paidPhase = pricingPhases.firstOrNull { it.billingCycleCount == 0 }
                        ?: run {
                            if (pricingPhases.size > 1) pricingPhases[1] else pricingPhases.last()
                        }

                    val trialPeriod = formatBillingPeriod(trialPhase.billingPeriod)

                    val trialText = getString(
                        com.eco.musicplayer.audioplayer.music.R.string.trial3day,
                        trialPeriod,
                        paidPhase.formattedPrice
                    )
                    binding.txtTrialOfferYear.text = trialText
                    binding.txtTrialOfferYear.visibility = android.view.View.VISIBLE

                    Log.d(TAG, "=== UI UPDATE ===")
                    Log.d(TAG, "Trial phase: ${trialPhase.formattedPrice} for ${trialPhase.billingPeriod} (cycles: ${trialPhase.billingCycleCount})")
                    Log.d(TAG, "Paid phase: ${paidPhase.formattedPrice} for ${paidPhase.billingPeriod} (cycles: ${paidPhase.billingCycleCount})")
                    Log.d(TAG, "Formatted trial period: $trialPeriod")
                    Log.d(TAG, "Display text: $trialText")
                }
            }

            binding.btnClaimOfferYear.setOnClickListener {
                initiatePurchaseFlow(productDetails)
            }
        }
    }

//      Định dạng billing period
//      P3D -> "3 days"
//      P1W -> "1 week"
//      P1M -> "1 month"
//      P1Y -> "1 year"

    private fun formatBillingPeriod(period: String): String {
        // Remove prefix "P"
        val withoutPrefix = period.removePrefix("P")

        return when {
            withoutPrefix.endsWith("D") -> {
                val days = withoutPrefix.removeSuffix("D").toIntOrNull() ?: 0
                if (days == 1) "1 day" else "$days days"
            }

            withoutPrefix.endsWith("W") -> {
                val weeks = withoutPrefix.removeSuffix("W").toIntOrNull() ?: 0
                if (weeks == 1) "1 week" else "$weeks weeks"
            }

            withoutPrefix.endsWith("M") -> {
                val months = withoutPrefix.removeSuffix("M").toIntOrNull() ?: 0
                if (months == 1) "1 month" else "$months months"
            }

            withoutPrefix.endsWith("Y") -> {
                val years = withoutPrefix.removeSuffix("Y").toIntOrNull() ?: 0
                if (years == 1) "1 year" else "$years years"
            }

            else -> withoutPrefix
        }
    }

    private fun findOfferById(
        offers: List<ProductDetails.SubscriptionOfferDetails>,
        offerId: String
    ): ProductDetails.SubscriptionOfferDetails? {
        return offers.find { it.offerId == offerId }
    }

    private fun showErrorState() {
        runOnUiThread {
            binding.txtNotFoundYearly.visibility = android.view.View.VISIBLE
            binding.txtTryAgain30Year.visibility = android.view.View.VISIBLE
        }
    }

    private fun initiatePurchaseFlow(productDetails: ProductDetails) {
        Log.d(TAG, "TODO: update then")
    }

    private fun handlePurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    Log.d(TAG, "Purchase updated: ${purchase.products}")
                    acknowledgePurchase(purchase)
                }
            }

            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User canceled the purchase")
            }

            else -> {
                Log.e(TAG, "Purchase error: ${billingResult.debugMessage}")
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        if (!purchase.isAcknowledged) {
            val acknowledgeParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

            billingClient.acknowledgePurchase(acknowledgeParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Purchase acknowledged successfully")
                } else {
                    Log.e(TAG, "Failed to acknowledge purchase: ${billingResult.debugMessage}")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
    }
}