package it.codeclub.pokeclub.db

import dagger.hilt.android.scopes.ActivityScoped
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonWithAbilities
import it.codeclub.pokeclub.exeptions.RepositoryException
import it.codeclub.pokeclub.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class PokemonRepositoryDbImpl @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun getPokemon(): Flow<List<PokemonEntity>> {
        return pokemonDao.getPokemonList()
    }

    override suspend fun getFavourites(): Flow<List<PokemonEntity>> {
        return pokemonDao.getFavourites()
    }

    override suspend fun getCaptured(): Flow<List<PokemonEntity>> {
        return pokemonDao.getCaptured()
    }

    @Throws(RepositoryException::class)
    override suspend fun getPokemonDetails(name: String): Resource<PokemonWithAbilities> {
        if (name.isEmpty())
            throw RepositoryException("Errore")
        return Resource.Success(pokemonDao.getPokemonDetails(name)[0])
    }

    override suspend fun update(pokemon: PokemonEntity) {
        pokemonDao.update(pokemon)
    }
}