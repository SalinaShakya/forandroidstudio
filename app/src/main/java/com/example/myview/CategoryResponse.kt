package com.example.myview

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoryResponse(
    @SerializedName("name") val name: String?,
    @SerializedName("imageUrl") val imageUrl: String?
)
