package it.codeclub.pokeclub.pokemonlist

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
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


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun PokedexScreen(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()
) {

    var saveSearch = remember { mutableStateOf("") }
    //variabili che permettono di gestire la keyboard della barra ricerca
    // ------------------------------------------
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    // ------------------------------------------
    var searchText = remember { mutableStateOf("") }
    //variabile utilizzata per capire se l'utente ha cliccato su cerca
    var isSearch by remember { mutableStateOf(false) }
    var rotate by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var isRotated by remember { mutableStateOf(false) }
    var boxVersion by remember { mutableStateOf(false) }
    var boxType by remember { mutableStateOf(false) }
    val boxAbility = remember { mutableStateOf(false) }

    //variabile che permette di mantenere traccia dello stato della rotazione
    val rotationState = animateFloatAsState(
        targetValue = if (isRotated) 360f else 0f,
        animationSpec = tween(durationMillis = 1000) // Durata dell'animazione in millisecondi
    )

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
            if (!isSearch) {
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
            } else {
                Row(
                ) {
                    //compare la barra di ricerca
                    TextField(
                        value = searchText.value,
                        onValueChange = { searchText.value = it },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                saveSearch.value = searchText.value
                                //tutto questo codice viene eseguito dopo aver cliccato invio
                                // questa parte di codice permette di
                                //far sparire la tastiera e di rendere invisibile la barra di ricerca
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                //codice che permette di far sparire la barra di ricerca quando clicco invio
                                //salvo la ricerca di dell'utente nella variabile che verrà usata per la query
                            }
                        ),
                        modifier = Modifier
                            .width(350.dp)
                            .padding(start = 5.dp, top = 65.dp, bottom = 6.dp)
                            .background(Color.White, shape = CircleShape),
                        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
                        shape = CircleShape,
                        colors = TextFieldDefaults.textFieldColors(
                            //backgroundColor = Color.DarkGray,
                            cursorColor = Color.White,
                            //textColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text("ex: Charizard", fontSize = 20.sp) }
                    )
                    //button cerca
                    IconButton(
                        onClick = {
                            //scompare la barra di ricerca
                            isSearch = !isSearch
                            //verifico quello che e' stato scritto dall'utente sia stato preso
                            //con successo
                            println(saveSearch.value) //non funziona
                        },
                        modifier = Modifier
                            .align(CenterVertically)
                            .padding(top = 52.dp, end = 10.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.search),
                            contentDescription = "Search",
                        )
                    }
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
                    .pointerInput(Unit) {
                        detectTapGestures { boxVersion = false }
                    }
                    .clickable { boxVersion = false }
                    .background(Color.White),
            ) {
                Column(
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
                    .pointerInput(Unit) {
                        detectTapGestures { boxType = false }
                    }
                    .clickable { boxType = false }
                    .background(Color.White),
            ) {
                Column(
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
                                    if (pokemon.isFavourite) R.drawable.star else R.drawable.star
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
                            if (pokemon.secondType != null) {
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
                            if (pokemon.isFavourite) {
                                // TODO load image from db
                            } else {
                                SubcomposeAsyncImage(
                                    model = pokemon.imageUrl,
                                    contentDescription = pokemon.name,
                                ) {
                                    val state = painter.state
                                    if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
                                        CircularProgressIndicator()
                                    } else {
                                        SubcomposeAsyncImageContent()
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }

        //button pokeball che ruota

        IconButton(
            onClick = {
                coroutineScope.launch {
                    isRotated = !isRotated
                    delay(900) // Attendiamo un secondo
                    rotate = !rotate
                }
            },
            modifier = Modifier
                //.padding(16.dp)
                .padding(end = 28.dp, bottom = 10.dp)
                .size(86.dp)
                .graphicsLayer(rotationZ = rotationState.value)
                .clickable { }
                .align(Alignment.BottomEnd)
                .background(Color.Unspecified)
        ) {
            Image(
                painter = painterResource(id = R.drawable.menupokeball),
                contentDescription = "FabButton",
                modifier = Modifier.fillMaxSize(), // Modificatore per riempire l'intera area del pulsante
                contentScale = ContentScale.FillBounds // Imposta la scala dell'immagine per adattarsi all'area del pulsante
            )
        }

        if (rotate) {
            Column(
                modifier = Modifier
                    .padding(35.dp)
                    .offset(y = 599.dp)
                    .offset(x = 239.dp)

            ) {
                Button(
                    onClick = {
                        // Aggiungi l'azione per il primo pulsante verticale
                    },
                    modifier = Modifier
                        .width(125.dp)
                        .height(33.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    // Testo o contenuto del primo pulsante verticale
                    Text(text = "team")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        //apro la barra di ricerca
                        isSearch = !isSearch
                        //chiudo la pokeball
                        coroutineScope.launch {
                            rotate = !rotate
                            isRotated = !isRotated
                        }
                    },
                    modifier = Modifier
                        .width(125.dp)
                        .height(33.dp),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Text(text = "cerca")
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "search image",
                        modifier = Modifier.padding(start = 20.dp)
                    )
                    // Testo o contenuto del secondo pulsante verticale
                }
            }
        }
    }
}



