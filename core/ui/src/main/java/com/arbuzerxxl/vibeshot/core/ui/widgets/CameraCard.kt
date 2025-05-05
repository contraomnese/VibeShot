package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight30
import com.arbuzerxxl.vibeshot.core.design.theme.itemHeight64
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding160
import com.arbuzerxxl.vibeshot.core.design.theme.padding8
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.ui.R

private const val EMPTY_DATA = "--"

@Composable
fun CameraCard(
    modifier: Modifier = Modifier,
    cameraModel: String,
    lensModel: String,
    aperture: String,
    focalLength: String,
    iso: String,
    flash: String,
    exposureTime: String,
    whiteBalance: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(padding16)
    ) {
        Box(
            modifier = Modifier
                .height(itemHeight64)
                .fillMaxWidth()
        ) {
            Icon(
                modifier = Modifier.size(itemHeight64),
                imageVector = VibeShotIcons.Camera,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = padding160),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Title(text = R.string.camera)
                    Body(
                        text = cameraModel
                    )
                }
                Column {
                    Title(text = R.string.lens)
                    Body(text = lensModel)
                }
            }
        }

        Box(
            modifier = Modifier
                .wrapContentWidth()
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(padding16)) {
                CameraData(
                    icon = VibeShotIcons.FocalLength, title = R.string.focal, body = focalLength
                )
                CameraData(
                    icon = VibeShotIcons.Aperture, title = R.string.aperture, body = aperture
                )
                CameraData(
                    icon = VibeShotIcons.ISO, title = R.string.iso, body = iso
                )
            }
            Column(
                modifier = Modifier.padding(start = padding160),
                verticalArrangement = Arrangement.spacedBy(padding16)
            ) {
                CameraData(
                    icon = VibeShotIcons.ExposureTime, title = R.string.shutter_speed, body = exposureTime
                )
                CameraData(
                    icon = VibeShotIcons.WhiteBalance, title = R.string.wb, body = whiteBalance
                )
                CameraData(
                    icon = VibeShotIcons.Flash, title = R.string.flash, body = flash
                )
            }
        }
    }
}

@Composable
private fun Title(
    @StringRes text: Int,
) {
    Text(
        text = stringResource(text).uppercase(),
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Light,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun Body(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text.checkEmpty(),
        style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 10.sp
        ),
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
private fun CameraData(
    icon: ImageVector,
    @StringRes title: Int,
    body: String,
) {
    Row(
        modifier = Modifier.height(itemHeight30),
    ) {
        Icon(
            modifier = Modifier.wrapContentHeight(),
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Column(
            modifier = Modifier
                .padding(start = padding8)
                .wrapContentHeight(),
        ) {
            Title(text = title)
            Body(text = body)
        }
    }
}

private fun String.checkEmpty(): String = if (this.isNotEmpty()) this else EMPTY_DATA

@DevicePreviews
@Composable
private fun CameraCardPreview() {
    VibeShotThemePreview {
        CameraCard(
            cameraModel = "Sony ILCE-7RM5",
            lensModel = "50mm f/1.2",
            aperture = "f/16.0",
            focalLength = "50.0 mm",
            iso = "",
            flash = "Off, Did not fire",
            exposureTime = "1/320",
            whiteBalance = "Auto",
        )
    }
}