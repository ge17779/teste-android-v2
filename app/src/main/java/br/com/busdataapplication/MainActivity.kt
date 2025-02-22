package br.com.busdataapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import br.com.busdataapplication.callbacks.CompletionCallback
import br.com.busdataapplication.models.Line
import br.com.busdataapplication.utils.BitmapUtils
import br.com.busdataapplication.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity(), CompletionCallback, OnMapReadyCallback {

    private val viewModel = MapViewModel()
    private lateinit var mMap: GoogleMap
    private val positionButton: Button by lazy { findViewById(R.id.position_button) }
    private val markerIcon: BitmapDescriptor by lazy { BitmapDescriptorFactory.fromBitmap(
        BitmapUtils.getBitmapFromVectorDrawable(this, R.drawable.ic_bus)) }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen: SplashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value == true }

        initListeners()
        viewModel.auth(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initListeners() {
        positionButton.setOnClickListener {
            loadVehicles()
        }
    }

    private val fetchLinesCallback : CompletionCallback = object : CompletionCallback {
        override fun onSuccess(any: Any) {
            if (any is List<*>){
                mMap.clear()
                addMarkers(any as List<Line>)
            }
        }

        override fun onFailure(error: String) {
            Log.e(TAG, "onFailure: $error")
        }
    }

    private fun loadVehicles(){
        viewModel.getVehiclesPosition(fetchLinesCallback)
    }

    fun moveCamera(){
        // Exemplo: adiciona um marcador no centro de São Paulo
        val saoPaulo = LatLng(-23.5505, -46.6333)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 12f))
    }

    fun addMarkers(lines: List<Line>){
        lines.forEach{ line ->
            line.vehicles.forEach { vehicle ->
                mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(vehicle.latitude!!, vehicle.longitude!!))
                        .title("${line.busDestinationSign} - ${line.busDestination}")
                        .icon(markerIcon))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        moveCamera()
    }

    override fun onSuccess(any: Any) {

    }

    override fun onFailure(error: String) {

    }
}