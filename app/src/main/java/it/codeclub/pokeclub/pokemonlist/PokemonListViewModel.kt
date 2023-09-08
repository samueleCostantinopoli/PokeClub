package it.codeclub.pokeclub.pokemonlist

import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonType
import it.codeclub.pokeclub.db.entities.PokemonWithVersionAndAbilities
import it.codeclub.pokeclub.db.entities.VersionEntity
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

    private lateinit var pokemonList: List<PokemonWithVersionAndAbilities>
    var shownPokemonList = mutableStateOf<List<PokemonWithVersionAndAbilities>>(listOf())

    private lateinit var abilitiesList: List<Ability>
    var shownAbilitiesList = mutableStateOf<List<Ability>>(listOf())

    // List of currently applied filters
    private var filterList = mutableListOf<FilterType>()

    var searchPokemonQuery = mutableStateOf("")
    var isSearchingPokemon = mutableStateOf(false)

    var searchAbilityQuery = mutableStateOf("")
    var abilityFilter = mutableStateOf<Ability?>(null)

    val versionGroupsList = mutableListOf<VersionEntity>()
    var versionGroup = mutableStateOf<VersionEntity?>(null)

    val firstType = mutableStateOf<PokemonType?>(null)
    val secondType = mutableStateOf<PokemonType?>(null)

    init {
        loadPokemon()
        loadVersionGroups()
        loadAbilities()
    }

    /**
     * Search Pokemon with matching name
     */
    fun searchPokemon() {
        applyFilters()
    }

    /**
     * Toggles favourite and captured filters
     */
    fun toggleFilter(filterType: FilterType) {
        if (filterList.contains(filterType))
            filterList.remove(filterType)
        else
            filterList.add(filterType)
        applyFilters()
    }

    /**
     * Applies all active filters to the list
     */
    private fun applyFilters() {
        // Init show pokemon as an empty list
        var toBeFiltered: List<PokemonWithVersionAndAbilities> = listOf()
        // Check if favourite and captured filters are active. Fill initial list.
        when (filterList.size) {
            0 -> {
                toBeFiltered = pokemonList
            }

            1 -> {
                toBeFiltered = pokemonList.filter {
                    if (filterList.contains(FilterType.FAVOURITES))
                        it.pokemonEntity.isFavourite
                    else
                        it.pokemonEntity.isCaptured
                }
            }

            2 -> {
                toBeFiltered = pokemonList.filter {
                    it.pokemonEntity.isFavourite and it.pokemonEntity.isCaptured
                }
            }
        }

        // Name filter
        if (searchPokemonQuery.value.isNotEmpty()) {
            shownPokemonList.value = toBeFiltered.filter {
                it.pokemonEntity.name.startsWith(
                    searchPokemonQuery.value.trim().lowercase()
                ) or it.pokemonEntity.pokemonId.toString()
                    .startsWith(searchPokemonQuery.value.trim())
            }
        } else
            shownPokemonList.value = toBeFiltered

        // Ability filter
        abilityFilter.value?.let { ability ->
            shownPokemonList.value = shownPokemonList.value.filter {
                it.abilities.contains(ability)
            }
        }

        //Version filter
        versionGroup.value?.let { versionGroupEntity ->
            shownPokemonList.value = shownPokemonList.value.filter {
                it.versionGroups.contains(versionGroupEntity)
            }
        }

        //First type filter
        firstType.value?.let { pokemonType ->
            shownPokemonList.value = shownPokemonList.value.filter {
                it.pokemonEntity.type == pokemonType
            }
        }

        //Second type filter
        secondType.value?.let { pokemonType ->
            shownPokemonList.value = shownPokemonList.value.filter {
                it.pokemonEntity.secondType == pokemonType
            }
        }

    }

    /**
     * Loads all pokemon from repository
     */
    private fun loadPokemon() {
        viewModelScope.launch {
            pokemonRepository.getPokemon().collect {
                pokemonList = it
                shownPokemonList.value = pokemonList
            }
        }
    }

    /**
     * Executes ability filtering by name
     */
    fun searchAbility() {
        if (searchAbilityQuery.value.isNotEmpty()) {
            shownAbilitiesList.value = abilitiesList.filter {
                it.nameEn.startsWith(
                    searchAbilityQuery.value.trim().lowercase()
                ) or it.nameIt.lowercase().startsWith(
                    searchAbilityQuery.value.trim().lowercase()
                ) or it.abilityId.toString().startsWith(
                    searchAbilityQuery.value.trim()
                )
            }
        } else {
            shownAbilitiesList.value = abilitiesList
            abilityFilter.value = null
            applyFilters()
        }
    }

    /**
     * Loads all abilities from repository
     */
    private fun loadAbilities() {
        viewModelScope.launch {
            pokemonRepository.getAbilities().collect {
                abilitiesList = it
                shownAbilitiesList.value = abilitiesList
            }
        }
    }

    /**
     * Loads all version groups from repository
     */
    private fun loadVersionGroups() {
        viewModelScope.launch {
            pokemonRepository.getVersionGroups().collect {
                versionGroupsList.addAll(it)
            }
        }
    }

    /**
     * Removes all filters
     */
    private fun removeFilters() {
        filterList.clear()
        searchPokemonQuery.value = ""
        isSearchingPokemon.value = false
    }

    /**
     * Toggles a pokemon favourite status
     */
    fun toggleFavourite(pokemon: PokemonEntity) {
        pokemon.isFavourite = !pokemon.isFavourite
        downloadPokemonImage(pokemon)
        loadPokemon()
    }

    /**
     * Toggles a pokemon captured status
     */
    fun toggleCaptured(pokemon: PokemonEntity) {
        removeFilters()
        pokemon.isCaptured = !pokemon.isCaptured
        update(pokemon)
    }

    /**
     * Updates a pokemon data
     */
    private fun update(pokemon: PokemonEntity) {
        viewModelScope.launch {
            pokemonRepository.update(pokemon)
        }
    }

    /**
     * Download a pokemon image, which is then saves into the repository
     */
    private fun downloadPokemonImage(pokemon: PokemonEntity) {
        val request = Request.Builder()
            .url(pokemon.imageUrl)
            .build()

        val okHttpClient = OkHttpClient.Builder().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                pokemon.image = null
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