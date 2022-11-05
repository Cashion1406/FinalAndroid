package com.example.finalandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.trip_row.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        //setupActionBarWithNavController(findNavController(R.id.fragmentContainerView))


        btm_navigate.setupWithNavController(findNavController(R.id.fragmentContainerView))


    }


    override fun onSupportNavigateUp(): Boolean {

        return findNavController(R.id.fragmentContainerView).navigateUp() || super.onSupportNavigateUp()
    }

}