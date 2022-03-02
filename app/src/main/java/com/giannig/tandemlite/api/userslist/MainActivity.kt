package com.giannig.tandemlite.api.userslist

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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giannig.tandemlite.R
import com.giannig.tandemlite.TandemActivity
import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.ui.theme.MyApplicationTheme
import com.skydoves.landscapist.glide.GlideImage

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
        viewModel.getUsersFromApi()
        setContent {
            val usersList = viewModel.getUsersMutableState.value
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainPage(usersList)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val user = TandemUser(
        id = 999,
        firstName = "Gianni",
        learns = listOf("DE", "IT"),
        natives = listOf("BA", "NA"),
        referenceCnt = 33,
        pictureUrl = "https://pluspng.com/img-png/png-user-icon-circled-user-icon-2240.png",
        topic = "I like to learn new things"
    )
    val users = listOf(user, user, user, user, user)
    MyApplicationTheme {
        TandemUserList(ViewModelState.ShowUserList(users))
    }
}

@Composable
fun MainPage(tandemUserState: ViewModelState) {
    Column {
        TopAppBar(
            backgroundColor = colorResource(id = android.R.color.white),
            elevation = 16.dp,
            title = {
                //todo remove hardcoded value
                Text(text = "Community")
            },
        )
        UserList(tandemUserState)
    }
}


@Composable
private fun UserList(tandemUsersState: ViewModelState) = when (tandemUsersState) {
    ViewModelState.Empty -> Text(text = "No users here", color = Color.Black)
    ViewModelState.Loading -> SetUpLoadingView()
    is ViewModelState.ShowErrorMessage -> Text(text = tandemUsersState.errorText.toString(), color = Color.Blue)
    is ViewModelState.ShowUserList -> TandemUserList(tandemUsersState)
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
fun TandemUserList(tandemUserList: ViewModelState.ShowUserList) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tandemUserList.userList.forEach {
            item {
                ProfileCardComposable(it)
            }
        }
    }
}


@Composable
fun ProfileCardComposable(user: TandemUser) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(4.dp))
            .background(color = colorResource(id = android.R.color.white))
            .padding(8.dp),
    ) {
        Row(modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(8.dp)
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
            .fillMaxHeight()
            .padding(start = 8.dp),
        verticalArrangement = Arrangement.aligned(Alignment.CenterVertically)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(user.firstName, fontWeight = FontWeight.Bold)
            if(user.referenceCnt == 0){
                Text("new", fontWeight = FontWeight.Bold)
            }else{
                Text(user.referenceCnt.toString(), fontWeight = FontWeight.Bold)
            }
        }

        Text(
            text = user.topic,
            style = MaterialTheme.typography.body2
        )
        FootNotes(user.natives, user.learns)
    }
}

@Composable
private fun FootNotes(natives: List<String>, learns: List<String>) {
    Row {
        Text(text = "native")
        natives.forEach {
            Text(text = it)
        }
        Text(text = "learns")
        learns.forEach {
            Text(text = it)
        }
        Text(text = "like")
    }
}

