package br.com.busdataapplication.network.responses

import br.com.busdataapplication.models.BusStop
import com.google.gson.annotations.SerializedName

data class BusStopResponse(
    @SerializedName("hr")
    val hour: String?,
    @SerializedName("p")
    val busStop: BusStop?
) {
    override fun toString(): String {
        return "BustStopResponse(" +
                "hour=$hour, " +
                "busStop=$busStop)"
    }
}
