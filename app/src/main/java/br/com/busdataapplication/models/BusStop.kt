package br.com.busdataapplication.models

import com.google.gson.annotations.SerializedName

data class BusStop(
    @SerializedName("cp")
    val id: Int?,
    @SerializedName("np")
    val name: String?,
    @SerializedName("ed")
    val address: String?,
    @SerializedName("py")
    val latitude: Double?,
    @SerializedName("px")
    val longitude: Double?,
    @SerializedName("l")
    val busLines: List<BusLine>?
) {
    override fun toString(): String {
        return "BusStop = { codeId: $id, name: $name, address: $address, latitude: $latitude, longitude: $longitude, busLines: $busLines }"
    }
}
