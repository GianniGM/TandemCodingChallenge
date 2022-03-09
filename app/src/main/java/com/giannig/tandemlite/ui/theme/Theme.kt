package com.giannig.tandemlite.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.giannig.tandemlite.R


private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val colors = LightColorPalette
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun UserCardItem(
    content: @Composable () -> Unit
) {
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
            content()
        }
    }
}

@Composable
fun LanguageText(
    modifier: Modifier,
    text: String
) {
    Text(
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        fontWeight = FontWeight.Light,
        modifier = modifier,
        text = "$text, ".uppercase(),
    )
}