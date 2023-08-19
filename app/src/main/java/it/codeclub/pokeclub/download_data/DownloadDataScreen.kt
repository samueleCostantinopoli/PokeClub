package it.codeclub.pokeclub.download_data

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.codeclub.pokeclub.R
import kotlinx.coroutines.delay

@Composable
fun DownloadDataScreen(
    navController: NavController,
    downloadDataViewModel: DownloadDataViewModel = hiltViewModel()
) {
    val abilityCounter = remember { downloadDataViewModel.abilityCounter }
    val abilityNumber = remember { downloadDataViewModel.abilityNumber }
    val currentAbility = remember { downloadDataViewModel.currentAbility }
    val pokemonCounter = remember { downloadDataViewModel.pokemonCounter }
    val pokemonNumber = remember { downloadDataViewModel.pokemonNumber }
    val currentPokemon = remember { downloadDataViewModel.currentPokemon }
    val downloadStatus = remember { downloadDataViewModel.currentStatus }

    var done = false

    val progress = remember { mutableStateOf(0.0f) }
    LaunchedEffect(Unit) {
        for (i in 0..100) {
            progress.value = i / 100f
            delay(15)
        }
    }
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0.33f, 0.33f, 0.33f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.BottomStart,
    ) {

        when (downloadStatus.value) {
            DownloadDataViewModel.DownloadStatus.INIT_DOWNLOAD -> {

            }

            DownloadDataViewModel.DownloadStatus.ABILITY_DOWNLOAD -> {
                Text(
                    text = "${stringResource(id = R.string.downloading)} " +
                            stringResource(R.string.abilities) +
                            "(${abilityCounter.value} ${stringResource(id = R.string.of)} ${abilityNumber.value})",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y=(-110).dp)
                )
                Text(
                    text = currentAbility.value!!.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y=(-90).dp)
                )
            }

            DownloadDataViewModel.DownloadStatus.POKEMON_DOWNLOAD -> {
                Text(
                    text = "${stringResource(id = R.string.downloading)} " +
                            stringResource(R.string.pokemon) +
                            "(${pokemonCounter.value} ${stringResource(id = R.string.of)} ${pokemonNumber.value})",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y=(-110).dp)
                )
                Text(
                    text = (currentPokemon.value!!.name),
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.BottomCenter).offset(y=(-90).dp)
                )
            }

            DownloadDataViewModel.DownloadStatus.DONE -> {

                if (!done) {
                    navController.navigate("pokemon_list_screen") {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                    navController.graph.setStartDestination("pokemon_list_screen")
                    done = true
                }
            }
        }
        if(!done) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(12.dp)
                    .offset(x = 40.dp, y = (-143).dp),
                progress = downloadDataViewModel.downloadProgress.value, // Assicura che il progresso sia compreso tra 0f e 1f
                color = Color.White,
                trackColor = Color.Red
            )
        }

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .scale(2.2f)
                .graphicsLayer { alpha = 0.3f },
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(bottom = 100.dp)
                    .scale(2.5f)
            )
        }
    }
}
