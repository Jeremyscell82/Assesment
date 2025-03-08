package com.lloydsbyte.redditassesment.network

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

class CharacterResponseModel {
    data class CharacterResponse(
        @SerializedName("results")
        val results: List<CharacterModel>
    )

    //Use for both DB and Network calls
    @Entity
    data class CharacterModel(
        @PrimaryKey(autoGenerate = true)
        val dbKey: Long,
        @ColumnInfo(name = "id")
        @SerializedName("id")
        val id: Int,
        @ColumnInfo(name = "name")
        @SerializedName("name")
        val name: String,
        @ColumnInfo(name = "status")
        @SerializedName("status")
        val status: String,
        @ColumnInfo(name = "species")
        @SerializedName("species")
        val species: String,
        @ColumnInfo(name = "image")
        @SerializedName("image")
        val imageUrl: String
    )
}