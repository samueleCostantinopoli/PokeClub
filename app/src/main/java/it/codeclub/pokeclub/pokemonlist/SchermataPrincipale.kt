package it.codeclub.pokeclub.pokemonlist

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.db.entities.PokemonType
import it.codeclub.pokeclub.pokemondetails.getColorForType
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
import timber.log.Timber

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
        animationSpec = tween(durationMillis = 1000) // Durata dell'animazione in millisecondi
    )
    var rotate by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val searchText = remember { pokemonListViewModel.searchQuery }
    val saveSearch = remember { mutableStateOf("") }

    //variabile utilizzata per capire se l'utente ha cliccato su ability
    val isAbilityClicked = remember { mutableStateOf(false) }
    val searchAbility = remember { mutableStateOf("") }
    val saveAbility = remember { mutableStateOf("") }
    //variabile utilizzata per capire se l'utente ha cliccato su cerca
    val isSearchExpanded = remember { pokemonListViewModel.isSearching }
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

    //variabile utilizzata dal sensei cantarini, qui andrà il numero di versioni che ci sono
    //sulla api, per ora la metto statica per vedere se funziona correttamente
    val numeroVersioni = listOf(
        "rosso fuoco",
        "verde foglia",
        "smeraldo",
        "oro",
        "argento",
        "rubino",
        "perla"
    )

    //lista che contiene le versioni che l'utente vuole filtrare per un pokemon
    //potrebbe contenere ad esempio ( 1,2,3) se l'utente vuole un pokemon che appartiene
    //alle prime 3 versioni
    val versionList = remember { mutableStateOf(mutableListOf<String>()) }

    //variabile utilizzata per capire se l'utente ha selezionato un elemento nel box value
    //se l'ha selezionato il backgound dell'elemento sarà di un colore diverso dagli altri
    //in modo da far capire che quell'elemento e' stato scelto
    val selectedItemStates = remember { mutableStateListOf<Boolean>() }
    val versionBackground = Color.Gray.copy(alpha = 0.6f)

    //variabile che contiene al lista dei tipi, anche questa dovrà essere popolata dal
    //maestro cantarini, per ora aggiungo un paio di stringhe statiche che rappresentano i tipi
    val typeList = remember { mutableStateOf(mutableListOf<String>()) }

    //val per ricordare cosa l'utente ha selezionato su ogni filtro
    val type1 = remember { mutableStateOf(String()) }
    val type2 = remember { mutableStateOf(String()) }
    val allTypes1 = remember { mutableStateOf(String()) }
    val allTypes2 = remember { mutableStateOf(String()) }
    val version = remember { mutableStateOf((String())) }
    val allVersion = remember { mutableStateOf(String()) }

    // inizialmente sono "null" poichè devono apparire tutti i tipi e nessuna versione in particolare attiva
    type1.value = null.toString()
    type2.value = null.toString()
    version.value = null.toString()

    //questa lista non sarà presente
    /*val pokemonTypes = listOf(
        "BUG",
        "DARK",
        "DRAGON",
        "ELECTRIC",
        "FAIRY",
        "FIGHTING",
        "FIRE",
        "FLYING",
        "GHOST",
        "GRASS",
        "GROUND",
        "ICE",
        "NORMAL",
        "POISON",
        "PSYCHIC",
        "ROCK",
        "STEEL",
        "WATER"
    )

     */



    //variabile utilizzata per capire se un tipo e' stato selezioanto o meno ( duale di selectedItemState)
    val selectedTypeState = remember { mutableStateListOf<Boolean>() }

    //variabile utilizzata per capire se è la prima volta che si clicca sul box type ( duale
    // di first time version
    val firstTimeType = remember {
        mutableStateOf(0)
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
                saveSearch,
                searchText,
                isAbilityClicked,
                searchAbility,
                saveAbility
            )

            //second row ( button version type e ability) piu lazy column che mostra i pokemon
            SecondRow(
                navController,
                boxVersion,
                version,
                allVersion,
                boxType1,
                boxType2,
                type1,
                type2,
                allTypes1,
                allTypes2,
                pokemonListViewModel,
                isAbilityClicked,
                isSearchExpanded,
                favourite,
                smallPokeballClick
            )

            //pokeball finale in basso a sinistra
            //button pokeball che ruota
            Column(modifier = Modifier.align(Alignment.BottomEnd)) {
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
                                    //chiudo la pokeball
                                    coroutineScope.launch {
                                        rotate = !rotate
                                        isRotated = !isRotated
                                    }
                                },
                                //modifier = Modifier
                                //    .width(125.dp)
                                //    .height(33.dp),
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
                                    }
                                },
                                //modifier = Modifier
                                //    .width(125.dp)
                                //    .height(33.dp),
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
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            isRotated = !isRotated
                            delay(900) // Attendiamo un secondo
                            rotate = !rotate
                        }
                    },
                    modifier = Modifier
                        .padding(end = 28.dp, bottom = 10.dp)
                        .size(86.dp)
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

            //gestione rotazione pokeball in fondo lo schermo
            /*if (rotate) {
                Column(
                    /*modifier = Modifier
                        .padding(35.dp)
                        .offset(y = 599.dp)
                        .offset(x = 239.dp)*/
                modifier=Modifier.align(Alignment.BottomEnd)
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
                            isSearchExpanded.value = !isSearchExpanded.value
                            // se la barra di ricerca per abilità e' aperta la chiudo
                            if (isAbilityClicked.value) {
                                isAbilityClicked.value = !isAbilityClicked.value
                            }
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
                    }
                }
            }*/

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
                                items(numeroVersioni.size) { index ->
                                    //verifico se il box viene aperto per la prima volta o se e' stato gia aperto
                                    //if (firstTimeVersion.value == 0) {
                                        //e' la prima volta che apro il box, l'utente non ha selezionato ancora nulla
                                        //tutti gli item sono su false, li aggiungo alla lista che ne tiene traccia
                                      //  for (i in numeroVersioni.indices) {
                                            //imposto a false ogni singolo elemento
                                        //    selectedItemStates.add(i, false)
                                          //  }
                                            //fatto ciò, questa vale come prima volta di apertura del box, imposto a 1 first time
                                            //firstTimeVersion.value = 1
                                            //fino al prossimo avvio dell'app non posso piu rientrare in questo if

                                    //}


                                            //se viene selezionato dall'utente l'elemento nella posizione index, il suo
                                            //background diventerà grigio ( facendo capire che e' stato selezionato) altrimenti
                                            //sarà bianco
                                            /*
                                    val backgroundColor =
                                        if (selectedItemStates[index]) Color.Gray else Color.White
                                    */
                                            // qui c'e' la card che non e' altro composta da un box e al suo interno
                                            // il testo che indica il nuomero di versione
                                            Box(
                                                modifier = Modifier
                                                    .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        //quando clicco il box voglio salvare la scelta dell' utente
                                                        /*
                                                        selectedItemStates[index] =
                                                            !selectedItemStates[index]
                                                        if (selectedItemStates[index]) {
                                                            versionList.value.add((index + 1).toString())
                                                        } else {
                                                            versionList.value.remove((index + 1).toString())
                                                        }

                                                         */
                                                        version.value = numeroVersioni[index]
                                                        boxVersion.value = !boxVersion.value
                                                        allVersion.value = null.toString()
                                                    }
                                                    .background(versionBackground),

                                                ) {
                                                Text(
                                                    text = numeroVersioni[index],
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
                                        allVersion.value = R.string.all_version.toString()
                                        version.value = null.toString()
                                        boxVersion.value = false
                                        /*
                                            for (i in 0 until versionList.value.size) {
                                                Timber
                                                    .tag("MyTag")
                                                    .d(versionList.value[i])
                                            }
                                            */
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
                                                getColorForType(PokemonType.values().get(index).toString())
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
                                                        //quando clicco il box voglio salvare la scelta dell' utente
                                                        //selectedTypeState[index] = !selectedTypeState[index]
                                                        //se il tipo viene selezionato per la ricerca
                                                        //if (selectedTypeState[index]) {
                                                        //verifico se ne ha già selezionati 2 di tipi
                                                        // typeList.value.add(pokemonTypes[index])
                                                        //}
                                                        //se il tipo viene rimosso ( quindi era stato precedentemente selezionato)
                                                        //lo rimuovo dalla lista dei selezionati
                                                        //else {
                                                        //typeList.value.remove(pokemonTypes[index])
                                                        //}
                                                        //chiudo il box, le variabili version sono state già salvate nella lista

                                                        /*for (i in 0 until typeList.value.size) {
                                                    Timber
                                                        .tag("MyTag")
                                                        .d(typeList.value[i])
                                                } */
                                                        type1.value = PokemonType.values().get(index).toString()
                                                        allTypes1.value = null.toString()
                                                        boxType1.value = false

                                                        //colora e cambia scritta del bottone TIPO1


                                                    }
                                                    .background(backgroundColor),

                                                ) {
                                                Text(
                                                    text = PokemonType.values()[index].toString(),
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
                                                allTypes1.value = R.string.all_types1.toString()
                                                type1.value = null.toString()
                                                boxType1.value = false
                                                /*for (i in 0 until typeList.value.size) {
                                            Timber
                                                .tag("MyTag")
                                                .d(typeList.value[i])
                                        }*/
                                            }
                                    )
                                    {
                                        Text(
                                            text = stringResource(R.string.all_types1),
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
                                                getColorForType(PokemonType.values()[index].toString())
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
                                                        //quando clicco il box voglio salvare la scelta dell' utente
                                                        //selectedTypeState[index] = !selectedTypeState[index]
                                                        //se il tipo viene selezionato per la ricerca
                                                        //if (selectedTypeState[index]) {
                                                        //verifico se ne ha già selezionati 2 di tipi
                                                        //  typeList.value.add(pokemonTypes[index])
                                                        //}
                                                        //se il tipo viene rimosso ( quindi era stato precedentemente selezionato)
                                                        //lo rimuovo dalla lista dei selezionati
                                                        //else {
                                                        //  typeList.value.remove(pokemonTypes[index])
                                                        //}

                                                        /*for (i in 0 until typeList.value.size) {
                                                    Timber
                                                        .tag("MyTag")
                                                        .d(typeList.value[i])
                                                }*/
                                                        type2.value = PokemonType.values()[index].toString()
                                                        allTypes2.value = null.toString()
                                                        boxType2.value = false

                                                        //colora e cambia scritta del bottone TIPO1

                                                    }
                                                    .background(backgroundColor),

                                                ) {
                                                Text(
                                                    text = PokemonType.values()[index].toString(),
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
                                                type2.value = null.toString()
                                                allTypes2.value = R.string.all_types2.toString()
                                                /*for (i in 0 until typeList.value.size) {
                                            Timber
                                                .tag("MyTag")
                                                .d(typeList.value[i])
                                        }*/
                                            }
                                    )
                                    {
                                        Text(
                                            text = stringResource(R.string.all_types2),
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
fun getColorForType(typeName: String): Color {
    val backgroundButton = Color.Gray.copy(alpha = 0.45f)
    return when (typeName) {
        "BUG" -> bug
        "DARK" -> dark
        "DRAGON" -> dragon
        "ELECTRIC" -> electric
        "FAIRY" -> fairy
        "FIGHTING" -> fighting
        "FIRE" -> fire
        "FLYING" -> flying
        "GHOST" -> ghost
        "GRASS" -> grass
        "GROUND" -> ground
        "ICE" -> ice
        "NORMAL" -> normal
        "POISON" -> poison
        "PSYCHIC" -> psychic
        "ROCK" -> rock
        "STEEL" -> steel
        "WATER" -> water
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

