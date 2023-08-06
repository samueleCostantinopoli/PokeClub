package it.codeclub.pokeclub.db.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonEntity(
    @PrimaryKey val pokemonId: Int,
    val name: String,
    val type: PokemonType,
    @ColumnInfo(defaultValue = "null")
    val secondType: PokemonType? = null,
    var isFavourite: Boolean = false,   // Pokemon not favourite by default
    var isCaptured: Boolean = false,   // Pokemon not favourite by default
    var dominantColor: Int = 0xfffffff,  // White as default sprite dominant color
    @ColumnInfo(defaultValue = "Not provided")
    val imageUrl: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, defaultValue = "0")
    val image: Bitmap? = null
)