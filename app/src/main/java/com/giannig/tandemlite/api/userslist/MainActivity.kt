package com.giannig.tandemlite.api.userslist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.giannig.tandemlite.TandemActivity
import com.giannig.tandemlite.ui.theme.MyApplicationTheme

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
        setContent {
            val usersList = viewModel.getUsersMutableState.value
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting(usersList.toString())
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}