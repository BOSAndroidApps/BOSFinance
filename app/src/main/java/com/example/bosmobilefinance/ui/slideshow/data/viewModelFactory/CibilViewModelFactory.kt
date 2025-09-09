package com.example.bosmobilefinance.ui.slideshow.data.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.repository.CibilRepository
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.CibilViewModel

class CibilViewModelFactory (private val repository: CibilRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(CibilViewModel::class.java) -> CibilViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}