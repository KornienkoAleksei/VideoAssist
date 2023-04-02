package com.example.videoassist

import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.LightGray
import com.example.videoassist.ui.theme.MainTextColor

@Composable
fun NewClip(
    navController: NavController
){

    var clipName by remember { mutableStateOf("") };
    var clipDescription by remember { mutableStateOf("") };
   Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .align(Alignment.TopCenter)) {
            HeaderNew(
                label = stringResource(id = R.string.createClip),
                navController = navController
            );
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                InputField(
                    value = clipName,
                    onValueChange = { clipName = it; },
                    label = stringResource(id = R.string.name),
                    singleLine = true
                );
                InputField(
                    value = clipDescription,
                    onValueChange = { clipDescription = it; },
                    label = stringResource(id = R.string.description),
                    singleLine = false
                )
                Text(text = stringResource(id = R.string.selectEquipment),
                    style = MaterialTheme.typography.titleMedium,
                    color = MainTextColor,
                    modifier = Modifier.padding(start = 16.dp, top = 32.dp))
                /*
                eqipment here
                 */


            }



        }
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(52.dp)
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.labelLarge
            )
        }

    }



}

@Composable
fun HeaderNew(modifier: Modifier = Modifier, label: String, navController: NavController) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 4.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black.copy(alpha = 0.0F),
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 48.dp, height = 48.dp),
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = stringResource(id = R.string.menuIcon),
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.displaySmall,
            color = MainTextColor,
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean,
    modifier: Modifier = Modifier
){
    val focusManager = LocalFocusManager.current;
    var focusState by remember { mutableStateOf(false) };
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