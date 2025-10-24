package com.example.androidtutorialhungeco.present

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.android.billingclient.api.*
import com.example.androidtutorialhungeco.R
import com.example.androidtutorialhungeco.iap.BillingManager
import com.example.androidtutorialhungeco.iap.PremiumManager

class Dialog30ActivityYear : AppCompatActivity(), PurchasesUpdatedListener {

    companion object {
        private const val TAG = "Dialog30ActivityYear"
        // Product ID - sẽ được thay thế bằng product ID thực tế khi có
        private const val YEARLY_PRODUCT_ID = "yearly_subscription_product_id"

        // Minimum loading time to show loading state (milliseconds)
        private const val MIN_LOADING_TIME = 1500L

        // Testing mode - set to true to simulate successful product query
        private const val TESTING_MODE = true
    }

    // UI Components
    private lateinit var txtNotFoundYearly: AppCompatTextView
    private lateinit var txtTryAgain30Year: AppCompatTextView
    private lateinit var btnLoadClaimOfferYear: AppCompatButton
    private lateinit var btnClaimOfferYear: AppCompatButton
    private lateinit var txtTrialOfferYear: AppCompatTextView
    private lateinit var txtPolicy: AppCompatTextView
    private lateinit var btnCloseYear: ImageButton

    // Managers
    private lateinit var billingManager: BillingManager
    private lateinit var premiumManager: PremiumManager

    // Product Details
    private var productDetails: ProductDetails? = null

    // Loading time tracking
    private var loadingStartTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sale_yearly)

        initViews()
        initManagers()
        setupClickListeners()

        // Check if user already has premium
        if (premiumManager.isPremium()) {
            showAlreadyPremiumState()
        } else {
            // Show loading state initially
            showLoadingState()
            initializeBilling()
        }
    }

    private fun initViews() {
        txtNotFoundYearly = findViewById(R.id.txtNotFoundYearly)
        txtTryAgain30Year = findViewById(R.id.txtTryAgain30Year)
        btnLoadClaimOfferYear = findViewById(R.id.btnLoadClaimOfferYear)
        btnClaimOfferYear = findViewById(R.id.btnClaimOfferYear)
        txtTrialOfferYear = findViewById(R.id.txtTrialOfferYear)
        txtPolicy = findViewById(R.id.txtPolicy)
        btnCloseYear = findViewById(R.id.btnCloseYear)
    }

    private fun initManagers() {
        billingManager = BillingManager(this, this)
        premiumManager = PremiumManager.getInstance(this)
    }

    private fun initializeBilling() {
        Log.d(TAG, "Initializing billing...")
        loadingStartTime = System.currentTimeMillis()

        // Show loading for minimum time
        Handler(Looper.getMainLooper()).postDelayed({
            // This ensures loading shows for at least MIN_LOADING_TIME
        }, MIN_LOADING_TIME)

        if (TESTING_MODE) {
            // Testing mode: simulate successful query after loading
            Log.d(TAG, "Testing mode enabled - simulating success")
            Handler(Looper.getMainLooper()).postDelayed({
                simulateSuccessfulQuery()
            }, MIN_LOADING_TIME)
        } else {
            // Real billing mode
            billingManager.connect { success, errorMessage ->
                if (success) {
                    Log.d(TAG, "Billing connected successfully")
                    queryProductDetails()
                } else {
                    Log.e(TAG, "Billing connection failed: $errorMessage")
                    showErrorStateAfterMinLoading()
                }
            }
        }
    }

    private fun queryProductDetails() {
        Log.d(TAG, "Querying product details...")

        billingManager.queryProductDetails(
            productIds = listOf(YEARLY_PRODUCT_ID),
            productType = BillingClient.ProductType.SUBS
        ) { productDetailsList ->
            if (!productDetailsList.isNullOrEmpty()) {
                productDetails = productDetailsList[0]
                Log.d(TAG, "Product details retrieved successfully")
                displayProductDetails(productDetails)
                showOfferStateAfterMinLoading()
            } else {
                Log.e(TAG, "No product details found")
                showErrorStateAfterMinLoading()
            }
        }
    }

    private fun simulateSuccessfulQuery() {
        Log.d(TAG, "Simulating successful product query for testing")
        runOnUiThread {
            // Simulate product data
            txtTrialOfferYear.text = "Try 3 days free, then $29.99/year"
            showOfferState()
        }
    }

    private fun showOfferStateAfterMinLoading() {
        val elapsedTime = System.currentTimeMillis() - loadingStartTime
        val remainingTime = MIN_LOADING_TIME - elapsedTime

        if (remainingTime > 0) {
            // Wait for remaining time before showing offer
            Handler(Looper.getMainLooper()).postDelayed({
                showOfferState()
            }, remainingTime)
        } else {
            // Already passed minimum loading time
            showOfferState()
        }
    }

    private fun showErrorStateAfterMinLoading() {
        val elapsedTime = System.currentTimeMillis() - loadingStartTime
        val remainingTime = MIN_LOADING_TIME - elapsedTime

        if (remainingTime > 0) {
            // Wait for remaining time before showing error
            Handler(Looper.getMainLooper()).postDelayed({
                showErrorState()
            }, remainingTime)
        } else {
            // Already passed minimum loading time
            showErrorState()
        }
    }

    private fun displayProductDetails(productDetails: ProductDetails?) {
        productDetails?.let { details ->
            runOnUiThread {
                // Lấy thông tin subscription offer
                val subscriptionOfferDetails = billingManager.getSubscriptionOfferDetails(details)

                if (subscriptionOfferDetails != null) {
                    // Lấy pricing phases
                    val pricingPhases = subscriptionOfferDetails.pricingPhases.pricingPhaseList

                    // Hiển thị thông tin trial nếu có
                    if (pricingPhases.size > 1) {
                        // Có trial period
                        val trialPhase = pricingPhases[0]
                        val mainPhase = pricingPhases[1]

                        val trialDuration = formatBillingPeriod(trialPhase.billingPeriod)
                        txtTrialOfferYear.text = "Try $trialDuration free, then ${mainPhase.formattedPrice}/year"

                        Log.d(TAG, "Trial offer: ${trialPhase.formattedPrice} for ${trialPhase.billingPeriod}")
                        Log.d(TAG, "Regular price: ${mainPhase.formattedPrice}")
                    } else if (pricingPhases.isNotEmpty()) {
                        // Không có trial
                        val mainPhase = pricingPhases[0]
                        txtTrialOfferYear.text = "${mainPhase.formattedPrice}/year"
                        Log.d(TAG, "Regular price: ${mainPhase.formattedPrice}")
                    }
                }
            }
        }
    }

    private fun formatBillingPeriod(period: String): String {
        // Format ISO 8601 period (e.g., P3D = 3 days, P1W = 1 week, P1M = 1 month)
        return when {
            period.contains("P3D") -> "3 days"
            period.contains("P7D") || period.contains("P1W") -> "7 days"
            period.contains("P1M") -> "1 month"
            period.contains("P1Y") -> "1 year"
            else -> period
        }
    }

    private fun setupClickListeners() {
        // Close button
        btnCloseYear.setOnClickListener {
            finish()
        }

        // Try again button
        txtTryAgain30Year.setOnClickListener {
            showLoadingState()
            initializeBilling()
        }

        // Claim offer button
        btnClaimOfferYear.setOnClickListener {
            launchPurchaseFlow()
        }
    }

    private fun launchPurchaseFlow() {
        if (productDetails == null) {
            Toast.makeText(this, "Product not available", Toast.LENGTH_SHORT).show()
            return
        }

        val offerToken = productDetails?.subscriptionOfferDetails?.firstOrNull()?.offerToken

        if (offerToken == null) {
            Toast.makeText(this, "Offer not available", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d(TAG, "Launching purchase flow...")

        val billingResult = billingManager.launchSubscriptionPurchaseFlow(
            activity = this,
            productDetails = productDetails!!,
            offerToken = offerToken
        )

        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(TAG, "Failed to launch billing flow: ${billingResult.responseCode}")
            Toast.makeText(this, "Failed to start purchase", Toast.LENGTH_SHORT).show()
        } else {
            Log.d(TAG, "Billing flow launched successfully")
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                if (purchases != null) {
                    Log.d(TAG, "Purchases updated: ${purchases.size} purchases")
                    for (purchase in purchases) {
                        handlePurchase(purchase)
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "User canceled purchase")
                Toast.makeText(this, "Purchase canceled", Toast.LENGTH_SHORT).show()
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.d(TAG, "Item already owned")
                Toast.makeText(this, "You already own this subscription", Toast.LENGTH_SHORT).show()
                onPurchaseSuccess(null)
            }
            else -> {
                Log.e(TAG, "Purchase failed: ${billingResult.responseCode} - ${billingResult.debugMessage}")
                Toast.makeText(this, "Purchase failed: ${billingResult.debugMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        Log.d(TAG, "Handling purchase: ${purchase.purchaseState}")

        when (purchase.purchaseState) {
            Purchase.PurchaseState.PURCHASED -> {
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                } else {
                    onPurchaseSuccess(purchase)
                }
            }
            Purchase.PurchaseState.PENDING -> {
                Log.d(TAG, "Purchase is pending")
                Toast.makeText(this, "Purchase is pending", Toast.LENGTH_LONG).show()
            }
            else -> {
                Log.d(TAG, "Purchase state: ${purchase.purchaseState}")
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        Log.d(TAG, "Acknowledging purchase...")

        billingManager.acknowledgePurchase(purchase) { success ->
            if (success) {
                Log.d(TAG, "Purchase acknowledged successfully")
                onPurchaseSuccess(purchase)
            } else {
                Log.e(TAG, "Failed to acknowledge purchase")
                Toast.makeText(this, "Failed to complete purchase", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onPurchaseSuccess(purchase: Purchase?) {
        Log.d(TAG, "Purchase successful!")

        // Lưu trạng thái premium
        val productId = purchase?.products?.firstOrNull() ?: YEARLY_PRODUCT_ID
        val purchaseToken = purchase?.purchaseToken

        premiumManager.savePremiumStatus(
            isPremium = true,
            productId = productId,
            purchaseToken = purchaseToken
        )

        runOnUiThread {
            Toast.makeText(this, "Thank you for your purchase!", Toast.LENGTH_LONG).show()

            // Đóng dialog sau 1.5 giây
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 1500)
        }
    }

    // UI State Management
    private fun showLoadingState() {
        runOnUiThread {
            txtNotFoundYearly.visibility = View.INVISIBLE
            txtTryAgain30Year.visibility = View.INVISIBLE
            btnClaimOfferYear.visibility = View.INVISIBLE
            txtTrialOfferYear.visibility = View.INVISIBLE
            btnLoadClaimOfferYear.visibility = View.VISIBLE
        }
    }

    private fun showErrorState() {
        runOnUiThread {
            txtNotFoundYearly.visibility = View.VISIBLE
            txtTryAgain30Year.visibility = View.VISIBLE
            btnClaimOfferYear.visibility = View.INVISIBLE
            txtTrialOfferYear.visibility = View.INVISIBLE
            btnLoadClaimOfferYear.visibility = View.INVISIBLE
        }
    }

    private fun showOfferState() {
        runOnUiThread {
            txtNotFoundYearly.visibility = View.INVISIBLE
            txtTryAgain30Year.visibility = View.INVISIBLE
                btnClaimOfferYear.visibility = View.VISIBLE
                txtTrialOfferYear.visibility = View.VISIBLE
            btnLoadClaimOfferYear.visibility = View.INVISIBLE
        }
    }

    private fun showAlreadyPremiumState() {
        runOnUiThread {
            txtNotFoundYearly.visibility = View.VISIBLE
            txtNotFoundYearly.text = "You already have premium!"
            txtTryAgain30Year.visibility = View.INVISIBLE
            btnClaimOfferYear.visibility = View.INVISIBLE
            txtTrialOfferYear.visibility = View.INVISIBLE
            btnLoadClaimOfferYear.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Đóng kết nối billing client
        billingManager.disconnect()
    }
}
