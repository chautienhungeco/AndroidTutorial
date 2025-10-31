package com.eco.musicplayer.audioplayer.music.remoteconfig

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

object RemoteConfigManager {
    private const val TAG = "RemoteConfigManager"
    private const val PAYWALL_CONFIG_KEY = "paywall_config"

    private val gson = Gson()

    private val paywallConfig: PaywallConfig? by lazy {
        try {
            val jsonString = RemoteConfigHelper.getString(PAYWALL_CONFIG_KEY)
            if (jsonString.isNotEmpty()) gson.fromJson(
                jsonString,
                PaywallConfig::class.java
            ) else null
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "Failed to parse paywall config JSON", e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "Error getting paywall config", e)
            null
        }
    }

    fun getPaywallConfigOrNull(): PaywallConfig? = paywallConfig

    val selectedProduct: InAppProduct? by lazy {
        val products = paywallConfig?.products
        val selectionPosition = paywallConfig?.selectionPosition ?: 0
        if (products != null && selectionPosition in products.indices) products[selectionPosition] else products?.firstOrNull()
    }
}
