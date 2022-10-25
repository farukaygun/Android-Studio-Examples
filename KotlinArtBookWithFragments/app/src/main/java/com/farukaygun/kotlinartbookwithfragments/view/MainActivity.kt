package com.farukaygun.kotlinartbookwithfragments.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.farukaygun.kotlinartbookwithfragments.R
import com.farukaygun.kotlinartbookwithfragments.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createMenu()

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        if (navHost != null) {
            navigationController = navHost.findNavController()
            NavigationUI.setupActionBarWithNavController(this@MainActivity, navigationController)
        }
    }

    private fun createMenu() {
        // Add menu items without overriding methods in the Activity
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.itemNewArt) {
                    val action = ArtListFragmentDirections.actionArtListFragmentToArtDetailsFragment(0,"new")
                    Navigation.findNavController(this@MainActivity, R.id.fragmentContainerView).navigate(action)
                }
                return true
            }
        })
    }
}