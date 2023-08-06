package it.codeclub.pokeclub.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["pokemonId", "abilityId"])
data class PokemonAbilityCrossRef(
    val pokemonId: Int,
    @ColumnInfo(index = true)
    val abilityId: Int
)
