package com.example.videoassist.functions

import com.example.videoassist.ClipItemRoom
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.R

//Checking equipment name for mistakes: empty name, length and existing name
fun checkEquipmentName (newEquipmentName: String, saveEquipmentName: String, errorEquipment: Int): Int {
    return when {
        errorEquipment == R.string.errAddExistingEquipment -> {
            if (saveEquipmentName == newEquipmentName) { R.string.errAddExistingEquipment } else { R.string.noError }
        }
        errorEquipment == R.string.errName && newEquipmentName.isEmpty() -> {return R.string.errName }
        newEquipmentName.length > 20 -> {
            R.string.errLongNameEquipment }
        else -> return R.string.noError
    }
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

//return clip Equipment Index by database Equipment Id
fun getClipEquipmentIndex(
    databaseEquipmentItemId: Int,
    clipEquipment: MutableList<EquipmentClip>
): Int {
    for (i in clipEquipment.indices) {
        if (clipEquipment[i].idEquipment == databaseEquipmentItemId) {
            return i
        }
    }
    return -1
}

//build list of active Clip Equipment
fun getActiveClipEquipmentList(
    equipmentClip: List<EquipmentClip>,
    databaseEquipment: List<EquipmentRoom>
) : List<EquipmentClip> {
    var equipment = mutableListOf<EquipmentClip>()
    for (equipmentClipItem in equipmentClip) {
        for (databaseEquipmentItem in databaseEquipment) {
            if (databaseEquipmentItem.idEquipment == equipmentClipItem.idEquipment) {
                if (databaseEquipmentItem.activeEquipment) {
                    equipment.add(equipmentClipItem)
                    break
                }
            }
        }
    }
    return equipment
}