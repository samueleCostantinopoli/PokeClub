package it.codeclub.pokeclub.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ability (
    @PrimaryKey val abilityId: Long,
    val nameIt: String,
    val effectIt: String,
    val nameEn: String,
    val effectEn: String,
    val isSpecial: Boolean = false

)