package it.codeclub.pokeclub.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import it.codeclub.pokeclub.db.entities.PokemonType
import timber.log.Timber
import java.io.ByteArrayOutputStream

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun getPokemonTypeFromInt(value: Int?): PokemonType? {
        return if (value == null)
            null
        else PokemonType.fromInt(value)
    }

    @TypeConverter
    fun pokemonTypeToInt(pokemonType: PokemonType?): Int? {
        return pokemonType?.value
    }

    @TypeConverter
    fun getStringFromBitmap(bitmap: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun getBitmapFromString(byteArray: ByteArray?): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray?.size!!)
    }
}