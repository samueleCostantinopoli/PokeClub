package it.codeclub.pokeclub.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import it.codeclub.pokeclub.R
import it.codeclub.pokeclub.utils.UIUtils.getLanguage

@Composable
fun SettingsScreen(
    navController: NavController
) {
    // Variabile remember per gestire la modalità scura
    val isDarkModeEnabled = remember { mutableStateOf(false) }
    // Variabile remember per gestire la scelta della lingua
    val language = remember { mutableStateOf("Italiano") }
    val currentLanguage = getLanguage()
    if (currentLanguage == "it" || currentLanguage == "en") {
        language.value = currentLanguage
    } else {
        language.value = "English"
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(Color.Black.copy(alpha = 0.1f))
        )
        // Box per la voce "Lingua"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.language),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = stringResource(id = R.string.app_language),
                    color = Color(0xFF545454),
                    fontSize = 14.sp
                )
            }
        }

        // Box per la voce "Modalità scura"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Modalità scura",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isDarkModeEnabled.value) "Tema scuro attivo" else "Tema chiaro attivo",
                        color = Color(0xFF545454),
                        fontSize = 14.sp
                    )
                }
                Switch(
                    checked = isDarkModeEnabled.value,
                    onCheckedChange = { isDarkModeEnabled.value = it },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Box per la voce "Aggiorna dati"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 0.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        navController.navigate("download_data_screen")
                    }
            ) {
                Text(
                    text = stringResource(id = R.string.update),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}