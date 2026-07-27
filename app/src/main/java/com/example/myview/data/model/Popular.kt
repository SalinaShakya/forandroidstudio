package com.example.myview.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Popular(
    @SerializedName("slug") val slug: String,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)