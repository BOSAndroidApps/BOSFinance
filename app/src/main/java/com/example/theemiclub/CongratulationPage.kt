package com.example.theemiclub

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.theemiclub.databinding.ActivityCongratulationPageBinding
import com.example.theemiclub.ui.slideshow.activity.DashBoard

class CongratulationPage : AppCompatActivity() {
    lateinit var binding:ActivityCongratulationPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityCongratulationPageBinding.inflate(layoutInflater)
         setContentView(binding.root)

         ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.setPadding(0, statusBarInsets.top, 0, statusBarInsets.bottom)
            WindowInsetsCompat.CONSUMED
        }
        setOnClickListner()

    }

    fun setOnClickListner(){

        binding.nextlayout.setOnClickListener {
            startActivity(Intent(this@CongratulationPage,DashBoard::class.java))
            finish()
        }

    }
    
}