package it.codeclub.pokeclub.utils

import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.graphics.ColorUtils

object UIUtils {

    private const val LIGHTNESS_FACTOR = 0.9f

    /**
     * Calculates the contrast of a text color (in its argb value) and
     * checks if it can be displayed on a given background.
     *
     * @param background the background color argb value
     *
     * @see <a href=https://support.google.com/accessibility/android/answer/7158390?hl=en>Accessibility guidelines</a>
     */
    private fun Int.canDisplayTextOnBackground(@ColorInt background: Int): Boolean =
        ColorUtils.calculateContrast(this, background) >= 4.5f

    /**
     * Adjusts a color lightness by using HSL
     * (Hue - Saturation - Lightness) values
     *
     * @param lightnessFactor the factor used to adjust lightness
     */
    private fun Int.adjustLightness(lightnessFactor: Float): Int {
        val hsl = FloatArray(3)
        ColorUtils.colorToHSL(this, hsl)
        hsl[2] = hsl[2] * lightnessFactor
        return ColorUtils.HSLToColor(hsl)
    }

    /**
     * Adjusts a color by repeatedly lowering its lightness,
     * until the foreground/background contrast is at least of 4.5
     *
     * @param backgroundColor the background color argb value
     * @param foregroundColor the foreground color argb value
     */
    @ColorInt
    fun adjustForBackground(
        @ColorInt backgroundColor: Int,
        foregroundColor: Int = Color.White.toArgb()
    ): Int {
        var color = foregroundColor
        while (!color.canDisplayTextOnBackground(backgroundColor)) {
            color = color.adjustLightness(
                LIGHTNESS_FACTOR
            )
        }
        return color
    }

    @Composable
    fun getLanguage(): String {
        val currentLocale = LocalConfiguration.current.locales[0]
        return currentLocale.language
    }

}