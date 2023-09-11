package it.codeclub.pokeclub.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

object UIUtils {

    @Composable
    fun getLanguage(): String {
        val currentLocale = LocalConfiguration.current.locales[0]
        return currentLocale.language
    }

}