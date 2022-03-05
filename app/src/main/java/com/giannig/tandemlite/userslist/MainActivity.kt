package com.giannig.tandemlite.userslist

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.giannig.tandemlite.R
import com.giannig.tandemlite.TandemActivity
import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.Flow

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
                    MainPage(viewModel.getUsersMutableState)
                }
            }
        }
    }
}


@Composable
fun MainPage(tandemUserState: Flow<PagingData<TandemUser>>) {
    Column {
        TopAppBar(
            backgroundColor = colorResource(id = android.R.color.white),
            elevation = 16.dp,
            title = {
                Text(text = stringResource(R.string.community_string))
            },
        )
        TandemUserList(tandemUserState)
    }
}

@Composable
fun TandemUserList(tandemUserList: Flow<PagingData<TandemUser>>) {
    val userlistItems: LazyPagingItems<TandemUser> = tandemUserList.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = userlistItems) { user ->
            user?.let {
                ProfileCardComposable(user)
            }
        }
    }
    userlistItems.run {
        when (val state = loadState.refresh) {
            LoadState.Loading -> SetUpLoadingView()
            is LoadState.NotLoading,
            is LoadState.Error -> {
                Text(
                    text = state.toString(),
                    color = Color.Blue
                )
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
fun ProfileCardComposable(user: TandemUser) {
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
            ProfileContentComposable(user)
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
fun ProfileContentComposable(user: TandemUser) {
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

        CardFooter(user)
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
private fun CardFooter(user: TandemUser) {
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
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "like",
                textAlign = TextAlign.End,
            )
        }
    }
}






