package com.example.videoassist.ui.blocks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

import com.example.videoassist.R
import com.example.videoassist.functions.Frame
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.ButtonLightGray
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.DarkGray

@Composable
fun BottomButton(
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
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
fun FrameIconButton(
    currentFrame: Frame,
    buttonFrame: Frame,
    enabled: Boolean,
    person: Boolean,
    onClick: () -> Unit,
    modifierButton: Modifier = Modifier,
    modifierIcon: Modifier = Modifier,
){
    val active:Boolean = currentFrame == buttonFrame
    val iconIcon = when (buttonFrame) {
        Frame.Landscape -> if (person) R.drawable.person_landscape else R.drawable.object_landscape
        Frame.FullBody -> R.drawable.person_fullbody
        Frame.Body -> if (person) R.drawable.person_body else R.drawable.object_body
        Frame.Face -> R.drawable.person_face
        Frame.Detail -> if (person) R.drawable.person_detail else R.drawable.object_detail
    }
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (active) Color.White else ButtonDarkGray,
            contentColor = if (active) Color.Black else ButtonLightGray,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        enabled = enabled,
        shape = CircleShape,
        contentPadding = PaddingValues( 0.dp),
        modifier = modifierButton
    ) {
        Icon(
            painterResource(id = iconIcon),
            contentDescription = buttonFrame.toString(),
            modifier = modifierIcon
        )
    }
}

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