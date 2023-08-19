package it.codeclub.pokeclub.pokemonlist

import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.domain.FilterType
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private lateinit var pokemonList: List<PokemonEntity>
    var shownPokemonList = mutableStateOf<List<PokemonEntity>>(listOf())

    // List of currently applied filters
    private var filterList = mutableListOf<FilterType>()

    var searchQuery = mutableStateOf("")
    var isSearching = mutableStateOf(false)

    init {
        loadPokemon()
    }

    /**
     * Search Pokemon with matching name
     */
    fun searchPokemon() {
        applyFilters()
    }

    fun toggleFilter(filterType: FilterType) {
        if (filterList.contains(filterType))
            filterList.remove(filterType)
        else
            filterList.add(filterType)
        applyFilters()
    }

    private fun applyFilters() {
        var toBeFiltered: List<PokemonEntity> = listOf()
        when (filterList.size) {
            0 -> {
                toBeFiltered = pokemonList
            }

            1 -> {
                toBeFiltered = pokemonList.filter {
                    if (filterList.contains(FilterType.FAVOURITES))
                        it.isFavourite
                    else
                        it.isCaptured
                }
            }

            2 -> {
                toBeFiltered = pokemonList.filter {
                    it.isFavourite and it.isCaptured
                }
            }
        }

        if (searchQuery.value.isNotEmpty()) {
            shownPokemonList.value = toBeFiltered.filter {
                it.name.startsWith(
                    searchQuery.value.trim().lowercase()
                ) || it.pokemonId.toString()
                    .startsWith(searchQuery.value.trim())
            }
        } else
            shownPokemonList.value = toBeFiltered
    }

    private fun loadPokemon() {
        viewModelScope.launch {
            pokemonRepository.getPokemon().collect {
                pokemonList = it
                shownPokemonList.value = pokemonList
            }
        }
    }

    private fun removeFilters() {
        filterList.clear()
        searchQuery.value = ""
        isSearching.value = false
    }

    fun toggleFavourite(pokemon: PokemonEntity) {
        pokemon.isFavourite = !pokemon.isFavourite
        downloadPokemonImage(pokemon)
        loadPokemon()
        removeFilters()
    }

    fun toggleCaptured(pokemon: PokemonEntity) {
        removeFilters()
        pokemon.isCaptured = !pokemon.isCaptured
        update(pokemon)
    }

    private fun update(pokemon: PokemonEntity) {
        viewModelScope.launch {
            pokemonRepository.update(pokemon)
        }
    }

    private fun downloadPokemonImage(pokemon: PokemonEntity) {
        val request = Request.Builder()
            .url(pokemon.imageUrl)
            .build()

        val okHttpClient = OkHttpClient.Builder().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // TODO save error image
                update(pokemon)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful)
                    response.body?.let { responseBody ->
                        val imageByteArray = responseBody.byteStream().readBytes()
                        val bmp = BitmapFactory.decodeByteArray(
                            imageByteArray,
                            0,
                            imageByteArray.size
                        )
                        pokemon.image = bmp
                        update(pokemon)
                    }
            }
        })
    }
}