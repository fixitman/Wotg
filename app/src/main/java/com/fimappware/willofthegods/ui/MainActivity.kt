package com.fimappware.willofthegods.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.fimappware.willofthegods.R
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navListener : NavController.OnDestinationChangedListener
    private lateinit var prefs : SharedPreferences
    private val tabs = setOf(R.id.numberFragment,R.id.groupListFragment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        prefs = getPreferences(Context.MODE_PRIVATE)

        //set up navigation on appBar and bottomNav
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(tabs)

        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        graph.startDestination = prefs.getInt(PREF_START_TAB,R.id.numberFragment)
        navController.graph = graph

        navListener = NavController.OnDestinationChangedListener{_, destination, _ ->
            if(destination.id in tabs){
                prefs.edit().putInt(PREF_START_TAB,destination.id).apply()
            }
        }


        setupActionBarWithNavController(navController, appBarConfiguration)
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(navListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(navListener)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(tabs.contains(navController.currentDestination?.id)) { //if we're on a top level fragment
            finishAfterTransition()
        }else{
            super.onBackPressed()
        }
    }

    companion object{
        private const val PREF_START_TAB = "StartTab"
    }

}