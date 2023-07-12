package it.codeclub.pokeclub.db.entities

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import it.codeclub.pokeclub.remote.data.Pokemon

@Entity
data class Pokemon(
    @PrimaryKey val pokemonId: Int,
    val name: String,
    val type: PokemonType,
    val secondType: PokemonType? = null,
    val isFavourite: Boolean = false,   // Pokemon not favourite by default
    val isCaptured: Boolean = false,   // Pokemon not favourite by default
    val dominantColor: Int = 0xfffffff,  // White as default sprite dominant color
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val data: Bitmap? = null,
    val height: Double? = 0.0,
    val weight: Double? = 0.0,
    val lp: Int? = 0,
    val attack: Int? = 0,
    val defense: Int? = 0,
    val spAttack: Int? = 0,
    val spDefense: Int? = 0,
    val speed: Int? = 0,
)