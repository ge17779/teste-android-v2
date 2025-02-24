package br.com.busdataapplication.models

import com.google.gson.annotations.SerializedName

data class Bus(
    @SerializedName("p")
    val prefix: String?,
    @SerializedName("a")
    val isAccessible: Boolean?,
    @SerializedName("ta")
    val utc: String?,
    @SerializedName("py")
    val latitude: Double?,
    @SerializedName("px")
    val longitude: Double?
)
