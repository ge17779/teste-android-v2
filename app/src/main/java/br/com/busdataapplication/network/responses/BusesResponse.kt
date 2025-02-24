package br.com.busdataapplication.network.responses

import br.com.busdataapplication.models.Bus
import com.google.gson.annotations.SerializedName

data class BusesResponse(
    @SerializedName("hr")
    val hour: String?,
    @SerializedName("vs")
    val buses: List<Bus>
) {
    override fun toString(): String {
        return "BusesResponse {" +
                "hour='$hour', " +
                "buses=$buses}"
    }
}
