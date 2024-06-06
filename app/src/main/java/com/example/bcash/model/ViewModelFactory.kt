package com.example.bcash.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bcash.service.repository.Repository
import com.example.bcash.ui.dashboard.DashboardViewModel
import com.example.bcash.ui.favorite.FavoriteViewModel
import com.example.bcash.ui.inventory.InventoryViewModel
import com.example.bcash.ui.login.LoginViewModel
import com.example.bcash.ui.register.RegisterViewModel
import com.example.bcash.utils.Injection.Injection

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> {
                DashboardViewModel(repository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }

            modelClass.isAssignableFrom(InventoryViewModel::class.java) -> {
                InventoryViewModel(repository) as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
        }
    }
}