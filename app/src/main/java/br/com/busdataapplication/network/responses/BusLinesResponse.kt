package br.com.busdataapplication.network.responses

import br.com.busdataapplication.models.BusLine
import com.google.gson.annotations.SerializedName

data class BusLinesResponse(
    @SerializedName("hr")
    val hour: String?,
    @SerializedName("l")
    val busLines: List<BusLine>
) {
    override fun toString(): String {
        return "VehiclesPositionResponse {" +
                "hour='$hour', " +
                "lines=$busLines" +
                "}"
    }
}