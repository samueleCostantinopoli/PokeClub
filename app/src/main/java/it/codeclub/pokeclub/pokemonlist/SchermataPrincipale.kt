package it.codeclub.pokeclub.pokemonlist

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.db.entities.PokemonType
import it.codeclub.pokeclub.ui.theme.AppGrey
import it.codeclub.pokeclub.ui.theme.bug
import it.codeclub.pokeclub.ui.theme.dark
import it.codeclub.pokeclub.ui.theme.dragon
import it.codeclub.pokeclub.ui.theme.electric
import it.codeclub.pokeclub.ui.theme.fairy
import it.codeclub.pokeclub.ui.theme.fighting
import it.codeclub.pokeclub.ui.theme.fire
import it.codeclub.pokeclub.ui.theme.flying
import it.codeclub.pokeclub.ui.theme.ghost
import it.codeclub.pokeclub.ui.theme.grass
import it.codeclub.pokeclub.ui.theme.ground
import it.codeclub.pokeclub.ui.theme.ice
import it.codeclub.pokeclub.ui.theme.normal
import it.codeclub.pokeclub.ui.theme.poison
import it.codeclub.pokeclub.ui.theme.psychic
import it.codeclub.pokeclub.ui.theme.rock
import it.codeclub.pokeclub.ui.theme.steel
import it.codeclub.pokeclub.ui.theme.water
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@OptIn(
    ExperimentalComposeUiApi::class
)
@Composable
fun MainView(
    navController: NavController,
    pokemonListViewModel: PokemonListViewModel = hiltViewModel()
) {
    var isRotated by remember { mutableStateOf(false) }
    val rotationState = animateFloatAsState(
        targetValue = if (isRotated) 360f else 0f,
        animationSpec = tween(durationMillis = 1000), // Durata dell'animazione in millisecondi
        label = "Rotation"
    )
    var isColumnVisible by remember { mutableStateOf(false) }
    var rotate by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchTextPokemon = remember { pokemonListViewModel.searchPokemonQuery }
    val saveSearchPokemon = remember { mutableStateOf("") }

    //variabile utilizzata per capire se l'utente ha cliccato su ability
    val isAbilityClicked = remember { mutableStateOf(false) }
    val searchTextAbility = remember { pokemonListViewModel.searchAbilityQuery }
    val saveSearchAbility = remember { mutableStateOf("") }
    //variabile utilizzata per capire se l'utente ha cliccato su cerca
    val isSearchExpanded = remember { pokemonListViewModel.isSearchingPokemon }
    val favourite = remember {
        mutableStateOf(false)
    }
    val smallPokeballClick = remember {
        mutableStateOf(false)
    }

    //variabili usate per capire se bisogna aprire i box dei diversi filtri
    val boxVersion = remember { mutableStateOf(false) }
    val boxType1 = remember { mutableStateOf(false) }
    val boxType2 = remember { mutableStateOf(false) }

    //variabile utilizzata per capire se e' la prima volta che l'utente ha cliccato sul box version
    //in quel caso i selected iniziali devono per forza essere settati tutti a false poiche l'utente
    //non ha ancora cliccato su nessuna versione, perciò i background saranno tutti bianchi
    val firstTimeVersion = remember {
        mutableStateOf(0)
    }

    //variabile utilizzata per capire se l'utente ha selezionato un elemento nel box value
    //se l'ha selezionato il backgound dell'elemento sarà di un colore diverso dagli altri
    //in modo da far capire che quell'elemento e' stato scelto
    val selectedItemStates = remember { mutableStateListOf<Boolean>() }
    val versionBackground = Color.Gray.copy(alpha = 0.6f)

    //val per ricordare cosa l'utente ha selezionato su ogni filtro
    val type1 = remember { pokemonListViewModel.firstType }
    val type2 = remember { pokemonListViewModel.secondType }
    val version = remember { pokemonListViewModel.versionGroup }
    val versionGroupsList = remember {
        pokemonListViewModel.versionGroupsList
    }

    //variabile utilizzata per capire se un tipo e' stato selezioanto o meno ( duale di selectedItemState)
    val selectedTypeState = remember { mutableStateListOf<Boolean>() }

    //variabile utilizzata per capire se è la prima volta che si clicca sul box type ( duale
    // di first time version
    val firstTimeType = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            Modifier
                .background(AppGrey)
                .fillMaxWidth()
        ) {
            //val expandedWidth = maxWidth - 16.dp * 2
            //prima row che contiene nome dell'app stella impostazioni e pokeball cattura
            FirstRow(
                navController,
                pokemonListViewModel,
                favourite,
                smallPokeballClick,
                isSearchExpanded,
                focusManager,
                keyboardController,
                saveSearchPokemon,
                searchTextPokemon,
                isAbilityClicked,
                searchTextAbility,
                saveSearchAbility
            )

            //second row ( button version type e ability) piu lazy column che mostra i pokemon
            SecondRow(
                navController,
                boxVersion,
                version,
                boxType1,
                boxType2,
                type1,
                type2,
                pokemonListViewModel,
                isAbilityClicked,
                isSearchExpanded,
                favourite,
                smallPokeballClick
            )

            //pokeball finale in basso a sinistra
            //button pokeball che ruota
            Column(modifier = Modifier.align(Alignment.BottomEnd)) {
                AnimatedVisibility(
                    visible = isColumnVisible,
                    enter = slideInVertically(initialOffsetY = { it/2 }),
                    exit = slideOutVertically ( targetOffsetY  = { it })
                ) {
                Box {
                    Column {
                        if (rotate) {
                            Button(
                                onClick = {
                                    //apro la barra di ricerca
                                    isSearchExpanded.value = !isSearchExpanded.value
                                    // se la barra di ricerca per abilità e' aperta la chiudo
                                    if (isAbilityClicked.value) {
                                        isAbilityClicked.value = !isAbilityClicked.value
                                    }
                                    isColumnVisible=!isColumnVisible
                                    //chiudo la pokeball
                                    coroutineScope.launch {
                                        rotate = !rotate
                                        isRotated = !isRotated
                                    }
                                },
                                shape = RoundedCornerShape(22.dp),
                            ) {
                                Text(text = "cerca")
                                //Image(
                                //    painter = painterResource(id = R.drawable.search),
                                //    contentDescription = "search image",
                                //    //modifier = Modifier.padding(start = 20.dp)
                                //    modifier = Modifier.size(20.dp)
                                //)
                            }
                            // bottone nella pokebll per ricerca tramite abilità
                            Button(
                                onClick = {
                                    //verifico se la barra di ricerca del pokemon ( per nome è aperta) nel caso la chiudo
                                    if (isSearchExpanded.value) {
                                        isSearchExpanded.value = !isSearchExpanded.value
                                    }
                                    // quando clicco deve aprirsi la barra di ricerca per abilità(che si trova in first row)
                                    isAbilityClicked.value = !isAbilityClicked.value
                                    //chiudo la pokeball
                                    coroutineScope.launch {
                                        rotate = !rotate
                                        isRotated = !isRotated
                                        isColumnVisible=!isColumnVisible
                                    }
                                },
                                shape = RoundedCornerShape(22.dp),
                            ) {
                                Text(text = "abilità")
                                //Image(
                                //    painter = painterResource(id = R.drawable.search),
                                //    contentDescription = "search image",
                                //    //modifier = Modifier.padding(start = 20.dp)
                                //    modifier = Modifier.size(20.dp)
                                //)
                            }
                        }
                    }
                }
            }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            isRotated = !isRotated
                            delay(900) // Attendiamo un secondo
                            rotate = !rotate
                            isColumnVisible=!isColumnVisible
                        }
                    },
                    modifier = Modifier
                        .padding(end = 28.dp, bottom = 10.dp)
                        .size(76.dp)
                        .graphicsLayer(rotationZ = rotationState.value)
                        // .clickable { }
                        //.align(Alignment.BottomEnd)
                        .background(Color.Unspecified)

                ) {
                    Image(
                        painter = painterResource(id = R.drawable.menupokeball),
                        contentDescription = "FabButton",
                        modifier = Modifier.fillMaxSize(), // Modificatore per riempire l'intera area del pulsante
                        contentScale = ContentScale.FillBounds // Imposta la scala dell'immagine per adattarsi all'area del pulsante
                    )
                }
            }

            //se l'utente clicca sul button version ( if box version.value verrà eseguito),
            //viene aperto un box in basso che gli fa scegliere
            //le versioni

            if (boxVersion.value) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                        .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(Color.White),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.select_version),
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 4.dp),
                            color = Color.DarkGray
                        )
                        Column {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentPadding = PaddingValues(top = 0.dp, start = 1.dp, end = 1.dp)
                            ) {
                                //qui andranno il numero di versioni che sono disponibili, 8 per esempio
                                items(versionGroupsList.size) { index ->
                                    // qui c'e' la card che non e' altro composta da un box e al suo interno
                                    // il testo che indica il nuomero di versione
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .fillMaxWidth()
                                            .clickable {
                                                version.value = versionGroupsList[index]
                                                boxVersion.value = !boxVersion.value
                                                pokemonListViewModel.searchPokemon()
                                            }
                                            .background(versionBackground),

                                        ) {
                                        Text(
                                            text = versionGroupsList[index].versionGroupName.capitalize(
                                                Locale.current
                                            ),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(top = 6.dp, bottom = 6.dp),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            //text "ok" contenuto in fondo alla column, quando l'utente cliccherà su di esso
                            //le versioni che ha selezionato verranno inviate per mostrare i risultati
                            //per ora eseguo una print sul log per essere certo che tutto quello mostraro
                            //sia conforme con ciò che ha selezionato l'utente
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp,
                                        start = 9.dp,
                                        end = 9.dp,
                                        bottom = 8.dp
                                    )
                                    .border(
                                        BorderStroke(1.dp, Color.Gray),
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp,
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp
                                        ),
                                    )
                                    .fillMaxWidth()
                                    .clickable {
                                        //chiudo il box, le variabili version sono state già salvate nella lista
                                        version.value = null
                                        boxVersion.value = false
                                    }
                            )
                            {
                                Text(
                                    text = stringResource(R.string.all_version),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(
                                            top = 4.dp,
                                            bottom = 4.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                        .background(color = AppGrey)
                                        .width(330.dp),
                                )
                            }
                        }
                    }
                }
            }


            //BOX TYPE
            //il box type si comporta in modo uguale al box version,
            if (boxType1.value) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                        .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(Color.White)
                    ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.select_type),
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 4.dp),
                            color = Color.DarkGray
                        )
                        Column {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentPadding = PaddingValues(
                                    top = 0.dp,
                                    start = 1.dp,
                                    end = 1.dp
                                )
                            ) {
                                //qui andranno il numero di tipi di pokemon presenti
                                items(PokemonType.values().size) { index ->
                                    //verifico se il box viene aperto per la prima volta o se e' stato gia aperto
                                    //if (firstTimeType.value == 0) {
                                    //e' la prima volta che apro il box, l'utente non ha selezionato ancora nulla
                                    //tutti gli item sono su false, li aggiungo alla lista che ne tiene traccia
                                    //for (i in pokemonTypes.indices) {
                                    //imposto a false ogni singolo elemento
                                    //selectedTypeState.add(i, false)
                                    //}
                                    //fatto ciò, questa vale come prima volta di apertura del box, imposto a 1 first time
                                    //firstTimeType.value = 1
                                    //fino al prossimo avvio dell'app non posso piu rientrare in questo if

                                    //}

                                    //se viene selezionato dall'utente l'elemento nella posizione index, il suo
                                    //background diventerà grigio ( facendo capire che e' stato selezionato) altrimenti
                                    //sarà bianco

                                    //adjustColorIntensity( getColorForType(pokemonTypes[index]),0.5f)
                                    //var backgroundColor:Color?=null

                                    val backgroundColor =
                                        //if (selectedTypeState[index])
                                        getColorForType(PokemonType.values()[index])
                                    //else
                                    //      Color.White
                                    // qui c'e' la card che non e' altro composta da un box e al suo interno
                                    // il testo che indica il nuomero di versione
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .fillMaxWidth()
                                            .clickable {
                                                type1.value = PokemonType.values()[index]
                                                boxType1.value = false
                                                pokemonListViewModel.searchPokemon()
                                            }
                                            .background(backgroundColor),

                                        ) {
                                        Text(
                                            text = stringResource(id = PokemonType.values()[index].value),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(top = 6.dp, bottom = 6.dp),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            //text "ok" contenuto in fondo alla column, quando l'utente cliccherà su di esso
                            //le versioni che ha selezionato verranno inviate per mostrare i risultati
                            //per ora eseguo una print sul log per essere certo che tutto quello mostraro
                            //sia conforme con ciò che ha selezionato l'utente
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp,
                                        start = 9.dp,
                                        end = 9.dp,
                                        bottom = 8.dp
                                    )
                                    .border(
                                        BorderStroke(1.dp, Color.Gray),
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp,
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp
                                        ),
                                    )
                                    .fillMaxWidth()
                                    .clickable {
                                        //chiudo il box, le variabili version sono state già salvate nella lista
                                        type1.value = null
                                        boxType1.value = false
                                        pokemonListViewModel.searchPokemon()
                                    }
                            )
                            {
                                Text(
                                    text = stringResource(R.string.all_types),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(
                                            top = 4.dp,
                                            bottom = 4.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                        .background(color = AppGrey)
                                        .width(330.dp),
                                )
                            }
                        }
                    }
                }
            }
            // tendine per la selezione del secondo tipo
            if (boxType2.value) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(1f)
                        .padding(top = 6.dp, start = 16.dp, end = 16.dp, bottom = 0.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(Color.White),


                    ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.select_type),
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 4.dp),
                            color = Color.DarkGray
                        )
                        Column {
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentPadding = PaddingValues(
                                    top = 0.dp,
                                    start = 1.dp,
                                    end = 1.dp
                                )
                            ) {
                                //qui andranno il numero di tipi di pokemon presenti
                                items(PokemonType.values().size) { index ->
                                    val backgroundColor =
                                        getColorForType(PokemonType.values()[index])
                                    // qui c'e' la card che non e' altro composta da un box e al suo interno
                                    // il testo che indica il nuomero di versione
                                    Box(
                                        modifier = Modifier
                                            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .fillMaxWidth()
                                            .clickable {
                                                type2.value = PokemonType.values()[index]
                                                boxType2.value = false
                                                pokemonListViewModel.searchPokemon()
                                            }
                                            .background(backgroundColor),

                                        ) {
                                        Text(
                                            text = stringResource(id = PokemonType.values()[index].value),
                                            fontSize = 20.sp,
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(top = 6.dp, bottom = 6.dp),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                            //text "ok" contenuto in fondo alla column, quando l'utente cliccherà su di esso
                            //le versioni che ha selezionato verranno inviate per mostrare i risultati
                            //per ora eseguo una print sul log per essere certo che tutto quello mostraro
                            //sia conforme con ciò che ha selezionato l'utente
                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = 8.dp,
                                        start = 9.dp,
                                        end = 9.dp,
                                        bottom = 8.dp
                                    )
                                    .border(
                                        BorderStroke(1.dp, Color.Gray),
                                        shape = RoundedCornerShape(
                                            topStart = 12.dp,
                                            topEnd = 12.dp,
                                            bottomStart = 12.dp,
                                            bottomEnd = 12.dp
                                        ),
                                    )
                                    .fillMaxWidth()
                                    .clickable {
                                        //chiudo il box, le variabili version sono state già salvate nella lista
                                        boxType2.value = false
                                        type2.value = null
                                        pokemonListViewModel.searchPokemon()
                                    }
                            )
                            {
                                Text(
                                    text = stringResource(R.string.all_types),
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(
                                            top = 4.dp,
                                            bottom = 4.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                        .background(color = AppGrey)
                                        .width(330.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


//funzione che restituisce il colore dominate, riceve il tipo ( che l'utente clicca
//nel filtro tipo), e in base a quale tipo ja cliccato ad esso e' associato un colore
fun getColorForType(type: PokemonType): Color {
    val backgroundButton = Color.Gray.copy(alpha = 0.45f)
    return when (type) {
        PokemonType.BUG -> bug
        PokemonType.DRAGON -> dragon
        PokemonType.DARK -> dark
        PokemonType.ELECTRIC -> electric
        PokemonType.FAIRY -> fairy
        PokemonType.FIGHTING -> fighting
        PokemonType.FIRE -> fire
        PokemonType.FLYING -> flying
        PokemonType.GHOST -> ghost
        PokemonType.GRASS -> grass
        PokemonType.GROUND -> ground
        PokemonType.ICE -> ice
        PokemonType.NORMAL -> normal
        PokemonType.POISON -> poison
        PokemonType.PSYCHIC -> psychic
        PokemonType.ROCK -> rock
        PokemonType.STEEL -> steel
        PokemonType.WATER -> water
        else -> backgroundButton
    }
}


// Funzione per modificare l'intensità di un colore
fun adjustColorIntensity(color: Color, factor: Float): Color {
    val red = color.red * factor
    val green = color.green * factor
    val blue = color.blue * factor
    val alpha = color.alpha

    return Color(red.coerceIn(0f, 1f), green.coerceIn(0f, 1f), blue.coerceIn(0f, 1f), alpha)
}

