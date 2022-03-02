package com.giannig.tandemlite

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class TandemActivity: ComponentActivity(){
    fun <T: ViewModel>createViewModel(viewModelClass: Class<T>): T {
        val dao = (application as TandemApplication).dao
        return ViewModelProvider(this, ViewModelFactory(dao)).get(viewModelClass)
    }
}