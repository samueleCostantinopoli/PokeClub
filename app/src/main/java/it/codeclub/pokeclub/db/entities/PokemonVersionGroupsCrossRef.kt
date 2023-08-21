package it.codeclub.pokeclub.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["pokemonId", "versionGroupName"])
data class PokemonVersionGroupsCrossRef (
    val pokemonId: Int,
    @ColumnInfo(index = true)
    val versionGroupName: String
)