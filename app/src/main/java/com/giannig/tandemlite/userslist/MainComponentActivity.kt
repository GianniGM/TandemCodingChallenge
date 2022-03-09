package com.giannig.tandemlite.userslist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.giannig.tandemlite.R
import com.giannig.tandemlite.TandemComponentActivity
import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.ui.theme.MyApplicationTheme
import com.giannig.tandemlite.ui.theme.Purple700
import kotlinx.coroutines.Job

//todo check readme
//todo remove unused resource
//todo unit test
//todo test error handling
class MainComponentActivity : TandemComponentActivity() {

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
    val usersItems: LazyPagingItems<TandemUser> = tandemViewModel
        .getTandemUsersList
        .collectAsLazyPagingItems()

    val onLikeUser = { tandemUser: TandemUser ->
        tandemViewModel.likeUser(tandemUser, !tandemUser.liked)
    }

    Column {
        TopAppBar(
            backgroundColor = Purple700,
            title = {
                Text(color = Color.White, text = stringResource(R.string.community_string))
            },
        )
        TandemUserList(usersItems, onLikeUser)
    }
}

@Composable
fun TandemUserList(usersItems: LazyPagingItems<TandemUser>, onLikeUser: (TandemUser) -> Job) {

    if (usersItems.itemCount == 0) {
        if(usersItems.loadState.refresh is LoadState.Loading){
            LoadingScreen()
        }else {
            RefreshButton(usersItems)
        }
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
                    onLikeUser(user)
                    usersItems.refresh()
                }
            }
        }
    }
    usersItems.run {
        if (loadState.append is LoadState.Loading) {
            LoadingScreen()
        }
    }
    usersItems.run {
        when (val state = loadState.refresh) {
            is LoadState.NotLoading,
            LoadState.Loading -> LoadingScreen()
            is LoadState.Error -> {
                RefreshButton(usersItems)
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
private fun LoadingScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            color = Purple700,
            modifier = Modifier.padding(16.dp)
        )
    }
}






