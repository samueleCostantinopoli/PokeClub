package it.codeclub.pokeclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import it.codeclub.pokeclub.download_data.DownloadDataScreen
import it.codeclub.pokeclub.pokemonlist.MainView
import it.codeclub.pokeclub.pokemondetails.DetailsScreen
import it.codeclub.pokeclub.settings.SettingsScreen
import it.codeclub.pokeclub.ui.theme.PokeClubTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viene lanciata una nuova activity
        setContent {
            PokeClubTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "download_data_screen"
                ) {
                    composable("pokemon_list_screen") {
                        //MainView è la nuova schermata presente in SchermataPrincipale
                        //nella cartella NewMainView
                        MainView(navController)
                    }
                    composable("download_data_screen") {
                        DownloadDataScreen(navController)
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
                    composable("settings") {
                        SettingsScreen()
                    }
                }
            }
        }
    }
}
