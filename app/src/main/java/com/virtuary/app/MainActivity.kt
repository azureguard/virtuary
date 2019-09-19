package com.virtuary.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
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
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = findNavController(R.id.nav_host_fragment)
        // Specify set of top level root page to show burger menu
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.familyFragment), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Listener for drawer items
        nav_view.setNavigationItemSelectedListener(this)

        // TODO: put the actual name and profile image to the drawer later
        // https://stackoverflow.com/questions/32806735/refresh-header-in-navigation-drawer/35952939#35952939
        drawerToggle = object : ActionBarDrawerToggle(
            this, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {
//            override fun onDrawerOpened(drawerView: View) {
//                super.onDrawerOpened(drawerView)
//                drawer_user_name.text = "Friedrich"
//            }
        }
        drawer_layout.addDrawerListener(drawerToggle)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawer_layout.closeDrawer(GravityCompat.START)
        displayScreen(item.itemId)
        return true
    }

    // Handle closing the drawer if back button is pressed
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    // Some required methods to make the action bar runs smoothly
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Sync the animation and the icon of the up or hamburger button
        drawerToggle.syncState()

        // TODO: May have better Implementation, but for now this works fine
        // Close drawer manually since super.onOptionsItemSelected doesn't close drawer when it's opened
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawerToggle.onOptionsItemSelected(item)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        drawerToggle.syncState()
        super.onPostCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    // Function to set the label in the action bar
    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    // Function for drawer item navigation
    private fun displayScreen(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_homeFragment)
            }

            R.id.nav_family -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_familyFragment)
            }

            R.id.nav_about -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_aboutFragment)
            }

            R.id.nav_edit_profile -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_manageAccount)
            }

            R.id.nav_settings -> {
                TODO() // Implement settings fragment and connect it here
            }
        }
    }
}
