package br.com.busdataapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.busdataapplication.R
import br.com.busdataapplication.adapters.LinesAdapter
import br.com.busdataapplication.callbacks.InfoCallback
import br.com.busdataapplication.viewmodel.MainViewModel

class LinesFragment(private val callback: InfoCallback? = null): Fragment(), AdapterView.OnItemClickListener {

    private val viewModel = MainViewModel()
    private lateinit var recyclerView: RecyclerView
    private val adapter = LinesAdapter(ArrayList(), this)

    companion object {
        private const val TAG = "LinesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lines, container, false)

        initViews(view)
        initListeners()

        val text = arguments?.getString("text")
        text?.let { viewModel.getLinesByParam(it) }

        return view
    }

    private fun initViews(view: View){
        recyclerView = view.findViewById(R.id.lines_list_recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    private fun initListeners(){
        viewModel.busLine.observe(viewLifecycleOwner) {
            callback?.onLinesCallback(it)
        }
        viewModel.busLines.observe(viewLifecycleOwner) {
            adapter.updateLines(it)
        }
    }

    fun getLinesByParam(param: String){
        viewModel.getLinesByParam(param)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val line = adapter.getItem(position)
        viewModel.getBusesAndBusStopsByLine(line)
    }
}