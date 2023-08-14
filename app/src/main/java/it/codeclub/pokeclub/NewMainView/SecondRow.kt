package it.codeclub.pokeclub.NewMainView

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.pokemonlist.PokemonListViewModel
import it.codeclub.pokeclub.pokemonlist.intToColor
import it.codeclub.pokeclub.ui.theme.AppGrey

@Composable
fun secondRow(
    navController: NavController,
    boxVersion: MutableState<Boolean>,
    boxType: MutableState<Boolean>,
    boxAbility: MutableState<Boolean>,
    pokemonListViewModel: PokemonListViewModel,
    isAbilityClicked: MutableState<Boolean>,
    isSearchExpanded: MutableState<Boolean>
) {

    val pokemonList by remember { pokemonListViewModel.pokemonList }

    //seconda riga, contiene i filtri abilità, fa comparire la barra di ricerca, piu la lazy column
    //che occupa il resto della schermata

    //lista che contiene le versioni che l'utente vuole filtrare per un pokemon
    //potrebbe contenere ad esempio ( 1,2,3) se l'utente vuole un pokemon che appartiene
    //alle prime 3 versioni
    var versionList = remember { mutableStateOf(mutableListOf<String>()) }


    //variabile utilizzata per capire se l'utente ha selezionato un elemento nel box value
    //se l'ha selezionato il backgound dell'elemento sarà di un colore diverso dagli altri
    //in modo da far capire che quell'elemento e' stato scelto
    val selectedItemStates = remember { mutableStateListOf<Boolean>() }

    //variabile utilizzata per capire se e' la prima volta che l'utente ha cliccato sul box version
    //in quel caso i selected iniziali devono per forza essere settati tutti a false poiche l'utente
    //non ha ancora cliccato su nessuna versione, perciò i background saranno tutti bianchi
    var firstTimeVersion = remember {
        mutableStateOf(0)
    }

    //variabile utilizzata dal sensei cantarini, qui andrà il numero di versioni che ci sono
    //sulla api, per ora la metto statica per vedere se funziona correttamente
    var numeroVersioni = 8

    //variabile che contiene al lista dei tipi, anche questa dovrà essere popolata dal
    //maestro cantarini, per ora aggiungo un paio di stringhe statiche che rappresentano i tipi
    var typeList = remember { mutableStateOf(mutableListOf<String>()) }

    //questa lista non sarà presente
    val pokemonTypes = listOf(
        "Tutti tipi",
        "Acqua",
        "Fuoco",
        "Erba",
        "Elettro",
        "Roccia",
        "Veleno",
        "Psico",
        "Volante"
    )

    //variabile utilizzata per capire se un tipo e' stato selezioanto o meno ( duale di selectedItemState)
    val selectedTypeState = remember { mutableStateListOf<Boolean>() }

    //variabile utilizzata per capire se è la prima volta che si clicca sul box type ( duale
    // di first time version
    var firstTimeType = remember {
        mutableStateOf(0)
    }
    //il tutto è contenuto in una riga
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(top = Dimensions.distanzaDallaPrimaRiga)
                .fillMaxWidth()
                .height(60.dp)
                .background(AppGrey)
        ) {
            //qui abbiamo i 3 tipi di filtri
            // Primo button per il filtro "VERSIONE"
            Button(
                onClick = {
                    boxVersion.value = !boxVersion.value
                    //se box type e' aperto lo chiudo
                    if (boxType.value) {
                        boxType.value = !boxType.value
                    }
                },
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
                onClick = {
                    boxType.value = !boxType.value
                    //se il box version e' aperto lo chiudo, non voglio 2 box aperti contemporaneamente
                    if (boxVersion.value) {
                        boxVersion.value = !boxVersion.value
                    }
                },
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
                onClick = {
                    //verifico se la barra di ricerca del pokemon ( per nome è aperta) nel caso la chiudo
                    if (isSearchExpanded.value) {
                        isSearchExpanded.value = !isSearchExpanded.value
                    }
                    // quando clicco deve aprirsi la barra di ricerca per abilità(che si trova in first row)
                    isAbilityClicked.value = !isAbilityClicked.value
                },
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
        //piu lazy column che occupa il resto della schermata
        LazyColumn(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
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
                        .background(color)
                        .clickable {
                            navController.navigate("pokemon_detail_screen/${pokemon.name}")
                        },
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
                                fontStyle = FontStyle.Italic,
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
                                    if (pokemon.isFavourite) R.drawable.fillstar else R.drawable.star
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
                            modifier = Modifier.padding(bottom = 5.dp)
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
                                    modifier = Modifier.padding(start = 34.dp, end = 34.dp),
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
                                        modifier = Modifier.padding(start = 34.dp, end = 34.dp),
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier
                        .size(100.dp, 87.dp)
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(topStart = 35.dp))
                        .clip(RoundedCornerShape(bottomStart = 35.dp)),
                    ){
                        Image(
                            //painter = rememberAsyncImagePainter(pokemon.image),
                            painter = painterResource(id = R.drawable.pokemon),
                            contentDescription = pokemon.name,
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                        )
                    }
                }
            }
        }

    }

    //se l'utente clicca sul button version ( if box version.value verrà eseguito),
    //viene aperto un box in basso che gli fa scegliere
    //le versioni
    if (boxVersion.value) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .padding(top = 500.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize()
                .border(
                    BorderStroke(2.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                )
                .background(Color.White),
            contentAlignment = Alignment.BottomCenter
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
                Column {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        //qui andranno il numero di versioni che sono disponibili, 8 per esempio
                        items(numeroVersioni) { index ->
                            //verifico se il box viene aperto per la prima volta o se e' stato gia aperto
                            if (firstTimeVersion.value == 0) {
                                //e' la prima volta che apro il box, l'utente non ha selezionato ancora nulla
                                //tutti gli item sono su false, li aggiungo alla lista che ne tiene traccia
                                for (i in 0 until numeroVersioni) {
                                    //imposto a false ogni singolo elemento
                                    selectedItemStates.add(i, false)
                                }
                                //fatto ciò, questa vale come prima volta di apertura del box, imposto a 1 first time
                                firstTimeVersion.value = 1
                                //fino al prossimo avvio dell'app non posso piu rientrare in questo if

                            }

                            //se viene selezionato dall'utente l'elemento nella posizione index, il suo
                            //background diventerà grigio ( facendo capire che e' stato selezionato) altrimenti
                            //sarà bianco

                            var backgroundColor =
                                if (selectedItemStates[index]) Color.Gray else Color.White

                            // qui c'e' la card che non e' altro composta da un box e al suo interno
                            // il testo che indica il nuomero di versione
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
                                    .fillMaxWidth()
                                    .clickable {
                                        //quando clicco il box voglio salvare la scelta dell' utente
                                        selectedItemStates[index] = !selectedItemStates[index]
                                        if (selectedItemStates[index]) {
                                            versionList.value.add((index + 1).toString())
                                        } else {
                                            versionList.value.remove((index + 1).toString())
                                        }
                                    }
                                    .background(backgroundColor),

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
                    //text "ok" contenuto in fondo alla column, quando l'utente cliccherà su di esso
                    //le versioni che ha selezionato verranno inviate per mostrare i risultati
                    //per ora eseguo una print sul log per essere certo che tutto quello mostraro
                    //sia conforme con ciò che ha selezionato l'utente
                    Text(
                        text = stringResource(R.string.ok),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 10.dp, start = 26.dp)
                            .clickable {
                                //chiudo il box, le variabili version sono state già salvate nella lista
                                boxVersion.value = false
                                for (i in 0 until versionList.value.size) {
                                    Log.d("MyTag", versionList.value[i])
                                }
                            }
                            .border(
                                BorderStroke(2.dp, Color.Gray),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .background(color = AppGrey)
                            .width(330.dp),
                    )
                }
            }
        }
    }

    //il box type si comporta in modo uguale al box version,

    if (boxType.value) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .padding(top = 500.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize()
                .border(
                    BorderStroke(2.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                )
                .background(Color.White),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.select_type),
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 6.dp),
                    color = Color.DarkGray
                )
                Column {
                    LazyColumn(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        //qui andranno il numero di tipi di pokemon presenti
                        items(pokemonTypes.size) { index ->
                            //verifico se il box viene aperto per la prima volta o se e' stato gia aperto
                            if (firstTimeType.value == 0) {
                                //e' la prima volta che apro il box, l'utente non ha selezionato ancora nulla
                                //tutti gli item sono su false, li aggiungo alla lista che ne tiene traccia
                                for (i in pokemonTypes.indices) {
                                    //imposto a false ogni singolo elemento
                                    selectedTypeState.add(i, false)
                                }
                                //fatto ciò, questa vale come prima volta di apertura del box, imposto a 1 first time
                                firstTimeType.value = 1
                                //fino al prossimo avvio dell'app non posso piu rientrare in questo if

                            }

                            //se viene selezionato dall'utente l'elemento nella posizione index, il suo
                            //background diventerà grigio ( facendo capire che e' stato selezionato) altrimenti
                            //sarà bianco

                            var backgroundColor =
                                if (selectedTypeState[index]) Color.Gray else Color.White

                            // qui c'e' la card che non e' altro composta da un box e al suo interno
                            // il testo che indica il nuomero di versione
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
                                    .fillMaxWidth()
                                    .clickable {
                                        //quando clicco il box voglio salvare la scelta dell' utente
                                        selectedTypeState[index] = !selectedTypeState[index]
                                        if (selectedTypeState[index]) {
                                            typeList.value.add(pokemonTypes[index])
                                        } else {
                                            typeList.value.remove(pokemonTypes[index])
                                        }
                                    }
                                    .background(backgroundColor),

                                ) {
                                Text(
                                    text = pokemonTypes[index],
                                    fontSize = 22.sp,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(top = 6.dp, bottom = 6.dp),
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                    //text "ok" contenuto in fondo alla column, quando l'utente cliccherà su di esso
                    //le versioni che ha selezionato verranno inviate per mostrare i risultati
                    //per ora eseguo una print sul log per essere certo che tutto quello mostraro
                    //sia conforme con ciò che ha selezionato l'utente
                    Text(
                        text = stringResource(R.string.ok),
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 10.dp, start = 26.dp)
                            .clickable {
                                //chiudo il box, le variabili version sono state già salvate nella lista
                                boxType.value = false
                                for (i in 0 until typeList.value.size) {
                                    Log.d("MyTag", typeList.value[i])
                                }
                            }
                            .border(
                                BorderStroke(2.dp, Color.Gray),
                                shape = RoundedCornerShape(8.dp),
                            )
                            .background(color = AppGrey)
                            .width(330.dp),
                    )
                }
            }
        }
    }
}
