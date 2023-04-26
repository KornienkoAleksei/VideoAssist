package com.example.videoassist.ui.blocks

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.ui.theme.DarkGray
import com.example.videoassist.ui.theme.MainTextColor

@Composable
fun SelectEquipment(
    value: EquipmentRoom,
    selectedEquipment: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp)
    ) {
        Checkbox(
                checked = selectedEquipment,
                onCheckedChange = onChecked,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.White,
                    uncheckedColor = Color.White,
                    checkmarkColor = DarkGray
                )
        )
        Text(
            text = value.nameEquipment,
            style = MaterialTheme.typography.bodyLarge,
            color = MainTextColor,
        )
    }
}