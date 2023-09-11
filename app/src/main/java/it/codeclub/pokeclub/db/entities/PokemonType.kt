package it.codeclub.pokeclub.db.entities

import it.codeclub.pokeclub.R

enum class PokemonType(val value: Int) {

    BUG(R.string.bug),
    DARK(R.string.dark),
    DRAGON(R.string.dragon),
    ELECTRIC(R.string.electric),
    FAIRY(R.string.fairy),
    FIGHTING(R.string.fighting),
    FIRE(R.string.fire),
    FLYING(R.string.flying),
    GHOST(R.string.ghost),
    GRASS(R.string.grass),
    GROUND(R.string.ground),
    ICE(R.string.ice),
    NORMAL(R.string.normal),
    POISON(R.string.poison),
    PSYCHIC(R.string.psychic),
    ROCK(R.string.rock),
    STEEL(R.string.steel),
    WATER(R.string.water),
    NULL(R.string.zero);

    companion object {
        fun fromInt(value: Int): PokemonType {
            var realValue = value
            when (realValue) {
                2131492948 -> realValue = 2131492949
                2131492984 -> realValue = 2131492986
                2131492963 -> realValue = 2131492965
            }

            val type = PokemonType.values().find { it.value == realValue }
            return type ?: NULL
        }
    }
}