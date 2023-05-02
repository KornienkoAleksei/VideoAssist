package com.example.videoassist.ui.blocks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.videoassist.R
import com.example.videoassist.functions.CameraMovementHorizontal
import com.example.videoassist.functions.CameraMovementVertical
import com.example.videoassist.functions.PersonOrientation
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.ButtonLightGray
import com.example.videoassist.ui.theme.DarkGray

@Composable
fun SpiderPersonOrientationButton(
    personOrientation: PersonOrientation,
    personOrientationButton: PersonOrientation, //button in static
    staticPersonOrientation: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val iconColor: Color
    val iconIcon: Int
    when (personOrientationButton) {
        PersonOrientation.StaticForward -> {
            iconIcon = if (staticPersonOrientation) R.drawable.object_static_forward_backward else R.drawable.object_forward
            iconColor = when (personOrientation) {
                PersonOrientation.StaticForward -> Color.White
                PersonOrientation.MoveForward -> Color.White
                else -> ButtonDarkGray
            }
        }
        PersonOrientation.StaticBackward -> {
            iconIcon = if (staticPersonOrientation) R.drawable.object_static_forward_backward else R.drawable.object_backward
            iconColor = when (personOrientation) {
                PersonOrientation.StaticBackward -> Color.White
                PersonOrientation.MoveBackward -> Color.White
                else -> ButtonDarkGray
            }
        }
        PersonOrientation.StaticLeft -> {
            iconIcon = if (staticPersonOrientation) R.drawable.object_static_left_right else R.drawable.object_left
            iconColor = when (personOrientation) {
                PersonOrientation.StaticLeft -> Color.White
                PersonOrientation.MoveLeft -> Color.White
                else -> ButtonDarkGray
            }
        }
        PersonOrientation.StaticRight -> {
            iconIcon = if (staticPersonOrientation) R.drawable.object_static_left_right else R.drawable.object_right
            iconColor = when (personOrientation) {
                PersonOrientation.StaticRight -> Color.White
                PersonOrientation.MoveRight -> Color.White
                else -> ButtonDarkGray
            }
        }
        else -> {
            iconIcon = R.drawable.person_detail
            iconColor = Color.Green
        }
    }
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkGray,
            contentColor = iconColor
        ),
        shape = RoundedCornerShape(1.dp),
        contentPadding = PaddingValues( 0.dp),
        modifier = Modifier
            .size(32.dp)
    ) {
        Icon(
            painterResource(id = iconIcon),
            contentDescription = personOrientationButton.toString(),
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun SpiderCameraVertical(
    cameraMovementVertical: CameraMovementVertical,
    modifier: Modifier = Modifier,
){
    val iconId = when (cameraMovementVertical){
        CameraMovementVertical.MoveUp -> {
            R.drawable.camera_vertical_up
        }
        CameraMovementVertical.Static -> {
            R.drawable.camera_vertical_static
        }
        CameraMovementVertical.MoveDown -> {
            R.drawable.camera_vertical_down
        }
    }
    val iconColor = if (cameraMovementVertical == CameraMovementVertical.Static) ButtonDarkGray else Color.White
    Icon(
        painterResource(id = iconId),
        contentDescription = "",
        tint = iconColor,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(172.dp)
    )
}

@Composable
fun SpiderCameraHorizontal(
    cameraMovementHorizontal: CameraMovementHorizontal,
    direction: Boolean, //then true- left, false - right
    modifier: Modifier = Modifier,
) {
    val icon1 = if (direction) R.drawable.camera_horizontal_panoramic_left else R.drawable.camera_horizontal_panoramic_right
    val cameraMovement1 = if (direction) CameraMovementHorizontal.PanoramicLeft else CameraMovementHorizontal.PanoramicRight
    val icon2 = if (direction) R.drawable.camera_horizontal_left else R.drawable.camera_horizontal_right
    val cameraMovement2 = if (direction) CameraMovementHorizontal.Left else CameraMovementHorizontal.Right
    val icon3 = if (direction) R.drawable.camera_horizontal_orbit_left else R.drawable.camera_horizontal_orbit_right
    val cameraMovement3 = if (direction) CameraMovementHorizontal.OrbitLeft else CameraMovementHorizontal.OrbitRight
    val alignment = if (direction) Alignment.Bottom else Alignment.Top
    Row(
        verticalAlignment = alignment,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painterResource(id = icon1),
            contentDescription = "",
            tint = if (cameraMovementHorizontal == cameraMovement1) Color.White else ButtonDarkGray,
        )
        Icon(
            painterResource(id = icon2),
            contentDescription = "",
            tint = if (cameraMovementHorizontal == cameraMovement2) Color.White else ButtonDarkGray,
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .height(62.dp)
        )
        Icon(
            painterResource(id = icon3),
            contentDescription = "",
            tint = if (cameraMovementHorizontal == cameraMovement3) Color.White else ButtonDarkGray,
        )
    }
}

@Composable
fun SpiderCameraButtonVertical(
    cameraMovementVertical: CameraMovementVertical,
    cameraMovementVerticalButton: CameraMovementVertical,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val label = when (cameraMovementVerticalButton) {
        CameraMovementVertical.MoveUp -> "U"
        CameraMovementVertical.MoveDown -> "D"
        else -> ""
    }
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (cameraMovementVertical == cameraMovementVerticalButton) Color.White else ButtonDarkGray,
            contentColor = if (cameraMovementVertical == cameraMovementVerticalButton) Color.Black else ButtonLightGray
        ),
        shape = CircleShape,
        contentPadding = PaddingValues( 0.dp),
        modifier = Modifier.size(32.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun SpiderCameraButtonHorizontal(
    cameraMovementHorizontal: CameraMovementHorizontal,
    cameraMovementHorizontalButton: CameraMovementHorizontal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val label = when (cameraMovementHorizontalButton) {
        CameraMovementHorizontal.Left -> "L"
        CameraMovementHorizontal.Right -> "R"
        CameraMovementHorizontal.Backward -> "B"
        CameraMovementHorizontal.Forward -> "F"
        CameraMovementHorizontal.PanoramicLeft -> "pL"
        CameraMovementHorizontal.PanoramicRight -> "pR"
        CameraMovementHorizontal.OrbitLeft -> "oL"
        CameraMovementHorizontal.OrbitRight -> "oR"
        else -> ""
    }
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (cameraMovementHorizontal == cameraMovementHorizontalButton) Color.White else ButtonDarkGray,
            contentColor = if (cameraMovementHorizontal == cameraMovementHorizontalButton) Color.Black else ButtonLightGray
        ),
        shape = CircleShape,
        contentPadding = PaddingValues( 0.dp),
        modifier = Modifier.size(32.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}