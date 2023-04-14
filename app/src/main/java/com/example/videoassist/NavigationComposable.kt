package com.example.videoassist

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

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
        composable("ClipScreen/{currentClip}", arguments = listOf(navArgument("currentClip") {
            defaultValue = 0
            NavType.IntType
        })) {
            val currentClip = it.arguments?.getInt("currentClip")
            currentClip?.let {
                ClipScreen(
                    navController = navController,
                    currentIdClip = currentClip,
                    databaseClips = databaseClips,
                    databaseEquipment = databaseEquipment,
                    database = database,
                )
            }
        }
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
    }
}