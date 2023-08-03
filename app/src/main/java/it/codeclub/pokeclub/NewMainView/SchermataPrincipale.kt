package it.codeclub.pokeclub.NewMainView

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.pokemonlist.PokemonListViewModel
import it.codeclub.pokeclub.ui.theme.AppGrey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
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
    var searchText = remember { mutableStateOf("") }
    var saveSearch = remember { mutableStateOf("") }

    //variabile utilizzata per capire se l'utente ha cliccato su ability
    val isAbilityClicked = remember { mutableStateOf(false) }
    var searchAbility = remember { mutableStateOf("") }
    var saveAbility = remember { mutableStateOf("") }
    //variabile utilizzata per capire se l'utente ha cliccato su cerca
    val isSearchExpanded = remember { mutableStateOf(false) }
    var favourite = remember {
        mutableStateOf(0)
    };
    var smallPokeballClick = remember {
        mutableStateOf(0)
    };
    var boxVersion = remember { mutableStateOf(false) }
    var boxType = remember { mutableStateOf(false) }
    val boxAbility = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints(
            Modifier
                .background(AppGrey)
                .fillMaxWidth()
        ) {
            val expandedWidth = maxWidth - 16.dp * 2
            //prima row che contiene nome dell'app stella impostazioni e pokeball cattura
            firstRow(favourite, smallPokeballClick, isSearchExpanded,focusManager,keyboardController,saveSearch,searchText,isAbilityClicked
            ,searchAbility,saveAbility)

            //second row ( button version type e ability) piu lazy column che mostra i pokemon
            secondRow(boxVersion,boxType,boxAbility,pokemonListViewModel,isAbilityClicked,isSearchExpanded)

            //pokeball finale in basso a sinistra
            //button pokeball che ruota
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
                    .align(Alignment.BottomEnd)
                    .background(Color.Unspecified)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.menupokeball),
                    contentDescription = "FabButton",
                    modifier = Modifier.fillMaxSize(), // Modificatore per riempire l'intera area del pulsante
                    contentScale = ContentScale.FillBounds // Imposta la scala dell'immagine per adattarsi all'area del pulsante
                )
            }

            if (rotate) {
                Column(
                    modifier = Modifier
                        .padding(35.dp)
                        .offset(y = 599.dp)
                        .offset(x = 239.dp)

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
                            if(isAbilityClicked.value){
                                isAbilityClicked.value=!isAbilityClicked.value
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
            }
        }
    }
}