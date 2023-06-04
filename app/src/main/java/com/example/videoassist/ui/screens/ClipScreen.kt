package com.example.videoassist.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.videoassist.*
import com.example.videoassist.R
import com.example.videoassist.functions.*
import com.example.videoassist.ui.blocks.*
import com.example.videoassist.ui.drawings.ClipScreenFootage
import com.example.videoassist.ui.theme.LightGray
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipScreen(
    navController: NavController,
    currentIdClip: Int,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
) {
    val coroutineScope = rememberCoroutineScope()
    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()
    var currentClip by remember {
        mutableStateOf(ClipItemRoom(0,"","","", mutableListOf<Footage>(), mutableListOf<EquipmentClip>())) }
    currentDatabaseClip?.let {
        currentClip = currentDatabaseClip as ClipItemRoom
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    var clipDelete by remember { mutableStateOf(false)}
    var footageDelete by remember { mutableStateOf(-1)}
    var closeMenu by remember { mutableStateOf(false)}
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column() {
                MediumTopAppBar(
                    title = { Text(text = currentClip.clipName, style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                    )},
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Black.copy( alpha = 0.0F,),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(width = 48.dp, height = 48.dp),) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.menuIcon),
                            )}},
                    actions = {
                        var expanded by remember { mutableStateOf(false) }
                        IconButton(
                            onClick = { expanded = true },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Black.copy(alpha = 0.0F),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.size(width = 48.dp, height = 48.dp),
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = stringResource(id = R.string.menuIcon),
                            )
                        }
                        DropdownMenu(expanded = expanded,
                            onDismissRequest = { expanded = false },
                            offset = DpOffset(0.dp, (-48).dp),
                            modifier = Modifier.background(LightGray)
                        ) {
                            DropdownMenuItem(
                                text = { Text(
                                    text = stringResource(id = R.string.edit),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,) },
                                onClick = { navController.navigate("ClipNew/${currentIdClip}") })
                            DropdownMenuItem(
                                text = { Text( text = stringResource(id = R.string.delete),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,) },
                                onClick = { clipDelete = true})
                            if (clipDelete){
                                AlertDialogDelete(
                                    onDismiss = {
                                        clipDelete = false
                                        expanded = false
                                                },
                                    onConfirm = { coroutineScope.launch {
                                        database.databaseDao().deleteClip(currentClip)
                                    }
                                        navController.navigateUp() },
                                    titleResource = R.string.deleteClip,
                                    focusRequester = focusRequester
                                )
                            }
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
                if (currentClip.clipFootageList.isEmpty()) {
                    Text(
                        text = currentClip.clipDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        },
        bottomBar = {
            BottomButton(
                onClick = { navController.navigate("FootageScreen/${currentIdClip}/${-1}") },
                plus = true, buttonText = R.string.footage)
        },
        content = {innerPadding ->
            if (currentClip.clipFootageList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()){
                    NoElements( iconId = R.drawable.video_library_48px,
                        buttonNameId = R.string.noFootage,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center))
                }
            } else {
                val lazyColumnState = rememberLazyListState()
                LazyColumn(
                    state = lazyColumnState,
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (currentClip.clipDescription.isNotEmpty()) {
                        item {
                            Text(
                                text = currentClip.clipDescription,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    items(currentClip.clipFootageList.size) {
                        ClipScreenFootage(
                            numberFootage = it,
                            currentFootage = currentClip.clipFootageList[it],
                            closeMenu = closeMenu,
                            databaseEquipment = databaseEquipment,
                            onEdit = {navController.navigate("FootageScreen/${currentIdClip}/${it}")},
                            onDelete = {
                                footageDelete = it
                                coroutineScope.launch {
                                    lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                                }
                            }
                        )
                    }
                    if (footageDelete != -1) {
                        item {
                            AlertDialogDelete(
                                onDismiss = { coroutineScope.launch {
                                        lazyColumnState.scrollToItem(footageDelete)
                                        footageDelete = -1
                                    closeMenu = true
                                }},
                                onConfirm = {
                                    currentClip.clipFootageList.remove(currentClip.clipFootageList[footageDelete])
                                    coroutineScope.launch { database.databaseDao().updateClip(currentClip) }
                                    footageDelete = -1
                                    Lifecycle.Event.ON_RESUME
                                },
                                titleResource = R.string.deleteFootage,
                                focusRequester = focusRequester
                            )
                        }
                    }
                }
            }
        }
    )
}

