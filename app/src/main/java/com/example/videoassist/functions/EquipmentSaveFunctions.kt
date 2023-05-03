package com.example.videoassist.functions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import com.example.videoassist.AppDatabase
import com.example.videoassist.EquipmentRoom
import com.example.videoassist.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
class SaveEquipment (
    val errorTextResource: Int,
    val saveEquipmentName: String,
    val createNewEquipment: Boolean,
    val newEquipmentConditional: Boolean,
)

//Save Equipment to database
fun saveEquipmentToDatabase(newEquipmentName:String,
                            coroutineScope: CoroutineScope,
                            databaseEquipment: List<EquipmentRoom>,
                            database: AppDatabase,
                            errorEquipment: Int,
                            lazyColumnState: LazyListState,
                            focusManager: FocusManager,
                            idEquipmentInput: Int,
                            ): SaveEquipment {
    var errorTextResource: Int = errorEquipment
    var saveEquipmentName = ""
    var createNewEquipment = true
    var newEquipmentConditional = false
    var scrollToEquipment = 0
    if (newEquipmentName.length in 1..20) {
        var idEquipment = idEquipmentInput
        //check for existing equipment in the database
        for (equipment in databaseEquipment.indices) {
            if (idEquipment == databaseEquipment[equipment].idEquipment) {
                scrollToEquipment = equipment
            }
            if (newEquipmentName == databaseEquipment[equipment].nameEquipment) {
                if (idEquipment == databaseEquipment[equipment].idEquipment) {
                    errorTextResource = R.string.noError
                } else if (databaseEquipment[equipment].activeEquipment) {
                    errorTextResource = R.string.errAddExistingEquipment
                    break
                } else {
                    errorTextResource = R.string.errAddInactiveEquipment
                    idEquipment = databaseEquipment[equipment].idEquipment
                    scrollToEquipment = equipment
                }
            }
        }
        val newEquipment = EquipmentRoom(
            nameEquipment = newEquipmentName,
            idEquipment = idEquipment,
            activeEquipment = true,
        )
        when (errorTextResource) {
            //write new equipment
            R.string.noError -> {
                coroutineScope.launch {
                    if (idEquipment == 0) {
                        database.databaseDao().insertEquipment(newEquipment)
                        lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                    } else {
                        database.databaseDao().updateEquipment(newEquipment)
                        lazyColumnState.scrollToItem(scrollToEquipment)
                    }
                }
                createNewEquipment = false
                newEquipmentConditional = true
                focusManager.moveFocus(FocusDirection.Down)
                errorTextResource = R.string.noError
            }
            //activate inactive equipment
            R.string.errAddInactiveEquipment -> {
                coroutineScope.launch {
                    database.databaseDao().updateEquipment(newEquipment)
                    lazyColumnState.scrollToItem(lazyColumnState.layoutInfo.totalItemsCount - 1)
                }
                createNewEquipment = false
                newEquipmentConditional = true
                focusManager.moveFocus(FocusDirection.Down)
                errorTextResource = R.string.noError
            }
            //throw error "Equipment is already exist"
            else -> {
                errorTextResource = R.string.errAddExistingEquipment
                saveEquipmentName = newEquipmentName
            }
        }
        return SaveEquipment(
            errorTextResource = errorTextResource,
            saveEquipmentName = saveEquipmentName,
            createNewEquipment = createNewEquipment,
            newEquipmentConditional = newEquipmentConditional
        )
    } else {
        //throw error "Enter name" or "Name must be less than 20 characters"
        return SaveEquipment(
            errorTextResource = if (newEquipmentName.length > 20) R.string.errLongNameEquipment else R.string.errName,
            saveEquipmentName = "",
            createNewEquipment = true,
            newEquipmentConditional = false
        )
    }
}

//add selected state to the created equipment
fun savedEquipmentName(
    databaseEquipment: List<EquipmentRoom>,
    newEquipmentName: String
): EquipmentClip {
    for (equipment in databaseEquipment) {
        if (newEquipmentName == equipment.nameEquipment) {
            return EquipmentClip(
                idEquipment = equipment.idEquipment,
                nameEquipment = equipment.nameEquipment,
                0
            )
        }
    }
    return return EquipmentClip( 0, "", 0)
}

