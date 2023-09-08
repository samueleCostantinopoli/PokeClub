package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonAndDetails(
    @Embedded val pokemon: PokemonWithVersionAndAbilities,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "pokemonEntityId"
    )
    val details: PokemonDetails,
)
