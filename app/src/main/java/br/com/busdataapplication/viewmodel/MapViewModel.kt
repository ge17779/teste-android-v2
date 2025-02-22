package br.com.busdataapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.busdataapplication.callbacks.CompletionCallback
import br.com.busdataapplication.network.NetworkConnection
import br.com.busdataapplication.network.NetworkConnection.Companion.token
import br.com.busdataapplication.network.OlhoVivoService
import br.com.busdataapplication.network.responses.AllVehiclesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val service = NetworkConnection.retrofit().create(OlhoVivoService::class.java)
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun auth(callback: CompletionCallback){
        service.authenticate(token).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    callback.onSuccess(response.body()!!)
                    Log.d(TAG, "onResponse: ${response.body()}")
                } else {
                    callback.onFailure("Falha na autenticação")
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                callback.onFailure("Falha na autenticação")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getVehiclesPosition(callback: CompletionCallback){
        service.getVehiclesPosition().enqueue(object : Callback<AllVehiclesResponse> {
            override fun onResponse(call: Call<AllVehiclesResponse>, response: Response<AllVehiclesResponse>) {
                if (response.isSuccessful){
                    val body = response.body() as AllVehiclesResponse
                    callback.onSuccess(body.lines)
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AllVehiclesResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}