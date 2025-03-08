package com.lloydsbyte.redditassesment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lloydsbyte.redditassesment.network.CharacterResponseModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacters(characters: List<CharacterResponseModel.CharacterModel>)

    @Query("SELECT * FROM charactermodel")
    fun getAllCharacters(): Flow<List<CharacterResponseModel.CharacterModel>>

    @Query("SELECT COUNT(*) FROM charactermodel")
    fun countItems(): Int

}