package com.example.videoassist.screens.commoncomposable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.videoassist.Frame
import com.example.videoassist.R
import com.example.videoassist.ui.theme.ButtonDarkGray
import com.example.videoassist.ui.theme.ButtonLightGray

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