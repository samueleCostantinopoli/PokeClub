package it.codeclub.pokeclub.local

import android.content.Context
import it.codeclub.pokeclub.utils.Constants.BASE_OFFSET
import it.codeclub.pokeclub.utils.Constants.FIRST_START_PREF
import it.codeclub.pokeclub.utils.Constants.PREFS_FILE

class SharedPrefsRepository constructor(
    private val context: Context
) {

    fun getAbilityOffset() = getSharedPreferences().getInt(ABILITY_OFFSET, BASE_OFFSET)

    fun getPokemonOffset() = getSharedPreferences().getInt(POKEMON_OFFSET, BASE_OFFSET)

    fun getFirstStartIndicator() = getSharedPreferences().getBoolean(FIRST_START_PREF, true)

    fun updateAbilityOffset(abilityOffset: Int) =
        getSharedPreferences().edit().putInt(ABILITY_OFFSET, abilityOffset).apply()

    fun updatePokemonOffset(pokemonOffset: Int) =
        getSharedPreferences().edit().putInt(POKEMON_OFFSET, pokemonOffset).apply()

    fun updateFirstStartIndicator() =
        getSharedPreferences().edit().putBoolean(FIRST_START_PREF, false).apply()

    private fun getSharedPreferences() =
        context.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    private companion object PreferencesNames {
        const val ABILITY_OFFSET = "ability_offset"
        const val POKEMON_OFFSET = "pokemon_offset"
    }
}