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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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

/**
 * Main activity that shows a list with the users
 */
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
                    val usersItems: LazyPagingItems<TandemUser> = viewModel
                        .getTandemUsersList
                        .collectAsLazyPagingItems()

                    val onLikeUser = { tandemUser: TandemUser ->
                        Log.d("BANANA", "$tandemUser")
                        viewModel.likeUser(tandemUser, tandemUser.liked)
                    }
                    MainPage(usersItems, onLikeUser)
                }
            }
        }
    }
}

/**
 * Main page containing all the possible actions and click listeners
 */
@Composable
fun MainPage(usersItems: LazyPagingItems<TandemUser>, onLikeUser: (TandemUser) -> Job) {

    Column {
        TopAppBar(
            backgroundColor = Purple700,
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    color = Color.White,
                    text = stringResource(R.string.community_string),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 16.dp)
                )

                Text(
                    color = Color.White,
                    text = stringResource(R.string.refresh_text),
                    modifier = Modifier
                        .clickable { usersItems.refresh() }
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = 16.dp)
                )
            }
        }
        TandemUserList(usersItems, onLikeUser)
    }
}

/**
 * Check the state from the lazy paging items and then shows its possible states.
 */
@Composable
fun TandemUserList(usersItems: LazyPagingItems<TandemUser>, onLikeUser: (TandemUser) -> Job) {
    usersItems.run {

        //Check the state coming from paging
        when (val state = loadState.refresh) {

            //is not loading, we have users
            is LoadState.NotLoading -> ShowUsersList(
                usersItems = usersItems,
                onLikeUser = onLikeUser
            )

            //state is loading we show a loading progress bar
            LoadState.Loading -> ShowLoadingScreen()

            //An error has occurred we show a toast and then try to refresh
            is LoadState.Error -> {
                if (!state.endOfPaginationReached) {
                    Toast.makeText(
                        LocalContext.current,
                        state.toString(), Toast.LENGTH_LONG
                    ).show()
                    RefreshButton {
                        usersItems.refresh()
                    }
                }
            }
        }

        //load if appending a new page is taking a while
        if (loadState.append is LoadState.Loading) {
            ShowLoadingScreen()
        }
    }
}

/**
 * Shows the list of users, if the list is empty shows a refresh button
 */
@Preview(showBackground = true)
@Composable
fun ShowUserItemPreview() {
    val mockedUser = TandemUser(
        id = 1,
        firstName = "James",
        learns = listOf("DE, EN"),
        natives = listOf("IT"),
        pictureUrl = "https://www.pngpix.com/wp-content/uploads/2016/03/Bunch-of-Bananas-PNG-image.png",
        referenceCnt = 0,
        topic = "I am james",
    )

    ProfileCardComposable(user = mockedUser, onItemClick = { Job() })
}

@Composable
private fun ShowUsersList(
    usersItems: LazyPagingItems<TandemUser>,
    onLikeUser: (TandemUser) -> Job
) {

    if (usersItems.itemCount == 0) {
        RefreshButton {
            usersItems.refresh()
        }
    }

    //Lazy column is "somehow" the compose version of a recyclerview
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = usersItems) { user ->
            user?.let {
                ProfileCardComposable(user, onLikeUser)
            }
        }
    }
}

/**
 * Whe show a button to refresh the list of users
 */
@Preview(showBackground = true)
@Composable
fun ShowRefreshStatePreview() {
    RefreshButton {}
}

@Composable
private fun RefreshButton(onClick: () -> Unit) {
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
                onClick()
            }
        ) {
            Text(text = stringResource(R.string.refresh_text))
        }
    }
}

/**
 * Screen with a loading indicator
 */
@Preview(showBackground = true)
@Composable
fun ShowLoadingScreenPreview() {
    ShowLoadingScreen()
}

@Composable
private fun ShowLoadingScreen() {
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






