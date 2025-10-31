package com.eco.musicplayer.audioplayer.music.present

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.eco.musicplayer.audioplayer.music.databinding.DialogSaleYearlyBinding
import com.eco.musicplayer.audioplayer.music.remoteconfig.InAppProduct
import com.eco.musicplayer.audioplayer.music.remoteconfig.RemoteConfig
import com.eco.musicplayer.audioplayer.music.remoteconfig.RemoteConfigManager

class Dialog30ActivityYear : AppCompatActivity() {

    //TODO Sử dụng lazy để tối ưu hiệu suất và đảm bảo thread-safe initialization
    private val binding: DialogSaleYearlyBinding by lazy(LazyThreadSafetyMode.NONE) {
        DialogSaleYearlyBinding.inflate(layoutInflater)
    }

    private val billingClient: BillingClient by lazy {
        BillingClient.newBuilder(this)
            .setListener { billingResult, purchases ->
                handlePurchasesUpdated(billingResult, purchases)
            }
            .enablePendingPurchases()
            .build()
    }

    private val remoteConfig: RemoteConfig by lazy {
        RemoteConfig()
    }
    private var selectedProduct: InAppProduct? = null
    private var allProducts: List<InAppProduct> = emptyList()

    // Flags để theo dõi việc khởi tạo các lazy properties
    private var isBillingClientInitialized = false
    private var isRemoteConfigInitialized = false

    companion object {
        private const val TAG = "Dialog30ActivityYear"
    }

    private var productId: String = ""
    private var productType: String = ""
    private var offerId: String = ""
    private var currentProductDetails: ProductDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Thiết lập trạng thái ban đầu: ẩn thông tin giá, hiển thị loading
        showLoadingState()

        // Remote Config sẽ được khởi tạo lazy khi gọi loadProductsFromRemoteConfig()
        loadProductsFromRemoteConfig()

        setupCloseButton()
        setupPurchaseButton()
    }

    private fun loadProductsFromRemoteConfig() {
        isRemoteConfigInitialized = true
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

            // Billing client sẽ được khởi tạo lazy khi gọi initializeBillingClient()
            initializeBillingClient()
        }
    }

    private fun setupCloseButton() {
        binding.btnCloseYear.setOnClickListener {
            finish()
        }
    }

    private fun setupPurchaseButton() {
        binding.btnClaimOfferYear.setOnClickListener {
            launchPurchaseFlow()
        }
    }

    private fun initializeBillingClient() {
        // billingClient đã được khởi tạo lazy, chỉ cần trigger và connect
        connectToBillingService()
    }

    private fun connectToBillingService() {
        isBillingClientInitialized = true
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                Log.d(TAG, "${Thread.currentThread()} connectToBillingService")
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Log.d(TAG, "Billing setup finished successfully")
                    Log.d(TAG, "=== SUBSCRIPTION PRODUCTS ===")
                    //check mua rồi
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

        when (productType) {
            BillingClient.ProductType.SUBS -> querySubscriptionProducts()
            BillingClient.ProductType.INAPP -> queryInAppProducts()
            else -> {
                Log.e(TAG, "Unknown product type: $productType")
                showErrorState()
            }
        }
    }

    private fun querySubscriptionProducts() {
        if (productId.isEmpty()) {
            Log.e(TAG, "Missing subscription product ID from Remote Config")
            showErrorState()
            return
        }

        // Hiển thị loading khi bắt đầu query
        showLoadingState()

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
                Log.d(TAG, "${Thread.currentThread()} querySubscriptionProducts")
                Log.d(TAG, "Query subscription product details successful")
                if (productDetailsList.isNotEmpty()) {
                    val productDetails = productDetailsList[0]
                    currentProductDetails = productDetails
                    Log.d(TAG, "Product ID: ${productDetails.productId}")
                    Log.d(TAG, "Product Type: ${productDetails.productType}")
                    Log.d(TAG, "Name: ${productDetails.name}")
                    Log.d(TAG, "Title: ${productDetails.title}")
                    Log.d(TAG, "Description: ${productDetails.description}")

                    displayOfferDetails(productDetails)
                    updateSubscriptionUI(productDetails)
                } else {
                    Log.e(TAG, "No subscription product details found")
                    showErrorState()
                }
            } else {
                Log.e(TAG, "Failed to query subscription product details: ${billingResult.debugMessage}")
                showErrorState()
            }
        }
    }

    private fun queryInAppProducts() {
        if (productId.isEmpty()) {
            Log.e(TAG, "Missing in-app product ID from Remote Config")
            showErrorState()
            return
        }

        // Hiển thị loading khi bắt đầu query
        showLoadingState()

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "${Thread.currentThread()} queryInAppProducts")
                Log.d(TAG, "Query in-app product details successful")
                if (productDetailsList.isNotEmpty()) {
                    val productDetails = productDetailsList[0]
                    currentProductDetails = productDetails
                    Log.d(TAG, "Product ID: ${productDetails.productId}")
                    Log.d(TAG, "Product Type: ${productDetails.productType}")
                    Log.d(TAG, "Name: ${productDetails.name}")
                    Log.d(TAG, "Title: ${productDetails.title}")
                    Log.d(TAG, "Description: ${productDetails.description}")

                    displayInAppDetails(productDetails)
                    updateInAppUI(productDetails)
                } else {
                    Log.e(TAG, "No in-app product details found")
                    showErrorState()
                }
            } else {
                Log.e(TAG, "Failed to query in-app product details: ${billingResult.debugMessage}")
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

    private fun showLoadingState() {
        runOnUiThread {
            // Ẩn thông tin giá và nút claim
            binding.txtTrialOfferYear.visibility = android.view.View.INVISIBLE
            binding.btnClaimOfferYear.visibility = android.view.View.INVISIBLE
            // Hiển thị nút loading
            binding.btnLoadClaimOfferYear.visibility = android.view.View.VISIBLE
            // Ẩn thông báo lỗi
            binding.txtNotFoundYearly.visibility = android.view.View.INVISIBLE
            binding.txtTryAgain30Year.visibility = android.view.View.INVISIBLE
        }
    }

    private fun updateSubscriptionUI(productDetails: ProductDetails) {
        runOnUiThread {
            // Ẩn loading, hiển thị thông tin giá đã cập nhật
            binding.btnLoadClaimOfferYear.visibility = android.view.View.INVISIBLE

            val offers = productDetails.subscriptionOfferDetails
            if (!offers.isNullOrEmpty()) {
                val offer = findOfferById(offers, offerId) ?: offers[0]

                val pricingPhases = offer.pricingPhases.pricingPhaseList

                //TODO phân loại offer chưa hợp lý, cần xem tổng quan các gói offer -> kiểm tra toàn diện
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
                    // Hiển thị nút claim sau khi đã có giá
                    binding.btnClaimOfferYear.visibility = android.view.View.VISIBLE

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
        }
    }

    private fun updateInAppUI(productDetails: ProductDetails) {
        runOnUiThread {
            // Ẩn loading, hiển thị thông tin giá đã cập nhật
            binding.btnLoadClaimOfferYear.visibility = android.view.View.INVISIBLE

            productDetails.oneTimePurchaseOfferDetails?.let { offer ->
                val priceText = offer.formattedPrice
                binding.txtTrialOfferYear.text = "Lifetime purchase for $priceText"
                binding.txtTrialOfferYear.visibility = android.view.View.VISIBLE
                // Hiển thị nút claim sau khi đã có giá
                binding.btnClaimOfferYear.visibility = android.view.View.VISIBLE

                Log.d(TAG, "=== UI UPDATE (INAPP) ===")
                Log.d(TAG, "Price: ${offer.formattedPrice}")
                Log.d(TAG, "Price Amount: ${offer.priceAmountMicros}")
                Log.d(TAG, "Price Currency: ${offer.priceCurrencyCode}")
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
            // Ẩn loading và các thành phần giá
            binding.btnLoadClaimOfferYear.visibility = android.view.View.INVISIBLE
            binding.txtTrialOfferYear.visibility = android.view.View.INVISIBLE
            binding.btnClaimOfferYear.visibility = android.view.View.INVISIBLE
            // Hiển thị thông báo lỗi
            binding.txtNotFoundYearly.visibility = android.view.View.VISIBLE
            binding.txtTryAgain30Year.visibility = android.view.View.VISIBLE
        }
    }

    private fun launchPurchaseFlow() {
        val productDetails = currentProductDetails
        if (productDetails == null) {
            Log.e(TAG, "Cannot launch purchase flow: productDetails is null")
            return
        }

        when (productType) {
            BillingClient.ProductType.SUBS -> launchSubscriptionPurchase(productDetails)
            BillingClient.ProductType.INAPP -> launchInAppPurchase(productDetails)
            else -> {
                Log.e(TAG, "Unknown product type: $productType")
            }
        }
    }

    private fun launchSubscriptionPurchase(productDetails: ProductDetails) {
        val offers = productDetails.subscriptionOfferDetails
        if (offers.isNullOrEmpty()) {
            Log.e(TAG, "No subscription offers found")
            return
        }

        // Tìm offer theo offerId, nếu không có thì lấy offer đầu tiên
        val selectedOffer = findOfferById(offers, offerId) ?: offers[0]
        
        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(selectedOffer.offerToken)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
        
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(TAG, "Failed to launch subscription purchase flow: ${billingResult.debugMessage}")
        } else {
            Log.d(TAG, "Subscription purchase flow launched successfully")
        }
    }

    private fun launchInAppPurchase(productDetails: ProductDetails) {
        val oneTimePurchaseOfferDetails = productDetails.oneTimePurchaseOfferDetails
        if (oneTimePurchaseOfferDetails == null) {
            Log.e(TAG, "No one-time purchase offer details found")
            return
        }

        val productDetailsParamsList = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()

        val billingResult = billingClient.launchBillingFlow(this, billingFlowParams)
        
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(TAG, "Failed to launch in-app purchase flow: ${billingResult.debugMessage}")
        } else {
            Log.d(TAG, "In-app purchase flow launched successfully")
        }
    }

    private fun handlePurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
            Log.e(TAG, "Purchase update failed: ${billingResult.debugMessage}")
            return
        }

        purchases?.forEach { purchase ->
            when {
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED -> {
                    if (!purchase.isAcknowledged) {
                        handlePurchaseSuccess(purchase)
                    } else {
                        Log.d(TAG, "Purchase already acknowledged: ${purchase.products}")
                    }
                }
                purchase.purchaseState == Purchase.PurchaseState.PENDING -> {
                    Log.d(TAG, "Purchase pending: ${purchase.products}")
                    // Xử lý purchase đang pending (có thể hiển thị thông báo cho user)
                }
                else -> {
                    Log.d(TAG, "Purchase state: ${purchase.purchaseState}")
                }
            }
        }
    }

    private fun handlePurchaseSuccess(purchase: Purchase) {
        Log.d(TAG, "=== PURCHASE SUCCESS ===")
        Log.d(TAG, "Purchase Token: ${purchase.purchaseToken}")
        Log.d(TAG, "Products: ${purchase.products}")
        Log.d(TAG, "Purchase State: ${purchase.purchaseState}")
        Log.d(TAG, "Product Type: $productType")

        when (productType) {
            BillingClient.ProductType.SUBS -> {
                // Subscription cần được acknowledge
                acknowledgePurchase(purchase)
            }
            BillingClient.ProductType.INAPP -> {
                // In-app purchase cần được consume (nếu là consumable) hoặc acknowledge (nếu là non-consumable)
                // Giả sử lifetime là non-consumable, nên acknowledge thay vì consume
                acknowledgePurchase(purchase)
                // Nếu là consumable product, sử dụng: consumePurchase(purchase)
            }
            else -> {
                Log.e(TAG, "Unknown product type for purchase handling")
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Purchase acknowledged successfully: ${purchase.products}")
                runOnUiThread {
                    // Có thể hiển thị thông báo thành công hoặc update UI
                    // finish() // Hoặc đóng dialog sau khi mua thành công
                }
            } else {
                Log.e(TAG, "Failed to acknowledge purchase: ${billingResult.debugMessage}")
            }
        }
    }

    private fun consumePurchase(purchase: Purchase) {
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.consumeAsync(consumeParams) { billingResult, purchaseToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                Log.d(TAG, "Purchase consumed successfully: $purchaseToken")
                runOnUiThread {
                    // Có thể hiển thị thông báo thành công hoặc update UI
                }
            } else {
                Log.e(TAG, "Failed to consume purchase: ${billingResult.debugMessage}")
            }
        }
    }

    private fun switchToProduct(product: InAppProduct) {
        selectedProduct = product
        productId = product.productId ?: ""
        offerId = product.offerId ?: ""
        currentProductDetails = null // Reset để đợi query mới

        // Xác định loại sản phẩm dựa trên productId
        productType = when {
            productId.contains("monthly") || productId.contains("yearly") -> BillingClient.ProductType.SUBS
            productId.contains("lifetime") -> BillingClient.ProductType.INAPP
            else -> BillingClient.ProductType.SUBS
        }

        Log.d(TAG, "Đã chuyển sang sản phẩm: $productId, Loại: $productType, Ưu đãi: $offerId")

        // Truy vấn lại chi tiết sản phẩm cho lựa chọn mới
        // billingClient đã được khởi tạo lazy, có thể gọi trực tiếp
        queryProductDetails()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Chỉ cleanup các lazy properties đã được khởi tạo để tránh khởi tạo không cần thiết
        if (isBillingClientInitialized) {
            billingClient.endConnection()
        }
        if (isRemoteConfigInitialized) {
            remoteConfig.destroy()
        }
    }
}