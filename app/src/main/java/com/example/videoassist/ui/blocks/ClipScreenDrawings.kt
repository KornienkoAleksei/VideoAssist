package com.example.videoassist.ui.blocks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.R
import com.example.videoassist.functions.*
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.TextGray

@Composable
fun ClipScreenFootage(
    numberFootage: Int,
    currentFootage: Footage,
    databaseEquipment: List<EquipmentRoom>,
    closeMenu: Boolean,
    onEdit: ()-> Unit,
    onDelete: ()-> Unit,
    modifier: Modifier = Modifier,
){
    var expanded by remember { mutableStateOf(false) }
    if (closeMenu) expanded = false
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
                    ClipScreenCameMovement(cameraMovementVertical = currentFootage.cameraMovementVertical,
                        cameraMovementHorizontal = currentFootage.cameraMovementHorizontal)
                    ClipScreenObjectMovement(person = currentFootage.person, frame = currentFootage.frame,
                        personOrientation = currentFootage.personOrientation)
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ){
                    TagCard(label = nameEquipmentById(currentFootage.idEquipment, databaseEquipment))
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
                    color = TextGray
                )
            }

        }
    }
}

@Composable
fun ClipScreenCameMovement(
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
                Icon(
                    painterResource(id = R.drawable.camera_icon),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp))
                Icon(
                    painterResource(id = R.drawable.clipscreen_move_forward),
                    contentDescription = "",
                    tint = Color.White)
            }
            CameraMovementHorizontal.Backward -> {
                Icon(
                    painterResource(id = R.drawable.clipscreen_move_backward),
                    contentDescription = "",
                    tint = Color.White)
                Icon(
                    painterResource(id = R.drawable.camera_icon),
                    contentDescription = "",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp))
            }
            CameraMovementHorizontal.PanoramicLeft -> ClipScreenCameraMovementColumn(
                direction = true,
                iconId = R.drawable.clipscreen_move_panoramic_left
            )
            CameraMovementHorizontal.Left -> ClipScreenCameraMovementColumn(
                direction = true,
                iconId = R.drawable.clipscreen_move_left
            )
            CameraMovementHorizontal.OrbitLeft -> ClipScreenCameraMovementColumn(
                direction = true,
                iconId = R.drawable.clipscreen_move_orbit_left
            )
            CameraMovementHorizontal.PanoramicRight -> ClipScreenCameraMovementColumn(
                direction = false,
                iconId = R.drawable.clipscreen_move_panoramic_right
            )
            CameraMovementHorizontal.Right -> ClipScreenCameraMovementColumn(
                direction = false,
                iconId = R.drawable.clipscreen_move_right
            )
            CameraMovementHorizontal.OrbitRight -> ClipScreenCameraMovementColumn(
                direction = false,
                iconId = R.drawable.clipscreen_move_orbit_right
            )
        }
    }
}

@Composable
fun ClipScreenCameraMovementColumn(
    direction: Boolean, //true when left
    iconId: Int,
    modifier: Modifier = Modifier
){
    Column(verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (direction){
            Icon(
                painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
            Icon(
                painterResource(id = R.drawable.camera_icon),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(24.dp))
        } else {
            Icon(
                painterResource(id = R.drawable.camera_icon),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(24.dp))
            Icon(
                painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
        }
    }
}

@Composable
fun ClipScreenObjectMovement(
    person: Boolean,
    frame: Frame,
    personOrientation: PersonOrientation,
    modifier: Modifier = Modifier
){
    when(personOrientation){
        PersonOrientation.StaticRight ->
            ClipScreenObjectMovementColumn( direction = false,
                iconId = R.drawable.clipscreen_static_left_right,
                frame = frame, person = person)
        PersonOrientation.MoveRight ->
            ClipScreenObjectMovementColumn( direction = false,
                iconId = R.drawable.clipscreen_move_right,
                frame = frame, person = person)
        PersonOrientation.StaticLeft ->
            ClipScreenObjectMovementColumn( direction = true,
                iconId = R.drawable.clipscreen_static_left_right,
                frame = frame, person = person)
        PersonOrientation.MoveLeft ->
            ClipScreenObjectMovementColumn( direction = true,
                iconId = R.drawable.clipscreen_move_left,
                frame = frame, person = person)
        PersonOrientation.StaticForward ->
            ClipScreenObjectMovementRow( direction = true,
                iconId = R.drawable.clipscreen_static_forward_backward,
                frame = frame, person = person)
        PersonOrientation.MoveForward ->
            ClipScreenObjectMovementRow( direction = true,
                iconId = R.drawable.clipscreen_move_backward,
                frame = frame, person = person)
        PersonOrientation.StaticBackward ->
            ClipScreenObjectMovementRow( direction = false,
                iconId = R.drawable.clipscreen_static_forward_backward,
                frame = frame, person = person)
        PersonOrientation.MoveBackward ->
            ClipScreenObjectMovementRow( direction = false,
                iconId = R.drawable.clipscreen_move_forward,
                frame = frame, person = person)
    }
}

@Composable
fun ClipScreenObjectMovementRow(
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
            Icon(
                painterResource(id = iconId),
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
            Icon(
                painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
        }
    }
}

@Composable
fun ClipScreenObjectMovementColumn(
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
            Icon(
                painterResource(id = iconId),
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
            Icon(
                painterResource(id = iconId),
                contentDescription = "",
                tint = Color.White)
        }
    }
}