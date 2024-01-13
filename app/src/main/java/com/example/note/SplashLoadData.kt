package com.example.note

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.note.ViewModel.FollowDataFromRoom
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

class SplashLoadData : AppCompatActivity() {
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_splash_load_data)
        checkOutDataAndSplashScreen()
    }
    private fun checkOutDataAndSplashScreen(){
        changeDisplayToHome()
    }
    private fun changeDisplayToHome(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}