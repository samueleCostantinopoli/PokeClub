package it.codeclub.pokeclub.download_data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonType
import it.codeclub.pokeclub.db.entities.PokemonVersionCrossRef
import it.codeclub.pokeclub.db.entities.VersionEntity
import it.codeclub.pokeclub.local.SharedPrefsRepository
import it.codeclub.pokeclub.remote.PokeAPI
import it.codeclub.pokeclub.remote.data.AbilityDetails
import it.codeclub.pokeclub.remote.data.AbilityList
import it.codeclub.pokeclub.remote.data.Pokemon
import it.codeclub.pokeclub.remote.data.PokemonList
import it.codeclub.pokeclub.remote.data.VersionGroups
import it.codeclub.pokeclub.utils.Constants.LIMIT
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class DownloadDataViewModel @Inject constructor(
    private val pokeApi: PokeAPI,
    private val pokemonRepository: PokemonRepository,
    private val sharedPrefsRepository: SharedPrefsRepository
) : ViewModel() {

    var currentStatus = mutableStateOf(DownloadStatus.INIT_DOWNLOAD)

    var downloadProgress = mutableStateOf(0.0f)

    // Abilities attributes
    var abilityNumber = mutableStateOf(0)
    var abilityCounter = mutableStateOf(0)
    private var abilityOffset = sharedPrefsRepository.getAbilityOffset()
    var currentAbility = mutableStateOf<AbilityDetails?>(null)

    // Version groups attributes
    var versionsNumber = mutableStateOf(0)
    var versionsCounter = mutableStateOf(0)
    private var versionsOffset: Int = sharedPrefsRepository.getVersionsOffset()
    var currentVersion = mutableStateOf<String?>(null)

    // Pokemon attributes
    var pokemonNumber = mutableStateOf(0)
    var pokemonCounter = mutableStateOf(0)
    private var pokemonOffset: Int = sharedPrefsRepository.getPokemonOffset()
    var currentPokemon = mutableStateOf<Pokemon?>(null)

    private val okHttpClient = OkHttpClient.Builder().callTimeout(10, TimeUnit.MINUTES).build()

    init {
        viewModelScope.launch {
            if (sharedPrefsRepository.getFirstStartIndicator()) {
                getAbilities()
                getVersions()
                getPokemon()
                sharedPrefsRepository.updateFirstStartIndicator()
            }
            currentStatus.value = DownloadStatus.DONE
        }
    }

    /**
     * When ViewModel is cleared, saves current download progress
     */
    override fun onCleared() {
        sharedPrefsRepository.updatePokemonOffset(pokemonOffset)
        sharedPrefsRepository.updateAbilityOffset(abilityOffset)
        sharedPrefsRepository.updateVersionsOffset(versionsOffset)
    }

    private suspend fun getAbilities() {
        var abilityList: AbilityList
        currentStatus.value = DownloadStatus.ABILITY_DOWNLOAD
        do {
            abilityList = pokeApi.getAbilityList(LIMIT, abilityOffset)
            if (abilityNumber.value == 0)
                abilityNumber.value = abilityList.count
            storeAbilities(abilityList)
            abilityOffset += LIMIT
            downloadProgress.value = abilityOffset.toFloat() / abilityNumber.value.toFloat()
        } while (abilityList.next != null)
        sharedPrefsRepository.updateAbilityOffset(abilityCounter.value)
    }

    private suspend fun getVersions() {
        var versionGroups: VersionGroups
        currentStatus.value = DownloadStatus.VERSIONS_DOWNLOAD
        do {
            versionGroups = pokeApi.getVersions(LIMIT, versionsOffset)
            if (versionsNumber.value == 0)
                versionsNumber.value = versionGroups.count
            storeVersion(versionGroups)
            versionsOffset += LIMIT
            downloadProgress.value =
                versionsOffset.toFloat() / versionsNumber.value.toFloat()
        } while (versionGroups.next != null)
        sharedPrefsRepository.updateVersionsOffset(versionsCounter.value)
    }

    private suspend fun getPokemon() {
        var pokemonList: PokemonList
        currentStatus.value = DownloadStatus.POKEMON_DOWNLOAD
        do {
            pokemonList = pokeApi.getPokemonList(LIMIT, pokemonOffset)
            if (pokemonNumber.value == 0)
                pokemonNumber.value = pokemonList.count
            getPokemonData(pokemonList)
            pokemonOffset += LIMIT

            downloadProgress.value = pokemonOffset.toFloat() / pokemonNumber.value.toFloat()
        } while (pokemonList.next != null)
        sharedPrefsRepository.updatePokemonOffset(pokemonCounter.value)
    }

    private suspend fun storeAbilities(abilityList: AbilityList) {
        abilityList.results.forEach { result ->
            val ability = pokeApi.getAbilityDetails(result.name)

            currentAbility.value = ability

            val foundName = ability.names.find { name ->
                name.language.name == "it"
            }
            val nameIt = foundName?.name ?: ability.name

            // Using findLast() to find the most recent text
            val foundEffectEn = ability.flavor_text_entries.findLast { entry ->
                entry.language.name == "en"
            }
            val effectEn = foundEffectEn?.flavor_text ?: "Not found"

            val foundEffectIt = ability.flavor_text_entries.findLast { entry ->
                entry.language.name == "it"
            }
            val effectIt = foundEffectIt?.flavor_text ?: effectEn

            pokemonRepository.insertNewAbility(
                Ability(
                    abilityId = ability.id.toLong(),
                    nameIt = nameIt,
                    nameEn = ability.name,
                    effectEn = effectEn,
                    effectIt = effectIt
                )
            )
            abilityCounter.value++
        }
    }

    /**
     * Stores a game version
     */
    private suspend fun storeVersion(versionGroups: VersionGroups) {
        viewModelScope.launch {
            versionGroups.results.forEach {
                currentVersion.value = it.name

                val versionEntity = VersionEntity(
                    it.name
                )
                pokemonRepository.insertVersionGroupEntity(versionEntity)
                versionsCounter.value++
            }
        }
    }

    /**
     * Download data from each pokemon
     *
     * @param pokemonList a list of pokemon to download data for
     */
    private suspend fun getPokemonData(pokemonList: PokemonList) {
        pokemonList.results.forEach {

            val pokemon = pokeApi.getPokemonInfo(it.name)

            currentPokemon.value = pokemon

            val pokemonEntity = PokemonEntity(
                pokemonId = pokemon.id,
                name = pokemon.name,
                type = PokemonType.valueOf(pokemon.types[0].type.name.uppercase()),
                secondType = if (pokemon.types.size == 2) PokemonType.valueOf(pokemon.types[1].type.name.uppercase()) else null,
                dominantColor = 0xffffff,
                imageUrl = pokemon.sprites.other.`official-artwork`.front_default ?: "Not provided"
            )

            if (pokemon.sprites.other.`official-artwork`.front_default != null) {
                val request = Request.Builder()
                    .url(pokemon.sprites.other.`official-artwork`.front_default)
                    .build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        // Saves entity with white as default dominant color
                        storePokemonEntity(pokemonEntity)
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
                                calcDominantColor(bmp) { color ->
                                    pokemonEntity.dominantColor = color.toInt()
                                    storePokemonEntity(pokemonEntity)
                                }
                            }
                    }
                })
            }

            // Saves PokemonVersionGroupsCrossRef
            viewModelScope.launch {
                pokemon.game_indices.forEach { gameIndex ->
                    pokemonRepository.insertPokemonVersionGroupsCrossRef(
                        PokemonVersionCrossRef(
                            pokemon.id,
                            gameIndex.version.name
                        )
                    )
                }
            }

            // Takes PokemonDetails values and saves them
            var lp = 0
            var attack = 0
            var defense = 0
            var spAttack = 0
            var spDefense = 0
            var speed = 0
            pokemon.stats.forEach { stat ->
                when (stat.stat.name) {
                    "hp" -> lp = stat.base_stat
                    "attack" -> attack = stat.base_stat
                    "defense" -> defense = stat.base_stat
                    "special-attack" -> spAttack = stat.base_stat
                    "special-defense" -> spDefense = stat.base_stat
                    "speed" -> speed = stat.base_stat
                }
            }
            pokemonRepository.insertNewPokemonDetails(
                PokemonDetails(
                    pokemonEntityId = pokemon.id,
                    height = pokemon.height.toDouble(),
                    weight = pokemon.weight.toDouble(),
                    lp = lp,
                    attack = attack,
                    defense = defense,
                    spAttack = spAttack,
                    spDefense = spDefense,
                    speed = speed
                )
            )

            pokemon.abilities.forEach { ability ->
                val abilityId = if (ability.ability.url.endsWith("/"))
                    ability.ability.url.dropLast(1).takeLastWhile { char -> char.isDigit() }
                else
                    ability.ability.url.takeLastWhile { char -> char.isDigit() }
                val pokemonAbilityCrossRef = PokemonAbilityCrossRef(
                    pokemonId = pokemon.id,
                    abilityId = abilityId.toInt()
                )
                pokemonRepository.insertPokemonAbilityCrossRef(pokemonAbilityCrossRef)
            }
            pokemonCounter.value++
        }
    }

    private fun storePokemonEntity(pokemonEntity: PokemonEntity) = viewModelScope.launch {
        pokemonRepository.insertNewPokemonEntity(pokemonEntity)
    }

    /**
     * Evaluates a bitmap dominant color
     *
     * @param bitmap the image on which evaluating dominant color
     * @param onFinish function called when the dominant color is evaluated
     */
    private fun calcDominantColor(bitmap: Bitmap, onFinish: (UInt) -> Unit) {
        val bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(colorValue.toUInt())
            }
        }
    }

    enum class DownloadStatus {
        INIT_DOWNLOAD,
        ABILITY_DOWNLOAD,
        VERSIONS_DOWNLOAD,
        POKEMON_DOWNLOAD,
        DONE
    }
}