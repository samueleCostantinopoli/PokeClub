package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

data class PokemonWithAbilities(
    @Embedded val pokemonDetails: PokemonDetails,
    @Relation(
        parentColumn = "detailsId",
        entityColumn = "abilityId",
        associateBy = Junction(PokemonDetailsAbilityCrossRef::class)
    )
    val abilities: List<Ability>
)
