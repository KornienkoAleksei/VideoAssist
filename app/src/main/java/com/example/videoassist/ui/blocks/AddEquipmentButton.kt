package com.example.videoassist.ui.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.videoassist.R
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.DarkGray

@Composable
fun AddEquipmentButton(
    focusRequester: FocusRequester,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = DarkGray,
            contentColor = Color.White
        ),
        border = BorderStroke(width = 1.dp, color = CaptionColor),
        modifier = Modifier
            .padding(start = 16.dp, bottom = 16.dp)
            .focusRequester(focusRequester)

    ) {
        Icon(
            Icons.Default.Add,
            contentDescription = stringResource(id = R.string.addIcon),
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = stringResource(id = R.string.add),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}