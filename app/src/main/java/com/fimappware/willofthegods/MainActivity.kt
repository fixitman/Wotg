package com.fimappware.willofthegods

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fimappware.willofthegods.data.AppDb

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(), FragmentChangeListener {

    private lateinit var vm: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val db = AppDb.getInstance(this)
        val factory = GroupViewModel.Factory(db)
        Log.d(TAG, "onCreate: initializing vm")
        vm = ViewModelProvider(this, factory ).get(GroupViewModel::class.java)
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
                .replace(R.id.nav_host_fragment, frag, frag.toString())
                .addToBackStack(frag.toString())
                .commit()
    }


    //added a comment
}