package com.example.videoassist.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.*
import com.example.videoassist.R
import com.example.videoassist.screens.commoncomposable.SaveButton
import com.example.videoassist.ui.blocks.AddEquipmentButton
import com.example.videoassist.ui.blocks.HeaderTopAppBar
import com.example.videoassist.ui.blocks.SelectEquipment
import com.example.videoassist.ui.theme.DarkGray
import com.example.videoassist.ui.theme.MainTextColor
import com.example.videoassist.ui.theme.SnackbarBackground
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewClip(
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

    var clipName by remember { mutableStateOf("") }
    var clipDescription by remember { mutableStateOf("") }
    var createNewEquipment by remember { mutableStateOf(false) }
    var selectedEquipment by remember { mutableStateOf(false) }
    var newEquipmentName by remember { mutableStateOf("") }
    var newEquipmentConditional by remember { mutableStateOf(false) }
    var clipEquipment by remember { mutableStateOf(mutableListOf<EquipmentClip>()) }
    var clipFootage by remember { mutableStateOf(mutableListOf<Footage>()) }
    var errorName by remember { mutableStateOf(false) }
    var errorEquipment by remember { mutableStateOf(false) }

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
            clipEquipment = currentClip.equipment
            clipFootage = currentClip.footage
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
            SaveButton(onClick = {
                focusManager.clearFocus()
                if (clipName !== "") {
                    val currentDate = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                    val formattedDate = dateFormat.format(currentDate)
                    currentClip = ClipItemRoom(
                        idClip = currentIdClip,
                        creationDate = if (currentIdClip == 0) formattedDate else currentClip.creationDate,
                        clipName = clipName,
                        clipDescription = clipDescription,
                        footage = clipFootage,
                        equipment = clipEquipment,
                    )
                    coroutineScope.launch {
                        if (currentIdClip == 0) {
                            database.databaseDao().insertClip(currentClip)

                        } else {
                            database.databaseDao().updateClip(currentClip)
                        }
                    }
                    navController.navigateUp()
                } else {
                    errorName = true
                }
            },
            plus = false, buttonText = R.string.save)
        },
        content = {innerPadding ->
            val lazyColumnState = rememberLazyListState()
            LazyColumn(
                contentPadding = innerPadding,
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                //Clip Name Field
                item {
                    if (clipName != "") errorName = false
                    InputField(
                        value = clipName,
                        onValueChange = { clipName = it; },
                        label = stringResource(id = R.string.name),
                        singleLine = true,
                        focusManager = focusManager,
                        error = errorName,
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
                        error = false,
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
                    //add selected for new equipment
                    if (newEquipmentConditional){
                        var databaseUpdate = false
                        for (equipment in databaseEquipment){
                            if (newEquipmentName == equipment.nameEquipment){
                                clipEquipment.add(
                                    EquipmentClip(
                                        idEquipment = equipment.idEquipment,
                                        nameEquipment = equipment.nameEquipment,
                                        counterEquipment = 0
                                    )
                                )
                                databaseUpdate = true
                                break
                            }
                        }
                        if (databaseUpdate) {
                            newEquipmentConditional = false
                            newEquipmentName = ""}
                    }
                    //draw equipment
                    items(databaseEquipment.size) { item ->
                        //selectedEquipment = equipmentMap.contains(databaseEquipment[item].idEquipment)

                        if (databaseEquipment[item].activeEquipment) {
                            selectedEquipment = false
                            var selectedEquipmentName = ""
                            var clipEquipmentIndex = -1
                            for (i in clipEquipment.indices) {
                                if (clipEquipment[i].idEquipment == databaseEquipment[item].idEquipment) {
                                    selectedEquipment = true
                                    clipEquipmentIndex = i
                                    break
                                }
                            }
                            SelectEquipment(value = databaseEquipment[item],
                                selectedEquipment = selectedEquipment,
                                onChecked = {
                                    selectedEquipment = it
                                    if (!selectedEquipment) {
                                        if (clipEquipment[clipEquipmentIndex].counterEquipment == 0) {
                                            clipEquipment.remove(clipEquipment[clipEquipmentIndex])
                                        } else {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = '"' + clipEquipment[clipEquipmentIndex].nameEquipment + context.getString(
                                                        R.string.errDeleteEquipment
                                                    ),
                                                    duration = SnackbarDuration.Short,
                                                )
                                            }
                                        }
                                    } else {
                                        clipEquipment.add(
                                            EquipmentClip(
                                                idEquipment = databaseEquipment[item].idEquipment,
                                                nameEquipment = databaseEquipment[item].nameEquipment,
                                                counterEquipment = 0
                                            )
                                        )
                                    }
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
                            errorEquipment = false
                        })
                }
                //New Equipment Alert Dialog
                if (createNewEquipment) {
                    item {
                        AlertDialog(
                            onDismissRequest = {
                                createNewEquipment = false
                                newEquipmentName = ""
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        if (newEquipmentName.isNotEmpty()) {
                                            errorEquipment = false
                                            val newEquipment = EquipmentRoom(
                                                nameEquipment = newEquipmentName,
                                                idEquipment = 0,
                                                activeEquipment = true,
                                            )
                                            coroutineScope.launch {
                                                database.databaseDao().insertEquipment(newEquipment)
                                                lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                                            }
                                            createNewEquipment = false
                                            newEquipmentConditional = true
                                            focusManager.moveFocus(FocusDirection.Down)
                                        } else {
                                            errorEquipment = true
                                        }
                                    },
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.save),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        createNewEquipment = false;
                                        newEquipmentName = ""
                                    },
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.cancel),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White,
                                    )
                                }
                            },
                            title = {
                                Text(
                                    text = stringResource(id = R.string.newEquipment),
                                    style = MaterialTheme.typography.displaySmall,
                                )
                            },
                            containerColor = DarkGray,
                            titleContentColor = Color.White,
                            textContentColor = Color.White,
                            text = {
                                Column() {
                                    if (newEquipmentName != "") errorEquipment = false
                                    //if (errorEquipment) errorEquipment = newEquipmentName == ""
                                    InputField(
                                        value = newEquipmentName,
                                        onValueChange = { newEquipmentName = it; },
                                        label = stringResource(id = R.string.name),
                                        singleLine = true,
                                        focusManager = focusManager,
                                        error = errorEquipment,
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                            },
                            modifier = Modifier.focusRequester(focusRequester),
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

