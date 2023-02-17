package com.lightspark.composeqr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun Smile() {
    Canvas(modifier = Modifier.fillMaxSize(fraction = 0.5f)) {
        // Draw a smiley face
        drawCircle(
            color = Color.Black,
            center = Offset(size.width / 2, size.height / 2),
            radius = size.width / 2
        )
        drawCircle(
            color = Color.White,
            center = Offset(
                size.width / 2 - size.width / 4,
                size.height / 2 - size.height / 4
            ),
            radius = size.width / 8
        )
        drawCircle(
            color = Color.White,
            center = Offset(
                size.width / 2 + size.width / 4,
                size.height / 2 - size.height / 4
            ),
            radius = size.width / 8
        )
        drawArc(
            color = Color.White,
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
fun SmilyDarkPreview() {
    QrCodeView(
        "https://lightspark.com/this-is-a-test-of-longer-urls-to-see-how-it-looks",
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
                Smile()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun SmilyLightSquarePreview() {
    QrCodeView(
        "https://lightspark.com/this-is-a-test-of-longer-urls-to-see-how-it-looks",
        modifier = Modifier.size(300.dp),
        colors = QrCodeColors(
            background = Color.White,
            foreground = Color.Black
        ),
        dotShape = DotShape.Square,
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Green)
            ) {
                Smile()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ColorfulStarOverlay() {
    QrCodeView(
        "https://lightspark.com/this-is-a-test-of-longer-urls-to-see-how-it-looks",
        modifier = Modifier.size(300.dp),
        colors = QrCodeColors(
            background = Color.Blue,
            foreground = Color.Yellow
        ),
        dotShape = DotShape.Circle,
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.Blue)
            ) {
                Canvas(modifier = Modifier.fillMaxSize(0.5f)) {
                    val coolArrow = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(size.width / 2, size.height / 2)
                        lineTo(0f, size.height)
                        lineTo(size.width, size.height / 2)
                        lineTo(size.width / 2, 0f)
                        lineTo(size.width, size.height / 2)
                        close()
                    }
                    drawPath(
                        path = coolArrow,
                        color = Color.White,
                        style = Fill
                    )
                }
            }
        })
}
