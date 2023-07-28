package it.codeclub.pokeclub.db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["pokemonId", "abilityId"])
data class PokemonAbilityCrossRef(
    val pokemonId: Int,
    val abilityId: Int
)
