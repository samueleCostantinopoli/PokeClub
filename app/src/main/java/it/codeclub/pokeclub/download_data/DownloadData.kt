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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import it.codeclub.pokeclub.R


@Composable
fun DownloadDataScreen(
    downloadDataViewModel: DownloadDataViewModel = hiltViewModel()
) {

    val abilityCounter = remember{downloadDataViewModel.abilityCounter}
    val abilityNumber = remember{downloadDataViewModel.abilityNumber}
    val pokemonCounter = remember{downloadDataViewModel.pokemonCounter}
    val pokemonNumber = remember{downloadDataViewModel.pokemonNumber}


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

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(12.dp)
                .offset(x = 40.dp, y = (-143).dp),
            progress = downloadDataViewModel.downloadProgress.value, // Assicura che il progresso sia compreso tra 0f e 1f
            color = Color.White,
            trackColor = Color.Red
        )

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .scale(2.2f)
                .graphicsLayer { alpha = 0.3f },
            colorFilter = ColorFilter.colorMatrix(colorMatrix)
        )

        Box (
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(bottom = 100.dp)
                    .scale(2.5f)
            )
        }

    }
}
