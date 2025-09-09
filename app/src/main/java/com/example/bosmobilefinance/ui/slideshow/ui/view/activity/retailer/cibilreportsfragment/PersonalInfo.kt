package com.bos.payment.appName.ui.view.travel.airfragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bosmobilefinance.databinding.FragmentUpcomingAirBinding

class UpcomingAir : Fragment() {

    lateinit var binding : FragmentUpcomingAirBinding

    companion object{

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentUpcomingAirBinding.inflate(inflater, container, false)


        return binding.root
    }



}