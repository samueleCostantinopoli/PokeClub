package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * This entity is used within the main or list view
 */
data class PokemonWithVersionGroupsAndAbilities(

    @Embedded val pokemonEntity: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "versionGroupName",
        associateBy = Junction(PokemonVersionGroupsCrossRef::class)
    )
    val versionGroups: List<VersionGroupEntity>,

    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "abilityId",
        associateBy = Junction(PokemonAbilityCrossRef::class)
    )
    val abilities: List<Ability>
)