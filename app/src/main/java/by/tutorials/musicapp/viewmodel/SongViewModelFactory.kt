package by.tutorials.musicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.tutorials.musicapp.di.ServiceLocator

class SongViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(
                repo = ServiceLocator.provideRepository()
            ) as T
        }
        throw IllegalArgumentException("Error ViewModel class")
    }
}