package it.codeclub.pokeclub.db

import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonWithAbilities
import it.codeclub.pokeclub.utils.Resource
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemon(): Flow<List<PokemonEntity>>
    suspend fun getFavourites(): Flow<List<PokemonEntity>>
    suspend fun getCaptured(): Flow<List<PokemonEntity>>
    suspend fun getPokemonDetails(name: String): Resource<PokemonWithAbilities>
    suspend fun update(pokemon: PokemonEntity)
}