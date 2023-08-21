package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PokemonWithVersionGroups(

    @Embedded val pokemonEntity: PokemonEntity,
    @Relation(
        parentColumn = "pokemonId",
        entityColumn = "versionGroupName",
        associateBy = Junction(PokemonVersionGroupsCrossRef::class)
    )
    val versionGroups: List<VersionGroupEntity>
)