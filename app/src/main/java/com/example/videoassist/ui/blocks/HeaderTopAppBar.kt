package com.example.videoassist.ui.blocks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.R
import com.example.videoassist.ui.theme.MainTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderTopAppBar(
    label: String,
    navController: NavController,
    modifier: Modifier = Modifier
){
        TopAppBar(
            title = {
                Text(
                text = label,
                style = MaterialTheme.typography.displaySmall,
                color = MainTextColor,
            )
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Black.copy( alpha = 0.0F,),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(width = 48.dp, height = 48.dp),) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.menuIcon),
                    )
                }
            },
            modifier = Modifier
        )
}