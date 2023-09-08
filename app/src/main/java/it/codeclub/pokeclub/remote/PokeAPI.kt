package it.codeclub.pokeclub.remote

import it.codeclub.pokeclub.remote.data.AbilityDetails
import it.codeclub.pokeclub.remote.data.AbilityList
import it.codeclub.pokeclub.remote.data.Pokemon
import it.codeclub.pokeclub.remote.data.PokemonList
import it.codeclub.pokeclub.remote.data.VersionGroups
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("version")
    suspend fun getVersionGroups(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): VersionGroups

    @GET("ability")
    suspend fun getAbilityList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): AbilityList

    @GET("ability/{name}")
    suspend fun getAbilityDetails(
        @Path("name") name: String
    ): AbilityDetails
}