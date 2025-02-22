package br.com.busdataapplication.network.responses

import br.com.busdataapplication.models.Line
import com.google.gson.annotations.SerializedName

data class AllVehiclesResponse(
    @SerializedName("hr")
    val hour: String?,
    @SerializedName("l")
    val lines: List<Line>
) {
    override fun toString(): String {
        return "VehiclesPositionResponse {" +
                "hour='$hour', " +
                "lines=$lines" +
                "}"
    }
}