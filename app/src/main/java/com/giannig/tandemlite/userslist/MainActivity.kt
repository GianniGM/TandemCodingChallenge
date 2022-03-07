package com.giannig.tandemlite.userslist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.giannig.tandemlite.R
import com.giannig.tandemlite.TandemActivity
import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.paging.TandemException
import com.giannig.tandemlite.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.tan

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
    val userlistItems: LazyPagingItems<TandemUser> = tandemViewModel
        .getTandemUsersList
        .collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = userlistItems) { user ->
            user?.let {
                ProfileCardComposable(user) { user ->
                    tandemViewModel.likeUser(user, !user.liked)
                }
            }
        }
    }
    userlistItems.run {
        when (val state = loadState.refresh) {
            LoadState.Loading -> SetUpLoadingView()
            is LoadState.NotLoading,
            is LoadState.Error -> {
                val lazyList = tandemViewModel.getTandemUsersList.collectAsLazyPagingItems()
                Text(text = "refresh", modifier = Modifier.clickable {
                    lazyList.refresh()
                })
                Toast.makeText(
                    LocalContext.current,
                    state.toString(), Toast.LENGTH_LONG
                ).show()
            }
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


@Composable
fun ProfileCardComposable(user: TandemUser, onItemClick: (TandemUser) -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(color = colorResource(id = android.R.color.white))
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Max)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            ProfilePictureComposable(user.pictureUrl, user.firstName)
            ProfileContentComposable(user, onItemClick)
        }
    }
}

@Composable
fun ProfilePictureComposable(pictureUrl: String, firstName: String) {
    Card(
        shape = CircleShape,
        border = BorderStroke(2.dp, color = Color.Black),
        modifier = Modifier.size(48.dp),
        elevation = 4.dp
    ) {
        GlideImage(
            imageModel = pictureUrl,
            contentDescription = firstName,
            placeHolder = painterResource(R.drawable.user_placeholder),
            error = painterResource(R.drawable.warning)
        )
    }
}

@Composable
fun ProfileContentComposable(user: TandemUser, onItemClick: (TandemUser) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, bottom = 8.dp)
    ) {
        CardHeader(user)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = user.topic,
                style = MaterialTheme.typography.body2,
            )
        }

        CardFooter(user, onItemClick)
    }
}

@Composable
private fun CardHeader(user: TandemUser) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                user.firstName,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            val refCountText = user.referenceCnt
                .takeIf { it > 0 }
                ?.toString()
                ?: stringResource(R.string.new_string)

            Text(
                text = refCountText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun CardFooter(user: TandemUser, onItemClick: (TandemUser) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f)
        ) {
            Text(
                textAlign = TextAlign.Start,
                text = stringResource(R.string.native_language),
                modifier = Modifier.padding(start = 0.dp, end = 2.dp)
            )
            user.natives.forEach {
                Text(text = "$it, ")
            }
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.learns),
                modifier = Modifier.padding(start = 0.dp, end = 4.dp),
            )
            user.learns.forEach {
                Text(text = "$it, ")
            }
        }

        Row(
            modifier = Modifier
                .wrapContentWidth()
                .weight(0.4f)
                .clickable { onItemClick(user) }
        ) {
            if (user.liked) {
                Image(
                    painterResource(R.drawable.liked),
                    contentDescription = user.liked.toString()
                )
            } else {
                Image(
                    painterResource(R.drawable.not_liked),
                    contentDescription = user.liked.toString()
                )
            }
        }
    }
}






