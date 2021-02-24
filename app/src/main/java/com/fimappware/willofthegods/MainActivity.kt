package com.fimappware.willofthegods

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fimappware.willofthegods.data.AppDb

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() , FragmentChangeListener {
    private val vm : GroupViewModel by viewModels { GroupViewModel.Factory(AppDb.getInstance(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun changeFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment,frag,frag.toString())
            .addToBackStack(frag.toString())
            .commit()
    }
}