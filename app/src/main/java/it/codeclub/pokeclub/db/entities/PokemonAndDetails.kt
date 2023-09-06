package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PokemonAndDetails(
    @Embedded val pokemon: PokemonWithVersionGroupsAndAbilities,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonEntityId"
    )
    val details: PokemonDetails,
)
