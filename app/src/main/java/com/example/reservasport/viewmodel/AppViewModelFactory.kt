package com.example.reservasport.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.reservasport.data.ReservaRepository

class AppViewModelFactory(private val repository: ReservaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReservaViewModel(repository) as T
    }
}