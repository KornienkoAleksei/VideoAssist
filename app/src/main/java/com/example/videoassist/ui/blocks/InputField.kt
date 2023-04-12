package com.example.videoassist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
){
    TextField(value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        label = {
            Text(text = label)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down);
            }
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Gray,
            textColor = MainTextColor,
            containerColor = LightGray,
            cursorColor = Color.White,
            errorCursorColor = Color.Red,
            focusedLabelColor = CaptionColor,
            unfocusedLabelColor = CaptionColor,))
}