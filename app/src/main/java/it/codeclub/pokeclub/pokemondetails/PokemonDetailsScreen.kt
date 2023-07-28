package it.codeclub.pokeclub.pokemondetails

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.utils.Resource
import java.util.Locale

@Composable
fun SecondScreen(
    context: Context,
    pokemonName: String,
    pokemonDetailsViewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    val pokemonInfo =
        produceState<Resource<PokemonAndDetails>>(initialValue = Resource.Loading()) {
            value = pokemonDetailsViewModel.getPokemonInfo(pokemonName)
        }.value

    //val remember per ricordare il valore dei filtri e l'apparizione della finestra che mostra le abilità
    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    var isFavourite by remember { mutableStateOf(pokemonInfo.data?.pokemon?.isFavourite!!) }
    //in base al valore inziale della var isFavourite la schermata viene settata con la stella vuota o piena
    val favouriteImage = if (isFavourite) R.drawable.star else R.drawable.starempty
    var isCaptured by remember { mutableStateOf(pokemonInfo.data?.pokemon?.isCaptured!!) }
    //in base al valore inziale della var isCaptured la schermata viene settata con la pokeball vuota o piena
    val capturedImage = if (isCaptured) R.drawable.smallpokeball else R.drawable.smallpokeballempty

    pokemonInfo.data?.let { pokemonDetails ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color(
                        pokemonDetails.pokemon.dominantColor
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                //primo box in alto contenente le informazioni principali del pokemon e il valore dei filtri
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    //prima riga con immagine pokemon, preferito e catturato
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopStart)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.pokemon),
                            contentDescription = "pokemon image",
                            modifier = Modifier
                                .padding(start = 150.dp, top = 8.dp)
                                .scale(1.4f)
                        )
                        Spacer(modifier = Modifier.width(90.dp))
                        Image(
                            painter = painterResource(favouriteImage),
                            contentDescription = context.getString(if (isFavourite) R.string.favourite else R.string.not_favourite),
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 3.dp)
                                .clickable { isFavourite = !isFavourite }
                        )
                        Image(
                            painter = painterResource(capturedImage),
                            contentDescription = context.getString(if (isCaptured) R.string.captured else R.string.not_captured),
                            modifier = Modifier
                                .size(28.dp)
                                .padding(end = 1.dp)
                                .padding(top = 3.dp)
                                .clickable { isCaptured = !isCaptured }
                        )
                    }
                    //seconda riga con id e nome del pokemon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp, start = 110.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "#${
                                pokemonDetails.pokemon.pokemonId.toString()
                                    .padStart(3, '0')
                            }",
                            color = Color(0xff505050),
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Light
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = pokemonName,
                            color = Color(0xff505050),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                    //terza riga con tipo del pokemon
                    if (pokemonDetails.pokemon.secondType != null) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 0.dp, top = 120.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp)
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color = Color.White) // TODO put type color
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = pokemonDetails.pokemon.type.name,
                                    color = Color.White
                                )
                            }
                            pokemonDetails.pokemon.secondType.let { type ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                        .height(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color = Color.White) // TODO put type color
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = type.name,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    } else {
                        //val colorType1
                        //DA RIDIMENSIONARE LA RIGA IN MODO CHE SI ESPANDA PER TUTTO L BOX
                        pokemonDetails.pokemon.secondType.let {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 0.dp, top = 120.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 4.dp)
                                        .height(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(color = Color.White) //TODO
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    pokemonDetails.pokemon.type.let { it1 ->
                                        Text(
                                            text = it1.name,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        //secondo box con l'anagrafica del pokemon, ovvero peso ed altezza
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color = Color(0xffffffff), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 0.dp, top = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = Color(0xffffffff))
                        .border(
                            width = 1.dp,
                            color = Color(0xff757575),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pokemonDetails.details.height.toString(),
                        color = Color.Black
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = Color(0xffffffff))
                        .border(
                            width = 1.dp,
                            color = Color(0xff757575),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = pokemonDetails.details.weight.toString(),
                        color = Color.Black
                    )
                }
            }
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 0.dp, top = 40.dp)
            ) {
                Text(
                    text = context.getString(R.string.height),
                    color = Color(0xFF757575),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 65.dp, end = 8.dp)
                        .align(Alignment.CenterVertically)
                )
                Text(
                    text = context.getString(R.string.weight),
                    color = Color(0xFF757575),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 75.dp, end = 8.dp) // Esempio di padding personalizzato
                        .align(Alignment.CenterVertically)
                )
            }
        }
        //abilità del pokemon: testo + box
        Text(
            text = "Abilità",
            fontSize = 16.sp,
            color = Color(0xff505050),
            modifier = Modifier
                //.align(Alignment.CenterHorizontally) // TODO fix this
                .padding(top = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color = Color(0xffffffff), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            //abilità primaria
            Row(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 0.dp, top = 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 0.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(color = Color(0xffffffff))
                        .border(
                            width = 1.dp,
                            color = Color(0xff757575),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (Locale.getDefault().displayName) {
                            "it" -> pokemonDetails.abilities[0].name_it
                            else -> pokemonDetails.abilities[0].name_en
                        },
                        color = Color.Black
                    )
                    //bottone info per la descrizione dell'abilità primaria
                    Image(
                        painter = painterResource(R.drawable.info),
                        contentDescription = "info",
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.TopEnd)
                            .clickable {
                                showDialog.value = true
                                dialogText.value = when (Locale.getDefault().displayName) {
                                    "it" -> pokemonDetails.abilities[0].effect_it
                                    else -> pokemonDetails.abilities[0].effect_en
                                }
                            }
                    )

                }
            }
            pokemonInfo.data.abilities[1].let { ability ->
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 0.dp, top = 40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 0.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = Color(0xffffffff))
                            .border(
                                width = 1.dp,
                                color = Color(0xff757575),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (Locale.getDefault().displayName) {
                                "it" -> ability.name_it
                                else -> ability.name_en
                            },
                            color = Color.Black
                        )
                        //bottone info con la descrizione dell'abilità speciale
                        Image(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "info",
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    showDialog.value = true
                                    dialogText.value = when (Locale.getDefault().displayName) {
                                        "it" -> ability.effect_it
                                        else -> ability.effect_en
                                    }
                                }
                        )
                    }
                }
            }
            pokemonInfo.data.abilities[2].let { ability ->
                val topPadding = 80
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 0.dp, top = topPadding.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 0.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color = Color(0xffffffff))
                            .border(
                                width = 1.dp,
                                color = Color(0xff757575),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (Locale.getDefault().displayName) {
                                "it" -> ability.name_it
                                else -> ability.name_en
                            },
                            color = Color.Black
                        )
                        //bottone info con la descrizione dell'abilità speciale
                        Image(
                            painter = painterResource(R.drawable.info),
                            contentDescription = "info",
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    showDialog.value = true
                                    dialogText.value = when (Locale.getDefault().displayName) {
                                        "it" -> ability.effect_it
                                        else -> ability.effect_en
                                    }
                                }
                        )
                    }
                }
            }
        }

        val statsValues = listOf(
            pokemonDetails.details.lp,
            pokemonDetails.details.attack,
            pokemonDetails.details.defense,
            pokemonDetails.details.spAttack,
            pokemonDetails.details.spDefense,
            pokemonDetails.details.speed
        )

        val weightValues = calculateFloatList(statsValues)

        val colorStatsList = listOf(
            Color(0xFFE1FFD3),
            Color(0xFFFFFBE6),
            Color(0xFFFFC499),
            Color(0xFFB2E3EF),
            Color(0xFF9EB5FF),
            Color(0xFFEF8DEC)
        )

        val statsName = listOf(
            context.getString(R.string.lp),
            context.getString(R.string.attack),
            context.getString(R.string.defense),
            context.getString(R.string.sp_attack),
            context.getString(R.string.sp_defense),
            context.getString(R.string.speed)
        )

        //statistiche base del pokemon: testo + box con i valori delle statistiche
        Text(
            text = "Statistiche base",
            fontSize = 16.sp,
            color = Color(0xff505050),
            modifier = Modifier
                //.align(Alignment.CenterHorizontally) // TODO fix this
                .padding(top = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column {
                //indicizzo statsName per nome della stat, valore, lo spazio che occupa nella riga e il colore
                //questo per ogni statistica di base
                statsName.forEachIndexed { index, name ->
                    val value = statsValues[index].toFloat()
                    val weight = weightValues[index]
                    val color = colorStatsList.getOrElse(index) { Color.Gray }
                    //invoco la funzione che genera la riga della statistica con le caratteristiche in questione
                    createStatRow(name, value.toInt().toString(), weight, color)()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        //check per il click della info sulle abilità con la conseguente apertura della finistra di descrizione
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = "Descrizione abilità") },
                text = { Text(text = dialogText.value) },
                confirmButton = {
                    Button(
                        onClick = { showDialog.value = false },
                        content = { Text("OK") }
                    )
                }
            )
        }

    }
}

//funzione per la creazione della riga della statistica
@SuppressLint("SuspiciousIndentation")
fun createStatRow(
    statName: String,
    statValue: String,
    weightValue: Float,
    color: Color
): @Composable () -> Unit {
    return {
        val newWeightValue = if (weightValue == 1f) 0f else weightValue
        val w = 3.1 - newWeightValue
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(0.9f)
                    .padding(end = 0.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                    .background(Color.White)
                    .border(
                        1.dp,
                        Color.Gray,
                        RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = statName,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .weight(w.toFloat())
                    .padding(start = 0.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 0.dp,
                            bottomStart = 0.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp
                        )
                    )
                    .background(color = color)
                    .padding(end = 0.dp)
                    .padding(8.dp)
            ) {
                Text(
                    text = statValue,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            if (newWeightValue != 0f)
                Box(
                    modifier = Modifier
                        .weight(weightValue)
                        .background(color = Color(0xffffffff))
                )
        }
    }
}

//funzione che calcola la lunghezza del box in funzione al valore della statistica
fun calculateFloatList(statList: List<Int>): List<Float> {
    val maxValue = statList.maxOrNull() ?: return emptyList()
    val floatList = mutableListOf<Float>()

    for (i in statList.indices) {
        val value = statList[i]
        val v = 3.1f - (maxValue.toFloat() / value.toFloat())
        val f = 3.1f - v
        floatList.add(f)
    }

    return floatList
}