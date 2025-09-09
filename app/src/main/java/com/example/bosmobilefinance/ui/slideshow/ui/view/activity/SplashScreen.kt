package com.example.bosmobilefinance.ui.slideshow.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.bosmobilefinance.databinding.SplashMainBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.localdb.SharedPreference
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.ChooseYourRolePage


class SplashScreen : AppCompatActivity() {
    lateinit var binding : SplashMainBinding
    lateinit var preference : SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        super.onCreate(savedInstanceState)

        binding = SplashMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preference = SharedPreference(this)

        Handler(Looper.getMainLooper()).postDelayed({
            // Code to run after delay
            if(preference.getBoolanValue(ConstantClass.LoggedIn,false)){
                val mainIntent = Intent(this@SplashScreen, DashBoard::class.java)
                startActivity(mainIntent)
                finish()
            }
            else{
                val mainIntent = Intent(this@SplashScreen, ChooseYourRolePage::class.java) //ChooseYourRolePage
                startActivity(mainIntent)
                finish()
            }


        }, 3000)

    }

}