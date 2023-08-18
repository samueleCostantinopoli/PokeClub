package it.codeclub.pokeclub.newMainView

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.ui.theme.AppGrey
import timber.log.Timber

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FirstRow(
    favourite: MutableState<Int>,
    smallPokeballClick: MutableState<Int>,
    isSearchExpanded: MutableState<Boolean>,
    focusManager: FocusManager,
    keyboardController: SoftwareKeyboardController?,
    saveSearch: MutableState<String>,
    searchText: MutableState<String>,
    isAbilityClicked: MutableState<Boolean>,
    searchAbility: MutableState<String>,
    saveAbility: MutableState<String>
) {
    Row(
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .height(Dimensions.grandezzaPrimaRiga)
            .background(AppGrey)
    ) {
        // la comparsa e scomparsa della barra di ricerca la gestisco con un box (contenuta nella row)
        // (solo quella parte, il resto degli elementi , stella pokeball impostazioni)
        // andranno nella row e non nella box

        Box(
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
                .background(AppGrey),
        ) {
            // comparsa nome app ( quindi scomparsa barra di ricerca)
            this@Row.AnimatedVisibility(
                visible = !isSearchExpanded.value && !isAbilityClicked.value,
                modifier = Modifier.background(AppGrey)
            ) {
                //qui metto il logo
                Image(
                    painter = painterResource(id = R.drawable.pokeclub),
                    contentDescription = "Logo principale in alto a sinistra",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(start = 10.dp, top = 20.dp)
                )
            }
            //comparsa barra di ricerca ( quindi scomparsa nome)
            this@Row.AnimatedVisibility(
                visible = isSearchExpanded.value, modifier = Modifier.background(
                    AppGrey
                )
            ) {
                TextField(
                    value = searchText.value,
                    onValueChange = { searchText.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            //tutto questo codice viene eseguito dopo aver cliccato invio
                            // questa parte di codice permette di
                            //far sparire la tastiera e di rendere invisibile la barra di ricerca
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            //codice che permette di far sparire la barra di ricerca quando clicco invio
                            //isSearchExpanded.value = !isSearchExpanded.value
                            //salvo la ricerca di dell'utente nella variabile che verrà usata per la query
                            saveSearch.value = searchText.value
                            //verifica che ilk testo sia effettivamente preso
                            Timber.tag("MyTag").d(saveSearch.value)
                        }
                    ),
                    //questo permette di continuare a scorrere orizzontalmente mentre si scrive sul
                    //text field e si va oltre lo spazio necessario(in pratica il testo scorre in
                    // modo orizzontale mentre si scrive oltre la dimensione del text field)
                    singleLine = true,
                    maxLines = 1,
                    //icona invio fa scomparire la barra di ricerca
                    trailingIcon = {
                        // Aggiungiamo un'icona per inviare la ricerca quando cliccata
                        IconButton(
                            onClick = {
                                // Tutto questo codice viene eseguito dopo aver cliccato sull'icona
                                keyboardController?.hide()
                                isSearchExpanded.value = false

                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.send),
                                contentDescription = "Send",
                                modifier=Modifier.size(26.dp),
                                tint = Color.Black
                            )
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 6.dp, end = 0.dp)
                        .background(Color.White, shape = CircleShape),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(fontSize = 16.sp),
                    placeholder = { Text("ex: Charizard", fontSize = 16.sp) }
                )
            }

            //comparsa barra di ricerca per abilità ( quindi scomparsa nome)

            this@Row.AnimatedVisibility(
                visible = isAbilityClicked.value, modifier = Modifier.background(
                    AppGrey
                )
            ) {
                TextField(
                    value = searchAbility.value,
                    onValueChange = { searchAbility.value = it },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            //tutto questo codice viene eseguito dopo aver cliccato invio
                            // questa parte di codice permette di
                            //far sparire la tastiera e di rendere invisibile la barra di ricerca
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            //codice che permette di far sparire la barra di ricerca quando clicco invio
                            //isSearchExpanded.value = !isSearchExpanded.value
                            //salvo la ricerca di dell'utente nella variabile che verrà usata per la query
                            saveAbility.value = searchAbility.value
                            //verifica che ilk testo sia effettivamente preso
                            Timber.tag("MyTag").d(saveAbility.value)
                        }
                    ),
                    //questo permette di continuare a scorrere orizzontalmente mentre si scrive sul
                    //text field e si va oltre lo spazio necessario(in pratica il testo scorre in
                    // modo orizzontale mentre si scrive oltre la dimensione del text field)
                    singleLine = true,
                    maxLines = 1,
                    //icona invio fa scomparire la barra di ricerca
                    trailingIcon = {
                        // Aggiungiamo un'icona per inviare la ricerca quando cliccata
                        IconButton(
                            onClick = {
                                // Tutto questo codice viene eseguito dopo aver cliccato sull'icona
                                keyboardController?.hide()
                                isAbilityClicked.value = false

                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.send),
                                contentDescription = "Send",
                                modifier=Modifier.size(26.dp),
                                tint = Color.Black
                            )
                        }
                    },
                    shape = CircleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 6.dp, end = 0.dp)
                        .background(Color.White, shape = CircleShape),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    textStyle = TextStyle(fontSize = 20.sp),
                    placeholder = { Text("ex: Erbaiuto", fontSize = 16.sp) }
                )
            }
        }

        // icon stella
        IconButton(
            onClick = {
                //setta solo i favoriti o meno
                // di default e' settato a 0 ( nessun favorito) se viene cliccato una
                //volta viene settato a 1 e cosi' via
                favourite.value = 1 - favourite.value
            },
            modifier = Modifier.padding(top = 17.dp, bottom = 0.dp, start = 15.dp,end=6.dp)
        ) {
            Icon(
                painter =
                if(favourite.value==1){
                    painterResource(id = R.drawable.fillstar)
                }else{
                    painterResource(id = R.drawable.star)
                     },
                contentDescription = "star",
                //tint = if (favourite.value == 1) Color.Red else Color.Black,
                modifier = Modifier.size(40.dp)
            )
        }
        //icon pokeball in front of star
        IconButton(
            onClick = {
                smallPokeballClick.value = 1 - smallPokeballClick.value
            },
            modifier = Modifier.padding(top = 17.dp, bottom = 0.dp, end = 6.dp)
        ) {
            Icon(
                painter =
                    if(smallPokeballClick.value==1){
                        painterResource(id = R.drawable.smallpokeball)
                    }else{
                        painterResource(id = R.drawable.smallpokeballempty)
                    },
                contentDescription = "small pokeball",
                modifier = Modifier.size(40.dp)
            )
        }
        //icon settings
        IconButton(
            onClick = {
                //open settings
            },
            modifier = Modifier.padding(top = 17.dp, bottom = 0.dp, end = 6.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.settings),
                contentDescription = "settings",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}