package it.codeclub.pokeclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import it.codeclub.pokeclub.NewMainView.MainView
import it.codeclub.pokeclub.download_data.DownloadDataScreen
import it.codeclub.pokeclub.download_data.DownloadDataViewModel
import it.codeclub.pokeclub.pokemondetails.DetailsScreen
import it.codeclub.pokeclub.ui.theme.PokeClubTheme
import it.codeclub.pokeclub.utils.Constants.FIRST_START_PREF
import it.codeclub.pokeclub.utils.Constants.PREFS_FILE

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viene lanciata una nuova activity
        setContent {
            PokeClubTheme {
                val navController = rememberNavController()

                if (getSharedPreferences(PREFS_FILE, MODE_PRIVATE).getBoolean(
                        FIRST_START_PREF,
                        true
                    )
                )
                    DownloadDataScreen(navController)
                else {
                    NavHost(
                        navController = navController,
                        startDestination = "pokemon_list_screen"
                    ) {
                        composable("pokemon_list_screen") {
                            //PokedexScreen è la schermata con la quale il maestro cantarini
                            //ha fatto la prova con i pokemon, in caso di errore con la mia basta
                            //togliere il commento e lanciare quella,presente in pokemonList
                            //PokedexScreen(navController)

                            //MainView è la nuova schermata presente in SchermataPrincipale
                            //nella cartella NewMainView
                            MainView(navController)
                        }
                        composable(
                            "pokemon_detail_screen/{pokemonName}",
                            arguments = listOf(
                                navArgument("pokemonName") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            val pokemonName = remember {
                                it.arguments!!.getString("pokemonName")
                            }
                            DetailsScreen(pokemonName!!)
                        }
                    }
                }
            }
        }
    }
}
