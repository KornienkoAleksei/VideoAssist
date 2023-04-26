package com.example.videoassist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NoElements(
    iconId: Int,
    buttonNameId: Int,
    modifier: Modifier = Modifier,
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Icon(
            painterResource(id = iconId),
            contentDescription = stringResource(id = R.string.libraryIcon),
            tint = Color(0xFFB6B6B6),
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = stringResource(id = buttonNameId),
            style = MaterialTheme.typography.displaySmall,
            color = Color(0xFFB6B6B6),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}