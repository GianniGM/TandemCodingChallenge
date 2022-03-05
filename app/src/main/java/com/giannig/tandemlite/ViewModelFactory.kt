package com.giannig.tandemlite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.giannig.tandemlite.api.db.TandemDao
import com.giannig.tandemlite.api.repositories.TandemRepository
import com.giannig.tandemlite.userslist.TandemViewModel
import java.lang.IllegalArgumentException

/**
 * Factory Implementation for View Models,
 * it injects database into MainViewModel
 *
 * NB: remove after dependency injection
 */
class ViewModelFactory(private val dao: TandemDao) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TandemViewModel::class.java)){
            return TandemViewModel(TandemRepository(dao)) as T
        }
        throw IllegalArgumentException("Unknown Model class")
    }
}