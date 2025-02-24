package br.com.busdataapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.busdataapplication.R
import br.com.busdataapplication.adapters.LinesAdapter.Companion.getLineColor
import br.com.busdataapplication.adapters.LinesAdapter.Companion.getNumericSign
import br.com.busdataapplication.adapters.LinesAdapter.Companion.getOriginToDestinationBySense
import br.com.busdataapplication.models.BusStop
import br.com.busdataapplication.models.BusLine
import br.com.busdataapplication.network.responses.BusStopResponse
import br.com.busdataapplication.utils.BitmapUtils
import br.com.busdataapplication.viewmodel.MainViewModel
import br.com.busdataapplication.viewmodel.MainViewModel.Companion.getEstimatedArrival
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class BusStopFragment: Fragment(), OnMapReadyCallback {

    private val viewModel = MainViewModel()
    private lateinit var mMap: GoogleMap
    private lateinit var busIcon: BitmapDescriptor
    private lateinit var busStopIcon: BitmapDescriptor
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var refreshBtn: ImageButton
    private lateinit var busLineInfoLayout: LinearLayout
    private lateinit var arrivalInfoLayout: LinearLayout
    private var stopBusName: TextView? = null
    private var estimatedArrivalTime: TextView? = null

    companion object {
        private const val TAG = "StopBusFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stopbus, container, false)

        initViews(view)
        initListeners()
        return view
    }

    private fun initViews(view: View){
        refreshBtn = view.findViewById(R.id.refresh_button)
        busLineInfoLayout = view.findViewById(R.id.bus_line_info_layout)
        stopBusName = view.findViewById(R.id.stop_bus_name)
        arrivalInfoLayout = view.findViewById(R.id.estimated_layout)
        estimatedArrivalTime = view.findViewById(R.id.estimated_arrival_time)
        busIcon = BitmapDescriptorFactory.fromBitmap(
            BitmapUtils.getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_bus))
        busStopIcon = BitmapDescriptorFactory.fromBitmap(
            BitmapUtils.getBitmapFromVectorDrawable(requireContext(), R.drawable.ic_bus_stop_marker))
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initListeners(){
        refreshBtn.setOnClickListener {
            getLineFromBundle()?.let {
                viewModel.getBusesAndBusStopsByLine(it)
            }
        }
        viewModel.busLine.observe(viewLifecycleOwner) {
            showBusLineInfo(getLineFromBundle())
        }
        viewModel.busStop.observe(viewLifecycleOwner) {
            showEstimatedByBusStopInfo(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener { marker ->
            marker.tag?.let {
                val busStopId = it as Int
                viewModel.getEstimatedByBusStop(busStopId)
            }
            false
        }
        loadLineInfo()
    }

    private fun loadLineInfo() {
        val line = getLineFromBundle()
        line?.let { busLine ->
            addMarkers(busLine)
            busLine.buses
                ?.takeIf { it.isNotEmpty() }
                ?.first()?.let { bus ->
                    moveCamera(LatLng(bus.latitude!!, bus.longitude!!))
                } ?: moveCamera()
        } ?: moveCamera()
    }

    private fun getLineFromBundle(): BusLine?{
        val lineJson = arguments?.getString("line")
        if (lineJson != null) {
            val line = Gson().fromJson(lineJson, BusLine::class.java)
            return line
        }
        return null
    }

    fun getBusStopsNearHere(nearHere: String){
        viewModel.getBusStopsByParam(nearHere)
    }

    private fun showBusLineInfo(busLine: BusLine?){
        busLine?.let {
            busLineInfoLayout.visibility = VISIBLE
            busLineInfoLayout.findViewById<View>(R.id.bus_line_color).setBackgroundColor(getLineColor(it.secondNumericSign))
            busLineInfoLayout.findViewById<TextView>(R.id.bus_destination_code).text = getNumericSign(it)
            busLineInfoLayout.findViewById<TextView>(R.id.destination).text = getOriginToDestinationBySense(it)
        }
    }

    private fun showEstimatedByBusStopInfo(info: BusStopResponse){
        arrivalInfoLayout.visibility = VISIBLE
        val busStop = info.busStop
        stopBusName?.text = busStop?.name
        val estimated = getEstimatedArrival(info, getLineFromBundle())
        estimatedArrivalTime?.text = estimated
    }

    private fun moveCamera(location: LatLng = LatLng(-23.5505, -46.6333)){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13f))
    }

    fun addMarkers(itemList: List<*>){
        if (itemList.isEmpty()) return

        if (itemList[0] is BusStop){
            val busStops = itemList as List<BusStop>
            busStops.forEach { busStop ->
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(busStop.latitude!!, busStop.longitude!!))
                        .title(busStop.name)
                        .icon(busStopIcon))
            }
            moveCamera(LatLng(busStops.first().latitude!!, busStops.first().longitude!!))
        } else if (itemList[0] is BusLine){
            val busLines = itemList as List<BusLine>
            busLines.forEach{ line ->
                line.buses?.forEach { bus ->
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(bus.latitude!!, bus.longitude!!))
                            .title(getOriginToDestinationBySense(line))
                            .icon(busIcon))
                }
                line.busStops?.forEach {
                    val marker = mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.latitude!!, it.longitude!!))
                            .title(it.name)
                            .icon(busStopIcon))
                    marker?.tag = it.id
                }
            }
        }
    }

    fun addMarkers(busLine: BusLine){
        mMap.clear()
        addMarkers(listOf(busLine))
    }
}