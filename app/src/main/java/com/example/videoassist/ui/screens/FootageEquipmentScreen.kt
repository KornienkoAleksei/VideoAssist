package com.example.videoassist.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.videoassist.ui.theme.SnackbarBackground
import kotlinx.coroutines.launch

@Composable
fun EquipmentScreen(
    navController: NavController,
    currentIdClip: Int,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
) {
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var createNewEquipment by remember { mutableStateOf(false) }
    var selectedEquipment by remember { mutableStateOf(false) }
    var newEquipmentName by remember { mutableStateOf("") }
    var newEquipmentConditional by remember { mutableStateOf(false) }
    var clipEquipment by remember { mutableStateOf(mutableListOf<EquipmentClip>()) }

    var currentClip by remember {
        mutableStateOf(
            ClipItemRoom(currentIdClip,"", "","", mutableListOf<Footage>(), mutableListOf<EquipmentClip>())) }

    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()

    var updateState by remember { mutableStateOf(false) }
    if (!updateState) {
        currentDatabaseClip?.let {
            currentClip = currentDatabaseClip as ClipItemRoom
            clipEquipment = currentClip.equipment
            updateState = true
        }
    }
    Scaffold(
        topBar = { HeaderTopAppBar(label = stringResource(id = R.string.editEquipment),
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
                focusManager.clearFocus()
                currentClip.equipment = clipEquipment
                coroutineScope.launch {
                        database.databaseDao().updateClip(currentClip)
                }
                navController.navigateUp()
            },
                plus = false, buttonText = R.string.save)
        },
        content = { innerPadding ->
            val lazyColumnState = rememberLazyListState()
            //add statement if user create new equipment
            LazyColumn(
                contentPadding = innerPadding,
            ) {
                if (databaseEquipment.isNotEmpty()) {
                    //add selected for new equipment
                    if (newEquipmentConditional) {
                        var databaseUpdate = false
                        for (equipment in databaseEquipment) {
                            if (newEquipmentName == equipment.nameEquipment) {
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
                            newEquipmentName = ""
                        }
                    }
                    //draw equipment
                    items(databaseEquipment.size) { item ->
                        if (databaseEquipment[item].activeEquipment) {
                            selectedEquipment = false
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
                        onClick = {
                            focusManager.moveFocus(FocusDirection.Down)
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
                        AlertDialog(
                            onDismissRequest = {
                                createNewEquipment = false
                                newEquipmentName = ""
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        if (newEquipmentName.isNotEmpty()) {
                                            val newEquipment = EquipmentRoom(
                                                nameEquipment = newEquipmentName,
                                                idEquipment = 0,
                                                activeEquipment = true,
                                            )
                                            coroutineScope.launch {
                                                database.databaseDao().insertEquipment(newEquipment)
                                            }
                                        }
                                        createNewEquipment = false
                                        newEquipmentConditional = true
                                        focusManager.moveFocus(FocusDirection.Down)
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
                                InputField(
                                    value = newEquipmentName,
                                    onValueChange = { newEquipmentName = it; },
                                    label = stringResource(id = R.string.name),
                                    singleLine = true,
                                    focusManager = focusManager,
                                    error = false,
                                )
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