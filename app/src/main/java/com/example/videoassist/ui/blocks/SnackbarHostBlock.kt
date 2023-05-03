package com.example.videoassist.ui.blocks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.videoassist.ui.theme.SnackbarBackground

//Snackbar Host drawing
//used in:
//      ClipNew
//      FootageScreen
//      ClipEquipmentScreen
@Composable
fun SnackbarHostBlock(
    snackbarHostState: SnackbarHostState
){
    SnackbarHost(
        snackbarHostState, modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 8.dp)
    ) {
        Card(
            shape = RoundedCornerShape(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = SnackbarBackground,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = it.visuals.message,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
