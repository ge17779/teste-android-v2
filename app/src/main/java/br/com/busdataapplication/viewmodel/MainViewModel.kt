package br.com.busdataapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.busdataapplication.callbacks.CompletionCallback
import br.com.busdataapplication.models.BusLine
import br.com.busdataapplication.models.BusStop
import br.com.busdataapplication.repository.BusLineRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = BusLineRepository()
    private val _isLoading = MutableLiveData(true)
    private val _line = MutableLiveData<BusLine>()
    private val _busStops = MutableLiveData<List<BusStop>>()
    private val _busLines = MutableLiveData<List<BusLine>>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    val line: LiveData<BusLine> = _line
    val busStops: LiveData<List<BusStop>> = _busStops
    val busLines: LiveData<List<BusLine>> = _busLines

    companion object {
        private const val TAG = "MainViewModel"
    }

    fun auth(){
        repository.authenticate(object : CompletionCallback{
            override fun onSuccess(any: Any) {
                _isLoading.value = false
            }

            override fun onFailure(error: String) {
                // Show error warning
            }
        })
    }

    fun getBusStopsByParam(nearHere: String){
        repository.getBusStopsByParam(nearHere, object : CompletionCallback {
            override fun onSuccess(any: Any) {
                if (any is List<*>) {
                    val busStops = any as List<BusStop>
                    viewModelScope.launch {
                        _busStops.value = busStops
                    }
                }
            }

            override fun onFailure(error: String) {
                // Show error warning
            }
        })
    }

    fun getLinesByParam(param: String){
        repository.getLinesByParam(param, object : CompletionCallback {
            override fun onSuccess(any: Any) {
                if (any is List<*>) {
                    val lines = any as List<BusLine>
                    viewModelScope.launch {
                        _busLines.value = lines
                    }
                }
            }

            override fun onFailure(error: String) {
                // Show error warning
            }
        })
    }

    fun getBusesAndBusStopsByLine(line: BusLine) {
        repository.getBusesAndBusStopsByLine(line, object : CompletionCallback {
            override fun onSuccess(any: Any) {
                if (any is BusLine){
                    viewModelScope.launch {
                        _line.value = any
                    }
                }
            }

            override fun onFailure(error: String) {
                // Show error warning
            }
        })
    }
}