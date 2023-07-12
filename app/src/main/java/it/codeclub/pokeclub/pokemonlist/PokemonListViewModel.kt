package it.codeclub.pokeclub.pokemonlist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.Pokemon
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var pokemonList = mutableStateListOf<Pokemon>()
}