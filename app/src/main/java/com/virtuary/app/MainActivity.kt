package com.virtuary.app

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.virtuary.app.firebase.StorageRepository
import com.virtuary.app.util.GlideApp
import com.virtuary.app.util.hideKeyboard
import com.virtuary.app.util.wrap
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

/**
 * Creates an Activity that hosts all of the fragments in the app
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var preferences: SharedPreferences
    private val mainActivityViewModel by viewModels<MainActivityViewModel>()
    internal lateinit var auth: FirebaseAuth
    internal val storageRepository = StorageRepository()

    companion object {
        var defaultSysLocale: Locale = Locale("en")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        defaultSysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            Resources.getSystem().configuration.locale
        }
        auth = FirebaseAuth.getInstance()
        preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)

        navController = findNavController(R.id.nav_host_fragment)
        // Specify set of top level root page to show burger menu
        appBarConfiguration =
            AppBarConfiguration(setOf(R.id.homeFragment, R.id.familyFragment), drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Listener for drawer items
        nav_view.setNavigationItemSelectedListener(this)

        // Load drawer header data
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
        val userName = headerView.findViewById<TextView>(R.id.drawer_user_name)
        val userPicture = headerView.findViewById<ImageView>(R.id.drawer_profile_picture)
        setDrawerData(userName, userPicture)

        auth.addAuthStateListener {
            if (it.currentUser != null) {
                setDrawerData(userName, userPicture)
                mainActivityViewModel.currentUser = it.currentUser!!.uid
            }
        }

        if (auth.currentUser?.displayName != null) {
            mainActivityViewModel.name.value = auth.currentUser?.displayName
        }

        mainActivityViewModel.name.observe(this, Observer {
            userName.text = it
        })

        mainActivityViewModel.imageUploaded.observe(this, Observer {
            if (it != null) {
                GlideApp.with(applicationContext)
                    .load(storageRepository.getImage(it))
                    .circleCrop().into(userPicture)
            }
        })

        // https://stackoverflow.com/questions/32806735/refresh-header-in-navigation-drawer/35952939#35952939
        drawerToggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            R.string.nav_app_bar_open_drawer_description,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(drawerToggle)
    }

    private fun setDrawerData(
        userName: TextView,
        userPicture: ImageView
    ) {
        userName.text = auth.currentUser?.displayName
        if (auth.currentUser?.photoUrl == null) {
            userPicture.setImageDrawable(getDrawable(R.drawable.ic_no_image))
        } else {
            GlideApp.with(applicationContext)
                .load(storageRepository.getImage(auth.currentUser?.photoUrl.toString()))
                .circleCrop()
                .into(userPicture)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        hideKeyboard()
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
        if(item.itemId == android.R.id.home) {
            drawerToggle.syncState()
        }

        // Close drawer manually since super.onOptionsItemSelected doesn't close drawer when it's opened
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawerToggle.onOptionsItemSelected(item)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawerToggle.syncState()
        }

        super.onPostCreate(savedInstanceState)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
        defaultSysLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            newConfig.locales[0]
        } else {
            @Suppress("DEPRECATION")
            newConfig.locale
        }
        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val themePref = preferences.getString("theme", "")?.toIntOrNull()
            ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        if (themePref == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            if (currentNightMode == Configuration.UI_MODE_NIGHT_NO) {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_NO
            } else {
                delegate.localNightMode = AppCompatDelegate.MODE_NIGHT_YES
            }
        }
        this.recreate()
    }

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener()
        { sharedPreferences, key ->
            when (key) {
                "theme" -> delegate.localNightMode =
                    sharedPreferences.getString(key, "")?.toIntOrNull() ?: -1
            }
            this.recreate()
        }

    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase)
        val newLanguage = sharedPreferences.getString("language", "sys")!!
        val theme = sharedPreferences.getString("theme", "-1")?.toIntOrNull() ?: -1
        val configLocale = if (newLanguage == "sys") {
            defaultSysLocale
        } else {
            Locale(newLanguage)
        }

        super.attachBaseContext(ContextWrapper(newBase).wrap(configLocale.language, theme))
    }

    override fun onResume() {
        preferences.registerOnSharedPreferenceChangeListener(listener)
        super.onResume()
    }

    override fun onPause() {
        preferences.unregisterOnSharedPreferenceChangeListener(listener)
        super.onPause()
    }

    // Function to set the label in the action bar
    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

    // Function for drawer item navigation
    private fun displayScreen(itemId: Int) {
        when (itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.action_global_homeFragment)
            }

            R.id.nav_family -> {
                navController.navigate(R.id.action_global_familyFragment)
            }

            R.id.nav_about -> {
                navController.navigate(R.id.action_global_aboutFragment)
            }

            R.id.nav_edit_profile -> {
                navController.navigate(R.id.action_global_manageAccount)
            }

            R.id.nav_settings -> {
                navController.navigate(R.id.action_global_editPreferencesFragment)
            }

            R.id.logout -> {
                auth.signOut()
                navController.navigate(R.id.action_global_landingFragment)
            }
        }
    }
}
