package com.giannig.tandemlite

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Extends the [ComponentActivity] where I inject the viewModel
 */
open class TandemComponentActivity: ComponentActivity(){
    fun <T: ViewModel>createViewModel(viewModelClass: Class<T>): T {
        val dao = (application as TandemApplication).dao
        return ViewModelProvider(this, ViewModelFactory(dao)).get(viewModelClass)
    }
}