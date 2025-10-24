package com.eco.musicplayer.audioplayer.music.iap

import android.app.Activity
import android.widget.Toast
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener

class DemoPlayBilling : Activity(), PurchasesUpdatedListener {

    private lateinit var billingManager: BillingManager
    private lateinit var premiumManager: PremiumManager

    private val PRODUCT_ID = "weekly_subscription"

    fun initializeBilling() {
        billingManager = BillingManager(this, this)
        premiumManager = PremiumManager.getInstance(this)

        billingManager.connect { success, errorMessage ->
            if (success) {
                queryProducts()
            } else {
                Toast.makeText(this, "Failed to connect: $errorMessage", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun queryProducts() {
        billingManager.queryProductDetails(
            productIds = listOf(PRODUCT_ID),
            productType = BillingClient.ProductType.SUBS
        ) { productDetailsList ->
            if (!productDetailsList.isNullOrEmpty()) {
                val product = productDetailsList[0]
                displayPrice(product)
            }
        }
    }

    private fun displayPrice(product: ProductDetails) {
        val price = billingManager.getFormattedPrice(product)
    }

    private fun purchaseProduct(product: ProductDetails) {
        val offerToken = product.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: return

        billingManager.launchSubscriptionPurchaseFlow(
            activity = this,
            productDetails = product,
            offerToken = offerToken
        )
    }

    override fun onPurchasesUpdated(p0: BillingResult, p1: MutableList<Purchase>?) {
        TODO("Not yet implemented")
    }
}