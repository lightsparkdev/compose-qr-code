package com.lightspark.composeqr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val URL_DATA = "https://lightspark.com/?doesnotmatter=this-is-a-test-of-longer-urls-to-see-how-it-looks"

@Composable
private fun Smile(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Black,
    smileColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        drawCircle(
            color = backgroundColor,
            center = Offset(size.width / 2, size.height / 2),
            radius = size.width / 2
        )
        drawCircle(
            color = smileColor,
            center = Offset(
                size.width / 2 - size.width / 4,
                size.height / 2 - size.height / 4
            ),
            radius = size.width / 8
        )
        drawCircle(
            color = smileColor,
            center = Offset(
                size.width / 2 + size.width / 4,
                size.height / 2 - size.height / 4
            ),
            radius = size.width / 8
        )
        drawArc(
            color = smileColor,
            startAngle = 0f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width / 4, size.height / 3),
            size = Size(size.width / 2, size.height / 2),
            style = Stroke(width = size.width / 8)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SmileyDarkPreview() {
    QrCodeView(
        data = URL_DATA,
        modifier = Modifier.size(300.dp),
        colors = QrCodeColors(
            background = Color.Black,
            foreground = Color.White
        ),
        dotShape = DotShape.Circle,
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Green)
            ) {
                Smile(modifier = Modifier.fillMaxSize(0.5f))
            }
        })
}

@Preview(showBackground = true)
@Composable
fun SmileyLightSquarePreview() {
    QrCodeView(
        data = URL_DATA,
        modifier = Modifier.size(300.dp),
        colors = QrCodeColors(
            background = Color.White,
            foreground = Color.Black
        ),
        dotShape = DotShape.Square,
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Smile(
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = Color.Yellow,
                    smileColor = Color.Black
                )
            }
        })
}

@Preview(showBackground = true)
@Composable
fun BoringPreview() {
    QrCodeView(
        data = URL_DATA,
        modifier = Modifier.size(300.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PurpleAndGold() {
    val purple = Color(0xFF552583)
    val gold = Color(0xFFFDB927)
    QrCodeView(
        data = URL_DATA,
        modifier = Modifier.size(300.dp),
        colors = QrCodeColors(
            background = purple,
            foreground = gold
        ),
        dotShape = DotShape.Circle,
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(purple)
            ) {
                BasicText(
                    text = "L",
                    style = TextStyle.Default.copy(
                        color = gold,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif
                    )
                )
            }
        })
}
