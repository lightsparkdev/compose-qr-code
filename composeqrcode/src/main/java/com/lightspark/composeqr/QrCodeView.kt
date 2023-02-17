package com.lightspark.composeqr

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A composable that renders a QR code from a data string.
 *
 * @param data The data to encode into a QR code.
 * @param modifier The Compose modifier to apply to the QR code.
 * @param colors The color palette to use for the QR code - defaults to black and white.
 * @param dotShape The shape of the individual dots in the QR code - defaults to square.
 * @param encoder The encoder to use to encode the data into a QR code. Meant to be able to stub out in tests if needed.
 * @param overlayContent Optional content to overlay on top of the QR code. This overlay is limited to 25% of the size
 *      of the QR code and will be positioned in the center of it.
 */
@Composable
fun QrCodeView(
    data: String,
    modifier: Modifier = Modifier,
    colors: QrCodeColors = QrCodeColors.default(),
    dotShape: DotShape = DotShape.Square,
    encoder: QrEncoder = QrEncoder(),
    overlayContent: (@Composable () -> Unit)? = null,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        QrCodeView(
            data,
            modifier = Modifier.fillMaxSize(),
            colors = colors,
            dotShape = dotShape,
            encoder = encoder
        )
        if (overlayContent != null) {
            Box(modifier = Modifier.fillMaxSize(fraction = 0.25f)) {
                overlayContent()
            }
        }
    }
}

/**
 * A composable that renders a QR code from a data string with no overlay content.
 *
 * @param data The data to encode into a QR code.
 * @param modifier The Compose modifier to apply to the QR code.
 * @param colors The color palette to use for the QR code - defaults to black and white.
 * @param dotShape The shape of the individual dots in the QR code - defaults to square.
 * @param encoder The encoder to use to encode the data into a QR code. Meant to be able to stub out in tests if needed.
 */
@Composable
fun QrCodeView(
    data: String,
    modifier: Modifier = Modifier,
    colors: QrCodeColors = QrCodeColors.default(),
    dotShape: DotShape = DotShape.Square,
    encoder: QrEncoder = QrEncoder()
) {
    val encodedData = remember { encoder(data) }

    Canvas(modifier = modifier.background(colors.background)) {
        encodedData?.let { matrix ->
            val cellSize = size.width / matrix.width
            for (x in 0 until matrix.width) {
                for (y in 0 until matrix.height) {
                    if (matrix.get(x, y) != 1.toByte() || isFinderCell(x, y, matrix.width)) continue
                    when (dotShape) {
                        DotShape.Square -> drawRect(
                            color = colors.foreground,
                            topLeft = Offset(x * cellSize, y * cellSize),
                            size = Size(cellSize, cellSize)
                        )
                        DotShape.Circle -> drawCircle(
                            color = colors.foreground,
                            center = Offset(
                                x * cellSize + cellSize / 2, y * cellSize + cellSize / 2
                            ),
                            radius = cellSize / 2
                        )
                    }
                }
            }
            drawFinderSquares(cellSize, colors, dotShape)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrCodeViewPreview() {
    QrCodeView(
        "https://lightspark.com/this-is-a-test-of-longer-urls-to-see-how-it-looks",
        modifier = Modifier.size(400.dp),
        colors = QrCodeColors(
            background = Color.White,
            foreground = Color.Black
        ),
        dotShape = DotShape.Circle,
        overlayContent = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp)
                    .clip(CircleShape)
                    .background(Color.Yellow)
            ) {
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
        })
}
