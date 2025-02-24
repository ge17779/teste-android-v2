package br.com.busdataapplication.repository

import android.util.Log
import br.com.busdataapplication.callbacks.CompletionCallback
import br.com.busdataapplication.models.BusLine
import br.com.busdataapplication.models.BusStop
import br.com.busdataapplication.network.NetworkConnection.Companion.service
import br.com.busdataapplication.network.NetworkConnection.Companion.token
import br.com.busdataapplication.network.responses.BusStopResponse
import br.com.busdataapplication.network.responses.BusesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BusLineRepository {

    companion object {
        private const val TAG = "BusLineRepository"
    }

    fun authenticate(callback: CompletionCallback?){
        service.authenticate(token).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    callback?.onSuccess(response.body()!!)
                    Log.d(TAG, "onResponse: ${response.body()}")
                } else {
                    callback?.onFailure("Falha na autenticação")
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback?.onFailure("Falha na autenticação")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getBusStopsByParam(nearHere: String, callback: CompletionCallback?){
        service.getBusStopsByParam(nearHere).enqueue(object : Callback<List<BusStop>> {
            override fun onResponse(call: Call<List<BusStop>>, response: Response<List<BusStop>>) {
                if (response.isSuccessful){
                    val body = response.body() as List<BusStop>
                    callback?.onSuccess(body)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    callback?.onFailure("Falha na requisição")
                }
            }

            override fun onFailure(call: Call<List<BusStop>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                callback?.onFailure("Falha na requisição")
            }

        })
    }

    fun getLinesByParam(param: String, callback: CompletionCallback?){
        service.getLinesByParam(param).enqueue(object : Callback<List<BusLine>> {
            override fun onResponse(call: Call<List<BusLine>>, response: Response<List<BusLine>>) {
                if (response.isSuccessful) {
                    val body = response.body() as List<BusLine>
                    callback?.onSuccess(body)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    callback?.onFailure("Falha na requisição")
                }
            }

            override fun onFailure(call: Call<List<BusLine>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                callback?.onFailure("Falha na requisição")
            }
        })
    }

    fun getBusStopsByLine(lineCode: Int, callback: CompletionCallback?){
        service.getBusStopsByLine(lineCode).enqueue(object : Callback<List<BusStop>> {
            override fun onResponse(call: Call<List<BusStop>>, response: Response<List<BusStop>>) {
                if (response.isSuccessful){
                    callback?.onSuccess(response.body()!!)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    callback?.onFailure("Falha na requisição")
                }
            }

            override fun onFailure(call: Call<List<BusStop>>, t: Throwable) {
                callback?.onFailure(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getBusesByLine(lineCode: Int, callback: CompletionCallback?){
        service.getBusesByLine(lineCode).enqueue(object : Callback<BusesResponse> {
            override fun onResponse(call: Call<BusesResponse>, response: Response<BusesResponse>) {
                if (response.isSuccessful){
                    callback?.onSuccess(response.body()!!)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    callback?.onFailure("Falha na requisição")
                }
            }

            override fun onFailure(call: Call<BusesResponse>, t: Throwable) {
                callback?.onFailure(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun getBusesAndBusStopsByLine(line: BusLine, callback: CompletionCallback?) {
        getBusStopsByLine(line.id, object : CompletionCallback {
            override fun onSuccess(any: Any) {
                val busStops = any as List<BusStop>
                line.busStops = busStops
                getBusesByLine(line.id, object : CompletionCallback {
                    override fun onSuccess(any: Any) {
                        val response = any as BusesResponse
                        line.buses = response.buses
                        callback?.onSuccess(line)
                    }

                    override fun onFailure(error: String) {
                        callback?.onFailure(error)
                    }
                })
            }

            override fun onFailure(error: String) {
                callback?.onFailure(error)
            }
        })
    }

    fun getEstimatedByBusStop(stopCode: Int, callback: CompletionCallback?){
        service.getEstimatedArrival(stopCode).enqueue(object : Callback<BusStopResponse> {
            override fun onResponse(
                call: Call<BusStopResponse>,
                response: Response<BusStopResponse>
            ) {
                if (response.isSuccessful) {
                    callback?.onSuccess(response.body()!!)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                    callback?.onFailure("Falha na requisição")
                }
            }

            override fun onFailure(call: Call<BusStopResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                callback?.onFailure("Falha na requisição")
            }
        })
    }
}