package it.codeclub.pokeclub.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ability (
    @PrimaryKey val abilityId: Int,
    val name: String,
    val effect: String,
    val isSpecial: Boolean = false
)