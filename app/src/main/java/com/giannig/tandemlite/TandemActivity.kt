package com.giannig.tandemlite

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class TandemActivity: AppCompatActivity(){
    fun <T: ViewModel>createViewModel(viewModelClass: Class<T>): T {
        val dao = (application as TandemApplication).dao
        return ViewModelProvider(this, ViewModelFactory(dao)).get(viewModelClass)
    }
}