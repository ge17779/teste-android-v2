package br.com.busdataapplication.callbacks

interface CompletionCallback {
    fun onSuccess(any: Any)
    fun onFailure(error: String)
}