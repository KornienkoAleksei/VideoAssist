package com.example.videoassist

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.videoassist.ui.screens.AddEquipmentToClip
import com.example.videoassist.ui.screens.NewFootage

@Composable
fun Navigation (navController: NavHostController,
                databaseClips: List<ClipItemRoom>,
                databaseEquipment: List<EquipmentRoom>,
                database: AppDatabase,
                ) {
    NavHost(
        navController = navController,
        startDestination = HomeScreen.route
    ) {
        composable(HomeScreen.route) {
            HomeScreen(navController = navController, databaseClips = databaseClips)
        }
        composable(NewClip.route) {
            NewClip(
                navController = navController,
                databaseEquipment = databaseEquipment,
                database = database
            )
        }
        //Clip screen
        composable("ClipScreen/{currentClip}", arguments = listOf(navArgument("currentClip") {
            defaultValue = 0
            NavType.IntType
        })) {
            val currentClip = it.arguments?.getInt("currentClip")
            currentClip?.let {
                ClipScreen(
                    navController = navController,
                    currentIdClip = currentClip,
                    database = database,
                )
            }
        }
        //New footage
        composable("NewFootage/{currentClip}", arguments = listOf(navArgument("currentClip") {
            defaultValue = 0
            NavType.IntType
        })){
            val currentClip = it.arguments?.getInt("currentClip")
            currentClip?.let {
                NewFootage(
                    navController = navController,
                    currentIdClip = currentClip,
                    database = database
                )
            }
        }
        //Add Equipment To Clip
        composable("AddEquipmentToClip/{currentClip}", arguments = listOf(navArgument("currentClip") {
            defaultValue = 0
            NavType.IntType
        })){
            val currentClip = it.arguments?.getInt("currentClip")
            currentClip?.let {
                AddEquipmentToClip(
                    navController = navController,
                    currentIdClip = currentClip,
                    databaseEquipment = databaseEquipment,
                    database = database
                )
            }
        }
    }
}