package com.example.videoassist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.ClipItemRoom
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.R
import com.example.videoassist.functions.EquipmentClip
import com.example.videoassist.functions.equipmentInClip
import com.example.videoassist.ui.blocks.TagCard
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.DarkGray
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor

// draw welcome screen, when there aren't clips
@Composable
fun NoClipHomeScreen(){
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
}

// Home screen header
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderHomeScreen(
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
                Icon(
                    Icons.Default.Menu,
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

//clip drawing home screen
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClipList (
    clipItemRoom: ClipItemRoom,
    databaseEquipment: List<EquipmentRoom>,
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
        //create list of equipment which used in footage
        var clipEquipmentUsed by remember { mutableStateOf(listOf<EquipmentClip>()) }
        if (clipEquipmentUsed.isEmpty()){
            clipEquipmentUsed = equipmentInClip(
                clipItemRoom = clipItemRoom,
                databaseEquipment = databaseEquipment
            )
        }
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
                repeat(clipEquipmentUsed.size) {
                    TagCard(
                        label = clipEquipmentUsed[it].nameEquipment + ": " + clipEquipmentUsed[it].counterEquipment.toString(),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                }
            }

        }
    }
}

//Home screen drawer
@Composable
fun DrawerHomeScreen(
    onClickClose: () -> Unit,
    onClickEquipment: () -> Unit
){
    Column(modifier = Modifier
        .background(LightGray)
        .fillMaxSize()) {
        Spacer(Modifier.height(16.dp))
        IconButton(
            onClick = onClickClose,
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
            onClick = onClickClose,
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
            onClick = onClickEquipment,
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = LightGray,
                selectedContainerColor = LightGray,
                ),
        )
    }
}