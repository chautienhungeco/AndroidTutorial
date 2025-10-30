package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.QueryProductDetailsParams
import com.eco.musicplayer.audioplayer.music.databinding.DialogSaleYearlyBinding
import com.eco.musicplayer.audioplayer.music.remoteconfig.InAppProduct
import com.eco.musicplayer.audioplayer.music.remoteconfig.RemoteConfig
import com.eco.musicplayer.audioplayer.music.remoteconfig.RemoteConfigManager

class Dialog30ActivityYear : AppCompatActivity() {

    private lateinit var binding: DialogSaleYearlyBinding

    private lateinit var billingClient: BillingClient
    private lateinit var remoteConfig: RemoteConfig
    private var selectedProduct: InAppProduct? = null
    private var allProducts: List<InAppProduct> = emptyList()

    companion object {
        private const val TAG = "Dialog30ActivityYear"
    }

    private var productId: String = ""
    private var productType: String = ""
    private var offerId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogSaleYearlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteConfig = RemoteConfig()

        loadProductsFromRemoteConfig()

        setupCloseButton()
    }

    private fun loadProductsFromRemoteConfig() {
        remoteConfig.fetchAndActivate {
            // Lấy danh sách product từ Remote Config
            allProducts = RemoteConfigManager.getPaywallConfigOrNull()?.products ?: emptyList()
            selectedProduct = RemoteConfigManager.selectedProduct

            selectedProduct?.let { product ->
                productId = product.productId ?: ""
                offerId = product.offerId ?: ""

                productType = when {
                    productId.contains("monthly") || productId.contains("yearly") -> BillingClient.ProductType.SUBS
                    productId.contains("lifetime") -> BillingClient.ProductType.INAPP
                    else -> BillingClient.ProductType.SUBS
                }

                Log.d(
                    TAG,
                    "Remote Config loaded - Product ID: $productId, Product Type: $productType, Offer ID: $offerId"
                )
                Log.d(TAG, "All products: $allProducts")
            }

            //ktra billing đã đc khởi tạo chưa
            if (::billingClient.isInitialized) {
                queryProductDetails()
            } else {
                initializeBillingClient()
            }
        }
    }

    private fun setupCloseButton() {
        binding.btnCloseYear.setOnClickListener {
            finish()
        }
    }

    private fun initializeBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .setListener { billingResult, purchases ->
                //TODO trinh mua hang
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
        if (productId.isEmpty() || productType.isEmpty()) {
            Log.e(TAG, "Missing product configuration from Remote Config")
            showErrorState()
            return
        }
        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(productType)
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

                    if (productType == BillingClient.ProductType.SUBS) {
                        displayOfferDetails(productDetails)
                    } else {
                        displayInAppDetails(productDetails)
                    }

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

    private fun displayInAppDetails(productDetails: ProductDetails) {
        Log.d(TAG, "=== INAPP DETAILS for ${productDetails.productId} ===")
        Log.d(TAG, "One-Time Purchase Product")
        Log.d(TAG, "Product ID: ${productDetails.productId}")
        Log.d(TAG, "Title: ${productDetails.title}")
        Log.d(TAG, "Description: ${productDetails.description}")

        productDetails.oneTimePurchaseOfferDetails?.let { offer ->
            Log.d(TAG, "Price: ${offer.formattedPrice}")
            Log.d(TAG, "Price Amount: ${offer.priceAmountMicros}")
            Log.d(TAG, "Price Currency: ${offer.priceCurrencyCode}")
        }
    }

    private fun updateUI(productDetails: ProductDetails) {
        runOnUiThread {
            if (productType == BillingClient.ProductType.SUBS) {
                val offers = productDetails.subscriptionOfferDetails
                if (!offers.isNullOrEmpty()) {
                    val offer = findOfferById(offers, offerId) ?: offers[0]

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

                        Log.d(TAG, "=== UI UPDATE (SUBS) ===")
                        Log.d(
                            TAG,
                            "Trial phase: ${trialPhase.formattedPrice} for ${trialPhase.billingPeriod} (cycles: ${trialPhase.billingCycleCount})"
                        )
                        Log.d(
                            TAG,
                            "Paid phase: ${paidPhase.formattedPrice} for ${paidPhase.billingPeriod} (cycles: ${paidPhase.billingCycleCount})"
                        )
                        Log.d(TAG, "Formatted trial period: $trialPeriod")
                        Log.d(TAG, "Display text: $trialText")
                    }
                }
            } else {
                productDetails.oneTimePurchaseOfferDetails?.let { offer ->
                    val priceText = offer.formattedPrice
                    binding.txtTrialOfferYear.text = "Lifetime purchase for $priceText"
                    binding.txtTrialOfferYear.visibility = android.view.View.VISIBLE

                    Log.d(TAG, "=== UI UPDATE (INAPP) ===")
                    Log.d(TAG, "Price: ${offer.formattedPrice}")
                    Log.d(TAG, "Price Amount: ${offer.priceAmountMicros}")
                    Log.d(TAG, "Price Currency: ${offer.priceCurrencyCode}")
                }
            }
        }
    }

//      Định dạng billing period
//      P3D -> "3 days"
//      P1W -> "1 week"
//      P1M -> "1 month"
//      P1Y -> "1 year"

    private fun formatBillingPeriod(period: String): String {
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
            binding.btnClaimOfferYear.visibility = android.view.View.INVISIBLE
            binding.txtTrialOfferYear.visibility = android.view.View.INVISIBLE
        }
    }

    // Đã loại bỏ logic mua hàng - chỉ hiển thị thông tin giá

    private fun switchToProduct(product: InAppProduct) {
        selectedProduct = product
        productId = product.productId ?: ""
        offerId = product.offerId ?: ""

        // Xác định loại sản phẩm dựa trên productId
        productType = when {
            productId.contains("monthly") || productId.contains("yearly") -> BillingClient.ProductType.SUBS
            productId.contains("lifetime") -> BillingClient.ProductType.INAPP
            else -> BillingClient.ProductType.SUBS
        }

        Log.d(TAG, "Đã chuyển sang sản phẩm: $productId, Loại: $productType, Ưu đãi: $offerId")

        // Truy vấn lại chi tiết sản phẩm cho lựa chọn mới
        if (::billingClient.isInitialized) {
            queryProductDetails()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
        remoteConfig.destroy()
    }
}