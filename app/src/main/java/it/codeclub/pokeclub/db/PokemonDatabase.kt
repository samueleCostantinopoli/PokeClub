package it.codeclub.pokeclub.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonVersionCrossRef
import it.codeclub.pokeclub.db.entities.VersionEntity

@Database(
    entities = [
        PokemonEntity::class,
        PokemonDetails::class,
        Ability::class,
        PokemonAbilityCrossRef::class,
        VersionEntity::class,
        PokemonVersionCrossRef::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}