package com.example.videoassist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.videoassist.ui.theme.CaptionColor
import com.example.videoassist.ui.theme.MainTextColor

@Composable
fun HomeScreen(
    navController: NavController,
    databaseClips: List<ClipItemRoom>
){
if (databaseClips.isEmpty()){
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.welcomescreenimage),
            contentDescription = stringResource(id = R.string.welcomeScreenImage),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )

        Column(verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxHeight()
            .align(Alignment.TopStart)) {
            Text(text = stringResource(id = R.string.greetings),
                style = MaterialTheme.typography.displayLarge,
                color = MainTextColor,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(start = 32.dp, bottom = 48.dp))
            Text(text = stringResource(id = R.string.greetingsBody),
                style = MaterialTheme.typography.displayMedium,
                color = CaptionColor,
                modifier = Modifier
                    .fillMaxWidth(0.65f)
                    .padding(start = 32.dp, bottom = 147.dp))
        }
        Header(modifier = Modifier.align(Alignment.TopCenter))
        Button(onClick = { navController.navigate(NewClip.route) },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors( containerColor = Color.White, contentColor = Color.Black),
            modifier = Modifier
                .padding(bottom = 24.dp, end = 16.dp)
                .size(84.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.Add,
                contentDescription = stringResource(id = R.string.addIcon),
                modifier = Modifier.size(36.dp)
            )
        }
    }
} else{
    Text(text = stringResource(id = R.string.app_name),
        color = Color.Magenta)
}


}

@Composable
fun Header(modifier: Modifier = Modifier) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 4.dp)) {
        IconButton(onClick = { /*TODO*/ },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black.copy( alpha = 0.0F,),
                contentColor = Color.White
            ),
            modifier = Modifier.size(width = 48.dp, height = 48.dp),) {
            Icon(Icons.Default.Menu,
                contentDescription = stringResource(id = R.string.menuIcon),
               )
        }
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displaySmall,
            color = MainTextColor,
            modifier = Modifier.padding(start = 6.dp)
        )
    }

}