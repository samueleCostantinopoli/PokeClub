package it.codeclub.pokeclub.db

import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.AbilityWithPokemon
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonVersionGroupsCrossRef
import it.codeclub.pokeclub.db.entities.PokemonWithVersionGroupsAndAbilities
import it.codeclub.pokeclub.db.entities.VersionGroupEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemon(): Flow<List<PokemonWithVersionGroupsAndAbilities>>
    suspend fun getAbilities(): Flow<List<Ability>>
    suspend fun getPokemonWithAbility(abilityId: Long): Flow<AbilityWithPokemon>
    suspend fun getPokemonDetails(name: String): Flow<PokemonAndDetails>
    suspend fun update(pokemon: PokemonEntity)
    suspend fun insertNewPokemonEntity(pokemonEntity: PokemonEntity)
    suspend fun insertNewPokemonDetails(pokemonDetails: PokemonDetails): Long
    suspend fun insertNewAbility(ability: Ability)
    suspend fun insertPokemonAbilityCrossRef(pokemonAbilityCrossRef: PokemonAbilityCrossRef)
    suspend fun insertVersionGroupEntity(versionGroupEntity: VersionGroupEntity)
    suspend fun insertPokemonVersionGroupsCrossRef(pokemonVersionGroupsCrossRef: PokemonVersionGroupsCrossRef)
}