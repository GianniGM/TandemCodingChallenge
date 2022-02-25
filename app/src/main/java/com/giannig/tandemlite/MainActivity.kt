package com.giannig.tandemlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

//todo check deps version
//todo check readme
//todo remove unused resource
//todo unit test
//todo paging
//todo room
//todo test error handling

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory()).get(TandemViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}