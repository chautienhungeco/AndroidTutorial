package com.eco.musicplayer.audioplayer.music.remoteconfig

import com.google.gson.annotations.SerializedName

data class InAppProduct(
    @SerializedName("productId")
    val productId: String? = null,

    @SerializedName("offerId")
    val offerId: String? = null
)