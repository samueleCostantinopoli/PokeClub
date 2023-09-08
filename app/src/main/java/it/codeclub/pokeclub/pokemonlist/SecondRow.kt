package it.codeclub.pokeclub.pokemonlist

import android.annotation.SuppressLint
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
import androidx.compose.ui.draw.clip
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
import it.codeclub.pokeclub.db.entities.PokemonType
import it.codeclub.pokeclub.db.entities.VersionGroupEntity
import it.codeclub.pokeclub.ui.theme.AppGrey

@SuppressLint("SuspiciousIndentation")
@Composable
fun SecondRow(
    navController: NavController,
    boxVersion: MutableState<Boolean>,
    version: MutableState<VersionGroupEntity?>,
    boxType1: MutableState<Boolean>,
    boxType2: MutableState<Boolean>,
    type1: MutableState<PokemonType?>,
    type2: MutableState<PokemonType?>,
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
        val colorLine = Color.Gray.copy(alpha = 0.3f)
        val backgroundButton = Color.Gray.copy(alpha = 0.45f)
        val textButton = Color.DarkGray.copy(
            red = Color.DarkGray.red + 0.1f,   // Aumenta il canale rosso
            green = Color.DarkGray.green + 0.1f, // Aumenta il canale verde
            blue = Color.DarkGray.blue + 0.1f   // Aumenta il canale blu
        )
        Row(
            modifier = Modifier
                .padding(top = Dimensions.distanzaDallaPrimaRiga)
                .fillMaxWidth()
                .height(45.dp)
                .background(AppGrey)
        ) {
            //qui abbiamo i 3 tipi di filtri
            // Primo button per il filtro "VERSIONE"

            Button(
                onClick = {
                    boxVersion.value = !boxVersion.value
                    //se box type e' aperto lo chiudo
                    if (boxType1.value) {
                        boxType1.value = !boxType1.value
                    }
                    if (boxType2.value) {
                        boxType2.value = !boxType2.value
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .height(40.dp)
                    .padding(top = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = if (version.value != null) {
                    ButtonDefaults.buttonColors(
                        containerColor = Color.DarkGray,
                        contentColor = Color.White
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = backgroundButton,
                        contentColor = textButton
                    )
                }
            ) {
                Text(
                    text = if (version.value != null)
                        version.value!!.versionGroupName.capitalize(Locale.current)
                    else stringResource(R.string.version)
                )
            }

            // Linea verticale divisoria per i bottoni dei filtri
            Divider(
                color = colorLine,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(40.dp)
                    .width(2.dp)
                    .padding(top = 6.dp)
            )
            // Secondo bottone per il filtro "TIPO"
            Button(
                onClick = {
                    boxType1.value = !boxType1.value
                    //se il box version e' aperto lo chiudo, non voglio 2 box aperti contemporaneamente
                    if (boxVersion.value) {
                        boxVersion.value = !boxVersion.value
                    }
                    if (boxType2.value) {
                        boxType2.value = !boxType2.value
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(top = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = if (type1.value != null) {
                    ButtonDefaults.buttonColors(
                        containerColor = getColorForType(type1.value!!),
                        contentColor = Color.White
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = backgroundButton,
                        contentColor = textButton
                    )
                }
            ) {
                Text(
                    text = if (type1.value != null)
                        stringResource(id = type1.value!!.value)
                    else
                        stringResource(R.string.first_type)
                )
            }
            // Linea verticale divisoria per i bottoni dei filtri
            Divider(
                color = colorLine,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(38.dp)
                    .width(2.dp)
                    .padding(top = 6.dp)
            )
            // Terzo bottone per il filtro secondo tipo
            Button(
                onClick = {
                    boxType2.value = !boxType2.value
                    //se il box version e' aperto lo chiudo, non voglio 2 box aperti contemporaneamente
                    if (boxVersion.value) {
                        boxVersion.value = !boxVersion.value
                    }
                    if (boxType1.value) {
                        boxType1.value = !boxType1.value
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 6.dp)
                    .height(40.dp)
                    .padding(top = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = if (type2.value != null) {
                    ButtonDefaults.buttonColors(
                        containerColor = getColorForType(type2.value!!), // Colore del bottone più scuro
                        contentColor = Color.White
                    )
                } else {
                    ButtonDefaults.buttonColors(
                        containerColor = backgroundButton, // Colore del bottone più scuro
                        contentColor = textButton
                    )
                }
            ) {
                Text(
                    text = if (type2.value != null)
                        stringResource(type2.value!!.value)
                    else
                        stringResource(R.string.second_type)
                )
            }
        }
        Divider(
            color = colorLine,
            thickness = 1.dp,
            modifier = Modifier.fillMaxWidth()
        )
        //piu lazy column che occupa il resto della schermata
        LazyColumn(
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp)
                .fillMaxSize()
        ) {
            items(pokemonList) { pokemon ->
                // Box principale per gli elementi della Lazy Column
                val domCol = Color(pokemon.pokemonEntity.dominantColor)
                val lighterDominantColor = domCol.copy(alpha = 0.3f)
                val color = domCol.copy(alpha = 0.7f)
                val darkenedColor = Color(
                    red = domCol.red * 0.5f,    // Riduci il valore del componente Rosso
                    green = domCol.green * 0.5f,  // Riduci il valore del componente Verde
                    blue = domCol.blue * 0.5f    // Riduci il valore del componente Blu
                )
                //val color = intToColor(pokemon.dominantColor)
                Spacer(Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        //.padding(bottom = 0.dp)
                        .fillMaxWidth()
                        .height(83.dp)
                        .clickable {
                            //quando si clicca il box bisogna navigare nella schermata dettaglio
                            navController.navigate("pokemon_detail_screen/${pokemon.pokemonEntity.name}")
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(color),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Upper row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                //.background(color)
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
                                    text = "#${
                                        pokemon.pokemonEntity.pokemonId.toString().padStart(3, '0')
                                    }",
                                    fontSize = 19.sp,
                                    fontStyle = FontStyle.Italic,
                                    //come colore inserisco il colore dominante con intensità più alta
                                    //color = Color(UIUtils.adjustForBackground(pokemon.dominantColor)),
                                    color = darkenedColor,
                                    modifier = Modifier.padding(start = 6.dp, end = 6.dp)
                                )
                                Text(
                                    text = pokemon.pokemonEntity.name.capitalize(Locale.current),
                                    fontSize = 18.sp,
                                    //color = Color(UIUtils.adjustForBackground(pokemon.dominantColor)),
                                    color = darkenedColor,
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
                                            if (pokemon.pokemonEntity.isFavourite) R.drawable.fillstar else R.drawable.star
                                        ),
                                        contentDescription = stringResource(
                                            if (pokemon.pokemonEntity.isFavourite) R.string.favourite else R.string.not_favourite
                                        ),
                                        modifier = Modifier
                                            .clickable {
                                                favouritesFilter.value = false
                                                capturedFilter.value = false
                                                pokemonListViewModel.toggleFavourite(
                                                    pokemon = pokemon.pokemonEntity
                                                )
                                            }
                                            .size(height = 30.dp, width = 30.dp)
                                            .align(
                                                Alignment.CenterEnd
                                            ),
                                        tint = darkenedColor
                                    )
                                }

                                // Immagine della pokeball per indicare se il pokemon è nella mia squadra
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            favouritesFilter.value = false
                                            capturedFilter.value = false
                                            pokemonListViewModel.toggleCaptured(pokemon = pokemon.pokemonEntity)
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
                                        painter = painterResource(if (pokemon.pokemonEntity.isCaptured) R.drawable.smallpokeball else R.drawable.smallpokeballempty),
                                        contentDescription = stringResource(
                                            if (pokemon.pokemonEntity.isCaptured) R.string.captured else R.string.not_captured
                                        ),
                                        modifier = Modifier
                                            .size(height = 25.dp, width = 25.dp)
                                            .align(
                                                Alignment.CenterEnd
                                            ),
                                        tint = darkenedColor
                                    )
                                }
                            }
                        }
                        Spacer(
                            modifier = Modifier
                                .height(4.dp)
                                .background(color)
                        )

                        // Pokemon type(s)
                        Row(
                            modifier = Modifier.padding(bottom = 6.dp)
                            //.background(color)
                        ) {
                            // Box che contiene il "tipo" del pokemon
                            // caso in cui il pokemon ha due tipi
                            if (pokemon.pokemonEntity.secondType != null) {
                                Box(
                                    modifier = Modifier
                                        .weight(3.32f)
                                        .padding(start = 12.dp, top = 8.dp)
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                /*if (isDominantBlack(color) || isDominantDarkBrown(
                                                        color
                                                    )
                                                ) Color.LightGray
                                                else Color.Black

                                                 */
                                                darkenedColor
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "   " + stringResource(pokemon.pokemonEntity.type.value) + "   ",
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.Center),
                                        /*
                                        color = if (isDominantBlack(color) || isDominantDarkBrown(
                                                color
                                            )
                                        ) Color.LightGray
                                        else Color.Black,

                                         */
                                        color = darkenedColor
                                    )
                                }

                                // Secondo box per i pokemon che hanno un doppio tipo
                                //pokemon.secondType?.let {
                                Box(
                                    modifier = Modifier
                                        .weight(3.32f)
                                        .padding(start = 6.dp, top = 8.dp, end = 12.dp)
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                /*
                                                if (isDominantBlack(color) || isDominantDarkBrown(
                                                        color
                                                    )
                                                ) Color.LightGray
                                                else Color.Black

                                                 */
                                                darkenedColor
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "   " + stringResource(pokemon.pokemonEntity.secondType.value) + "   ",
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.Center),
                                        /*
                                        color = if (isDominantBlack(color) || isDominantDarkBrown(
                                                color
                                            )
                                        ) Color.LightGray
                                        else Color.Black,

                                         */
                                        color = darkenedColor
                                    )
                                }
                            } else {
                                // se il pomeon ha un solo tipo, il box del tipo deve prendere tutta la riga
                                Box(
                                    modifier = Modifier
                                        .weight(6.64f)
                                        .padding(start = 12.dp, top = 8.dp, end = 12.dp)
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                darkenedColor
                                            ),
                                            shape = RoundedCornerShape(8.dp),
                                        )
                                        .padding(start = 4.dp)
                                ) {
                                    Text(
                                        text = "   " + stringResource(pokemon.pokemonEntity.type.value) + "   ",
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.Center),
                                        /*
                                        color = if (isDominantBlack(color) || isDominantDarkBrown(
                                                color
                                            )
                                        ) Color.LightGray
                                        else Color.Black,

                                         */
                                        color = darkenedColor
                                    )
                                }
                            }
                        }
                    }

                    val imageModifier = Modifier
                        .fillMaxHeight()
                        .weight(0.36f)
                        .background(lighterDominantColor)
                        .clip(
                            RoundedCornerShape(
                                topStart = 0.dp,
                                topEnd = 15.dp,
                                bottomEnd = 15.dp,
                                bottomStart = 0.dp
                            )
                        )
                    if (pokemon.pokemonEntity.isFavourite) {
                        if (pokemon.pokemonEntity.image == null) {
                            Image(
                                painter = painterResource(id = R.drawable.download_error),
                                contentDescription = stringResource(
                                    id = R.string.error
                                )
                            )
                        } else {
                            Image(
                                bitmap = pokemon.pokemonEntity.image!!.asImageBitmap(),
                                contentDescription = pokemon.pokemonEntity.name,
                                modifier = imageModifier.size(83.dp)
                            )
                        }
                    } else
                        AsyncImage(
                            model = pokemon.pokemonEntity.imageUrl,
                            contentDescription = pokemon.pokemonEntity.name,
                            contentScale = ContentScale.Fit,
                            modifier = imageModifier
                        )
                } // chiusura row che rappresenta l'item
            } // chiusura parentesi items
        } // chiusura lazy column
    } // chiusura parentesi column che racchiude bottoni filtri + lazy column
} // chiusura fun

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

