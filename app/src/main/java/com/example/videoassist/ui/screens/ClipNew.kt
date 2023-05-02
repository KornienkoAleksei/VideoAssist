package com.example.videoassist.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import com.example.videoassist.ui.theme.MainTextColor
import com.example.videoassist.ui.theme.SnackbarBackground
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClipNew(
    navController: NavController,
    currentIdClip: Int,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyColumnState = rememberLazyListState()

    var clipName by remember { mutableStateOf("") }
    var clipDescription by remember { mutableStateOf("") }
    var createNewEquipment by remember { mutableStateOf(false) }
    var selectedEquipment by remember { mutableStateOf(false) }
    var newEquipmentName by remember { mutableStateOf("") }
    var newEquipmentConditional by remember { mutableStateOf(false) }
    var clipEquipment by remember { mutableStateOf(mutableListOf<EquipmentClip>()) }
    var clipFootage by remember { mutableStateOf(mutableListOf<Footage>()) }
    var errorTextEquipment by remember { mutableStateOf(R.string.noError) }
    var errorTextName by remember { mutableStateOf(R.string.noError) }

    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()
    var currentClip by remember {
        mutableStateOf(
            ClipItemRoom(currentIdClip,"",
            clipName,clipDescription, clipFootage, clipEquipment)
        ) }
    var updateState by remember { mutableStateOf(false) }
    if (!updateState) {
        currentDatabaseClip?.let {
            currentClip = currentDatabaseClip as ClipItemRoom
            clipName = currentClip.clipName
            clipDescription = currentClip.clipDescription
            clipEquipment = currentClip.equipmentList
            updateState = true
        }
    }

    Scaffold(
        topBar = { HeaderTopAppBar(label = stringResource(id = if (currentIdClip == 0) R.string.createClip else R.string.editClip),
           navController = navController)},
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
                //check for right clip name format and save clip
                if (clipName.length in 1 .. 26){
                    errorTextName = R.string.noError
                    focusManager.clearFocus()
                    val currentDate = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(currentDate)
                    currentClip = ClipItemRoom(
                        idClip = currentIdClip,
                        creationDate = if (currentIdClip == 0) formattedDate else currentClip.creationDate,
                        clipName = clipName,
                        clipDescription = clipDescription,
                        clipFootageList = clipFootage,
                        equipmentList = clipEquipment,
                    )
                    coroutineScope.launch {
                        if (currentIdClip == 0) {
                            database.databaseDao().insertClip(currentClip)
                        } else {
                            database.databaseDao().updateClip(currentClip)
                        }
                    }
                    navController.navigate(HomeScreen.route)
                } else {
                    errorTextName = if (clipName.length > 26) R.string.errLongNameEquipment else R.string.errName
                    coroutineScope.launch {
                        lazyColumnState.scrollToItem(0)
                    }
                }
            },
            plus = false, buttonText = R.string.save)
        },
        content = {innerPadding ->
            LazyColumn(
                contentPadding = innerPadding,
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                //Clip Name Field
                item {
                    //check clip name format and throw error
                    if (errorTextName != R.string.noError ) {
                        errorTextName = if (clipName.isEmpty()) R.string.errName else R.string.noError
                    }
                    if (clipName.length > 26) errorTextName = R.string.errLongNameClip
                    InputField(
                        value = clipName,
                        onValueChange = { clipName = it; },
                        label = stringResource(id = R.string.name),
                        singleLine = true,
                        focusManager = focusManager,
                        error = errorTextName != R.string.noError,
                        errorTextResource = errorTextName
                    )
                }
                //Clip Description Field
                item {
                    InputField(
                        value = clipDescription,
                        onValueChange = { clipDescription = it; },
                        label = stringResource(id = R.string.description),
                        singleLine = false,
                        focusManager = focusManager,
                        error = false, errorTextResource = R.string.errName
                    )
                }
                //Select an equipment
                item {
                    Text(
                        text = stringResource(id = R.string.selectEquipment),
                        style = MaterialTheme.typography.titleMedium,
                        color = MainTextColor,
                        modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 16.dp)
                    )
                }
                //Draw each equipment
                if (databaseEquipment.isNotEmpty()) {
                    //add selected to the created equipment
                    if (newEquipmentConditional) {
                        val savedEquipment = savedEquipmentName(
                            databaseEquipment = databaseEquipment,
                            newEquipmentName = newEquipmentName
                        )
                        if (savedEquipment != 0) {
                            clipEquipment.add(savedEquipment)
                            newEquipmentConditional = false
                            newEquipmentName = ""
                        }
                    }
                    //draw equipment
                    items(databaseEquipment.size) { item ->
                        if (databaseEquipment[item].activeEquipment) {
                            //compare equipment in clip and database
                            selectedEquipment = false
                            var clipEquipmentIndex = -1
                            for (i in clipEquipment.indices) {
                                if (clipEquipment[i].idEquipment == databaseEquipment[item].idEquipment) {
                                    selectedEquipment = true
                                    clipEquipmentIndex = i
                                    break
                                }
                            }
                            //draw each equipment
                            SelectEquipment(value = databaseEquipment[item],
                                selectedEquipment = selectedEquipment,
                                onChecked = {
                                    selectedEquipment = it
                                    if (!selectedEquipment) {
                                        if (!equipmentUsedInClip(clipFootage, clipEquipmentIndex)) {
                                            clipEquipment.remove(
                                                EquipmentClip (idEquipment = clipEquipmentIndex)
                                            )
                                        } else {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = '"' + databaseEquipment[item].nameEquipment + context.getString(
                                                        R.string.errDeleteEquipment
                                                    ),
                                                    duration = SnackbarDuration.Short,
                                                )
                                            }
                                        }
                                    } else clipEquipment.add(databaseEquipment[item].idEquipment)
                                })
                        }
                    }
                }
                //add equipment Button
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    AddEquipmentButton(focusRequester = focusRequester,
                        onClick = { focusManager.moveFocus(FocusDirection.Down)
                            focusRequester.requestFocus()
                            createNewEquipment = true
                            coroutineScope.launch {
                                lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                            }
                        })
                }
                //New Equipment Alert Dialog
                if (createNewEquipment) {
                    item {
                        var saveEquipmentName by remember { mutableStateOf("") }
                        errorTextEquipment = checkEquipmentName(
                            newEquipmentName = newEquipmentName,
                            saveEquipmentName = saveEquipmentName,
                            errorEquipment = errorTextEquipment
                        )
                        AlertDialogEquipment(
                            focusRequester = focusRequester, focusManager = focusManager,
                            onDismiss = {
                                createNewEquipment = false
                                newEquipmentName = ""
                                errorTextEquipment = R.string.noError
                            },
                            onConfirm = {
                                val savedEquipmentDatabase = saveEquipmentToDatabase(
                                    newEquipmentName = newEquipmentName,
                                    coroutineScope = coroutineScope,
                                    databaseEquipment = databaseEquipment,
                                    database = database,
                                    errorEquipment = errorTextEquipment,
                                    lazyColumnState = lazyColumnState,
                                    focusManager = focusManager,
                                    idEquipmentInput = 0,
                                )
                                errorTextEquipment = savedEquipmentDatabase.errorTextResource
                                saveEquipmentName = savedEquipmentDatabase.saveEquipmentName
                                createNewEquipment = savedEquipmentDatabase.createNewEquipment
                                newEquipmentConditional = savedEquipmentDatabase.newEquipmentConditional
                            },
                            titleResource = R.string.newEquipment,
                            newEquipmentNameInput = newEquipmentName,
                            newEquipmentNameOutput = { newEquipmentName = it; },
                            errorTextResource = errorTextEquipment,
                        )
                    }
                }
            }
        },
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures( onTap = { focusManager.clearFocus() } )
        }
    )
}

