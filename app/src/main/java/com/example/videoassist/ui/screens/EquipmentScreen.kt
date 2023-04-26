package com.example.videoassist.ui.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.*
import com.example.videoassist.R
import com.example.videoassist.screens.commoncomposable.SaveButton
import com.example.videoassist.ui.blocks.HeaderTopAppBar
import com.example.videoassist.ui.theme.DarkGray
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor
import com.example.videoassist.ui.theme.TextGray
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentScreen(
    navController: NavController,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
){
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val focusRequester = remember { FocusRequester() }
    val lazyColumnState = rememberLazyListState()

    var currentEquipmentName by remember { mutableStateOf("") }
    var currentEquipmentId by remember { mutableStateOf(0) }
    var currentEquipmentActive by remember { mutableStateOf(true) }
    var currentEquipmentPosition by remember { mutableStateOf(-1) }
    var editState by remember { mutableStateOf(false) }
    var errorEquipment by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { HeaderTopAppBar(label = stringResource(id = R.string.equipment),
            navController = navController)
        },
        bottomBar = {
            SaveButton(plus = true, buttonText = R.string.add,
                onClick = { editState = true
                    coroutineScope.launch {
                        lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                    }})
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
                                    editState = true
                                    currentEquipmentName = databaseEquipment[it].nameEquipment
                                    currentEquipmentId = databaseEquipment[it].idEquipment
                                    coroutineScope.launch {
                                        lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
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
                                })
                        }
                    }
                    if (editState) {
                        item {
                            AlertDialog(
                                onDismissRequest = {
                                    editState = false
                                    currentEquipmentId = 0
                                    currentEquipmentName =""
                                },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            if (currentEquipmentName.isNotEmpty()) {
                                                errorEquipment = false
                                                val newEquipment = EquipmentRoom(
                                                    nameEquipment = currentEquipmentName,
                                                    idEquipment = currentEquipmentId,
                                                    activeEquipment = true,
                                                )
                                                coroutineScope.launch {
                                                    if (currentEquipmentId == 0) {
                                                        database.databaseDao().insertEquipment(newEquipment)
                                                    } else {
                                                        database.databaseDao().updateEquipment(newEquipment)
                                                    }
                                                    lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                                                    //currentEquipmentId = 0
                                                    //currentEquipmentName =""
                                                }
                                                editState = false
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
                                            editState = false;
                                            currentEquipmentId = 0
                                            currentEquipmentName =""
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
                                        if (currentEquipmentName != "") errorEquipment = false
                                        //if (errorEquipment) errorEquipment = currentEquipmentName == ""
                                        InputField(
                                            value = currentEquipmentName,
                                            onValueChange = { currentEquipmentName = it; },
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