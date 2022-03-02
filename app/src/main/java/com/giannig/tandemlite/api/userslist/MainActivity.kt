package com.giannig.tandemlite.api.userslist

import android.os.Bundle
import android.widget.TextView
import com.giannig.tandemlite.R
import com.giannig.tandemlite.TandemActivity

//todo check deps version
//todo check readme
//todo remove unused resource
//todo unit test
//todo paging
//todo room
//todo test error handling

class MainActivity : TandemActivity() {

    private val viewModel by lazy {
        createViewModel(TandemViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.getUsersFromApi()
        viewModel.state.observe(this) {
            findViewById<TextView>(R.id.response).text = it.toString()
        }
    }
}