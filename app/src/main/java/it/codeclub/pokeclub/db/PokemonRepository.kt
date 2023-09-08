package it.codeclub.pokeclub.db

import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.AbilityWithPokemon
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonVersionCrossRef
import it.codeclub.pokeclub.db.entities.PokemonWithVersionAndAbilities
import it.codeclub.pokeclub.db.entities.VersionEntity
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {

    suspend fun getPokemon(): Flow<List<PokemonWithVersionAndAbilities>>
    suspend fun getAbilities(): Flow<List<Ability>>
    suspend fun getVersionGroups(): Flow<List<VersionEntity>>
    suspend fun getPokemonWithAbility(abilityId: Long): Flow<AbilityWithPokemon>
    suspend fun getPokemonDetails(name: String): Flow<PokemonAndDetails>
    suspend fun update(pokemon: PokemonEntity)
    suspend fun insertNewPokemonEntity(pokemonEntity: PokemonEntity)
    suspend fun insertNewPokemonDetails(pokemonDetails: PokemonDetails): Long
    suspend fun insertNewAbility(ability: Ability)
    suspend fun insertPokemonAbilityCrossRef(pokemonAbilityCrossRef: PokemonAbilityCrossRef)
    suspend fun insertVersionGroupEntity(versionEntity: VersionEntity)
    suspend fun insertPokemonVersionGroupsCrossRef(pokemonVersionCrossRef: PokemonVersionCrossRef)
}