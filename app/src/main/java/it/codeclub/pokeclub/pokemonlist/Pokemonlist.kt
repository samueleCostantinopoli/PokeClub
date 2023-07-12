package it.codeclub.pokeclub.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import it.codeclub.pokeclub.R

@Preview
@Composable
fun call(){
    PokedexScreen()
}

@Preview
@Composable
fun PreviewChangeableImage() {
    MaterialTheme {
        PokedexScreen()
    }
}

@Composable
fun PokedexScreen() {

    val boxVersion = remember { mutableStateOf(false) }
    val boxTipe = remember { mutableStateOf(false) }
    val boxAbility = remember { mutableStateOf(false) }

    // Box che racchiude tutta la schermata
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFBFEFEFF)) // Colore sfondo grigio chiaro
            .fillMaxWidth()
    ) {
        // Box bianco in alto con il titolo "Pokedex" e i bottoni
        Box(
            modifier = Modifier
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
                }
        ) {
            Text(
                text = "Pokédex",
                fontSize = 30.sp,
                modifier = Modifier.padding(16.dp),
                fontWeight = Bold
            )
            // Inserimento dell'immagine "stella", che è anche cliccabile per accedere ai pokemon salvati come preferiti
            Image(
                painter = painterResource(R.drawable.star),
                contentDescription = "Stella dei preferiti",
                modifier = Modifier
                    //TODO .clickable(onClick = /* Azione della stella dei preferiti */)
                    .padding(start = 250.dp, top = 15.dp)
                    .size(height = 45.dp, width = 35.dp),
            )
            // Immagine della pokeball cliccabile, che serve a visualizzare la squadra salvata
            Image(
                painter = painterResource(R.drawable.smallpokeball),
                contentDescription = "Stella dei preferiti",
                modifier = Modifier
                    //TODO .clickable(onClick = /* Azione della stella dei preferiti */)
                    .padding(start = 297.dp, top = 15.dp)
                    .size(height = 45.dp, width = 35.dp),
            )
            // Immagine impostazioni
            Image(
                painter = painterResource(R.drawable.settings),
                contentDescription = "Stella dei preferiti",
                modifier = Modifier
                    //TODO .clickable(onClick = /* Azione della stella dei preferiti */)
                    .padding(start = 350.dp, top = 27.dp)
                    .scale(1.65f)
            )
            // Linea utilizzata per inserire i filtri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 0.dp, top = 65.dp, bottom = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Primo bottono per il filtro "VERSIONE"
                Button(
                    onClick = { boxVersion.value =  !boxVersion.value },
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
                    Text(text = "VERSIONE")
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
                    onClick = { boxTipe.value =  !boxTipe.value },
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
                    Text(text = "TIPO")
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
                    onClick = { boxAbility.value =  !boxAbility.value },
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
                    Text(text = "ABILITA'")
                }
            }
        }

        if(boxVersion.value){
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 535.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .border(
                        BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .background(Color.White),
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(8) { index ->
                        // Box che contiene le versioni dei pokemon
                        Box(
                            modifier = Modifier
                                .padding(top = 14.dp, start = 8.dp, end = 8.dp)
                                .border(
                                    BorderStroke(2.dp, Color.Gray),
                                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                                )
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 22.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(top = 6.dp , bottom = 6.dp),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        if(boxTipe.value){
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 535.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .border(
                        BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                    )
                    .background(Color.White),
            )

            {
                Text(
                    text = "Seleziona una versione",
                    fontSize = 24.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 6.dp , bottom = 6.dp),
                    color = Color.DarkGray
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(8) { index ->
                        // Box che contiene le versioni dei pokemon
                        Box(
                            modifier = Modifier
                                .padding(top = 34.dp, start = 8.dp, end = 8.dp)
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
                                    .padding(top = 6.dp , bottom = 6.dp),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        if(boxAbility.value){
            Box(
                modifier = Modifier
                    .zIndex(1f)
                    .padding(top = 535.dp, start = 16.dp, end = 16.dp)
                    .fillMaxSize()
                    .border(
                        BorderStroke(2.dp, Color.Gray),
                        shape = RoundedCornerShape(8.dp),
                    )
                    .background(Color.White),
            )
            {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(8) { index ->
                        // Box che contiene le versioni dei pokemon
                        Box(
                            modifier = Modifier
                                .padding(top = 14.dp, start = 8.dp, end = 8.dp)
                                .border(
                                    BorderStroke(2.dp, Color.Gray),
                                    shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
                                )
                                .fillMaxWidth(),
                        ) {
                            Text(
                                text = "${index + 1}",
                                fontSize = 22.sp,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(top = 6.dp , bottom = 6.dp),
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        // Lazy Column con i rettangoli
        LazyColumn(
            modifier = Modifier
                .padding(top = 135.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize()
        ) {
            items(10) { index ->
                var starImage by remember { mutableStateOf(true) }
                var pokeImage by remember { mutableStateOf(true) }
                // Box principale per gli elementi della Lazy Column
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.Green),
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
                                text = "#00${index + 1}",
                                fontSize = 19.sp,
                                fontStyle = Italic,
                                color = Color.Gray,
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                            )
                            // Box creato per avere una dimensione fissa del testo "nome pokemon"
                            Box(
                                modifier = Modifier.size(width = 137.dp, height = 28.dp)
                            ){
                                Text(
                                    text = "Nome pokemon",
                                    fontSize = 18.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 1.dp)
                                )
                            }
                            // Immagine della stella per indicare se il pokemon è tra i preferiti
                            Box(
                                modifier = Modifier
                                    .clickable { starImage = !starImage },
                                contentAlignment = Alignment.Center
                            ) {
                                val imageRes = if (starImage) R.drawable.starempty else R.drawable.star
                                Image(
                                    painter = painterResource(imageRes),
                                    contentDescription = "Changeable Image",
                                    modifier = Modifier
                                        .size(height = 30.dp, width = 30.dp),
                                )
                            }

                            // Immagine della pokeball per indicare se il pokemon è nella mia squadra
                            Box(
                                modifier = Modifier
                                    .clickable { pokeImage = !pokeImage },
                                contentAlignment = Alignment.Center
                            ) {
                                val imageRes = if (pokeImage) R.drawable.smallpokeballempty else R.drawable.smallpokeball
                                Image(
                                    painter = painterResource(imageRes),
                                    contentDescription = "Changeable Image",
                                    modifier = Modifier
                                        .size(height = 30.dp, width = 30.dp)
                                )
                            }

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
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
                                    text = "tipo",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 43.dp,end = 43.dp),
                                    color = Color.DarkGray
                                )
                            }
                            // Secondo box per i pokemon che hanno un doppio tipo
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
                                    text = "tipo",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(start = 43.dp,end = 43.dp),
                                    color = Color.DarkGray
                                )
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
                        // Foto del pokemon
                        Image(
                            painter = painterResource(R.drawable.pokemon),
                            contentDescription = "Immagine del pokemon",
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)

                        )
                    }

                }
            }
        }
        // Menu in basso a destra, raffigurato da una pokeball
        Image(
            painter = painterResource(id = R.drawable.menupokeball),
            contentDescription = "menu a tendina a forma di pokeball",
            modifier = Modifier
                //TODO .clickable(onClick = /* Azione per il menù */)
                .align(Alignment.BottomEnd)
                .padding(end = 32.dp, bottom = 30.dp)
                .scale(2f),
        )
    }
}
