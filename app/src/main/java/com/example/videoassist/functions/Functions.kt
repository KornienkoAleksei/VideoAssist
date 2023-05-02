package com.example.videoassist.functions

import com.example.videoassist.ClipItemRoom
import com.example.videoassist.EquipmentRoom

fun staticPersonOrientationFun (
    personOrientation: PersonOrientation
): Boolean {
    return  when (personOrientation) { PersonOrientation.StaticRight -> true
        PersonOrientation.StaticLeft -> true
        PersonOrientation.StaticForward -> true
        PersonOrientation.StaticBackward -> true
        PersonOrientation.MoveForward -> false
        PersonOrientation.MoveBackward -> false
        PersonOrientation.MoveLeft -> false
        PersonOrientation.MoveRight -> false}
}

//return true if equipment used in existing footage
fun equipmentUsedInClip (
    clipFootageList: MutableList<Footage>,
    equipmentId:Int
): Boolean {
    for (footage in clipFootageList) {
        if (footage.idEquipment == equipmentId) {
            return true
        }
    }
    return false
}

//return equipment name by id
fun nameEquipmentById(
    equipmentId: Int,
    databaseEquipment: List<EquipmentRoom>,
): String {
    for (equipment in databaseEquipment){
        if (equipment.idEquipment == equipmentId)
            return equipment.nameEquipment
    }
    return ""
}

//return list equipment with names which use in footage
fun equipmentInClip(
    clipItemRoom: ClipItemRoom,
    databaseEquipment: List<EquipmentRoom>,
) : List<EquipmentClip> {
    var clipEquipmentUsed = mutableListOf<EquipmentClip>()
    for (footage in clipItemRoom.clipFootageList){
        FindElement@ for (databaseEquipmentItem in databaseEquipment){
            if (databaseEquipmentItem.idEquipment == footage.idEquipment){
                //add first element to list
                if (clipEquipmentUsed.isEmpty()){
                    clipEquipmentUsed.add(
                        EquipmentClip(databaseEquipmentItem.idEquipment, databaseEquipmentItem.nameEquipment, 1)
                    )
                    break@FindElement
                }
                // second and others
                for (equipment in clipEquipmentUsed) {
                    if (equipment.idEquipment == databaseEquipmentItem.idEquipment) {
                        var counter = equipment.counterEquipment
                        clipEquipmentUsed.remove(equipment)
                        clipEquipmentUsed.add(
                            EquipmentClip(databaseEquipmentItem.idEquipment, databaseEquipmentItem.nameEquipment, ++counter)
                               )
                        break@FindElement
                    }
                }
                clipEquipmentUsed.add(
                    EquipmentClip(databaseEquipmentItem.idEquipment, databaseEquipmentItem.nameEquipment, 1)
                )
                break@FindElement
            }
        }
    }
    return clipEquipmentUsed
}