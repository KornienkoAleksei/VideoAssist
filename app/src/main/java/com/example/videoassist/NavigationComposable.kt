package com.example.videoassist

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun Navigation (navController: NavHostController, databaseClips: List<ClipItemRoom>, databaseEquipment: List<EquipmentRoom>, database: AppDatabase) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen.route){
        composable(HomeScreen.route){
            HomeScreen(navController = navController,  databaseClips = databaseClips)
        }
        composable(NewClip.route){
            NewClip(navController = navController, databaseEquipment=databaseEquipment, database=database)
        }
        composable(ClipScreen.route){
            ClipScreen(navController = navController)
        }
        composable(NewFootage.route){
            NewFootage(navController = navController)
        }
    }
}