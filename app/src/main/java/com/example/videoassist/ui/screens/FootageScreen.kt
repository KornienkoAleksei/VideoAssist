package com.example.videoassist

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.functions.*
import com.example.videoassist.ui.blocks.*
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.SnackbarBackground
import kotlinx.coroutines.launch

@Composable
fun FootageScreen(
    navController: NavController,
    currentIdClip: Int,
    currentIdFootage: Int,
    databaseEquipment: List<EquipmentRoom>,
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
            footageNumber += currentDatabaseClip!!.clipFootageList.size
        } else {
            footageNumber = currentIdFootage + 1
            if (!updateState) {
                person = currentClip.clipFootageList[currentIdFootage].person
                frame = currentClip.clipFootageList[currentIdFootage].frame
                cameraMovementVertical = currentClip.clipFootageList[currentIdFootage].cameraMovementVertical
                cameraMovementHorizontal = currentClip.clipFootageList[currentIdFootage].cameraMovementHorizontal
                personOrientation = currentClip.clipFootageList[currentIdFootage].personOrientation
                editEquipment = currentClip.clipFootageList[currentIdFootage].idEquipment
                selectedEquipment = currentClip.clipFootageList[currentIdFootage].idEquipment
                clipNotes = currentClip.clipFootageList[currentIdFootage].notes
                updateState = true
            }
        }
    }
    val staticPersonOrientation = staticPersonOrientationFun(personOrientation = personOrientation)

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
            BottomButton(onClick = {
                if (selectedEquipment != 0) {
                    val currentFootage = Footage(
                        person = person,
                        frame = frame,
                        cameraMovementVertical = cameraMovementVertical,
                        cameraMovementHorizontal = cameraMovementHorizontal,
                        personOrientation = personOrientation,
                        idEquipment = selectedEquipment,
                        notes = clipNotes)
                    if (currentIdFootage == -1) {
                        currentClip.clipFootageList.add(currentFootage)
                    } else {
                        currentClip.clipFootageList[currentIdFootage] = currentFootage
                    }
                    coroutineScope.launch {
                        database.databaseDao().updateClip(currentClip)
                    }
                    navController.navigateUp()
                } else {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = context.getString(R.string.selectEquipment),
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
                    databaseEquipment = databaseEquipment,
                    equipmentClip = currentClip.equipmentList,
                    onClickEquipment = { selectedEquipment = it },
                    onClickAdd = {
                        navController.navigate("ClipEquipmentScreen/${currentIdClip}")
                    })
                //notes
                InputField(
                    value = clipNotes,
                    onValueChange = { clipNotes = it; },
                    label = stringResource(id = R.string.notes),
                    singleLine = false,
                    focusManager = focusManager,
                    error = false, errorTextResource = R.string.errName
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