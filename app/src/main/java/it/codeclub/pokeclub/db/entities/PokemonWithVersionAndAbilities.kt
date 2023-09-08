package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * This entity is used within the main or list view
 */
data class PokemonWithVersionAndAbilities(

    @Embedded val pokemonEntity: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "versionGroupName",
        associateBy = Junction(PokemonVersionCrossRef::class)
    )
    val versionGroups: List<VersionEntity>,

    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "abilityId",
        associateBy = Junction(PokemonAbilityCrossRef::class)
    )
    val abilities: List<Ability>
)