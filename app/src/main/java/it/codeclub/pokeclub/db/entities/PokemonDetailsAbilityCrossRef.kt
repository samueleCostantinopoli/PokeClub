package it.codeclub.pokeclub.db.entities

import androidx.room.Entity

@Entity(primaryKeys = ["detailsId", "abilityId"])
data class PokemonDetailsAbilityCrossRef(
    val detailsId: Int,
    val abilityId: Int
)
