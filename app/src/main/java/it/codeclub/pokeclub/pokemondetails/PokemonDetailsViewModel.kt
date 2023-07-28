package it.codeclub.pokeclub.pokemondetails

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.db.entities.PokemonWithAbilities
import it.codeclub.pokeclub.utils.Resource
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor (
    private val pokemonRepository: PokemonRepository
): ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<PokemonAndDetails> {
        return pokemonRepository.getPokemonDetails(pokemonName)
    }
}
