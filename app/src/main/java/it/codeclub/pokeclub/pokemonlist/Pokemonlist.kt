package it.codeclub.pokeclub.pokemonlist

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.domain.FilterType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview
@Composable
fun Call() {

}

@Preview
@Composable
fun PreviewChangeableImage() {
    MaterialTheme {

    }
}

fun intToColor(colorValue: Int): Color {
    return Color(colorValue)
}


@Composable
fun PokedexScreen(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()
) {
    var rotationState by remember { mutableStateOf(0f) }
    val rotation by animateFloatAsState(targetValue = rotationState, finishedListener = {
        rotationState = 0f
    })
    val coroutineScope = rememberCoroutineScope()
    var isRotated by remember { mutableStateOf(false) }
    var boxVersion by remember { mutableStateOf(false) }
    var boxType by remember { mutableStateOf(false) }
    val boxAbility = remember { mutableStateOf(false) }

    var isClickOutsideHandledVersion by remember { mutableStateOf(false) }
    var isClickOutsideHandledType by remember { mutableStateOf(false) }

    var isFavouritesFilterActive by remember {
        mutableStateOf(false)
    }

    var isCapturedFilterActive by remember {
        mutableStateOf(false)
    }

    // Box che racchiude tutta la schermata
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFBFEFEFF)) // Colore sfondo grigio chiaro
            .fillMaxWidth()
    ) {
        // Box bianco in alto con il titolo "Pokedex" e i bottoni
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFFFFF)) // Colore bianco blocco superiore
            .drawBehind {
                val shadowColor = Color(0xFFCCCCCC)
                // Linea utilizzata per separare le scritte in alto con la lista dei pokemon
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(0.0F, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 0.5.dp.toPx()
                )
                // Effetto ombra sotto la linea creata in precedenza
                drawRect(
                    color = shadowColor,
                    topLeft = Offset(0.0F, size.height),
                    size = Size(size.width, 4.dp.toPx())
                )
            }) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp),
                fontWeight = Bold
            )
            // Inserimento dell'immagine "stella", che è anche cliccabile per accedere ai pokemon salvati come preferiti
            Image(
                painter = painterResource(R.drawable.star),
                contentDescription = stringResource(R.string.favourites_filter),
                modifier = Modifier
                    .clickable {
                        if (isFavouritesFilterActive)
                            pokemonListViewModel.filterBy(FilterType.NONE)
                        else
                            pokemonListViewModel.filterBy(FilterType.FAVOURITES)
                        isFavouritesFilterActive = !isFavouritesFilterActive
                    }
                    .padding(start = 250.dp, top = 15.dp)
                    .size(height = 45.dp, width = 35.dp),
            )
            // Immagine della pokeball cliccabile, che serve a visualizzare la squadra salvata
            Image(
                painter = painterResource(R.drawable.smallpokeball),
                contentDescription = stringResource(R.string.captured_filter),
                modifier = Modifier
                    .clickable {
                        if (isCapturedFilterActive)
                            pokemonListViewModel.filterBy(FilterType.NONE)
                        else
                            pokemonListViewModel.filterBy(FilterType.CAPTURED)
                        isCapturedFilterActive = !isCapturedFilterActive
                    }
                    .padding(start = 297.dp, top = 15.dp)
                    .size(height = 45.dp, width = 35.dp),
            )
            // Immagine impostazioni
            Image(
                painter = painterResource(R.drawable.settings),
                contentDescription = stringResource(R.string.settings),
                modifier = Modifier
                    //TODO .clickable(onClick = /* Vai alla pagina delle impostazioni */)
                    .padding(start = 350.dp, top = 27.dp)
                    .scale(1.65f)
            )
            // Linea utilizzata per inserire i filtri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 65.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Primo bottono per il filtro "VERSIONE"
                Button(
                    onClick = { boxVersion = !boxVersion },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                        .height(40.dp)
                        .padding(top = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray, // Colore del bottone più scuro
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.version))
                }

                // Linea verticale divisoria per i bottoni dei filtri
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(40.dp)
                        .width(2.dp)
                        .padding(top = 6.dp)
                )
                // Secondo bottone per il filtro "TIPO"
                Button(
                    onClick = { boxType = !boxType },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .padding(top = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray, // Colore del bottone più scuro
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.type))
                }
                // Linea verticale divisoria per i bottoni dei filtri
                Divider(
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(38.dp)
                        .width(2.dp)
                        .padding(top = 6.dp)
                )
                // Terzo bottone per il filtro "ABILITA'"
                Button(
                    onClick = { boxAbility.value = !boxAbility.value },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                        .height(40.dp)
                        .padding(top = 6.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray, // Colore del bottone più scuro
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.ability))
                }
            }
        }

        if (boxVersion) {
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 535.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .border(
                        BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .pointerInput (Unit) {
                        detectTapGestures { boxVersion = false }
                    }
                    .clickable{ boxVersion = false }
                    .background(Color.White),
            ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.select_version),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 6.dp, bottom = 6.dp),
                        color = Color.DarkGray
                    )
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(8) { index ->
                            // Box che contiene le versioni dei pokemon
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                    .border(
                                        BorderStroke(2.dp, Color.Gray),
                                        shape = RoundedCornerShape(
                                            topStart = 10.dp,
                                            topEnd = 10.dp
                                        ),
                                    )
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 22.sp,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(top = 6.dp, bottom = 6.dp),
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }

            }
        }

        if (boxType) {
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 535.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .border(
                        BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                    )
                    .pointerInput (Unit) {
                                         detectTapGestures { boxType = false }
                    }
                    .clickable{ boxType = false }
                    .background(Color.White),
                ) {
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = stringResource(R.string.select_type),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(top = 6.dp, bottom = 8.dp),
                        color = Color.DarkGray
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(8) { index ->
                            // Box che contiene le versioni dei pokemon
                            Box(
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                    .border(
                                        BorderStroke(2.dp, Color.Gray),
                                        shape = RoundedCornerShape(8.dp),
                                    )
                                    .fillMaxWidth(),
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 22.sp,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(top = 6.dp, bottom = 6.dp),
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }
        }

        if (boxAbility.value) {
            //TODO ricerca abilità
        }
        /*
        DisposableEffect(isClickOutsideHandledVersion){
            if(isClickOutsideHandledVersion){
                boxVersion = false
                isClickOutsideHandledVersion = false
            }
            onDispose { }
        }

        DisposableEffect(isClickOutsideHandledType){
            if(isClickOutsideHandledType){
                boxType = false
                isClickOutsideHandledType = false
            }
            onDispose { }
        }*/

        val pokemonList by remember { pokemonListViewModel.pokemonList }

        // Lazy Column con i rettangoli
        LazyColumn(
            modifier = Modifier
                .padding(top = 135.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize()
        ) {
            items(pokemonList) { pokemon ->
                // Box principale per gli elementi della Lazy Column
                val color = intToColor(pokemon.dominantColor)
                pokemon.dominantColor
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(color),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 8.dp)
                                .fillMaxWidth(),
                        ) {
                            // Numero del pokemon nel pokedex
                            Text(
                                text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                                fontSize = 19.sp,
                                fontStyle = Italic,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                            )
                            // Box creato per avere una dimensione fissa del testo "nome pokemon"
                            Box(
                                modifier = Modifier.size(width = 137.dp, height = 28.dp)
                            ) {
                                Text(
                                    text = pokemon.name,
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                            // Immagine della stella per indicare se il pokemon è tra i preferiti
                            Box(
                                modifier = Modifier.clickable {
                                    pokemonListViewModel.toggleFavourite(pokemon = pokemon)
                                }, contentAlignment = Alignment.Center
                            ) {
                                val imageRes =
                                    if (pokemon.isFavourite) R.drawable.star else R.drawable.starempty
                                Image(
                                    painter = painterResource(imageRes),
                                    contentDescription = stringResource(
                                        if (pokemon.isFavourite) R.string.favourite else R.string.not_favourite
                                    ),
                                    modifier = Modifier.size(height = 30.dp, width = 30.dp)
                                )
                            }

                            // Immagine della pokeball per indicare se il pokemon è nella mia squadra
                            Box(
                                modifier = Modifier.clickable {
                                    pokemonListViewModel.toggleCaptured(pokemon = pokemon)
                                }, contentAlignment = Alignment.Center
                            ) {
                                val imageRes =
                                    if (pokemon.isCaptured) R.drawable.smallpokeball else R.drawable.smallpokeballempty
                                Image(
                                    painter = painterResource(imageRes),
                                    contentDescription = stringResource(
                                        if (pokemon.isCaptured) R.string.captured else R.string.not_captured
                                    ),
                                    modifier = Modifier.size(height = 30.dp, width = 30.dp)
                                )
                            }

                        }
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            // Box che contiene il "tipo" del pokemon
                            Box(
                                modifier = Modifier
                                    .padding(start = 18.dp, top = 10.dp)
                                    .border(
                                        BorderStroke(1.dp, Color.Gray),
                                        shape = RoundedCornerShape(2.dp),
                                    )
                                    .padding(start = 4.dp)
                            ) {

                                Text(
                                    text = stringResource(pokemon.type.value),
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 43.dp, end = 43.dp),
                                    color = Color.DarkGray
                                )
                            }
                            // Secondo box per i pokemon che hanno un doppio tipo
                            pokemon.secondType?.let {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 6.dp, top = 10.dp)
                                        .border(
                                            BorderStroke(1.dp, Color.Gray),
                                            shape = RoundedCornerShape(2.dp),
                                        )
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = stringResource(pokemon.secondType.value),
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(start = 43.dp, end = 43.dp),
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                    // Box utilizzato per contenere la foto del pokemon
                    Box(
                        modifier = Modifier
                            .size(90.dp, 100.dp)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(topStart = 35.dp))
                            .clip(RoundedCornerShape(bottomStart = 35.dp))
                            .background(Color.White),
                    ) {
                        if (pokemon.isFavourite) {
                            Image(
                                painter = painterResource(R.drawable.pokemon),
                                contentDescription = pokemon.name,
                                modifier = Modifier
                                    .size(80.dp)
                                    .align(Alignment.Center)
                            )
                        } else {
                            SubcomposeAsyncImage(model = pokemon.imageUrl,
                                contentDescription = pokemon.name,
                                loading = {
                                    CircularProgressIndicator(
                                        modifier = Modifier.fillMaxSize()
                                    )
                                })
                        }
                    }

                }
            }
        }
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    val totalRotation = 360f // Numero di gradi per una rotazione completa
                    val duration = 30L // Durata totale della rotazione in millisecondi
                    val increment =
                        totalRotation / duration.toFloat() // Incremento di rotazione per ogni millisecondo
                    for (i in 0 until duration.toInt()) {
                        rotationState += increment
                        delay(1) // Aggiungi un ritardo di 1 millisecondo
                    }
                    isRotated = !isRotated
                }
            },

            contentColor = contentColorFor(backgroundColor = Color.Transparent),
            modifier = Modifier
                .padding(16.dp)
                .size(86.dp)
                .graphicsLayer(rotationZ = rotation)
                .clickable { }
                .align(Alignment.BottomEnd)
                .background(Color.Transparent)
        ) {
            Image(

                painter = painterResource(id = R.drawable.menupokeball), // Replace with your image resource
                contentDescription = "FabButton",
                modifier = Modifier.fillMaxSize(), // Modificatore per riempire l'intera area del pulsante
                contentScale = ContentScale.FillBounds // Imposta la scala dell'immagine per adattarsi all'area del pulsante
            )
        }
        if (isRotated) {
            Column(
                modifier = Modifier
                    .padding(35.dp)
                    .align(Alignment.CenterEnd)
                    .offset(y = 260.dp)
                    .offset(x = 30.dp)

            ) {
                Button(
                    onClick = {
                        // Aggiungi l'azione per il primo pulsante verticale
                    },
                    modifier = Modifier
                        .width(125.dp)
                        .height(30.dp),
                    shape = androidx.compose.ui.graphics.RectangleShape,
                ) {
                    // Testo o contenuto del primo pulsante verticale
                    Text(text = "cerca")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        // Aggiungi l'azione per il secondo pulsante verticale
                    },
                    modifier = Modifier
                        .width(125.dp)
                        .height(30.dp),
                    shape = androidx.compose.ui.graphics.RectangleShape,
                ) {
                    // Testo o contenuto del secondo pulsante verticale
                    Text(text = "team")
                }
            }
        }
    }
}

