package it.codeclub.pokeclub

import androidx.test.platform.app.InstrumentationRegistry
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.di.AppModule
import it.codeclub.pokeclub.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class PokemonRepositoryDbImplTest {

    @Test
    fun testPokemonDetails() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val repositoryDbImplTest = AppModule.providePokemonRepository(
            AppModule.providePokemonDatabase(appContext)
        )

        var result: Resource<PokemonAndDetails>? = null
        runBlocking {
            launch {
                result = repositoryDbImplTest.getPokemonDetails("Bulbasaur")
                Assert.assertNotNull(result)
            }
        }
    }
}