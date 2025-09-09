package com.example.bosmobilefinance.ui.slideshow.ui.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bos.payment.appName.ui.view.travel.airfragment.PersonalInfo
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.BureauScore
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAIS
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CAPSSummery
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.cibilreportsfragment.CreditAccounts

private const val NUM_TABS = 5

class FlightViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm,lifecycle) {


    override fun getItemCount(): Int {
        return NUM_TABS
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PersonalInfo()
            1-> CAIS()
            2 -> CreditAccounts()
            3->BureauScore()
            4->CAPSSummery()
            else -> PersonalInfo()
        }



    }


}