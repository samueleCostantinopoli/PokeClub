package it.codeclub.pokeclub.pokemonlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.ui.theme.AppGrey
import it.codeclub.pokeclub.utils.UIUtils

@Composable
fun SecondRow(
    navController: NavController,
    boxVersion: MutableState<Boolean>,
    boxType: MutableState<Boolean>,
    pokemonListViewModel: PokemonListViewModel,
    isAbilityClicked: MutableState<Boolean>,
    isSearchExpanded: MutableState<Boolean>,
    favouritesFilter: MutableState<Boolean>,
    capturedFilter: MutableState<Boolean>
) {

    val pokemonList by remember { pokemonListViewModel.shownPokemonList }

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
                Spacer(Modifier.height(18.dp))
                Row(
                    modifier = Modifier
                        .background(color, RoundedCornerShape(15.dp))
                        .padding(bottom = 0.dp)
                        .fillMaxWidth()
                        .height(83.dp)
                        .clickable {
                            //quando si clicca il box bisogna navigare nella schermata dettaglio
                            navController.navigate("pokemon_detail_screen/${pokemon.name}")
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Upper row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 6.dp, top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Pokemon ID and name
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Numero del pokemon nel pokedex
                                Text(
                                    text = "#${pokemon.pokemonId.toString().padStart(3, '0')}",
                                    fontSize = 19.sp,
                                    fontStyle = FontStyle.Italic,
                                    //come colore inserisco il colore dominante con intensità più alta
                                    color = Color(UIUtils.adjustForBackground(pokemon.dominantColor)),
                                    modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                                )
                                Text(
                                    text = pokemon.name.capitalize(Locale.current),
                                    fontSize = 18.sp,
                                    color = Color(UIUtils.adjustForBackground(pokemon.dominantColor)),
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                            // Pokemon favourite and captured icons
                            Row(
                                modifier = Modifier.padding(end = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Immagine della stella per indicare se il pokemon è tra i preferiti
                                Box(
                                    modifier = Modifier
                                        // .clickable {
                                        //pokemonListViewModel.toggleFavourite(pokemon = pokemon)
                                        //}
                                        .padding(start = 10.dp), //contentAlignment = Alignment.End
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            if (pokemon.isFavourite) R.drawable.fillstar else R.drawable.star
                                        ),
                                        contentDescription = stringResource(
                                            if (pokemon.isFavourite) R.string.favourite else R.string.not_favourite
                                        ),
                                        modifier = Modifier
                                            .clickable {
                                                favouritesFilter.value = false
                                                capturedFilter.value = false
                                                pokemonListViewModel.toggleFavourite(
                                                    pokemon = pokemon
                                                )
                                            }
                                            .size(height = 30.dp, width = 30.dp)
                                            .align(
                                                Alignment.CenterEnd
                                            ),
                                        tint = if (pokemon.isFavourite)
                                            Color.Unspecified
                                        else
                                            Color(UIUtils.adjustForBackground(pokemon.dominantColor))

                                    )
                                }

                                // Immagine della pokeball per indicare se il pokemon è nella mia squadra
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            favouritesFilter.value = false
                                            capturedFilter.value = false
                                            pokemonListViewModel.toggleCaptured(pokemon = pokemon)
                                        }
                                        .padding(start = 5.dp)
                                        .shadow(
                                            elevation = if (isDominantBlack(color) || isDominantDarkBrown(
                                                    color
                                                )
                                            ) 8.dp else 0.dp,
                                            shape = RoundedCornerShape(50)
                                        )
                                    //contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(if (pokemon.isCaptured) R.drawable.smallpokeball else R.drawable.smallpokeballempty),
                                        contentDescription = stringResource(
                                            if (pokemon.isCaptured) R.string.captured else R.string.not_captured
                                        ),
                                        modifier = Modifier
                                            .size(height = 25.dp, width = 25.dp)
                                            .align(
                                                Alignment.CenterEnd
                                            ),
                                        tint = if (pokemon.isCaptured)
                                            Color.Unspecified
                                        else
                                            Color(UIUtils.adjustForBackground(pokemon.dominantColor))
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        // Pokemon type(s)
                        Row(
                            modifier = Modifier.padding(bottom = 5.dp)
                        ) {
                            // Box che contiene il "tipo" del pokemon
                            Box(
                                modifier = Modifier
                                    .padding(start = 10.dp, top = 10.dp)
                                    .border(
                                        BorderStroke(
                                            1.dp,
                                            if (isDominantBlack(color) || isDominantDarkBrown(
                                                    color
                                                )
                                            ) Color.LightGray
                                            else Color.Black
                                        ),
                                        shape = RoundedCornerShape(2.dp),
                                    )
                                    .padding(start = 4.dp)
                            ) {
                                Text(
                                    text = "   " + stringResource(pokemon.type.value) + "   ",
                                    fontSize = 16.sp,
                                    modifier = Modifier.align(Alignment.Center),
                                    color = if (isDominantBlack(color) || isDominantDarkBrown(
                                            color
                                        )
                                    ) Color.LightGray
                                    else Color.Black,
                                )
                            }

                            // Secondo box per i pokemon che hanno un doppio tipo
                            pokemon.secondType?.let {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 6.dp, top = 10.dp)
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                if (isDominantBlack(color) || isDominantDarkBrown(
                                                        color
                                                    )
                                                ) Color.LightGray
                                                else Color.Black
                                            ),
                                            shape = RoundedCornerShape(2.dp),
                                        )
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "   " + stringResource(pokemon.secondType.value) + "   ",
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.Center),
                                        color = if (isDominantBlack(color) || isDominantDarkBrown(
                                                color
                                            )
                                        ) Color.LightGray
                                        else Color.Black,
                                    )
                                }
                            }
                        }
                    }

                    val imageModifier = Modifier
                        .fillMaxHeight()
                        .background(Color.White)
                        .border(
                            1.dp,
                            Color(pokemon.dominantColor),
                            RoundedCornerShape(0.dp, 15.dp, 15.dp, 0.dp)
                        )
                    if (pokemon.isFavourite) {
                        Image(
                            bitmap = pokemon.image!!.asImageBitmap(),
                            contentDescription = pokemon.name,
                            modifier = imageModifier.size(83.dp)
                        )
                    } else
                        AsyncImage(
                            model = pokemon.imageUrl,
                            contentDescription = pokemon.name,
                            contentScale = ContentScale.Fit,
                            modifier = imageModifier
                        )
                }
            }
        }
    }
}

fun intToColor(colorValue: Int): Color {
    return Color(colorValue)
}

fun isDominantBlack(color: Color, threshold: Float = 0.2f): Boolean {
    val luminance = (0.299f * color.red + 0.587f * color.green + 0.114f * color.blue)
    return luminance <= threshold
}

fun isDominantDarkRed(color: Color, threshold: Float = 0.2f): Boolean {
    val luminance = (0.299f * color.red + 0.587f * color.green + 0.114f * color.blue)
    val redness = color.red / (color.red + color.green + color.blue)

    return luminance <= threshold && redness >= 0.2f
}

fun isDominantDarkBrown(
    color: Color,
    lowerThreshold: Float = 0.1f,
    upperThreshold: Float = 0.3f
): Boolean {
    val luminance = (0.299f * color.red + 0.587f * color.green + 0.114f * color.blue)
    return luminance in lowerThreshold..upperThreshold
}