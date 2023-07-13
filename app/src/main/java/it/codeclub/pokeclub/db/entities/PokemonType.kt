package it.codeclub.pokeclub.db.entities

import androidx.room.Entity
import it.codeclub.pokeclub.R

@Entity
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
    WATER(R.string.water);

    companion object {
        fun fromInt(value: Int) = PokemonType.values().first { it.value == value }
    }
}