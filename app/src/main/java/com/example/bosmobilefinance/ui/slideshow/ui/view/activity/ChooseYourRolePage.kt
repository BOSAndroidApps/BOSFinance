package com.example.bosmobilefinance.ui.slideshow.ui.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bosmobilefinance.R
import com.example.bosmobilefinance.databinding.ActivityChooseYourRolePageBinding
import com.example.bosmobilefinance.ui.slideshow.activity.LoginPage
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.Customer
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.Retailer
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.loginType

class ChooseYourRolePage : AppCompatActivity() {
   lateinit var binding : ActivityChooseYourRolePageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseYourRolePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

        setOnClickListner()

    }

    fun setOnClickListner(){

        binding.retailerid.setOnClickListener{
            loginType = Retailer
            binding.retailerid.strokeColor = resources.getColor(R.color.darkpurple)
            binding.customerid.strokeColor = resources.getColor(R.color.white)
            val mainIntent = Intent(this@ChooseYourRolePage, LoginPage::class.java)
            startActivity(mainIntent)
        }

        binding.customerid.setOnClickListener{
            loginType = Customer
            binding.customerid.strokeColor = resources.getColor(R.color.darkpurple)
            binding.retailerid.strokeColor = resources.getColor(R.color.white)
            val mainIntent = Intent(this@ChooseYourRolePage, LoginPage::class.java)
            startActivity(mainIntent)
        }

    }


}