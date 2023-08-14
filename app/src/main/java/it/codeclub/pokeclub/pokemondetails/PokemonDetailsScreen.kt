package it.codeclub.pokeclub.pokemondetails

import android.annotation.SuppressLint
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.db.entities.Ability
import it.codeclub.pokeclub.ui.theme.*
import it.codeclub.pokeclub.utils.Resource
import java.util.Locale

@Composable
fun DetailsScreen(
    pokemonName: String,
    pokemonDetailsViewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    pokemonDetailsViewModel.getPokemonInfo(pokemonName)

    val pokemonInfo = remember {
        pokemonDetailsViewModel.pokemonDetails
    }

    when (pokemonInfo.value) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is Resource.Error -> {
            // TODO show error message
        }

        is Resource.Success -> {
            //val remember per ricordare il valore dei filtri
            var isFavourite by remember { mutableStateOf(pokemonInfo.value.data?.pokemon?.isFavourite!!) }
            //in base al valore inziale della var isFavourite la schermata viene settata con la stella vuota o piena
            val favouriteImage = if (isFavourite) R.drawable.fillstar else R.drawable.star
            var isCaptured by remember { mutableStateOf(pokemonInfo.value.data?.pokemon?.isCaptured!!) }
            //in base al valore inziale della var isCaptured la schermata viene settata con la pokeball vuota o piena
            val capturedImage =
                if (isCaptured) R.drawable.smallpokeball else R.drawable.smallpokeballempty

            pokemonInfo.value.data!!.let { pokemonDetails ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val type1Color = getColorForType(pokemonDetails.pokemon.type.name)
                    val lighterTypeColor = type1Color.copy(alpha = 0.5f)

                    val backgroundColorTop = if (pokemonDetails.pokemon.secondType != null) {
                        val type2Color = getColorForType(pokemonDetails.pokemon.secondType.name)
                        Brush.verticalGradient(listOf(type1Color, type2Color))
                    } else {
                        Brush.verticalGradient(listOf(lighterTypeColor, type1Color))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(brush = backgroundColorTop)
                            .padding(8.dp)
                    ) {
                        //primo box in alto contenente le informazioni principali del pokemon e il valore dei filtri
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            //prima riga con immagine pokemon, preferito e catturato
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopStart)
                            ) {
                                // TODO get real image
                                Image(
                                    painter = painterResource(R.drawable.pokemon),
                                    contentDescription = "pokemon image",
                                    modifier = Modifier
                                        .padding(start = 170.dp, top = 8.dp)
                                        .scale(1.4f)
                                )
                                Spacer(modifier = Modifier.width(100.dp))
                                Image(
                                    painter = painterResource(favouriteImage),
                                    contentDescription = stringResource(if (isFavourite) R.string.favourite else R.string.not_favourite),
                                    modifier = Modifier
                                        .size(28.dp)
                                        .padding(end = 3.dp)
                                        .clickable { isFavourite = !isFavourite }
                                )
                                Image(
                                    painter = painterResource(capturedImage),
                                    contentDescription = stringResource(if (isCaptured) R.string.captured else R.string.not_captured),
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
                                    text = pokemonName.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    },
                                    color = Color(0xff505050),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
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
                                        .background(getColorForType(pokemonDetails.pokemon.type.name))
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = pokemonDetails.pokemon.type.name,
                                        color = Color.White
                                    )
                                }
                                //terza riga con tipo del pokemon
                                if (pokemonDetails.pokemon.secondType != null) {
                                    pokemonDetails.pokemon.secondType.let { type ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(start = 4.dp)
                                                .height(32.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(getColorForType(pokemonDetails.pokemon.secondType.name))
                                                .padding(4.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = type.name,
                                                color = Color.White
                                            )
                                        }
                                    }
                                } else {
                                    // caso in cui il pokemon ha un solo tipo
                                    pokemonDetails.pokemon.type.let {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 0.dp, top = 0.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(end = 4.dp)
                                                    .height(32.dp)
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(getColorForType(pokemonDetails.pokemon.type.name))
                                                    .padding(4.dp),
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

                        //secondo box con l'anagrafica del pokemon, ovvero peso ed altezza
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    color = Color(0xffffffff),
                                    shape = RoundedCornerShape(8.dp)
                                )
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
                                    text = stringResource(R.string.height),
                                    color = Color(0xFF757575),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 65.dp, end = 8.dp)
                                        .align(Alignment.CenterVertically)
                                )
                                Text(
                                    text = stringResource(R.string.weight),
                                    color = Color(0xFF757575),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(
                                            start = 75.dp,
                                            end = 8.dp
                                        ) // Esempio di padding personalizzato
                                        .align(Alignment.CenterVertically)
                                )
                            }
                        }
                        //abilità del pokemon: testo + box
                        Text(
                            text = stringResource(R.string.ability),
                            fontSize = 16.sp,
                            color = Color(0xffffffff),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    color = Color(0xffffffff),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Column {
                                //generezione dinamiche delle abilità del pokemon
                                pokemonInfo.value.data!!.abilities.forEachIndexed { index, ability ->
                                    AbilityRow(ability = ability)
                                    if (index < pokemonInfo.value.data!!.abilities.size - 1) {
                                        Spacer(modifier = Modifier.height(8.dp))
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
                            stringResource(R.string.lp),
                            stringResource(R.string.attack),
                            stringResource(R.string.defense),
                            stringResource(R.string.sp_attack),
                            stringResource(R.string.sp_defense),
                            stringResource(R.string.speed)
                        )

                        val total = statsValues?.sum() ?: 0

                        //statistiche base del pokemon: testo + box con i valori delle statistiche
                        Text(
                            text = stringResource(R.string.stats),
                            fontSize = 16.sp,
                            color = Color(0xffffffff),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 4.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(
                                    color = Color(0xFFFFFFFF),
                                    shape = RoundedCornerShape(8.dp)
                                )
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
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 260.dp)
                                    .align(Alignment.Center)
                            ) {
                                Text(
                                    text = buildString {
                                        append(stringResource(R.string.total))
                                        append(" : ")
                                        append(total)
                                    },
                                    fontSize = 16.sp,
                                    color = Color(0xff505050),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp)
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

//funzione che mappa i colori in base al tipo
fun getColorForType(typeName: String): Color {
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
        else -> Color.Black
    }
}


//funzione che genera le righe delle abilità
@Composable
fun AbilityRow(ability: Ability) {
    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 0.dp, top = 0.dp)
    ) {
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
                    text = when (Locale.getDefault().language.lowercase()) {
                        "it" -> ability.name_it
                        else -> ability.name_en
                    },
                    color = Color.Black
                )
                //bottone info per la descrizione dell'abilità primaria
                Image(
                    painter = painterResource(R.drawable.info),
                    contentDescription = "info",
                    modifier = Modifier
                        .size(27.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            showDialog.value = true
                            dialogText.value =
                                when (Locale.getDefault().language.lowercase()) {
                                    "it" -> ability.effect_it
                                    else -> ability.effect_en
                                }
                        }
                )
            }
        }

        //check per il click della info sulle abilità con la conseguente apertura della finistra di descrizione
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = stringResource(R.string.description)) },
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
        val newWeightValue = weightValue * 3.1
        val w = 3.1 - (3.1 - newWeightValue)
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
            if (weightValue != 1f) {
                Box(
                    modifier = Modifier
                        .weight((3.1 - newWeightValue).toFloat())
                        .background(color = Color(0xffffffff))
                )
            }
        }
    }
}

//funzione che calcola la lunghezza del box in funzione al valore della statistica
fun calculateFloatList(statList: List<Int>): List<Float> {
    val maxValue = statList.maxOrNull() ?: return emptyList()
    val floatList = mutableListOf<Float>()

    for (i in statList.indices) {
        val value = statList[i]
        val f = (value.toFloat() / maxValue.toFloat())
        floatList.add(f)
    }

    return floatList
}