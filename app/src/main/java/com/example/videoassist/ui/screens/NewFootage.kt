package com.example.videoassist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.videoassist.*
import com.example.videoassist.screens.commoncomposable.FrameIconButton
import com.example.videoassist.screens.commoncomposable.SaveButton
import com.example.videoassist.ui.blocks.HeaderTopAppBar
import com.example.videoassist.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun NewFootage(
    navController: NavController,
    currentIdClip: Int,
    currentIdFootage: Int,
    database: AppDatabase,
){
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()
    var currentClip by remember {
        mutableStateOf(ClipItemRoom(0,"","","",
            mutableListOf<Footage>(), mutableListOf<EquipmentClip>())) }
    var person by remember { mutableStateOf(true) }
    var frame by remember { mutableStateOf(Frame.Landscape) }
    var cameraMovementVertical by remember { mutableStateOf(CameraMovementVertical.Static) }
    var cameraMovementHorizontal by remember { mutableStateOf(CameraMovementHorizontal.Static) }
    var personOrientation by remember { mutableStateOf(PersonOrientation.StaticForward) }
    var selectedEquipment by remember { mutableStateOf(0)}
    var editEquipment by remember { mutableStateOf(0)}
    var clipNotes by remember { mutableStateOf("") }
    var updateState by remember { mutableStateOf(false) }
    var footageNumber = 1
    currentDatabaseClip?.let {
        currentClip = currentDatabaseClip as ClipItemRoom
        if (currentIdFootage == -1) {
            footageNumber += currentDatabaseClip!!.footage.size
        } else {
            footageNumber = currentIdFootage + 1
            if (!updateState) {
                person = currentClip.footage[currentIdFootage].person
                frame = currentClip.footage[currentIdFootage].frame
                cameraMovementVertical =
                    currentClip.footage[currentIdFootage].cameraMovementVertical
                cameraMovementHorizontal =
                    currentClip.footage[currentIdFootage].cameraMovementHorizontal
                personOrientation = currentClip.footage[currentIdFootage].personOrientation
                editEquipment = currentClip.footage[currentIdFootage].idEquipment
                selectedEquipment = currentClip.footage[currentIdFootage].idEquipment
                clipNotes = currentClip.footage[currentIdFootage].notes
                updateState = true
            }
        }
    }
    val staticPersonOrientation =
        when (personOrientation) { PersonOrientation.StaticRight -> true
            PersonOrientation.StaticLeft -> true
            PersonOrientation.StaticForward -> true
            PersonOrientation.StaticBackward -> true
            PersonOrientation.MoveForward -> false
            PersonOrientation.MoveBackward -> false
            PersonOrientation.MoveLeft -> false
            PersonOrientation.MoveRight -> false}

    Scaffold(
        topBar = { HeaderTopAppBar(label = "${stringResource(id = R.string.footageNum)}$footageNumber",
            navController = navController)
        },
        snackbarHost = {
            SnackbarHost(
                snackbarHostState, modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = SnackbarBackground,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = it.visuals.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
        bottomBar = {
            SaveButton(onClick = {
                if (selectedEquipment != 0) {
                    if (selectedEquipment != editEquipment && editEquipment !=0){
                        for (equipment in currentClip.equipment) {
                            if (editEquipment == equipment.idEquipment) {
                                equipment.counterEquipment--
                                break
                            }
                        }
                    }
                    for (equipment in currentClip.equipment) {
                        if (selectedEquipment == equipment.idEquipment) {
                            equipment.counterEquipment++
                            break
                        }
                    }
                    val currentFootage = Footage(
                        person = person,
                        frame = frame,
                        cameraMovementVertical = cameraMovementVertical,
                        cameraMovementHorizontal = cameraMovementHorizontal,
                        personOrientation = personOrientation,
                        idEquipment = selectedEquipment,
                        notes = clipNotes)
                    if (currentIdFootage == -1) {
                        currentClip.footage.add(currentFootage)
                    } else {
                        currentClip.footage[currentIdFootage] = currentFootage
                    }
                    coroutineScope.launch {
                        database.databaseDao().updateClip(currentClip)
                    }
                    navController.navigateUp()
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.errAddNewFootage),
                            duration = SnackbarDuration.Short,
                        )
                    }
                }},
                plus = false, buttonText = R.string.save)
        },
        content = {innerPadding->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    //.padding(top = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                // Person <-> Objects toggle button
                FootageToggleButton(
                    person = person,
                    onClickPerson = { person = true },
                    onClickObject = {
                        person = false
                        if (frame == Frame.FullBody || frame == Frame.Face) {
                            frame = Frame.Body
                        }
                    })
                //Frame Icon Buttons
                Row(modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)) {
                    FrameIconButton(
                        currentFrame = frame,
                        buttonFrame = Frame.Landscape,
                        person = person,
                        enabled = true,
                        onClick = { frame = Frame.Landscape },
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
                            onClick = { frame = Frame.FullBody },
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
                        onClick = { frame = Frame.Body },
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
                            onClick = { frame = Frame.Face },
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
                        onClick = { frame = Frame.Detail },
                        modifierButton = Modifier
                            .padding(12.dp)
                            .size(48.dp),
                        modifierIcon = Modifier.size(48.dp))
                }
                //Text Block
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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
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
                            SpiderCameraHorizontal(
                                cameraMovementHorizontal = cameraMovementHorizontal,
                                direction = true
                            )
                            SpiderCameraButtonHorizontal(
                                cameraMovementHorizontal = cameraMovementHorizontal,
                                cameraMovementHorizontalButton = CameraMovementHorizontal.OrbitLeft,
                                onClick = {
                                    cameraMovementHorizontal =
                                        if (cameraMovementHorizontal != CameraMovementHorizontal.OrbitLeft) CameraMovementHorizontal.OrbitLeft else CameraMovementHorizontal.Static
                                })
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
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
                                onClick = {
                                    cameraMovementHorizontal = CameraMovementHorizontal.Static
                                },
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
                            SpiderCameraHorizontal(
                                cameraMovementHorizontal = cameraMovementHorizontal,
                                direction = false
                            )
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
                        SpiderPersonOrientationButton(
                            personOrientation = personOrientation,
                            personOrientationButton = PersonOrientation.StaticLeft,
                            staticPersonOrientation = staticPersonOrientation,
                            onClick = {
                                personOrientation = when (personOrientation) {
                                    PersonOrientation.StaticLeft -> PersonOrientation.MoveLeft
                                    PersonOrientation.MoveLeft -> PersonOrientation.StaticLeft
                                    else -> if (staticPersonOrientation) PersonOrientation.StaticLeft else PersonOrientation.MoveLeft

                                }
                            })
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SpiderPersonOrientationButton(
                                personOrientation = personOrientation,
                                personOrientationButton = PersonOrientation.StaticForward,
                                staticPersonOrientation = staticPersonOrientation,
                                onClick = {
                                    personOrientation = when (personOrientation) {
                                        PersonOrientation.StaticForward -> PersonOrientation.MoveForward
                                        PersonOrientation.MoveForward -> PersonOrientation.StaticForward
                                        else -> if (staticPersonOrientation) PersonOrientation.StaticForward else PersonOrientation.MoveForward
                                    }
                                })
                            FrameIconButton(
                                currentFrame = frame,
                                buttonFrame = frame,
                                person = person,
                                enabled = true,
                                onClick = {
                                    personOrientation = when (personOrientation) {
                                        PersonOrientation.MoveBackward -> PersonOrientation.StaticBackward
                                        PersonOrientation.MoveForward -> PersonOrientation.StaticForward
                                        PersonOrientation.MoveLeft -> PersonOrientation.StaticLeft
                                        PersonOrientation.MoveRight -> PersonOrientation.StaticRight
                                        PersonOrientation.StaticBackward -> PersonOrientation.MoveBackward
                                        PersonOrientation.StaticForward -> PersonOrientation.MoveForward
                                        PersonOrientation.StaticLeft -> PersonOrientation.MoveLeft
                                        PersonOrientation.StaticRight -> PersonOrientation.MoveRight}},
                                modifierButton = Modifier
                                    .padding(12.dp)
                                    .size(32.dp),
                                modifierIcon = Modifier.size(32.dp))
                            SpiderPersonOrientationButton(
                                personOrientation = personOrientation,
                                personOrientationButton = PersonOrientation.StaticBackward,
                                staticPersonOrientation = staticPersonOrientation,
                                onClick = {
                                    personOrientation = when (personOrientation) {
                                        PersonOrientation.StaticBackward -> PersonOrientation.MoveBackward
                                        PersonOrientation.MoveBackward -> PersonOrientation.StaticBackward
                                        else -> if (staticPersonOrientation) PersonOrientation.StaticBackward else PersonOrientation.MoveBackward
                                    }
                                })
                        }
                        SpiderPersonOrientationButton(
                            personOrientation = personOrientation,
                            personOrientationButton = PersonOrientation.StaticRight,
                            staticPersonOrientation = staticPersonOrientation,
                            onClick = {
                                personOrientation = when (personOrientation) {
                                    PersonOrientation.StaticRight -> PersonOrientation.MoveRight
                                    PersonOrientation.MoveRight -> PersonOrientation.StaticRight
                                    else -> if (staticPersonOrientation) PersonOrientation.StaticRight else PersonOrientation.MoveRight
                                }
                            })
                    }
                }
                //Equipment
                ClipEquipmentButtons(
                    selectedEquipment = selectedEquipment,
                    equipment = currentClip.equipment,
                    onClickEquipment = { selectedEquipment = it },
                    onClickAdd = {
                        navController.navigate("FootageEquipmentScreen/${currentIdClip}")
                    })
                //notes
                InputField(
                    value = clipNotes,
                    onValueChange = { clipNotes = it; },
                    label = stringResource(id = R.string.notes),
                    singleLine = false,
                    focusManager = focusManager,
                    error = false,
                )
            }
        },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }
    )
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
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 12.dp)
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
                contentColor = Color.White,
            ),
            border = BorderStroke(width = 1.dp, color = CaptionColor),
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
            border = BorderStroke(width = 1.dp, color = CaptionColor),
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClipEquipmentButtons(
    selectedEquipment: Int,
    equipment: List<EquipmentClip>,
    onClickEquipment: (Int) -> Unit,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
){
    Text(
        text = stringResource(id = R.string.equipment),
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        repeat(equipment.size) {
            val thisEquipment = selectedEquipment == equipment[it].idEquipment
            OutlinedButton(
                onClick = { onClickEquipment(equipment[it].idEquipment) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (thisEquipment) Color.White else DarkGray,
                    contentColor = Color.White,
                ),
                border = BorderStroke(width = 1.dp, color = CaptionColor),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(32.dp),
            ) {
                if (thisEquipment) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = equipment[it].nameEquipment,
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(14.dp)
                    )
                }
                Text(
                    text = equipment[it].nameEquipment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (thisEquipment) Color.Black else Color.White,
                )
            }
        }
        repeat(1){
            OutlinedButton(
                onClick = onClickAdd,
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DarkGray,
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(32.dp),
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.addIcon),
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}