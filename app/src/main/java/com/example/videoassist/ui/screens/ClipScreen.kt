package com.example.videoassist.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.example.videoassist.*
import com.example.videoassist.R
import com.example.videoassist.screens.commoncomposable.FrameIconButton
import com.example.videoassist.screens.commoncomposable.SaveButton
import com.example.videoassist.screens.commoncomposable.TagCard
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.TextGray
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipScreen(
    navController: NavController,
    currentIdClip: Int,
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
                                onClick = { navController.navigate("NewClip/${currentIdClip}") })
                            DropdownMenuItem(
                                text = { Text( text = stringResource(id = R.string.delete),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,) },
                                onClick = {
                                    coroutineScope.launch {
                                        database.databaseDao().deleteClip(currentClip)
                                    }
                                    navController.navigateUp()
                            })
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                if (currentClip.footage.isEmpty()) {
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
            SaveButton(
                onClick = { navController.navigate("NewFootage/${currentIdClip}/${-1}") },
                plus = true, buttonText = R.string.footage)
        },
        content = {innerPadding ->
            if (currentClip.footage.isEmpty()) {
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
                            onEdit = {navController.navigate("NewFootage/${currentIdClip}/${it}")},
                            onDelete = {
                                for (equipment in currentClip.equipment) {
                                    if (currentClip.footage[it].idEquipment == equipment.idEquipment) {
                                        equipment.counterEquipment--
                                        break
                                    }
                                }
                                currentClip.footage.remove(currentClip.footage[it])
                                coroutineScope.launch {
                                    database.databaseDao().updateClip(currentClip)
                                }
                                Lifecycle.Event.ON_RESUME
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun ClipscreenFootage(
    numberFootage: Int,
    currentFootage: Footage,
    currentEquipment: String,
    onEdit: ()-> Unit,
    onDelete: ()-> Unit,
    modifier: Modifier = Modifier,
){
    var expanded by remember { mutableStateOf(false) }
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
                        onClick = { expanded = true },
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
                    DropdownMenu(expanded = expanded,
                        onDismissRequest = { expanded = false },
                        offset = DpOffset(0.dp, (-24).dp),
                        modifier = Modifier.background(ButtonDarkGray)
                    ) {
                        DropdownMenuItem(text = { Text(
                            text = stringResource(id = R.string.edit),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                        ) }, onClick = onEdit)
                        DropdownMenuItem(text = { Text(
                            text = stringResource(id = R.string.delete),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                        )  }, onClick = onDelete)
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