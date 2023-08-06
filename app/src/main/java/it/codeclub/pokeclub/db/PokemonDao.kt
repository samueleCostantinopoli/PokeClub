package it.codeclub.pokeclub.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.AbilityWithPokemon
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM PokemonEntity")
    fun getPokemonList(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM PokemonEntity WHERE isFavourite = 1")
    fun getFavourites(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM PokemonEntity WHERE isCaptured = 1")
    fun getCaptured(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM Ability")
    fun getAbilities(): Flow<List<Ability>>

    @Transaction
    @Query("SELECT * FROM Ability WHERE abilityId = :abilityId")
    fun getPokemonWithAbility(abilityId: Long): Flow<AbilityWithPokemon>

    @Transaction
    @Query("SELECT * FROM PokemonEntity WHERE name = :name")
    fun getPokemonDetails(name: String): Flow<PokemonAndDetails>

    /**
     * Used to add pokemon to favourites or to captured group
     */
    @Update
    suspend fun update(pokemonEntity: PokemonEntity)

    @Upsert
    suspend fun insert(pokemonEntity: PokemonEntity)

    @Upsert
    suspend fun insert(pokemonDetails: PokemonDetails): Long

    @Upsert
    suspend fun insert(ability: Ability)

    @Upsert
    suspend fun insert(pokemonAbilityCrossRef: PokemonAbilityCrossRef)

}