package com.example.videoassist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.screens.commoncomposable.SaveButton
import com.example.videoassist.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewClip(
    navController: NavController,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
) {
    var clipName by remember { mutableStateOf("") }
    var clipDescription by remember { mutableStateOf("") }
    var createNewEquipment by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var equipmentName by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val equipmentSet by remember { mutableStateOf(mutableMapOf<Int, String>())}
    var selectedEquipment by remember { mutableStateOf(false) }
    var newEquipmentName by remember { mutableStateOf("") }
    var newEquipmentConditional by remember { mutableStateOf(false) }
    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }
    ) {
        val lazyColumnState = rememberLazyListState()
        LazyColumn(
            state = lazyColumnState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp)
        ) {
            //Header
            item {
                HeaderNew(
                    label = stringResource(id = R.string.createClip),
                    navController = navController
                )
            }
            //Clip Name Field
            item {
                InputField(
                    value = clipName,
                    onValueChange = { clipName = it; },
                    label = stringResource(id = R.string.name),
                    singleLine = true,
                    focusManager = focusManager,
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
                            equipmentSet[equipment.idEquipment] =
                                equipment.nameEquipment
                            databaseUpdate = true
                        }
                    }
                    if (databaseUpdate) {
                    newEquipmentConditional = false
                    newEquipmentName = ""}
                }
                //draw equipment
                items(databaseEquipment.size) { item ->
                    selectedEquipment = equipmentSet.contains(databaseEquipment[item].idEquipment)
                    SelectEquipment(value = databaseEquipment[item],
                        selectedEquipment = selectedEquipment,
                        onChecked = {
                            selectedEquipment = it
                            if (selectedEquipment) {
                                equipmentSet[databaseEquipment[item].idEquipment] =
                                    databaseEquipment[item].nameEquipment
                            } else {
                                equipmentSet.remove(databaseEquipment[item].idEquipment)
                            }
                        })
                }
            }
            //add equipment Button
            item {
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
                            )
                        },
                        modifier = Modifier.focusRequester(focusRequester),
                    )
                }
            }
        }
        //button save
        SaveButton(onClick = {
            focusManager.clearFocus()
            if (clipName !== "") {
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)
                val equipmentList = mutableListOf<EquipmentClip>()
                for (equipment in equipmentSet) {
                    val equipmentInstance = EquipmentClip (
                        idEquipment = equipment.key,
                        nameEquipment = equipment.value,
                        counterEquipment = 0,)
                    equipmentList.add(equipmentInstance)
                }
                val newClip = ClipItemRoom(
                    idClip = 0,
                    creationDate = formattedDate,
                    clipName = clipName,
                    clipDescription = clipDescription,
                    footage = emptyList(),
                    equipment = equipmentList,
                )
                coroutineScope.launch {
                    database.databaseDao().insertClip(newClip)
                }
                navController.navigateUp()
            }
        })
    }
}

@Composable
fun HeaderNew(modifier: Modifier = Modifier, label: String, navController: NavController) {
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
            text = label,
            style = MaterialTheme.typography.displaySmall,
            color = MainTextColor,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectEquipment(
    value: EquipmentRoom,
    selectedEquipment: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Checkbox(
            checked = selectedEquipment,
            onCheckedChange = onChecked,
            colors = CheckboxDefaults.colors(
                checkedColor = Color.White,
                uncheckedColor = Color.White,
                checkmarkColor = DarkGray
            )
        )
        Text(
            text = value.nameEquipment,
            style = MaterialTheme.typography.bodyLarge,
            color = MainTextColor,
        )
    }
}

@Composable
fun AddEquipmentButton(
    focusRequester: FocusRequester,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkGray,
            contentColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = ButtonGray),
        modifier = Modifier
            .padding(bottom = 32.dp)
            .padding(start = 16.dp, bottom = 16.dp)
            .focusRequester(focusRequester)

    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = stringResource(id = R.string.addIcon),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = stringResource(id = R.string.add),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}