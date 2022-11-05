package com.example.finalandroid.model

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    //Shared mode
    val PRIVATE_MODE = 0


    private val pref_name= "SharedPrefernce"
    private val logged = "is_login"



    val pref: SharedPreferences = context.getSharedPreferences(pref_name, PRIVATE_MODE)
}