package com.example.videoassist

import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class ClipItemRoom (
    @PrimaryKey(autoGenerate = true) val idClip: Int,
    val creationDate: String,
    val clipName: String,
    val clipDescription: String,
)

@Entity
data class Equipment(
    @PrimaryKey(autoGenerate = true) val idEquipment: Int,
    val nameEquipment: String
)

@Entity
data class FootageItemRoom (
    @PrimaryKey(autoGenerate = true) val idFootage: Int,
    val person: Boolean,
    val personMovement: Int,
    val cameraMotion: Int,
    val notes: String,
)

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM ClipItemRoom")
    fun getAllClip() : LiveData<List<ClipItemRoom>>

    @Query("SELECT * FROM Equipment")
    fun getAllEquipment() : LiveData<List<Equipment>>

    @Insert
    fun insertClip(vararg clip: ClipItemRoom)

    @Insert
    fun insertEquipment(vararg equipment: Equipment)

}

/*
after version=1 add,
autoMigrations = [
    AutoMigration (from = 1, to = 2)
  ]
 */

@Database(entities = [ClipItemRoom::class, Equipment::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun databaseDao():  DatabaseDao
}