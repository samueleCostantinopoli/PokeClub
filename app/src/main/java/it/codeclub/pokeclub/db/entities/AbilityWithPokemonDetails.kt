package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AbilityWithPokemonDetails(

    @Embedded val ability: Ability,
    @Relation(
        parentColumn = "abilityId",
        entityColumn = "detailsId",
        associateBy = Junction(PokemonDetailsAbilityCrossRef::class)
    )
    val pokemonDetails: List<PokemonDetails>
)