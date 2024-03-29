package it.codeclub.pokeclub.db

import dagger.hilt.android.scopes.ActivityScoped
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.AbilityWithPokemon
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonVersionCrossRef
import it.codeclub.pokeclub.db.entities.PokemonWithVersionAndAbilities
import it.codeclub.pokeclub.db.entities.VersionEntity
import it.codeclub.pokeclub.exceptions.RepositoryException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class PokemonRepositoryDbImpl @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun getPokemon(): Flow<List<PokemonWithVersionAndAbilities>> = pokemonDao.getPokemonList()
    override suspend fun getAbilities(): Flow<List<Ability>> = pokemonDao.getAbilities()
    override suspend fun getVersionGroups(): Flow<List<VersionEntity>> = pokemonDao.getVersionGroups()

    override suspend fun getPokemonWithAbility(abilityId: Long): Flow<AbilityWithPokemon> =
        pokemonDao.getPokemonWithAbility(abilityId)

    @Throws(RepositoryException::class)
    override suspend fun getPokemonDetails(name: String): Flow<PokemonAndDetails> {
        if (name.isEmpty())
            throw RepositoryException("Error")
        return pokemonDao.getPokemonDetails(name)
    }

    override suspend fun update(pokemon: PokemonEntity) = pokemonDao.update(pokemon)

    override suspend fun insertNewPokemonEntity(pokemonEntity: PokemonEntity) =
        pokemonDao.insert(pokemonEntity)

    override suspend fun insertNewPokemonDetails(pokemonDetails: PokemonDetails): Long =
        pokemonDao.insert(pokemonDetails)

    override suspend fun insertNewAbility(ability: Ability) = pokemonDao.insert(ability)
    override suspend fun insertPokemonAbilityCrossRef(pokemonAbilityCrossRef: PokemonAbilityCrossRef) =
        pokemonDao.insert(pokemonAbilityCrossRef)

    override suspend fun insertVersionGroupEntity(versionEntity: VersionEntity) =
        pokemonDao.insert(versionEntity)

    override suspend fun insertPokemonVersionGroupsCrossRef(pokemonVersionCrossRef: PokemonVersionCrossRef) =
        pokemonDao.insert(pokemonVersionCrossRef)
}