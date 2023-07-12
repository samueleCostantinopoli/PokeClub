package it.codeclub.pokeclub.db

import android.content.Context
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.db.entities.Pokemon
import it.codeclub.pokeclub.exeptions.RepositoryException
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PokemonRepositoryDbImpl @Inject constructor(
    private val pokemonDao: PokemonDao
) : PokemonRepository {

    override suspend fun getPokemon(): Flow<List<Pokemon>> {
        return pokemonDao.getPokemonList()
    }

    @Throws(RepositoryException::class)
    override suspend fun getPokemonDetails(name: String): Flow<Pokemon> {
        if (name.isEmpty())
            throw RepositoryException("Errore")
        return pokemonDao.getPokemonDetails(name)
    }


}