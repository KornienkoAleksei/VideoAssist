package com.example.videoassist.screens.commoncomposable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import com.example.videoassist.R
import com.example.videoassist.ui.theme.DarkGray

@Composable
fun SaveButton(
    onClick: () -> Unit,
    buttonText: Int,
    plus: Boolean,
    modifier: Modifier = Modifier
){
    Card(
        colors = CardDefaults.cardColors( containerColor = DarkGray ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, top = 8.dp)
                .height(52.dp)
                .padding(horizontal = 16.dp)
        ) {
            if (plus) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.addIcon),
                    modifier = Modifier.size(14.dp)
                )}
            Text(
                text = stringResource(id = buttonText).uppercase(),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}