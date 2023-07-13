package it.codeclub.pokeclub.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonDetailsAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonEntity

@Database(
    entities = [PokemonEntity::class, PokemonDetails::class, Ability::class, PokemonDetailsAbilityCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}