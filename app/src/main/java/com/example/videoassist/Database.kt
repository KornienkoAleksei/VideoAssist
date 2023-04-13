package com.example.videoassist

import androidx.lifecycle.LiveData
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class ClipItemRoom (
    @PrimaryKey(autoGenerate = true) val idClip: Int,
    val creationDate: String,
    val clipName: String,
    val clipDescription: String,
    val footage: List<Footage>,
    val equipment: List<EquipmentClip>
)

@Entity
data class EquipmentRoom(
    @PrimaryKey(autoGenerate = true) val idEquipment: Int,
    val nameEquipment: String
)

enum class Frame { Landscape, FullBody, Body, Face, Detail}

enum class CameraMovementVertical { Static, MoveUp, MoveDown}

enum class CameraMovementHorizontal { Static, Forward, Backward, Left, Right,
    PanoramicLeft, PanoramicRight, OrbitLeft, OrbitRight}

enum class PersonOrientation { MoveLeft, MoveRight, MoveForward, MoveBackward,
    StaticLeft, StaticRight, StaticForward, StaticBackward}

data class Footage (
    val person: Boolean,
    val frame: Frame,
    val cameraMovementVertical: CameraMovementVertical,
    val cameraMovementHorizontal: CameraMovementHorizontal,
    val personOrientation: PersonOrientation,
    val idEquipment: Int,
    val notes: String
)

data class EquipmentClip (
    val idEquipment: Int,
    val nameEquipment: String,
    val counterEquipment: Int,
)

class Converters {
    @TypeConverter
    fun fromFootageList (value: List<Footage>): String{return Gson().toJson(value)};

    @TypeConverter
    fun toFootageList (value: String) : List<Footage>{
        val footageType = object :TypeToken<List<Footage>>() {}.type
        return Gson().fromJson(value, footageType)
    }

    @TypeConverter
    fun fromEquipmentClip (value: List<EquipmentClip>): String{return Gson().toJson(value)}

    @TypeConverter
    fun toEquipmentClip (value: String) : List<EquipmentClip>{
        val equipmentType = object : TypeToken<List<EquipmentClip>>() {}.type
        return Gson().fromJson(value, equipmentType)
    }
}

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM ClipItemRoom")
    fun getAllClip() : LiveData<List<ClipItemRoom>>

    @Query("SELECT * FROM EquipmentRoom")
    fun getAllEquipment() : LiveData<List<EquipmentRoom>>

    @Insert
    suspend fun insertClip(vararg clip: ClipItemRoom)

    @Insert
    suspend fun insertEquipment(vararg equipment: EquipmentRoom)

}

/*
after version=1 add,
autoMigrations = [
    AutoMigration (from = 1, to = 2)
  ]
 */

@Database(entities = [ClipItemRoom::class, EquipmentRoom::class], version = 1,)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun databaseDao():  DatabaseDao
}