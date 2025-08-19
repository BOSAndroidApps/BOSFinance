package com.example.theemiclub.ui.slideshow.ui.view.activity.retailer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityEmicalculationBinding

class EMICalculationPage : AppCompatActivity() {
    lateinit var binding : ActivityEmicalculationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityEmicalculationBinding.inflate(layoutInflater)
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
        setDataOnUI()

    }


    fun setDataOnUI(){
        val companyAdapter = ArrayAdapter.createFromResource(this,  R.array.mobile_company_names, R.layout.mobilenamelayout)
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.companyName.adapter = companyAdapter

        // Listen to company selection
        binding.companyName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val modelArrayId = when (position) {
                    0 -> R.array.apple_models
                    1 -> R.array.samsung_models
                    2 -> R.array.oneplus_models
                    3 -> R.array.realme_models
                    else -> null
                }

                modelArrayId?.let {
                    val models = resources.getStringArray(it)
                   // val modelAdapter = ArrayAdapter(this@EMICalculationPage, android.R.layout.simple_spinner_item, models)
                    val modelAdapter = ArrayAdapter.createFromResource(this@EMICalculationPage,it, R.layout.mobilenamelayout)
                    modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.mobilemodelname.adapter = modelAdapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



    }



    fun setOnClickListner(){
        binding.back.setOnClickListener { finish() }

        binding.emicalculationbuttonlayout.setOnClickListener {
            startActivity(Intent(this@EMICalculationPage, EMICalculationDetailsPage::class.java))
        }
    }




}