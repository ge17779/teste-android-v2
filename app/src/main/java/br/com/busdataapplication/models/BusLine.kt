package br.com.busdataapplication.models

import com.google.gson.annotations.SerializedName

data class BusLine(
    @SerializedName("c")
    val destinationSign: String?,
    @SerializedName("cl")
    val id: Int,
    @SerializedName("lt")
    val firstNumericSign: String?,
    @SerializedName("tl")
    val secondNumericSign: Int?,
    @SerializedName("sl")
    val sense: Int?,
    @SerializedName("lt0")
    val destination: String?,
    @SerializedName("lt1")
    val origin: String?,
    @SerializedName("tp")
    val mainToSecondary: String?,
    @SerializedName("ts")
    val secondaryToMain: String?,
    @SerializedName("qv")
    val trackedVehicles: Int?,
    @SerializedName("vs")
    var buses: List<Bus>? = emptyList(),
    var busStops: List<BusStop>? = emptyList()
) {
    override fun toString(): String {
        return "BusLine(" +
                "destinationSign=$destinationSign, " +
                "lineCode=$id, " +
                "firstNumericSign=$firstNumericSign, " +
                "secondNumericSign=$secondNumericSign, " +
                "sense=$sense, " +
                "destination=$destination, " +
                "origin=$origin, " +
                "mainToSecondary=$mainToSecondary, " +
                "secondaryToMain=$secondaryToMain, " +
                "trackedVehicles=$trackedVehicles, " +
                "busStops=$busStops, " +
                "buses=$buses)"
    }
}
