package com.example.videoassist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.videoassist.R
import com.example.videoassist.functions.CameraMovementHorizontal
import com.example.videoassist.functions.CameraMovementVertical
import com.example.videoassist.functions.Frame
import com.example.videoassist.functions.PersonOrientation
import com.example.videoassist.ui.blocks.*
import com.example.videoassist.ui.theme.ButtonDarkGray

@Composable
fun FrameIconButtons (
    frame: Frame,
    person: Boolean,
    onButton: (Frame) -> Unit
) {
    Row(modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)) {
        FrameIconButton(
            currentFrame = frame,
            buttonFrame = Frame.Landscape,
            person = person,
            enabled = true,
            onClick = { onButton(Frame.Landscape) },
            modifierButton = Modifier
                .padding(12.dp)
                .size(48.dp),
            modifierIcon = Modifier.size(48.dp))
        if (person) {
            FrameIconButton(
                currentFrame = frame,
                buttonFrame = Frame.FullBody,
                person = person,
                enabled = true,
                onClick = { onButton(Frame.FullBody) },
                modifierButton = Modifier
                    .padding(12.dp)
                    .size(48.dp),
                modifierIcon = Modifier.size(48.dp))
        }
        FrameIconButton(
            currentFrame = frame,
            buttonFrame = Frame.Body,
            person = person,
            enabled = true,
            onClick = { onButton(Frame.Body) },
            modifierButton = Modifier
                .padding(12.dp)
                .size(48.dp),
            modifierIcon = Modifier.size(48.dp))
        if (person) {
            FrameIconButton(
                currentFrame = frame,
                buttonFrame = Frame.Face,
                person = person,
                enabled = true,
                onClick = { onButton(Frame.Face) },
                modifierButton = Modifier
                    .padding(12.dp)
                    .size(48.dp),
                modifierIcon = Modifier.size(48.dp))
        }
        FrameIconButton(
            currentFrame = frame,
            buttonFrame = Frame.Detail,
            person = person,
            enabled = true,
            onClick = { onButton(Frame.Detail) },
            modifierButton = Modifier
                .padding(12.dp)
                .size(48.dp),
            modifierIcon = Modifier.size(48.dp))
    }
}


@Composable
fun SpiderDrawing(
    cameraMovementVertical: CameraMovementVertical,
    cameraMovementHorizontal: CameraMovementHorizontal,
    personOrientation: PersonOrientation,
    staticPersonOrientation: Boolean,
    frame: Frame,
    person: Boolean,
    onCameraVertical: (CameraMovementVertical) -> Unit,
    onCameraHorizontal: (CameraMovementHorizontal) -> Unit,
    onPerson: (PersonOrientation) -> Unit,
    //onPersonCentre: () -> Unit
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        //camera movement vertical
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SpiderCameraButtonVertical(
                cameraMovementVertical = cameraMovementVertical,
                cameraMovementVerticalButton = CameraMovementVertical.MoveUp,
                onClick = { onCameraVertical(CameraMovementVertical.MoveUp) })
            SpiderCameraVertical(cameraMovementVertical = cameraMovementVertical)
            SpiderCameraButtonVertical(
                cameraMovementVertical = cameraMovementVertical,
                cameraMovementVerticalButton = CameraMovementVertical.MoveDown,
                onClick = { onCameraVertical(CameraMovementVertical.MoveDown) })
        }
        //camera movement horizontal
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            SpiderCameraButtonHorizontal(
                cameraMovementHorizontal = cameraMovementHorizontal,
                cameraMovementHorizontalButton = CameraMovementHorizontal.Left,
                onClick = {onCameraHorizontal(CameraMovementHorizontal.Left)})
            Row(verticalAlignment = Alignment.Top) {
                SpiderCameraButtonHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    cameraMovementHorizontalButton = CameraMovementHorizontal.PanoramicLeft,
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.PanoramicLeft)})
                SpiderCameraHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    direction = true
                )
                SpiderCameraButtonHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    cameraMovementHorizontalButton = CameraMovementHorizontal.OrbitLeft,
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.OrbitLeft)})
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                SpiderCameraButtonHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    cameraMovementHorizontalButton = CameraMovementHorizontal.Backward,
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.Backward)})
                Icon(
                    painterResource(id = R.drawable.camera_horizontal_backward),
                    contentDescription = "",
                    tint = if (cameraMovementHorizontal == CameraMovementHorizontal.Backward) Color.White else ButtonDarkGray,
                    modifier = Modifier.padding(start = 5.dp, end = 8.dp)
                )
                TextButton(
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.Static)},
                    shape = RoundedCornerShape(1.dp),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        painterResource(id = R.drawable.camera_icon),
                        contentDescription = "",
                        tint = if (cameraMovementHorizontal == CameraMovementHorizontal.Static) Color.White else ButtonDarkGray,
                    )
                }
                Icon(
                    painterResource(id = R.drawable.camera_horizontal_forward),
                    contentDescription = "",
                    tint = if (cameraMovementHorizontal == CameraMovementHorizontal.Forward) Color.White else ButtonDarkGray,
                    modifier = Modifier.padding(start = 8.dp, end = 5.dp)
                )
                SpiderCameraButtonHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    cameraMovementHorizontalButton = CameraMovementHorizontal.Forward,
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.Forward)})
            }
            Row(verticalAlignment = Alignment.Bottom) {
                SpiderCameraButtonHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    cameraMovementHorizontalButton = CameraMovementHorizontal.PanoramicRight,
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.PanoramicRight)})
                SpiderCameraHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    direction = false
                )
                SpiderCameraButtonHorizontal(
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    cameraMovementHorizontalButton = CameraMovementHorizontal.OrbitRight,
                    onClick = {onCameraHorizontal(CameraMovementHorizontal.OrbitRight)})
            }
            SpiderCameraButtonHorizontal(
                cameraMovementHorizontal = cameraMovementHorizontal,
                cameraMovementHorizontalButton = CameraMovementHorizontal.Right,
                onClick = {onCameraHorizontal(CameraMovementHorizontal.Right)})
        }
        //object movement
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SpiderPersonOrientationButton(
                personOrientation = personOrientation,
                personOrientationButton = PersonOrientation.StaticLeft,
                staticPersonOrientation = staticPersonOrientation,
                onClick = { onPerson(PersonOrientation.StaticLeft)})
            Row(verticalAlignment = Alignment.CenterVertically) {
                SpiderPersonOrientationButton(
                    personOrientation = personOrientation,
                    personOrientationButton = PersonOrientation.StaticForward,
                    staticPersonOrientation = staticPersonOrientation,
                    onClick = { onPerson(PersonOrientation.StaticForward)})
                //when press central icon send moveForward
                FrameIconButton(
                    currentFrame = frame,
                    buttonFrame = frame,
                    person = person,
                    enabled = true,
                    onClick = { onPerson(PersonOrientation.MoveForward)},
                    modifierButton = Modifier
                        .padding(12.dp)
                        .size(32.dp),
                    modifierIcon = Modifier.size(32.dp))
                SpiderPersonOrientationButton(
                    personOrientation = personOrientation,
                    personOrientationButton = PersonOrientation.StaticBackward,
                    staticPersonOrientation = staticPersonOrientation,
                    onClick = { onPerson(PersonOrientation.StaticBackward)})
            }
            SpiderPersonOrientationButton(
                personOrientation = personOrientation,
                personOrientationButton = PersonOrientation.StaticRight,
                staticPersonOrientation = staticPersonOrientation,
                onClick = { onPerson(PersonOrientation.StaticRight)})
        }
    }
}

