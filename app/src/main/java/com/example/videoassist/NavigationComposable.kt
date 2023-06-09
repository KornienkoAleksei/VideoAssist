package com.example.videoassist

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.videoassist.ui.ClipNew
import com.example.videoassist.ui.screens.*

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
            HomeScreen(navController = navController, databaseClips = databaseClips, databaseEquipment = databaseEquipment)
        }
        //New Clip
        composable("ClipNew/{currentIdClip}", arguments = listOf(navArgument("currentIdClip") {
            defaultValue = 0
            NavType.IntType
        })) {
            val currentIdClip = it.arguments?.getInt("currentIdClip")
            currentIdClip?.let {
                ClipNew(
                    navController = navController,
                    currentIdClip = currentIdClip,
                    databaseEquipment = databaseEquipment,
                    database = database
                )
            }
        }
        //Clip screen
        composable("ClipScreen/{currentIdClip}", arguments = listOf(navArgument("currentIdClip") {
            defaultValue = 0
            NavType.IntType
        })) {
            val currentIdClip = it.arguments?.getInt("currentIdClip")
            currentIdClip?.let {
                ClipScreen(
                    navController = navController,
                    currentIdClip = currentIdClip,
                    databaseEquipment = databaseEquipment,
                    database = database,
                )
            }
        }
        //New footage
        composable("FootageScreen/{currentIdClip}/{currentIdFootage}", arguments = listOf(
            navArgument("currentIdClip") {
            defaultValue = 0
            NavType.IntType},
            navArgument("currentIdFootage") {
                defaultValue = 0
                NavType.IntType},
        )){
            val currentIdClip = it.arguments?.getInt("currentIdClip")
            val currentIdFootage = it.arguments?.getInt("currentIdFootage")
            currentIdClip?.let {
                currentIdFootage?.let {
                    FootageScreen(
                        navController = navController,
                        currentIdClip = currentIdClip,
                        currentIdFootage = currentIdFootage,
                        databaseEquipment = databaseEquipment,
                        database = database
                    )
                }
            }
        }
        //Footage Equipment Screen
        composable("ClipEquipmentScreen/{currentIdClip}", arguments = listOf(navArgument("currentIdClip") {
            defaultValue = 0
            NavType.IntType
        })){
            val currentIdClip = it.arguments?.getInt("currentIdClip")
            currentIdClip?.let {
                ClipEquipmentScreen(
                    navController = navController,
                    currentIdClip = currentIdClip,
                    databaseEquipment = databaseEquipment,
                    database = database
                )
            }
        }
        //EquipmentScreen
        composable(EquipmentScreen.route) {
            EquipmentScreen(
                navController = navController,
                databaseEquipment = databaseEquipment,
                database = database
            )
        }
    }
}