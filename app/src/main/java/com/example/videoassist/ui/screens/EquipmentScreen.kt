package com.example.videoassist.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.AppDatabase
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.R
import com.example.videoassist.functions.checkEquipmentName
import com.example.videoassist.functions.saveEquipmentToDatabase
import com.example.videoassist.ui.blocks.AlertDialogEquipment
import com.example.videoassist.ui.blocks.BottomButton
import com.example.videoassist.ui.blocks.HeaderTopAppBar
import com.example.videoassist.ui.blocks.NoElements
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor
import com.example.videoassist.ui.theme.TextGray
import kotlinx.coroutines.launch

@Composable
fun EquipmentScreen(
    navController: NavController,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
){
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val lazyColumnState = rememberLazyListState()
    var currentEquipmentName by remember { mutableStateOf("") }
    var currentEquipmentId by remember { mutableStateOf(0) }
    var createNewEquipment by remember { mutableStateOf(false) }
    var errorTextEquipment by remember { mutableStateOf(R.string.noError) }
    var titleResource by remember { mutableStateOf(R.string.newEquipment) }

    Scaffold(
        topBar = { HeaderTopAppBar(label = stringResource(id = R.string.equipment),
            navController = navController)
        },
        bottomBar = {
            BottomButton(plus = true, buttonText = R.string.add,
                onClick = {
                    createNewEquipment = true
                    currentEquipmentId = 0
                    currentEquipmentName =""
                    titleResource = R.string.newEquipment
                    coroutineScope.launch {
                        lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount)
                    }
                    })
        },
        content = { innerPadding ->
            if (databaseEquipment.isEmpty()){
                Box(modifier = Modifier.fillMaxSize()){
                    NoElements( iconId = R.drawable.outline_add_a_photo_24,
                        buttonNameId = R.string.noEquipment,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center))
                }
            } else {
                LazyColumn(
                    state = lazyColumnState,
                    contentPadding = innerPadding,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(databaseEquipment.size){
                        if (databaseEquipment[it].activeEquipment) {
                            EquipmentEquipmentscreen(
                                name = databaseEquipment[it].nameEquipment,
                                onEdit = {
                                    createNewEquipment = true
                                    currentEquipmentName = databaseEquipment[it].nameEquipment
                                    currentEquipmentId = databaseEquipment[it].idEquipment
                                    titleResource = R.string.editEquipment
                                    coroutineScope.launch {
                                        lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount-1)
                                    }
                                },
                                onDelete = {
                                    val equipment = EquipmentRoom(
                                        idEquipment = databaseEquipment[it].idEquipment,
                                        nameEquipment = databaseEquipment[it].nameEquipment,
                                        activeEquipment = false
                                    )
                                    coroutineScope.launch {
                                        database.databaseDao().updateEquipment(equipment)
                                    }
                                    errorTextEquipment = R.string.noError
                                })
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(2.dp))
                    }
                }
            }
            //New or Edit Equipment Alert Dialog
            if (createNewEquipment) {
                var saveEquipmentName by remember { mutableStateOf("") }
                errorTextEquipment = checkEquipmentName(
                    newEquipmentName = currentEquipmentName,
                    saveEquipmentName = saveEquipmentName,
                    errorEquipment = errorTextEquipment
                )
                AlertDialogEquipment(
                    focusRequester = focusRequester, focusManager = focusManager,
                    onDismiss = {
                        createNewEquipment = false
                        currentEquipmentName = ""
                        errorTextEquipment = R.string.noError
                    },
                    onConfirm = {
                        val save = saveEquipmentToDatabase(
                            newEquipmentName = currentEquipmentName,
                            coroutineScope = coroutineScope,
                            databaseEquipment = databaseEquipment,
                            database = database,
                            errorEquipment = errorTextEquipment,
                            lazyColumnState = lazyColumnState,
                            focusManager = focusManager,
                            idEquipmentInput = currentEquipmentId
                        )
                        errorTextEquipment = save.errorTextResource
                        saveEquipmentName = save.saveEquipmentName
                        createNewEquipment = save.createNewEquipment
                    },
                    titleResource = titleResource,
                    newEquipmentNameInput = currentEquipmentName,
                    newEquipmentNameOutput = { currentEquipmentName = it; },
                    errorTextResource = errorTextEquipment,
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
fun EquipmentEquipmentscreen(
    name: String,
    onEdit: ()-> Unit,
    onDelete: ()-> Unit,
    modifier: Modifier = Modifier,
){
    Surface(shape = RoundedCornerShape(16.dp),
        color = LightGray,
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 4.dp)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ){
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                color = MainTextColor,
            )
            
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ){
                //edit button
                IconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.0F),
                        contentColor = TextGray
                    ),
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        painterResource(id = R.drawable.edit_m_24px),
                        contentDescription = stringResource(id = R.string.editIcon),
                    )
                }
                //delete button
                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.0F),
                        contentColor = TextGray
                    ),
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        painterResource(id = R.drawable.trashcan_m_24px),
                        contentDescription = stringResource(id = R.string.deleteIcon),
                    )
                }
            }
        }
    }
}