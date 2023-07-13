package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Relation

data class PokemonAndDetails(
    @Embedded val pokemon: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "detailsId"
    )
    val details: PokemonDetails
)
