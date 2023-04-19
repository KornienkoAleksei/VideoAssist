package com.example.videoassist.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.videoassist.AppDatabase
import com.example.videoassist.EquipmentClip
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.SelectEquipment

@Composable
fun AddEquipmentToClip(
    navController: NavController,
    currentIdClip: Int,
    databaseEquipment: List<EquipmentRoom>,
    database: AppDatabase,
) {
    var usingEquipment = mutableListOf<EquipmentClip>()
    var checkedEquipment by remember { mutableStateOf(0) }
    var selectedEquipment by remember { mutableStateOf(false)}
    //add statement if user create new equipment

    LazyColumn() {
            if (databaseEquipment.isNotEmpty()) {
                items(databaseEquipment.size) { item ->
                    var usingEquipmentIndex: Int = 0
                    for (i in usingEquipment) {
                        if (i.idEquipment == databaseEquipment[item].idEquipment) {
                            selectedEquipment = true
                            usingEquipmentIndex = usingEquipment.indexOf(i)
                        } else {
                            selectedEquipment = false
                        }
                        selectedEquipment = i.idEquipment == databaseEquipment[item].idEquipment
                        if (selectedEquipment) break
                    }
                    SelectEquipment(value = databaseEquipment[item],
                        selectedEquipment,
                        onChecked = {
                            selectedEquipment = it
                            if (selectedEquipment) {
                                usingEquipment.add(
                                    EquipmentClip(
                                        databaseEquipment[item].idEquipment,
                                        databaseEquipment[item].nameEquipment,
                                        0
                                    )
                                )
                            } else {
                                //you can delete ONLY if EquipmentClip.counterEquipment == 0
                                usingEquipment.removeAt(usingEquipmentIndex)

                            }
                        })
                }
            }

    }
}