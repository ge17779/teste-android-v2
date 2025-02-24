package br.com.busdataapplication.callbacks

interface ResultCallback<T> {
    fun onResult(result: T)
}