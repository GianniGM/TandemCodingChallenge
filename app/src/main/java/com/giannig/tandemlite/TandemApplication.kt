package com.giannig.tandemlite

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.giannig.tandemlite.api.db.DatabaseBuilder
import com.giannig.tandemlite.api.db.TandemDao

/**
 * Application where injects the viewModel into the viewModel provider
 */
class TandemApplication: Application() {

    var viewModelFactory: ViewModelProvider.AndroidViewModelFactory? = null

    lateinit var dao: TandemDao

    override fun onCreate() {
        super.onCreate()

        dao = DatabaseBuilder.getInstance(this).TandemDao()

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
    }

}