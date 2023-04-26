package com.example.videoassist.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.screens.commoncomposable.TagCard
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.videoassist.*
import com.example.videoassist.R
import com.example.videoassist.ui.theme.DarkGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    databaseClips: List<ClipItemRoom>,
    database: AppDatabase,
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
                    Column(modifier = Modifier
                        .background(LightGray)
                        .fillMaxSize()) {
                        Spacer(Modifier.height(16.dp))
                        IconButton(
                            onClick = { coroutineScope.launch { drawerState.close() } },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_close_24),
                                contentDescription = "Close menu",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(Modifier.height(16.dp))
                        NavigationDrawerItem(
                            label = { Text(
                                text = stringResource(id = R.string.clips),
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White) },
                            selected = true,
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = LightGray,
                                selectedContainerColor = LightGray,

                            ),
                        )
                        Spacer(Modifier.height(8.dp))
                        NavigationDrawerItem(
                            label = { Text(
                                text = stringResource(id = R.string.equipment),
                                style = MaterialTheme.typography.displaySmall,
                                color = Color.White) },
                            selected = true,
                            onClick = {
                                coroutineScope.launch { drawerState.close() }
                                navController.navigate("EquipmentScreen")
                            },
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = LightGray,
                                selectedContainerColor = LightGray,

                                ),
                        )
                    }
                }
            }

        },
        content = {
            Scaffold(
                topBar = { Header(onClick = { coroutineScope.launch { drawerState.open() } }, noClip = databaseClipsIsEmpty ) },
                floatingActionButtonPosition = FabPosition.End,
                floatingActionButton = {
                    Button(
                        onClick = { navController.navigate("NewClip/${0}") },
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
                        Box(modifier = Modifier.fillMaxSize()) {
                            Image(
                                painter = painterResource(id = R.drawable.welcomescreenimage),
                                contentDescription = stringResource(id = R.string.welcomeScreenImage),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Alignment.Center)
                            )
                            Column(
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.greetings),
                                    style = MaterialTheme.typography.displayLarge,
                                    color = MainTextColor,
                                    modifier = Modifier
                                        .fillMaxWidth(0.65f)
                                        .padding(start = 32.dp, bottom = 48.dp)
                                )
                                Text(
                                    text = stringResource(id = R.string.greetingsBody),
                                    style = MaterialTheme.typography.displayMedium,
                                    color = CaptionColor,
                                    modifier = Modifier
                                        .fillMaxWidth(0.65f)
                                        .padding(start = 32.dp, bottom = 147.dp)
                                )
                            }
                        }
                    } else {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    onClick: () -> Unit,
    noClip: Boolean,
    modifier: Modifier = Modifier) {
    TopAppBar(
        title = {Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MainTextColor,
        )},
        navigationIcon = {
            IconButton(onClick = onClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Black.copy( alpha = 0.0F,),
                    contentColor = Color.White
                ),
                modifier = Modifier.size(width = 48.dp, height = 48.dp),) {
                Icon(Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.menuIcon),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (noClip) Color.DarkGray.copy( alpha = 0.0F,) else DarkGray
        ),
        modifier = Modifier
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClipList (
    clipItemRoom: ClipItemRoom,
    navController: NavController,
    modifier: Modifier = Modifier
){
    Surface(onClick = { navController.navigate("ClipScreen/${clipItemRoom.idClip}") },
        shape = RoundedCornerShape(16.dp),
        color = LightGray,
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 8.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 52.dp)
            ) {
                Icon(
                    painterResource(id = R.drawable.movie_48px),
                    contentDescription = stringResource(id = R.string.movieIcon),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = clipItemRoom.creationDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = CaptionColor,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            Text(
                text = clipItemRoom.clipName, style = MaterialTheme.typography.displaySmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(clipItemRoom.equipment.size) {
                    if (clipItemRoom.equipment[it].counterEquipment != 0) {
                        TagCard(
                            label = clipItemRoom.equipment[it].nameEquipment + ": " + clipItemRoom.equipment[it].counterEquipment.toString(),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
            }
        }
    }
}