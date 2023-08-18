package it.codeclub.pokeclub.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.domain.FilterType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var pokemonList = mutableStateOf<List<PokemonEntity>>(listOf())

    private var cachedPokemonList = listOf<PokemonEntity>()
    private var isSearchStarting = true
    private var isSearching = mutableStateOf(false)

    init {
        loadPokemon()
    }

    fun searchPokemon(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }

            val results = listToSearch.filter {
                it.name.startsWith(query.trim().lowercase()) || it.pokemonId.toString()
                    .startsWith(query.trim())
            }

            if (isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }

    fun filterBy(filterType: FilterType) {
        when (filterType) {
            FilterType.FAVOURITES -> {
                viewModelScope.launch {
                    pokemonRepository.getFavourites().onEach {
                        pokemonList.value = it
                    }
                }
            }
            FilterType.CAPTURED -> {
                viewModelScope.launch {
                    pokemonRepository.getCaptured().onEach {
                        pokemonList.value = it
                    }
                }
            }
            FilterType.NONE -> {
                viewModelScope.launch {
                    pokemonRepository.getPokemon().onEach {
                        pokemonList.value = it
                    }
                }
            }
        }
    }

    private fun loadPokemon() {
        viewModelScope.launch {
            pokemonRepository.getPokemon().collect {
                pokemonList.value = it
            }
        }
    }

    fun toggleFavourite(pokemon: PokemonEntity) {
        pokemon.isFavourite = !pokemon.isFavourite
        update(pokemon)
    }

    fun toggleCaptured(pokemon: PokemonEntity) {
        pokemon.isCaptured = !pokemon.isCaptured
        update(pokemon)
    }

    private fun update(pokemon: PokemonEntity) {
        viewModelScope.launch {
            pokemonRepository.update(pokemon)
        }
    }

    fun calculateDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}