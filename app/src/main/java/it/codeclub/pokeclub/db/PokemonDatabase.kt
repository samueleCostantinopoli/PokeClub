package it.codeclub.pokeclub.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.codeclub.pokeclub.db.entities.Pokemon

@Database(
    entities = [Pokemon::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
}