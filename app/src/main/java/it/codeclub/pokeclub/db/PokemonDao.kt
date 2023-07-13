package it.codeclub.pokeclub.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonWithAbilities
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM PokemonEntity")
    fun getPokemonList(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM PokemonEntity WHERE isFavourite = 1")
    fun getFavourites(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM PokemonEntity WHERE isCaptured = 1")
    fun getCaptured(): Flow<List<PokemonEntity>>

    @Transaction
    @Query("SELECT * FROM PokemonDetails WHERE name = :name")
    fun getPokemonDetails(name: String): List<PokemonWithAbilities>

    /**
     * Used to add pokemon to favourites or to captured group
     */
    @Update
    fun update(pokemon: PokemonEntity)

}