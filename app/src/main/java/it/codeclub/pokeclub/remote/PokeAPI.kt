package it.codeclub.pokeclub.remote

import it.codeclub.pokeclub.remote.data.AbilityDetails
import it.codeclub.pokeclub.remote.data.AbilityList
import it.codeclub.pokeclub.remote.data.Pokemon
import it.codeclub.pokeclub.remote.data.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming

interface PokeAPI {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon

    @GET("ability")
    suspend fun getAbilityList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): AbilityList

    @GET("ability/{name}")
    suspend fun getAbilityDetails(
        @Path("name") name: String
    ): AbilityDetails
}