package com.example.theemiclub.ui.slideshow.data.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel

class CommonViewModelFactory(private val repository: AuthRepository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(AuthenticationViewModel::class.java) -> AuthenticationViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}