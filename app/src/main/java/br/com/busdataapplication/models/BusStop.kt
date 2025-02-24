package br.com.busdataapplication.models

import com.google.gson.annotations.SerializedName

data class BusStop(
    @SerializedName("cp")
    val codeId: Int?,
    @SerializedName("np")
    val name: String?,
    @SerializedName("ed")
    val address: String?,
    @SerializedName("py")
    val latitude: Double?,
    @SerializedName("px")
    val longitude: Double?
) {
    override fun toString(): String {
        return "BusStop = { codeId: $codeId, name: $name, address: $address, latitude: $latitude, longitude: $longitude }"
    }
}
