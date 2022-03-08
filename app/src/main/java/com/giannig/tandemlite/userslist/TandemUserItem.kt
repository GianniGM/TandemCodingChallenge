package com.giannig.tandemlite.userslist

import android.R
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
import androidx.compose.ui.unit.dp
import com.giannig.tandemlite.api.dto.TandemUser
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileCardComposable(user: TandemUser, onItemClick: (TandemUser) -> Unit) {
    Card(
        modifier = Modifier
            .wrapContentWidth()
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(color = colorResource(id = R.color.white))
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
            placeHolder = painterResource(com.giannig.tandemlite.R.drawable.user_placeholder),
            error = painterResource(com.giannig.tandemlite.R.drawable.warning)
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
                ?: stringResource(com.giannig.tandemlite.R.string.new_string)

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
    Row {

        Column {
            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.Start)
            ) {
                Text(
                    textAlign = TextAlign.Start,
                    text = stringResource(com.giannig.tandemlite.R.string.native_language),
                    modifier = Modifier.padding(start = 0.dp, end = 2.dp)
                )
                user.natives.forEach {
                    Text(text = "$it, ")
                }
            }

            Row {
                Text(
                    text = stringResource(com.giannig.tandemlite.R.string.learns),
                    modifier = Modifier.padding(start = 0.dp, end = 4.dp),
                )
                user.learns.forEach {
                    Text(text = "$it, ")
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
                        painterResource(com.giannig.tandemlite.R.drawable.liked),
                        contentDescription = user.liked.toString()
                    )
                } else {
                    Image(
                        painterResource(com.giannig.tandemlite.R.drawable.not_liked),
                        contentDescription = user.liked.toString()
                    )
                }
            }
        }
    }
}