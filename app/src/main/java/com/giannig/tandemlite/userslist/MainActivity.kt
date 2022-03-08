package com.giannig.tandemlite.userslist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.giannig.tandemlite.R
import com.giannig.tandemlite.TandemActivity
import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.ui.theme.MyApplicationTheme

//todo check readme
//todo remove unused resource
//todo unit test
//todo paging
//todo test error handling
class MainActivity : TandemActivity() {

    private val viewModel by lazy {
        createViewModel(TandemViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainPage(viewModel)
                }
            }
        }
    }
}


@Composable
fun MainPage(tandemViewModel: TandemViewModel) {
    Column {
        TopAppBar(
            backgroundColor = colorResource(id = android.R.color.white),
            elevation = 16.dp,
            title = {
                Text(text = stringResource(R.string.community_string))
            },
        )
        TandemUserList(tandemViewModel)
    }
}

@Composable
fun TandemUserList(tandemViewModel: TandemViewModel) {
    val usersItems: LazyPagingItems<TandemUser> = tandemViewModel
        .getTandemUsersList
        .collectAsLazyPagingItems()

    if(usersItems.itemCount == 0){
        RefreshButton(usersItems)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = usersItems) { user ->
            user?.let {
                ProfileCardComposable(user) { user ->
                    tandemViewModel.likeUser(user, !user.liked)
                }
            }
        }
    }
    usersItems.run {
        when (val state = loadState.refresh) {
            LoadState.Loading -> SetUpLoadingView()
            is LoadState.NotLoading -> {/* todo */}
            is LoadState.Error -> {
                val lazyList = tandemViewModel.getTandemUsersList.collectAsLazyPagingItems()
                RefreshButton(lazyList)
                Toast.makeText(
                    LocalContext.current,
                    state.toString(), Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

@Composable
private fun RefreshButton(usersItems: LazyPagingItems<TandemUser>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(23.dp)
    ) {
        Button(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center),
            onClick = {
                usersItems.refresh()
            }) {
            Text(text = stringResource(R.string.refresh_text))
        }
    }
}

@Composable
private fun SetUpLoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Color.LightGray,
            modifier = Modifier.padding(16.dp)
        )
    }
}






