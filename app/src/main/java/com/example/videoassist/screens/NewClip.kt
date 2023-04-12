package com.example.videoassist

import android.media.audiofx.DynamicsProcessing.Eq
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.videoassist.ui.theme.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewClip(
    navController: NavController,
    databaseClips: List<ClipItemRoom>,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
    //lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    var clipName by remember { mutableStateOf("") };
    var clipDescription by remember { mutableStateOf("") };
    var createNewEquipment by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var equipmentName by remember { mutableStateOf("") };
    val focusManager = LocalFocusManager.current;
    var focusState by remember { mutableStateOf(false) };
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTapGestures(
                onTap = { focusManager.clearFocus() }
            )
        }
        ) {
        val lazyColumnState = rememberLazyListState();
            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp)
                    
            ) {
                item {
                    HeaderNew(
                        label = stringResource(id = R.string.createClip),
                        navController = navController
                    );
                }
                item {
                    InputField(
                        value = clipName,
                        onValueChange = { clipName = it; },
                        label = stringResource(id = R.string.name),
                        singleLine = true,
                        focusManager = focusManager,
                    );
                }
                item {
                    InputField(
                        value = clipDescription,
                        onValueChange = { clipDescription = it; },
                        label = stringResource(id = R.string.description),
                        singleLine = false,
                        focusManager = focusManager,
                    )
                }
                item {
                    Text(
                        text = stringResource(id = R.string.selectEquipment),
                        style = MaterialTheme.typography.titleMedium,
                        color = MainTextColor,
                        modifier = Modifier.padding(start = 16.dp, top = 32.dp, bottom = 16.dp)
                    )
                }
                if (databaseEquipment.isNotEmpty()) {
                    items(databaseEquipment.size) { item ->
                        SelectEquipment(value = databaseEquipment[item])
                    }
                }
                item {
                    OutlinedButton(
                        onClick = {
                            focusManager.clearFocus();
                            focusRequester.requestFocus();
                            createNewEquipment = true;
                        },
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

                if (createNewEquipment) {
                    item {
                        AlertDialog(
                            onDismissRequest = {
                                createNewEquipment = false;
                                equipmentName = "";
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    if (equipmentName.isNotEmpty()) {
                                        val newEquipment = EquipmentRoom(
                                            nameEquipment = equipmentName,
                                            idEquipment = 0
                                        );
                                        coroutineScope.launch {
                                            database.databaseDao().insertEquipment(newEquipment)
                                        }
                                        equipmentName = "";
                                    }
                                    createNewEquipment = false;
                                }) {
                                    Text(
                                        text = stringResource(id = R.string.save),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { createNewEquipment = false; }) {
                                    Text(
                                        text = stringResource(id = R.string.cancel),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White
                                    )
                                }
                            },
                            title = {
                                Text(
                                    text = stringResource(id = R.string.newEquipment),
                                    style = MaterialTheme.typography.displaySmall
                                )
                            },
                            containerColor = DarkGray,
                            titleContentColor = Color.White,
                            textContentColor = Color.White,
                            text = {
                                InputField(
                                    value = equipmentName,
                                    onValueChange = { equipmentName = it; },
                                    label = stringResource(id = R.string.name),
                                    singleLine = true,
                                    focusManager = focusManager,
                                )
                            }
                        )
                    }
                }

            }
        Button( //DO NOT TOUCH!!!!!!!!!!!
            onClick = { focusManager.clearFocus() },
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(52.dp)
                .padding(horizontal = 16.dp)
                //.weight(1f)
        ) {
            Text(
                text = stringResource(id = R.string.saveCap),
                style = MaterialTheme.typography.labelLarge
            )
        }
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

@Composable
fun SelectEquipment(
    value: EquipmentRoom,
    modifier: Modifier = Modifier
) {
    Row(modifier = Modifier.fillMaxWidth()
        .padding(16.dp)) {
        Text(text = value.nameEquipment,
            style = MaterialTheme.typography.bodyLarge,
            color = MainTextColor,)
    }
}