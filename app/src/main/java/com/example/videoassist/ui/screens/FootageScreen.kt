package com.example.videoassist.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.*
import com.example.videoassist.R
import com.example.videoassist.functions.*
import com.example.videoassist.ui.blocks.*
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

    //footage variables
    var person by remember { mutableStateOf(true) }
    var frame by remember { mutableStateOf(Frame.Landscape) }
    var cameraMovementVertical by remember { mutableStateOf(CameraMovementVertical.Static) }
    var cameraMovementHorizontal by remember { mutableStateOf(CameraMovementHorizontal.Static) }
    var personOrientation by remember { mutableStateOf(PersonOrientation.StaticForward) }
    var selectedEquipment by remember { mutableStateOf(0)}
    var editEquipment by remember { mutableStateOf(0)}
    var footageNumber = 1
    var clipNotes by remember { mutableStateOf("") }

    // get clip from database
    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()

    //create clip instance
    var currentClip by remember {
        mutableStateOf(
            ClipItemRoom(0,"","","",
            mutableListOf<Footage>(), mutableListOf<EquipmentClip>())
        ) }

    //update clip once when the currentDatabaseClip has been received
    var updateState by remember { mutableStateOf(false) }
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

    //get Person movement if true static else movement
    val staticPersonOrientation = getPersonMovement(personOrientation = personOrientation)

    Scaffold(
        topBar = { HeaderTopAppBar(label = "${stringResource(id = R.string.footageNum)}$footageNumber",
            navController = navController)
        },
        snackbarHost = {
            SnackbarHostBlock(snackbarHostState = snackbarHostState)
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
                plus = false, buttonText = R.string.save
            )
        },
        content = {innerPadding->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
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
                FrameIconButtons(frame = frame, person = person, onButton = {frame = it })
                //Text Block
                FootageTextMovement(
                    cameraMovementVertical = cameraMovementVertical,
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    personOrientation = personOrientation
                )
                //spider
                SpiderDrawing(
                    cameraMovementVertical = cameraMovementVertical,
                    cameraMovementHorizontal = cameraMovementHorizontal,
                    personOrientation = personOrientation,
                    staticPersonOrientation = staticPersonOrientation,
                    frame = frame,
                    person = person,
                    onCameraVertical = {
                        cameraMovementVertical =
                            inverseCameraVertical(
                                cameraMovementVertical = cameraMovementVertical,
                                button = it
                            )
                    },
                    onCameraHorizontal = {
                        cameraMovementHorizontal =
                            inverseCameraHorizontal(
                                cameraMovementHorizontal = cameraMovementHorizontal,
                                button = it
                            )
                    },
                    onPerson = {
                        personOrientation = personOrientationButton(
                            personOrientation = personOrientation,
                            staticPersonOrientation = staticPersonOrientation,
                            button = it
                        )
                    },
                )
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
                    error = false, errorTextResource = R.string.errName,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp).fillMaxWidth()
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