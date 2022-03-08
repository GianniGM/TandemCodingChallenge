package com.giannig.tandemlite.userslist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giannig.tandemlite.R.*
import com.giannig.tandemlite.api.dto.TandemUser
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileCardComposable(user: TandemUser, onItemClick: (TandemUser) -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(color = colorResource(id = color.white))
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
            placeHolder = painterResource(drawable.user_placeholder),
            error = painterResource(drawable.warning)
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

@Composable
private fun CardFooter(user: TandemUser, onItemClick: (TandemUser) -> Unit) {
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
                    Text(
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        text = "$it, ".uppercase(),
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
                    Text(
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        text = "$it, ".uppercase(),
                        modifier = Modifier
                            .padding(start = 0.dp, end = 4.dp)
                            .alignByBaseline()
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .size(16.dp)
                    .clickable { onItemClick(user) }
                    .align(Alignment.End)
            ) {
                if (user.liked) {
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
