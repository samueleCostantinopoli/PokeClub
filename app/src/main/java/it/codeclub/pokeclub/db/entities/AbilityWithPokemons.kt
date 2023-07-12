package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import it.codeclub.pokeclub.remote.data.Pokemon

data class AbilityWithPokemons(

    @Embedded val ability: Ability,
    @Relation(
        parentColumn = "abilityId",
        entityColumn = "pokemonId",
        associateBy = Junction(PokemonAbility::class)
    )
    val pokemons: List<Pokemon>
)