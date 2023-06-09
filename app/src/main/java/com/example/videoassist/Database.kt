package com.example.videoassist

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.videoassist.functions.EquipmentClip
import com.example.videoassist.functions.Footage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity
data class ClipItemRoom (
    @PrimaryKey(autoGenerate = true) val idClip: Int,
    val creationDate: String,
    var clipName: String,
    var clipDescription: String,
    var clipFootageList: MutableList<Footage>,
    var equipmentList: MutableList<EquipmentClip>
)

@Entity
data class EquipmentRoom(
    @PrimaryKey(autoGenerate = true) val idEquipment: Int,
    val nameEquipment: String,
    var activeEquipment: Boolean
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

    @Query("SELECT * FROM ClipItemRoom WHERE idClip = :idClip")
    fun getClip(vararg idClip: Int) : LiveData<ClipItemRoom>

    @Query("SELECT * FROM EquipmentRoom")
    fun getAllEquipment() : LiveData<List<EquipmentRoom>>

    @Insert
    suspend fun insertClip(vararg clip: ClipItemRoom)

    @Insert
    suspend fun insertEquipment(vararg equipment: EquipmentRoom)

    @Update
    suspend fun updateClip(vararg clip: ClipItemRoom)

    @Update
    suspend fun updateEquipment(vararg equipment: EquipmentRoom)

    @Delete
    suspend fun deleteClip(vararg clip: ClipItemRoom)
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