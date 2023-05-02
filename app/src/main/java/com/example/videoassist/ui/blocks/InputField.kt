package com.example.videoassist.ui.blocks

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.videoassist.R
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor
import com.example.videoassist.ui.theme.SnackbarBackground

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    error: Boolean,
    errorTextResource: Int,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
){
    //val context = LocalContext.current
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
        isError = error,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth(),
        trailingIcon = {if (error) Icon(painter = painterResource(id = R.drawable.baseline_error_24), contentDescription = "error", modifier = Modifier.size(24.dp), tint = SnackbarBackground)},
        supportingText = {if (error) Text(text = stringResource(id = errorTextResource))},
        colors = TextFieldDefaults.colors(
            focusedTextColor = MainTextColor,
            unfocusedTextColor = MainTextColor,
            errorTextColor = SnackbarBackground,
            disabledTextColor = Color.Gray,

            focusedContainerColor = LightGray,
            unfocusedContainerColor = LightGray,
            disabledContainerColor = LightGray,
            errorContainerColor = LightGray,

            cursorColor = Color.White,
            errorCursorColor = Color.White,

            errorIndicatorColor = SnackbarBackground,
            errorSupportingTextColor = SnackbarBackground,

            focusedLabelColor = CaptionColor,
            unfocusedLabelColor = CaptionColor,
            errorLabelColor = SnackbarBackground,
        ))
}