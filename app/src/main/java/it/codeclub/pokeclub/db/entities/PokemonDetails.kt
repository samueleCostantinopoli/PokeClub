package it.codeclub.pokeclub.db.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonDetails(
    @PrimaryKey val detailsId: Int,
    @Embedded val pokemon: PokemonEntity,
    val pokemonEntityId: Int,
    val height: Double,
    val weight: Double,
    val lp: Int,
    val attack: Int,
    val defense: Int,
    val spAttack: Int,
    val spDefense: Int,
    val speed: Int,
)