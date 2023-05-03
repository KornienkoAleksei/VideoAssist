package com.example.videoassist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.ClipList
import com.example.videoassist.DrawerHomeScreen
import com.example.videoassist.HeaderHomeScreen
import com.example.videoassist.NoClipHomeScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    databaseClips: List<ClipItemRoom>,
    databaseEquipment: List<EquipmentRoom>,
) {
    val coroutineScope = rememberCoroutineScope()
    var drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val databaseClipsIsEmpty = databaseClips.isEmpty()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(modifier = Modifier.fillMaxWidth(0.75f)
                ) {
                ModalDrawerSheet(){
                    DrawerHomeScreen(
                        onClickClose = { coroutineScope.launch { drawerState.close() } },
                        onClickEquipment = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("EquipmentScreen")
                        }
                    )
                }
            }

        },
        content = {
            Scaffold(
                topBar = { HeaderHomeScreen(onClick = { coroutineScope.launch { drawerState.open() } }, noClip = databaseClipsIsEmpty ) },
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    Button(
                        onClick = { navController.navigate("ClipNew/${0}") },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier
                            .padding(bottom = 24.dp, end = 16.dp)
                            .size(84.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.addIcon),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                content = { innerPadding ->
                    if (databaseClipsIsEmpty) {
                        // draw welcome screen, when there aren't clips
                        NoClipHomeScreen()
                    } else {
                        //draw home screen with clips
                        val lazyColumnState = rememberLazyListState();
                        LazyColumn(
                            contentPadding = innerPadding,
                            state = lazyColumnState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (!databaseClipsIsEmpty) {
                                items(databaseClips.size) {
                                    ClipList(
                                        clipItemRoom = databaseClips[databaseClips.size - 1 - it],
                                        databaseEquipment = databaseEquipment,
                                        navController = navController
                                    );
                                }
                            }
                        }
                    }
                }
            )
        }
    )
}