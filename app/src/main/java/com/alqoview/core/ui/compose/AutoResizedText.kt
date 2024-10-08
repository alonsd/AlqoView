package com.alqoview.core.ui.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.isUnspecified

@Composable
fun AutoResizedText(
    modifier: Modifier = Modifier,
    text: AnnotatedString,
    style: TextStyle = MaterialTheme.typography.body1,
    color: Color = style.color,
    onTextSizeFinalized: (textSize: Float) -> Unit
) {
    var resizedTextStyle by remember {
        mutableStateOf(style)
    }
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    val defaultFontSize = MaterialTheme.typography.body1.fontSize

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if (shouldDraw.not()) return@drawWithContent
            drawContent()
        },
        softWrap = false,
        style = resizedTextStyle,
        onTextLayout = { result ->
            if (result.didOverflowWidth.not()) {
                shouldDraw = true
                onTextSizeFinalized(resizedTextStyle.fontSize.value)
                return@Text
            }
            if (style.fontSize.isUnspecified) {
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = defaultFontSize
                )
            }
            resizedTextStyle = resizedTextStyle.copy(
                fontSize = resizedTextStyle.fontSize * 0.95
            )
        }
    )
}