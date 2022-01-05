package com.example.kraftyhomes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.kraftyhomes.databinding.ActivityMainBinding
import com.example.kraftyhomes.fragments.DashboardFragment
import com.example.kraftyhomes.fragments.HomeFragment
import com.example.kraftyhomes.fragments.LoginFragment
import com.example.kraftyhomes.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            it.isChecked = true

            when(it.itemId){
                R.id.home -> replaceFragment(HomeFragment(), it.title.toString())
                R.id.dashboard -> replaceFragment(DashboardFragment(), it.title.toString())
                R.id.sync -> Toast.makeText(this, "Clicked Sync", Toast.LENGTH_SHORT).show()
                R.id.trash -> Toast.makeText(this, "Clicked Trash", Toast.LENGTH_SHORT).show()
                R.id.settings -> replaceFragment(SettingsFragment(), it.title.toString())
                R.id.login -> replaceFragment(LoginFragment(), it.title.toString())
                R.id.share -> Toast.makeText(this, "Clicked Share", Toast.LENGTH_SHORT).show()
                R.id.review -> Toast.makeText(this, "Clicked Review", Toast.LENGTH_SHORT).show()

            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment, title: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}