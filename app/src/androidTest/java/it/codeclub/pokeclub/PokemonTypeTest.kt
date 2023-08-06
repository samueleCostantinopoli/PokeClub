package it.codeclub.pokeclub

import it.codeclub.pokeclub.db.entities.PokemonType
import org.junit.Test

class PokemonTypeTest {

    @Test
    fun testPokemonTypeMatching() {
        assert(PokemonType.fromInt(2131492933) == PokemonType.GRASS) {
            System.err.println("Not matching")
        }
    }

    @Test
    fun testPokemonNullTypeMatching() {
        assert(PokemonType.fromInt(2131492933) == PokemonType.GRASS) {
            System.err.println("Not matching")
        }
    }

}