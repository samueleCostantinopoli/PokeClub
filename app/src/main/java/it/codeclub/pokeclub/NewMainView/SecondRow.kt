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
import androidx.compose.runtime.setValue
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
    pokemonListViewModel: PokemonListViewModel,
    isAbilityClicked: MutableState<Boolean>,
    isSearchExpanded: MutableState<Boolean>
) {

    val pokemonList by remember { pokemonListViewModel.pokemonList }

    //seconda riga, contiene i filtri abilità, fa comparire la barra di ricerca, piu la lazy column
    //che occupa il resto della schermata

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
                            //quando si clicca il box bisogna navigare nella schermata dettaglio
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
                                },
                                contentAlignment = Alignment.Center
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
}
