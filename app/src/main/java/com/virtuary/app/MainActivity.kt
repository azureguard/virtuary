package com.virtuary.app

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Creates an Activity that hosts all of the fragments in the app
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = findNavController(R.id.nav_host_fragment)
        // Specify set of top level root page to show burger menu
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment),drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displayScreen(item.itemId)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Function for drawer item navigation
    private fun displayScreen(itemId : Int) {
        when(itemId){
            R.id.nav_home -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
            }

            R.id.nav_family -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.familyFragment)
            }

            R.id.nav_about -> {
                TODO() // Implement about fragment and connect it here
            }

            R.id.nav_edit_profile -> {
                TODO() // Implement edit profile fragment and connect it here
            }

            R.id.nav_settings -> {
                TODO() // Implement settings fragment and connect it here
            }
        }
    }
}
