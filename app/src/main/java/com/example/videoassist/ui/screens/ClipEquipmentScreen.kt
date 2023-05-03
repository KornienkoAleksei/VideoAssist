package com.example.videoassist.ui.screens

import android.util.Log
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
import com.example.videoassist.functions.*
import com.example.videoassist.ui.blocks.*
import com.example.videoassist.ui.theme.DarkGray
import com.example.videoassist.ui.theme.SnackbarBackground
import kotlinx.coroutines.launch

@Composable
fun ClipEquipmentScreen(
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
    var clipFootage by remember { mutableStateOf(mutableListOf<Footage>()) }
    var errorTextEquipment by remember { mutableStateOf(R.string.noError) }

    var currentClip by remember {
        mutableStateOf(
            ClipItemRoom(currentIdClip,"", "","", mutableListOf<Footage>(), mutableListOf<EquipmentClip>())) }

    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()

    var updateState by remember { mutableStateOf(false) }
    if (!updateState) {
        currentDatabaseClip?.let {
            currentClip = currentDatabaseClip as ClipItemRoom
            clipEquipment = currentClip.equipmentList
            clipFootage = currentClip.clipFootageList
            updateState = true
        }
    }
    Scaffold(
        topBar = { HeaderTopAppBar(label = stringResource(id = R.string.editEquipment),
            navController = navController)
        },
        snackbarHost = { SnackbarHostBlock(snackbarHostState = snackbarHostState) },
        bottomBar = {
            BottomButton(onClick = {
                focusManager.clearFocus()
                currentClip.equipmentList = clipEquipment
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
                    //add selected to the created equipment
                    if (newEquipmentConditional) {
                        val savedEquipment = savedEquipmentName(
                            databaseEquipment = databaseEquipment,
                            newEquipmentName = newEquipmentName
                        )
                        if (savedEquipment.idEquipment != 0) {
                            clipEquipment.add(savedEquipment)
                            newEquipmentConditional = false
                            newEquipmentName = ""
                        }
                    }
                    //draw equipment
                    items(databaseEquipment.size) { item ->
                        if (databaseEquipment[item].activeEquipment) {
                            //find out clipEquipmentIndex
                            //where databaseIdEquipment are equal clipEquipmentId
                            var clipEquipmentIndex = getClipEquipmentIndex(
                                databaseEquipmentItemId = databaseEquipment[item].idEquipment,
                                clipEquipment = clipEquipment
                            )
                            //when equipment don`t used in clip clipEquipmentIndex == -1
                            selectedEquipment = clipEquipmentIndex != -1
                            //draw each equipment
                            SelectEquipment(value = databaseEquipment[item],
                                selectedEquipment = selectedEquipment,
                                onChecked = {
                                    selectedEquipment = it
                                    val nameEquip = databaseEquipment[item].nameEquipment
                                    Log.i("aleks", "$nameEquip : $selectedEquipment")
                                    if (!selectedEquipment) {
                                        //check there aren`t footage with this equipment
                                        //if true - remove clipEquipment else- show ShackBar
                                        if (!equipmentUsedInClip( clipFootage, databaseEquipment[item].idEquipment)) {
                                            clipEquipment.remove(clipEquipment[clipEquipmentIndex])
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
                                    } else {
                                        clipEquipment.add(
                                            EquipmentClip(
                                                databaseEquipment[item].idEquipment,
                                                databaseEquipment[item].nameEquipment,
                                                0
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
                                newEquipmentConditional =
                                    savedEquipmentDatabase.newEquipmentConditional
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