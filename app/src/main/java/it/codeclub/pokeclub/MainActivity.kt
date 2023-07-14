package it.codeclub.pokeclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import it.codeclub.pokeclub.db.PokemonDao_Impl
import it.codeclub.pokeclub.db.PokemonDatabase_Impl
import it.codeclub.pokeclub.db.PokemonRepositoryDbImpl
import it.codeclub.pokeclub.pokemonlist.PokedexScreen
import it.codeclub.pokeclub.pokemonlist.PokemonListViewModel
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viene lanciata una nuova activity
        setContent {
            val database=PokemonDatabase_Impl()
            val pokemonRepositoryDbImpl:PokemonRepositoryDbImpl= PokemonRepositoryDbImpl(PokemonDao_Impl(database))
            val pokemonListViewModel:PokemonListViewModel=PokemonListViewModel(pokemonRepositoryDbImpl)
            PokedexScreen(this, pokemonListViewModel)
        }
    }
}
