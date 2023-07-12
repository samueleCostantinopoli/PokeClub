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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.codeclub.pokeclub.R


val weightValues = calculateFloatList(statsValue)
val colorStatsList = listOf(
    Color(0xFFE1FFD3),
    Color(0xFFFFFBE6),
    Color(0xFFFFC499),
    Color(0xFFB2E3EF),
    Color(0xFF9EB5FF),
    Color(0xFFEF8DEC)
)
val statsName = listOf("Ps", "Att", "Dif", "AttSp", "DifSp", "Vel")

@Composable
fun SecondScreen() {
    //val remember per ricordare il valore dei filtri e l'apparizione della finestra che mostra le abilità
    val showDialog = remember { mutableStateOf(false) }
    val dialogText = remember { mutableStateOf("") }
    var isFavourite by remember { mutableStateOf(isFavouriteOrNot) }
    //in base al valore inziale della var isFavourite la schermata viene settata con la stella vuota o piena
    val favouriteImage = if (isFavourite) R.drawable.star else R.drawable.starempty
    var isCaptured by remember { mutableStateOf(isCapturedOrNot) }
    //in base al valore inziale della var isCaptured la schermata viene settata con la pokeball vuota o piena
    val capturedImage = if (isCaptured) R.drawable.smallpokeball else R.drawable.smallpokeballempty

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = dominantColor)
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
                ){
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
                        contentDescription = "favourite",
                        modifier = Modifier
                            .size(28.dp)
                            .padding(end = 3.dp)
                            .clickable { isFavourite = !isFavourite }
                    )
                    Image(
                        painter = painterResource(capturedImage),
                        contentDescription = "captured",
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
                        text = pokemonId,
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
                if (textType2 != null) {
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
                                .background(color = colorType1)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = textType1,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 4.dp)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(color = colorType2)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = textType2 ?:"",
                                color = Color.White
                            )
                        }
                    }
                }else {
                    //DA RIDIMENSIONARE LA RIGA IN MODO CHE SI ESPANDA PER TUTTO L BOX
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
                                .background(color = colorType1)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = textType1,
                                color = Color.White
                            )
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
                            text = height,
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
                            text = wheigth,
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
                        text = "Altezza",
                        color = Color(0xFF757575),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 65.dp, end = 8.dp)
                            .align(Alignment.CenterVertically)
                    )
                    Text(
                        text = "Peso",
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
                    .align(Alignment.CenterHorizontally)
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
                            text = primaryAbility,
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
                                    dialogText.value = firstAbilityDescription
                                }
                        )

                    }
                }
                if(secondAbility != null){
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
                                text = secondAbility ?:"",
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
                                        dialogText.value = secondAbilityDescription ?:""
                                    }
                            )
                        }
                    }
                }
                if (specialAbility1 != null){
                    val topPadding = if (secondAbility != null) 80 else 40
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
                                text = specialAbility1 ?:"",
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
                                        dialogText.value = specialAbility1Description ?:""
                                    }
                            )
                        }
                    }
                }
            }
            //statistiche base del pokemon: testo + box con i valori delle statistiche
            Text(
                text = "Statistiche base",
                fontSize = 16.sp,
                color = Color(0xff505050),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
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
                        val value = statsValue[index].toFloat()
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
}

//funzione per la creazione della riga della statistica
@SuppressLint("SuspiciousIndentation")
fun createStatRow(statName: String, statValue: String, weightValue: Float, color: Color): @Composable () -> Unit {
    return {
        val newWeightValue = if(weightValue == 1f) 0f else weightValue
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
fun calculateFloatList(statList: List<String>): List<Float> {
    val intList = statList.map { it.toInt() }
    val maxValue = intList.maxOrNull() ?: return emptyList()
    val floatList = mutableListOf<Float>()

    for (i in intList.indices) {
        val value = intList[i]
        val v = 3.1f - (maxValue.toFloat() / value.toFloat())
        val f = 3.1f - v
        floatList.add(f)
    }

    return floatList
}