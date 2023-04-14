package com.example.videoassist

import android.content.Context
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.ButtonLightGray
import com.example.videoassist.ui.theme.DarkGray
import com.example.videoassist.ui.theme.MainTextColor
import java.util.EventObject

@Composable
fun NewFootage(
    navController: NavController,
    currentIdClip: Int,
    database: AppDatabase,
){
    val currentClip by database.databaseDao().getClip(currentIdClip).observeAsState()
    val focusManager = LocalFocusManager.current
    var number = 1
    var person by remember { mutableStateOf(true) }
    var frame by remember { mutableStateOf(Frame.Landscape) }
    var cameraMovementVertical by remember { mutableStateOf(CameraMovementVertical.Static) }
    var cameraMovementHorizontal by remember { mutableStateOf(CameraMovementHorizontal.Static) }
    var personOrientation by remember { mutableStateOf(PersonOrientation.StaticForward) }
    currentClip?.let {
        number += currentClip!!.footage.size
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            FootageHeader(number = number, navController = navController)
            FootageToggleButton(person = person, onClickPerson = {person = true}, onClickObject = {person = false})
            Row(modifier = Modifier.padding(start = 16.dp, bottom = 32.dp)) {
                FrameIconButton(
                    currentFrame = frame,
                    buttonFrame = Frame.Landscape,
                    person = person,
                    iconSize = 48,
                    onClick = { frame = Frame.Landscape })
                if (person) {
                    FrameIconButton(
                        currentFrame = frame,
                        buttonFrame = Frame.FullBody,
                        person = person,
                        iconSize = 48,
                        onClick = { frame = Frame.FullBody })
                }
                FrameIconButton(
                    currentFrame = frame,
                    buttonFrame = Frame.Body,
                    person = person,
                    iconSize = 48,
                    onClick = { frame = Frame.Body })
                if (person) {
                    FrameIconButton(
                        currentFrame = frame,
                        buttonFrame = Frame.Face,
                        person = person,
                        iconSize = 48,
                        onClick = { frame = Frame.Face })
                }
                FrameIconButton(
                    currentFrame = frame,
                    buttonFrame = Frame.Detail,
                    person = person,
                    iconSize = 48,
                    onClick = { frame = Frame.Detail })
            }
            FootageTextMovement(
                cameraMovementVertical = cameraMovementVertical,
                cameraMovementHorizontal = cameraMovementHorizontal,
                personOrientation = personOrientation
            )
            //spider
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
                        onClick = {
                            cameraMovementVertical =
                                if (cameraMovementVertical != CameraMovementVertical.MoveUp) CameraMovementVertical.MoveUp else CameraMovementVertical.Static
                        })
                    SpiderCameraVertical(cameraMovementVertical = cameraMovementVertical)
                    SpiderCameraButtonVertical(
                        cameraMovementVertical = cameraMovementVertical,
                        cameraMovementVerticalButton = CameraMovementVertical.MoveDown,
                        onClick = {
                            cameraMovementVertical =
                                if (cameraMovementVertical != CameraMovementVertical.MoveDown) CameraMovementVertical.MoveDown else CameraMovementVertical.Static
                        })
                }
                //camera movement horizontal
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    SpiderCameraButtonHorizontal(
                        cameraMovementHorizontal = cameraMovementHorizontal,
                        cameraMovementHorizontalButton = CameraMovementHorizontal.Left,
                        onClick = {
                            cameraMovementHorizontal =
                                if (cameraMovementHorizontal != CameraMovementHorizontal.Left) CameraMovementHorizontal.Left else CameraMovementHorizontal.Static
                        })
                    Row(verticalAlignment = Alignment.Top) {
                        SpiderCameraButtonHorizontal(
                            cameraMovementHorizontal = cameraMovementHorizontal,
                            cameraMovementHorizontalButton = CameraMovementHorizontal.PanoramicLeft,
                            onClick = {
                                cameraMovementHorizontal =
                                    if (cameraMovementHorizontal != CameraMovementHorizontal.PanoramicLeft) CameraMovementHorizontal.PanoramicLeft else CameraMovementHorizontal.Static
                            })
                        SpiderCameraHorizontal(cameraMovementHorizontal = cameraMovementHorizontal, direction = true)
                        SpiderCameraButtonHorizontal(
                            cameraMovementHorizontal = cameraMovementHorizontal,
                            cameraMovementHorizontalButton = CameraMovementHorizontal.OrbitLeft,
                            onClick = {
                                cameraMovementHorizontal =
                                    if (cameraMovementHorizontal != CameraMovementHorizontal.OrbitLeft) CameraMovementHorizontal.OrbitLeft else CameraMovementHorizontal.Static
                            })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically ) {
                        SpiderCameraButtonHorizontal(
                            cameraMovementHorizontal = cameraMovementHorizontal,
                            cameraMovementHorizontalButton = CameraMovementHorizontal.Backward,
                            onClick = {
                                cameraMovementHorizontal =
                                    if (cameraMovementHorizontal != CameraMovementHorizontal.Backward) CameraMovementHorizontal.Backward else CameraMovementHorizontal.Static
                            })
                        Icon(
                            painterResource(id = R.drawable.camera_horizontal_backward),
                            contentDescription = "",
                            tint = if (cameraMovementHorizontal == CameraMovementHorizontal.Backward) Color.White else ButtonDarkGray,
                            modifier = Modifier.padding(start = 5.dp, end = 8.dp)
                        )
                        TextButton(
                            onClick = { cameraMovementHorizontal = CameraMovementHorizontal.Static },
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
                            onClick = {
                                cameraMovementHorizontal =
                                    if (cameraMovementHorizontal != CameraMovementHorizontal.Forward) CameraMovementHorizontal.Forward else CameraMovementHorizontal.Static
                            })
                    }
                    Row(verticalAlignment = Alignment.Bottom) {
                        SpiderCameraButtonHorizontal(
                            cameraMovementHorizontal = cameraMovementHorizontal,
                            cameraMovementHorizontalButton = CameraMovementHorizontal.PanoramicRight,
                            onClick = {
                                cameraMovementHorizontal =
                                    if (cameraMovementHorizontal != CameraMovementHorizontal.PanoramicRight) CameraMovementHorizontal.PanoramicRight else CameraMovementHorizontal.Static
                            })
                        SpiderCameraHorizontal(cameraMovementHorizontal = cameraMovementHorizontal, direction = false)
                        SpiderCameraButtonHorizontal(
                            cameraMovementHorizontal = cameraMovementHorizontal,
                            cameraMovementHorizontalButton = CameraMovementHorizontal.OrbitRight,
                            onClick = {
                                cameraMovementHorizontal =
                                    if (cameraMovementHorizontal != CameraMovementHorizontal.OrbitRight) CameraMovementHorizontal.OrbitRight else CameraMovementHorizontal.Static
                            })
                    }
                    SpiderCameraButtonHorizontal(
                        cameraMovementHorizontal = cameraMovementHorizontal,
                        cameraMovementHorizontalButton = CameraMovementHorizontal.Right,
                        onClick = {
                            cameraMovementHorizontal =
                                if (cameraMovementHorizontal != CameraMovementHorizontal.Right) CameraMovementHorizontal.Right else CameraMovementHorizontal.Static
                        })
                }
                //object movement
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    FrameIconButton(
                        currentFrame = frame,
                        buttonFrame = frame,
                        person = person,
                        iconSize = 32,
                        onClick = { /*To DO*/})

                }
            }

        }
    }
}


@Composable
fun FootageHeader(
    modifier: Modifier = Modifier,
    number: Int,
    navController: NavController
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 4.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black.copy(alpha = 0.0F),
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 48.dp, height = 48.dp),
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.menuIcon),
            )
        }
        Text(
            text = stringResource(id = R.string.footageNum)+number.toString(),
            style = MaterialTheme.typography.displaySmall,
            color = MainTextColor,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun FootageToggleButton(
    person: Boolean,
    onClickPerson: () -> Unit,
    onClickObject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(40.dp)
    ) {
        OutlinedButton(
            onClick = onClickPerson,
            shape = RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 0,
                bottomStartPercent = 50,
                bottomEndPercent = 0
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (person) ButtonDarkGray else DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            if (person) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.addIcon),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(14.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.person),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
        }
        OutlinedButton(
            onClick = onClickObject,
            shape = RoundedCornerShape(
                topStartPercent = 0,
                topEndPercent = 50,
                bottomStartPercent = 0,
                bottomEndPercent = 50
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (!person) ButtonDarkGray else DarkGray,
                contentColor = Color.White,
            ),
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            if (!person) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.addIcon),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(14.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.objectButton),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
        }
    }
}

@Composable
fun FrameIconButton(
    currentFrame: Frame,
    buttonFrame: Frame,
    person: Boolean,
    iconSize: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    val active:Boolean = currentFrame == buttonFrame
    val iconIcon = when (buttonFrame) {
        Frame.Landscape -> if (person) R.drawable.person_landscape else R.drawable.object_landscape
        Frame.FullBody -> R.drawable.person_fullbody
        Frame.Body -> if (person) R.drawable.person_body else R.drawable.object_body
        Frame.Face -> R.drawable.person_face
        Frame.Detail -> if (person) R.drawable.person_detail else R.drawable.object_detail
    }
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) Color.White else ButtonDarkGray,
            contentColor = if (active) Color.Black else ButtonLightGray
        ),
        shape = CircleShape,
        contentPadding = PaddingValues( 0.dp),
        modifier = Modifier
            .padding(end = 24.dp)
            .size(iconSize.dp) //48.dp
    ) {
        Icon(
            painterResource(id = iconIcon),
            contentDescription = buttonFrame.toString(),
            modifier = Modifier.size(iconSize.dp)
        )
    }
}

@Composable
fun FootageTextMovement(
    cameraMovementVertical: CameraMovementVertical,
    cameraMovementHorizontal: CameraMovementHorizontal,
    personOrientation: PersonOrientation,
    modifier: Modifier = Modifier,
){
    var personString = when (personOrientation) {
        PersonOrientation.StaticForward -> { stringResource(id = R.string.StaticForward) }
        PersonOrientation.StaticBackward -> { stringResource(id = R.string.StaticBackward) }
        PersonOrientation.MoveLeft -> { stringResource(id = R.string.MoveLeft) }
        PersonOrientation.MoveRight -> { stringResource(id = R.string.MoveRight) }
        PersonOrientation.MoveForward -> { stringResource(id = R.string.MoveForward)}
        PersonOrientation.MoveBackward -> { stringResource(id = R.string.MoveBackward) }
        PersonOrientation.StaticLeft -> { stringResource(id = R.string.StaticLeft) }
        PersonOrientation.StaticRight -> { stringResource(id = R.string.StaticRight) }
    }
    var cameraVerticalString = when (cameraMovementVertical) {
        CameraMovementVertical.Static -> { stringResource(id = R.string.Static) }
        CameraMovementVertical.MoveUp -> {stringResource(id = R.string.MoveUp)}
        CameraMovementVertical.MoveDown -> {stringResource(id = R.string.MoveDown)}
    }
    var cameraHorizontalString = when (cameraMovementHorizontal) {
        CameraMovementHorizontal.Static -> { stringResource(id = R.string.Static) }
        CameraMovementHorizontal.Forward -> {stringResource(id = R.string.Forward)}
        CameraMovementHorizontal.Backward -> {stringResource(id = R.string.Backward)}
        CameraMovementHorizontal.Left -> {stringResource(id = R.string.Left)}
        CameraMovementHorizontal.Right -> {stringResource(id = R.string.Right)}
        CameraMovementHorizontal.PanoramicLeft -> {stringResource(id = R.string.PanoramicLeft)}
        CameraMovementHorizontal.PanoramicRight -> {stringResource(id = R.string.PanoramicRight)}
        CameraMovementHorizontal.OrbitLeft -> {stringResource(id = R.string.OrbitLeft)}
        CameraMovementHorizontal.OrbitRight -> {stringResource(id = R.string.OrbitRight)}
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row( //Person
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.person),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = personString,
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
        }
        Row( //Camera
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.camera),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            if (cameraMovementVertical == CameraMovementVertical.Static) {
                Text(
                    text = cameraHorizontalString,
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            } else{
                Text(
                    text = cameraVerticalString + ", " + cameraHorizontalString,
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun SpiderCameraVertical(
    cameraMovementVertical: CameraMovementVertical,
    modifier: Modifier = Modifier,
){
    val iconId = when (cameraMovementVertical){
        CameraMovementVertical.MoveUp -> {R.drawable.camera_vertical_up}
        CameraMovementVertical.Static -> {R.drawable.camera_vertical_static}
        CameraMovementVertical.MoveDown -> {R.drawable.camera_vertical_down}
    }
    val iconColor = if (cameraMovementVertical == CameraMovementVertical.Static) ButtonDarkGray else Color.White
    Icon(
        painterResource(id = iconId),
        contentDescription = "",
        tint = iconColor,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .height(190.dp)
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