package com.giannig.tandemlite.userslist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.giannig.tandemlite.R.*
import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.ui.theme.LanguageText
import com.giannig.tandemlite.ui.theme.UserCardItem
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.Job

@Preview(showBackground = true)
@Composable
fun ShowUserItem() {
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

/**
 * Show the item to put into the lazy list
 */
@Composable
fun ProfileCardComposable(user: TandemUser, onItemClick: (TandemUser) -> Job) {
    UserCardItem {
        ProfilePictureComposable(user.pictureUrl, user.firstName)
        ProfileContentComposable(user, onItemClick)
    }
}

/**
 * Shows the Image section with the user image
 */
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
            placeHolder = painterResource(drawable.user_placeholder),
            error = painterResource(drawable.warning)
        )
    }
}

/**
 * Shows the component on the right of the user image
 */
@Composable
fun ProfileContentComposable(user: TandemUser, onItemClick: (TandemUser) -> Job) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, bottom = 8.dp)
    ) {
        CardHeader(user)
        CardBody(user)
        CardFooter(user, onItemClick)
    }
}

/**
 * Shows the Header with the user name and the rfc count
 */
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
            if (refCountText != null) {
                Text(
                    text = refCountText,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(23.dp)
                        .align(Alignment.CenterEnd),
                    painter = painterResource(
                        id = drawable.new_image
                    ),
                    contentDescription = stringResource(id = string.new_string)
                )
            }
        }
    }
}

/**
 * Shows the Body of the card with the description of the user
 */
@Composable
private fun CardBody(user: TandemUser) {
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
}

/**
 * Shows the Footer of the item, with the languages learned, the native and a like button
 */
@Composable
private fun CardFooter(user: TandemUser, onItemClick: (TandemUser) -> Job) {
    Row {
        Column {
            Row(
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = stringResource(string.native_language),
                    modifier = Modifier
                        .padding(start = 0.dp, end = 4.dp)
                        .alignByBaseline()
                )
                user.natives.forEach {
                    LanguageText(
                        text = it,
                        modifier = Modifier
                            .padding(start = 0.dp, end = 4.dp)
                            .alignByBaseline()
                    )
                }
            }

            Row {
                Text(
                    text = stringResource(string.learns),
                    modifier = Modifier
                        .padding(start = 0.dp, end = 4.dp)
                        .alignByBaseline(),
                )
                user.learns.forEach {
                    LanguageText(
                        modifier = Modifier
                            .padding(start = 0.dp, end = 4.dp)
                            .alignByBaseline(), text = it
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            val userLiked = remember {
                mutableStateOf(user.liked)
            }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .size(16.dp)
                    .clickable {
                        val newUser = user.copy(liked = !userLiked.value)
                        userLiked.value = newUser.liked
                        onItemClick(newUser)
                    }
                    .align(Alignment.End)
            ) {
                if (userLiked.value) {
                    Image(
                        painterResource(drawable.liked),
                        contentDescription = user.liked.toString()
                    )
                } else {
                    Image(
                        painterResource(drawable.not_liked),
                        contentDescription = user.liked.toString()
                    )
                }
            }
        }
    }
}
