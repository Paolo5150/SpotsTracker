package com.pf.spotstracker

import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.pf.spotstracker.databinding.ActivityMainBinding
import com.pf.spotstracker.utils.LogDebug
import com.pf.spotstracker.utils.alertDialogOk


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var LOCATION_ACCESS_PERMISSION_CODE = 0

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    lateinit var placesClient: PlacesClient

    companion object {
        private var app : MainActivity? = null;
         fun getMainActivity(): MainActivity? {
             return app;
         }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainActivity.app = this
        checkPermissions()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navigationView: NavigationView = binding.navView
        val drawer = binding.drawerLayout

        val bottomSheet = binding.bottomSheet.bottomSheet
        var bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_DRAGGING

        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)

        Places.initialize(applicationContext, getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            // Straight up quit if permission is denied
            0 -> if(grantResults[0] != PackageManager.PERMISSION_GRANTED) finish()
        }
    }

    private fun checkPermissions()
    {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            //Maybe explain
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
            {
                alertDialogOk("GPS Required", "The application requires the GPS.",
                        DialogInterface.OnClickListener { dialog, which ->
                            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_ACCESS_PERMISSION_CODE) })
            }
            else
            {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_ACCESS_PERMISSION_CODE)
            }
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),LOCATION_ACCESS_PERMISSION_CODE)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_test -> navController.navigate(R.id.firstFragment)
        }
        binding.drawerLayout.close()
        return true
    }

}