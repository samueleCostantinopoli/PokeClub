package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class AbilityWithPokemon(

    @Embedded val ability: Ability,
    @Relation(
        parentColumn = "abilityId",
        entityColumn = "pokemonId",
        associateBy = Junction(PokemonAbilityCrossRef::class)
    )
    val pokemonDetails: List<PokemonEntity>
)