package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.bosmobilefinance.R
import com.example.bosmobilefinance.databinding.ActivityCibilReportsDetailsPageBinding
import com.example.bosmobilefinance.ui.slideshow.ui.view.adapter.FlightViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CibilReportsDetailsPage : AppCompatActivity() {
    lateinit var binding : ActivityCibilReportsDetailsPageBinding
    val statusArray = listOf("Personal", "CAIS", "Account","Bureau","CAPS")
    lateinit var  viewPager: ViewPager2
    lateinit var  tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCibilReportsDetailsPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom)

            WindowInsetsCompat.CONSUMED
        }

        setView()
        binding.back.setOnClickListener {
            finish()
        }

    }


    private fun setView(){

        viewPager = binding.viewPager
        tabLayout = binding.tablayout

        val adapter = FlightViewPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.isUserInputEnabled = true
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabView =  LayoutInflater.from(tabLayout.context).inflate(R.layout.tab_title, null)
            val text=tabView.findViewById<TextView>(R.id.tabText)
            text.text = statusArray[position]
            tab.customView= tabView
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val textView = tab.customView as? TextView
                textView?.isSelected = true // triggers ColorStateList
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val textView = tab.customView as? TextView
                textView?.isSelected = false
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        // Also mark the initially selected tab (0)
        (tabLayout.getTabAt(tabLayout.selectedTabPosition)?.customView as? TextView)?.isSelected = true

    }



}