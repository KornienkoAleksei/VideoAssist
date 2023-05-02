package com.example.videoassist.ui.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.videoassist.R
import com.example.videoassist.ui.theme.DarkGray

@Composable
fun AlertDialogEquipment(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    titleResource: Int,
    newEquipmentNameInput: String,
    newEquipmentNameOutput: (String) -> Unit,
    errorTextResource: Int,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(
                    text = stringResource(id = R.string.save),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = titleResource),
                style = MaterialTheme.typography.displaySmall,
            )
        },
        containerColor = DarkGray,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        text = {
            Column() {
                InputField(
                    value = newEquipmentNameInput,
                    onValueChange = newEquipmentNameOutput,
                    label = stringResource(id = R.string.name),
                    singleLine = true,
                    focusManager = focusManager,
                    error = errorTextResource != R.string.noError,
                    errorTextResource = errorTextResource,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        },
        modifier = Modifier.focusRequester(focusRequester),
    )
}



@Composable
fun AlertDialogDelete(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    titleResource: Int,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
){
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(
                    text = stringResource(id = R.string.yes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(id = R.string.no),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                )
            }
        },
        title = {
            Text(
                text = stringResource(id = titleResource),
                style = MaterialTheme.typography.displaySmall,
            )
        },
        containerColor = DarkGray,
        titleContentColor = Color.White,
        textContentColor = Color.White,
        text = {
            Column() {
                Text(
                    text = stringResource(id = R.string.deleteMessage),
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                )
            }
        },
        modifier = Modifier.focusRequester(focusRequester),
    )
}


