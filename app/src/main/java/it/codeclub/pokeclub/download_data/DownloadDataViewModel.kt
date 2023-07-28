package it.codeclub.pokeclub.download_data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.db.entities.PokemonAbilityCrossRef
import it.codeclub.pokeclub.db.entities.PokemonDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.db.entities.PokemonType
import it.codeclub.pokeclub.remote.PokeAPI
import it.codeclub.pokeclub.remote.data.AbilityList
import it.codeclub.pokeclub.remote.data.PokemonList
import it.codeclub.pokeclub.utils.Constants.BASE_OFFSET
import it.codeclub.pokeclub.utils.Constants.BASE_URL
import it.codeclub.pokeclub.utils.Constants.LIMIT
import kotlinx.coroutines.launch
import javax.inject.Inject

class DownloadDataViewModel @Inject constructor(
    private val pokeApi: PokeAPI,
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            getAbilities()
            getPokemon()
        }
    }

    private suspend fun getAbilities() {
        var offset = BASE_OFFSET
        var currentUrl: String? = "${BASE_URL}/ability/"
        var abilityList: AbilityList
        do {
            abilityList = pokeApi.getAbilityList(offset, LIMIT)
            storeAbilities(abilityList)
            offset += LIMIT
            currentUrl = abilityList.next
        } while (currentUrl != null)
    }

    private suspend fun getPokemon() {
        // TODO("Aggiornamento automatico dell'offset")
        var offset = BASE_OFFSET
        var currentUrl: String? = "${BASE_URL}/pokemon/"
        var pokemonList: PokemonList
        do {
            pokemonList = pokeApi.getPokemonList(offset, LIMIT)
            getPokemonDetails(pokemonList)
            offset += LIMIT
            currentUrl = pokemonList.next
        } while (currentUrl != null)
    }

    private suspend fun storeAbilities(abilityList: AbilityList) {
        abilityList.results.forEach {
            val ability = pokeApi.getAbilityDetails(it.name)

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

            val abilityEntity = Ability(
                abilityId = ability.id.toLong(),
                name_it = nameIt,
                name_en = ability.name,
                effect_en = effectEn,
                effect_it = effectIt
            )
            pokemonRepository.insertNewAbility(abilityEntity)
        }
    }

    private suspend fun getPokemonDetails(pokemonList: PokemonList) {
        pokemonList.results.forEach {
            val pokemon = pokeApi.getPokemonInfo(it.name)
            val pokemonEntity = PokemonEntity(
                pokemonId = pokemon.id,
                name = pokemon.name,
                type = PokemonType.BUG, // TODO convert type
                secondType = PokemonType.BUG,
                dominantColor = 0,
                imageUrl = pokemon.sprites.front_default
            )
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
            val pokemonDetails = PokemonDetails(
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
            pokemonRepository.insertNewPokemonEntity(pokemonEntity)
            val detailsId = pokemonRepository.insertNewPokemonDetails(pokemonDetails)

            pokemon.abilities.forEach { ability ->
                val abilityId = ability.ability.url
                    .substringAfter("https://pokeapi.co/api/v2/ability/")
                    .substringBefore("/")
                    .toInt()
                val pokemonAbilityCrossRef = PokemonAbilityCrossRef(
                    pokemonId = pokemon.id,
                    abilityId = abilityId
                )
                pokemonRepository.insertPokemonAbilityCrossRef(pokemonAbilityCrossRef)
            }
        }
    }
}