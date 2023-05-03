package com.example.videoassist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.videoassist.functions.*
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.DarkGray

@Composable
fun FootageTextMovement(
    cameraMovementVertical: CameraMovementVertical,
    cameraMovementHorizontal: CameraMovementHorizontal,
    personOrientation: PersonOrientation,
    modifier: Modifier = Modifier,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row( //Person
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.person),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            Text(
                text = "$personOrientation",
                textAlign = TextAlign.Right,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
        }
        Row( //Camera
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.camera),
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
            )
            if (cameraMovementVertical == CameraMovementVertical.Static) {
                Text(
                    text = "$cameraMovementHorizontal",
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            } else{
                Text(
                    text = "$cameraMovementVertical, $cameraMovementHorizontal",
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun FootageToggleButton(
    person: Boolean,
    onClickPerson: () -> Unit,
    onClickObject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 12.dp)
            .height(40.dp)
    ) {
        OutlinedButton(
            onClick = onClickPerson,
            shape = RoundedCornerShape(
                topStartPercent = 50,
                topEndPercent = 0,
                bottomStartPercent = 50,
                bottomEndPercent = 0
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (person) ButtonDarkGray else DarkGray,
                contentColor = Color.White,
            ),
            border = BorderStroke(width = 1.dp, color = CaptionColor),
            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            if (person) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.addIcon),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(14.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.person),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
        }
        OutlinedButton(
            onClick = onClickObject,
            shape = RoundedCornerShape(
                topStartPercent = 0,
                topEndPercent = 50,
                bottomStartPercent = 0,
                bottomEndPercent = 50
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (!person) ButtonDarkGray else DarkGray,
                contentColor = Color.White,
            ),
            border = BorderStroke(width = 1.dp, color = CaptionColor),
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            if (!person) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = stringResource(id = R.string.addIcon),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(14.dp)
                )
            }
            Text(
                text = stringResource(id = R.string.objectButton),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ClipEquipmentButtons(
    selectedEquipment: Int,
    equipmentClip: List<EquipmentClip>,
    databaseEquipment: List<EquipmentRoom>,
    onClickEquipment: (Int) -> Unit,
    onClickAdd: () -> Unit,
    modifier: Modifier = Modifier,
){
    //create list of active Clip Equipment
    var equipment by remember { mutableStateOf(listOf<EquipmentClip>()) }
    if (equipment.isEmpty()){
        equipment = getActiveClipEquipmentList(
            equipmentClip = equipmentClip,
            databaseEquipment = databaseEquipment)
    }
    Text(
        text = stringResource(id = R.string.equipment),
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        repeat(equipment.size) {it ->
            val thisEquipment = selectedEquipment == equipment[it].idEquipment
            OutlinedButton(
                onClick = { onClickEquipment(equipment[it].idEquipment) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (thisEquipment) Color.White else DarkGray,
                    contentColor = Color.White,
                ),
                border = BorderStroke(width = 1.dp, color = CaptionColor),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(32.dp),
            ) {
                if (thisEquipment) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = equipment[it].nameEquipment,
                        tint = Color.Black,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(14.dp)
                    )
                }
                Text(
                    text = equipment[it].nameEquipment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (thisEquipment) Color.Black else Color.White,
                )
            }
        }
        repeat(1){
            OutlinedButton(
                onClick = onClickAdd,
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = DarkGray,
                    contentColor = Color.White,
                ),
                border = BorderStroke(width = 1.dp, color = CaptionColor),
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(32.dp),
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.addIcon),
                    tint = Color.White,
                    modifier = Modifier.size(12.dp)
                )
            }
        }
    }
}
