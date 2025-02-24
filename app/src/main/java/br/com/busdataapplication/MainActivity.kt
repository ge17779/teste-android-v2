package br.com.busdataapplication

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import br.com.busdataapplication.callbacks.InfoCallback
import br.com.busdataapplication.fragments.LinesFragment
import br.com.busdataapplication.fragments.BusStopFragment
import br.com.busdataapplication.models.BusLine
import br.com.busdataapplication.viewmodel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), InfoCallback {

    private val viewModel = MainViewModel()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var splashScreen: SplashScreen
    private lateinit var searchView: EditText
    private var text: String = ""
    private var line: BusLine? = null

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initListeners()
        viewModel.auth()

        // Exibe o primeiro fragmento ao abrir o app
        if (savedInstanceState == null) {
            loadFragment(BusStopFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun initViews() {
        searchView = findViewById(R.id.search_lines)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
    }

    private fun initListeners() {
        splashScreen.setKeepOnScreenCondition { viewModel.isLoading.value == true }
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_lines -> {
                    val bundle = Bundle()
                    bundle.putString("text", text)
                    val linesFragment = LinesFragment(this)
                    linesFragment.arguments = bundle
                    loadFragment(linesFragment)
                }
                R.id.nav_stop -> {
                    val bundle = Bundle()
                    bundle.putString("line", Gson().toJson(line))
                    Log.d(TAG, "initListeners: $line")
                    val busStopFragment = BusStopFragment()
                    busStopFragment.arguments = bundle
                    loadFragment(busStopFragment)
                }
            }
            true
        }
        searchView.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                text = searchView.text.toString()
                val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (fragment is LinesFragment){
                    fragment.getLinesByParam(text)
                } else {
                    bottomNavigationView.selectedItemId = R.id.nav_lines
                }
                true
            } else {
                false
            }
        }
    }

    override fun onLinesCallback(line: BusLine) {
        this.line = line
        bottomNavigationView.selectedItemId = R.id.nav_stop
    }
}