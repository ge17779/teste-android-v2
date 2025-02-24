package br.com.busdataapplication.callbacks

import br.com.busdataapplication.models.BusLine

interface InfoCallback {
    fun onLinesCallback(line: BusLine)
}