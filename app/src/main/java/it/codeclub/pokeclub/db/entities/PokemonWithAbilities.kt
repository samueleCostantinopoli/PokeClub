package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PokemonWithAbilities(
    @Embedded val pokemon: Pokemon,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "abilityId",
        associateBy = Junction(PokemonAbility::class)
    )
    val abilities: List<Ability>
)
