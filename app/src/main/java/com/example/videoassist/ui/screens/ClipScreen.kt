package com.example.videoassist


import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.screens.commoncomposable.FrameIconButton
import com.example.videoassist.screens.commoncomposable.TagCard
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor
import com.example.videoassist.ui.theme.TextGray
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClipScreen(
    navController: NavController,
    currentIdClip: Int,
    database: AppDatabase,
) {
    val currentDatabaseClip by database.databaseDao().getClip(currentIdClip).observeAsState()
    var currentClip by remember {
        mutableStateOf(ClipItemRoom(0,"","","", emptyList<Footage>(), emptyList<EquipmentClip>())) }
    currentDatabaseClip?.let {
        currentClip = currentDatabaseClip as ClipItemRoom
    }
    Column(modifier = Modifier.fillMaxSize()) {
        if (currentClip.footage.isEmpty()) {
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(top = 16.dp)){
                ClipHeader(
                    navController = navController,
                    clipName = currentClip.clipName,
                    clipDescription = currentClip.clipDescription
                )
                ClipscreenNoFootage( modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center))
            }
        } else {
            val lazyColumnState = rememberLazyListState()
            LazyColumn(
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                item {
                    ClipHeader(
                        navController = navController,
                        clipName = currentClip.clipName,
                        clipDescription = currentClip.clipDescription
                    )
                }
                    items(currentClip.footage.size) {
                        var equipmentName: String = ""
                        for (equipment in currentClip.equipment){
                            if (equipment.idEquipment == currentClip.footage[it].idEquipment) {
                                equipmentName = equipment.nameEquipment
                                break
                            }
                        }
                        ClipscreenFootage(
                            numberFootage = it,
                            currentFootage = currentClip.footage[it],
                            currentEquipment = equipmentName,
                            onClick = { /*TODO*/ })
                    }
            }
        }
        //add footage button
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
                //.align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(52.dp)
                .padding(horizontal = 16.dp)
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
        if (clipDescription.isNotEmpty()) {
            Text(
                text = clipDescription, style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }
    }
}

@Composable
fun ClipscreenNoFootage(
    modifier: Modifier = Modifier,

){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
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
}

@Composable
fun ClipscreenFootage(
    numberFootage: Int,
    currentFootage: Footage,
    currentEquipment: String,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier,
){
    Surface(shape = RoundedCornerShape(16.dp),
        color = LightGray,
        contentColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = "#${numberFootage + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextGray,
                    )
                    ClipscreenCameMovement(cameraMovementVertical = currentFootage.cameraMovementVertical,
                        cameraMovementHorizontal = currentFootage.cameraMovementHorizontal)
                    ClipscreenObjectMovement(person = currentFootage.person, frame = currentFootage.frame,
                        personOrientation = currentFootage.personOrientation)
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ){
                    TagCard(label = currentEquipment)
                    IconButton(
                        onClick = { /*TO DO*/ },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black.copy(alpha = 0.0F),
                            contentColor = TextGray
                        ),
                        modifier = Modifier.size(24.dp),
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = stringResource(id = R.string.menuIcon),
                        )
                    }
                }
            }
            if (currentFootage.notes.isNotEmpty()){
                Text(text = currentFootage.notes,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextGray)
            }

        }
    }
}

@Composable
fun ClipscreenCameMovement(
    cameraMovementVertical: CameraMovementVertical,
    cameraMovementHorizontal: CameraMovementHorizontal,
    modifier: Modifier = Modifier,
){
    Row(verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),) {
        when (cameraMovementVertical) {
            CameraMovementVertical.Static -> {}
            CameraMovementVertical.MoveUp -> Icon(
                painterResource(id = R.drawable.clipscreen_move_up),
                contentDescription = "",
                tint = Color.White)
            CameraMovementVertical.MoveDown -> Icon(
                painterResource(id = R.drawable.clipscreen_move_down),
                contentDescription = "",
                tint = Color.White)
        }
        when (cameraMovementHorizontal) {
            CameraMovementHorizontal.Static -> Icon(
                painterResource(id = R.drawable.camera_icon),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(24.dp))
            CameraMovementHorizontal.Forward -> {
                Icon(painterResource(id = R.drawable.camera_icon),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp))
                Icon(painterResource(id = R.drawable.clipscreen_move_forward),
                    contentDescription = "",
                    tint = Color.White)
            }
            CameraMovementHorizontal.Backward -> {
                Icon(painterResource(id = R.drawable.clipscreen_move_backward),
                    contentDescription = "",
                    tint = Color.White)
                Icon(painterResource(id = R.drawable.camera_icon),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp))
            }
            CameraMovementHorizontal.PanoramicLeft -> ClipscreenCameraMovementColumn(
                direction = true,
                iconId = R.drawable.clipscreen_move_panoramic_left
            )
            CameraMovementHorizontal.Left -> ClipscreenCameraMovementColumn(
                direction = true,
                iconId = R.drawable.clipscreen_move_left
            )
            CameraMovementHorizontal.OrbitLeft -> ClipscreenCameraMovementColumn(
                direction = true,
                iconId = R.drawable.clipscreen_move_orbit_left
            )
            CameraMovementHorizontal.PanoramicRight -> ClipscreenCameraMovementColumn(
                direction = false,
                iconId = R.drawable.clipscreen_move_panoramic_right
            )
            CameraMovementHorizontal.Right -> ClipscreenCameraMovementColumn(
                direction = false,
                iconId = R.drawable.clipscreen_move_right
            )
            CameraMovementHorizontal.OrbitRight -> ClipscreenCameraMovementColumn(
                direction = false,
                iconId = R.drawable.clipscreen_move_orbit_right
            )
        }
    }
}

@Composable
fun ClipscreenCameraMovementColumn(
    direction: Boolean, //true when left
    iconId: Int,
    modifier: Modifier = Modifier
){
  Column(verticalArrangement = Arrangement.spacedBy(4.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
  ) {
      if (direction){
          Icon(painterResource(id = iconId),
              contentDescription = "",
              tint = Color.White)
          Icon(painterResource(id = R.drawable.camera_icon),
              contentDescription = "",
              tint = Color.White,
          modifier = Modifier.size(24.dp))
      } else {
          Icon(painterResource(id = R.drawable.camera_icon),
              contentDescription = "",
              tint = Color.White,
              modifier = Modifier.size(24.dp))
          Icon(painterResource(id = iconId),
              contentDescription = "",
              tint = Color.White)
      }
  }
}

@Composable
fun ClipscreenObjectMovement(
    person: Boolean,
    frame: Frame,
    personOrientation: PersonOrientation,
    modifier: Modifier = Modifier
){
    when(personOrientation){
        PersonOrientation.StaticRight ->
            ClipscreenObjectMovementColumn( direction = false,
                iconId = R.drawable.clipscreen_static_left_right,
                frame = frame, person = person)
        PersonOrientation.MoveRight ->
            ClipscreenObjectMovementColumn( direction = false,
                iconId = R.drawable.clipscreen_move_right,
                frame = frame, person = person)
        PersonOrientation.StaticLeft ->
            ClipscreenObjectMovementColumn( direction = true,
                iconId = R.drawable.clipscreen_static_left_right,
                frame = frame, person = person)
        PersonOrientation.MoveLeft ->
            ClipscreenObjectMovementColumn( direction = true,
                iconId = R.drawable.clipscreen_move_left,
                frame = frame, person = person)
        PersonOrientation.StaticForward ->
            ClipscreenObjectMovementRow( direction = true,
                iconId = R.drawable.clipscreen_static_forward_backward,
                frame = frame, person = person)
        PersonOrientation.MoveForward ->
            ClipscreenObjectMovementRow( direction = true,
                iconId = R.drawable.clipscreen_move_backward,
                frame = frame, person = person)
        PersonOrientation.StaticBackward ->
            ClipscreenObjectMovementRow( direction = false,
                iconId = R.drawable.clipscreen_static_forward_backward,
                frame = frame, person = person)
        PersonOrientation.MoveBackward ->
            ClipscreenObjectMovementRow( direction = false,
                iconId = R.drawable.clipscreen_move_forward,
                frame = frame, person = person)
    }
}

@Composable
fun ClipscreenObjectMovementRow(
    direction: Boolean, //true when forward
    iconId: Int,
    frame: Frame,
    person: Boolean,
    modifier: Modifier = Modifier
){
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (direction){
            Icon(painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
            FrameIconButton(
                currentFrame = frame,
                buttonFrame = frame,
                enabled = false,
                person = person,
                onClick = { /*TODO*/ },
                modifierButton = Modifier.size(32.dp),
                modifierIcon = Modifier.size(32.dp))
        } else {
            FrameIconButton(
                currentFrame = frame,
                buttonFrame = frame,
                enabled = false,
                person = person,
                onClick = { /*TODO*/ },
                modifierButton = Modifier.size(32.dp),
                modifierIcon = Modifier.size(32.dp))
            Icon(painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
        }
    }
}

@Composable
fun ClipscreenObjectMovementColumn(
    direction: Boolean, //true when left
    iconId: Int,
    frame: Frame,
    person: Boolean,
    modifier: Modifier = Modifier
){
    Column(verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (direction){
            Icon(painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
            FrameIconButton(
                currentFrame = frame,
                buttonFrame = frame,
                enabled = false,
                person = person,
                onClick = { /*TODO*/ },
                modifierButton = Modifier.size(32.dp),
                modifierIcon = Modifier.size(32.dp))
        } else {
            FrameIconButton(
                currentFrame = frame,
                buttonFrame = frame,
                enabled = false,
                person = person,
                onClick = { /*TODO*/ },
                modifierButton = Modifier.size(32.dp),
                modifierIcon = Modifier.size(32.dp))
            Icon(painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
        }
    }
}