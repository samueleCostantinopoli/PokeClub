package it.codeclub.pokeclub.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VersionEntity (
    @PrimaryKey val versionGroupName: String
)