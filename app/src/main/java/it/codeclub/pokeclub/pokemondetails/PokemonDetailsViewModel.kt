package it.codeclub.pokeclub.pokemondetails

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.utils.Resource
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var pokemonDetails = mutableStateOf<Resource<PokemonAndDetails>>(Resource.Loading())

    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            pokemonRepository.getPokemonDetails(pokemonName).collect {
                pokemonDetails.value = Resource.Success(it)
            }
        }
    }
}
