package it.codeclub.pokeclub.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import it.codeclub.pokeclub.db.entities.Pokemon

import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    /**
     * Queries only Pokemon names, types, favourites and dominant colors
     */
    @Query("SELECT name, pokemonId, type, secondType, isFavourite, isCaptured, dominantColor FROM pokemon")
    fun getPokemonList(): Flow<List<Pokemon>>

    /**
     * Queries all the details of a given Pokemon by its name
     */
    @Query("SELECT * FROM pokemon WHERE name = :name")
    fun getPokemonDetails(name: String): Flow<Pokemon>

    @Query("SELECT * FROM pokemon WHERE isFavourite = 1")
    fun getFavourites(): Flow<List<Pokemon>>

    /**
     * Adds a Pokemon to favourites
     */
    @Update
    fun addToFavourites(pokemon: Pokemon)

    /**
     * Removes a Pokemon from favourites
     */
    @Update
    fun removeFromFavourites(pokemon: Pokemon)
}