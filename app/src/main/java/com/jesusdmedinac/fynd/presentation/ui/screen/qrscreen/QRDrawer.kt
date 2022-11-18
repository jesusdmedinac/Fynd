package com.jesusdmedinac.fynd.presentation.ui.screen.qrscreen

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import com.jesusdmedinac.fynd.presentation.ui.theme.FyndTheme
import kotlin.math.ceil

@Composable
fun QRDrawer(
    text: String,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val width = ceil(size.width).toInt()
        val height = ceil(size.height).toInt()
        generateQrImage(text, width, height)
            ?.asImageBitmap()
            ?.let(::drawImage)
    }
}

fun generateQrImage(text: String, width: Int, height: Int): Bitmap? {
    if (text.isNotEmpty()) {
        val smallerDimension = if (width < height) width else height

        val qrgEncoder = QRGEncoder(
            text,
            null,
            QRGContents.Type.TEXT,
            smallerDimension,
        )
        qrgEncoder.colorBlack = android.graphics.Color.BLACK
        qrgEncoder.colorWhite = android.graphics.Color.WHITE
        return try {
            qrgEncoder.bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    } else {
        return null
    }
}

@Composable
@Preview
fun QRDrawerPreview() {
    FyndTheme {
        QRDrawer(
            "A",
            modifier = Modifier,
        )
    }
}