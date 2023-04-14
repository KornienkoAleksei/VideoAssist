package com.example.videoassist


import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClipScreen(
    navController: NavController,
    currentIdClip: Int,
    databaseClips: List<ClipItemRoom>,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
) {
    val currentClip by database.databaseDao().getClip(currentIdClip).observeAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        ClipHeader(
            navController = navController,
            clipName = currentClip?.clipName.toString(),
            clipDescription = currentClip?.clipDescription.toString()
        )
        if (currentClip?.footage?.isEmpty() == true) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                Icon(
                    painterResource(id = R.drawable.video_library_48px),
                    contentDescription = stringResource(id = R.string.libraryIcon),
                    tint = Color(0xFFB6B6B6),
                    modifier = Modifier.size(48.dp)
                )
                Text(
                    text = stringResource(id = R.string.noFootage),
                    style = MaterialTheme.typography.displaySmall,
                    color = Color(0xFFB6B6B6),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                item {

                }

            }

        }

        Button(
            onClick = {
                navController.navigate("NewFootage/${currentIdClip}")
            },
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(52.dp)
                .padding(horizontal = 16.dp)
            //.weight(1f)
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = stringResource(id = R.string.addIcon),
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = stringResource(id = R.string.footage),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }



}

@Composable
fun ClipHeader (
     navController: NavController,
     clipName: String,
     clipDescription: String,
     modifier: Modifier = Modifier,
){
    Column() {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
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
            IconButton(
                onClick = { /*TO DO*/ },
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
        }
        Text(text = clipName, style = MaterialTheme.typography.displaySmall,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp))
        Text(text = clipDescription, style = MaterialTheme.typography.bodyLarge,
            color = Color.White,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp))
    }
}