package it.codeclub.pokeclub.db

import it.codeclub.pokeclub.db.entities.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemon(): Flow<List<Pokemon>>

    suspend fun getPokemonDetails(name: String): Flow<Pokemon>
}