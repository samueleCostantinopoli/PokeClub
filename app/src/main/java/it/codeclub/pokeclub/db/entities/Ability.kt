package it.codeclub.pokeclub.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ability (
    @PrimaryKey val abilityId: Long,
    val name_it: String,
    val effect_it: String,
    val name_en: String,
    val effect_en: String,
    val isSpecial: Boolean = false

)