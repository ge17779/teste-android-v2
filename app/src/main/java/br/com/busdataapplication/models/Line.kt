package br.com.busdataapplication.models

import com.google.gson.annotations.SerializedName

data class Line(
    @SerializedName("c")
    val busDestinationSign: String?,
    @SerializedName("cl")
    val busLineCode: Int?,
    @SerializedName("sl")
    val directionOfOperation: Int?,
    @SerializedName("lt0")
    val busDestination: String?,
    @SerializedName("lt1")
    val busOrigin: String?,
    @SerializedName("qv")
    val trackedVehicles: Int?,
    @SerializedName("vs")
    val vehicles: List<Vehicle>
)
