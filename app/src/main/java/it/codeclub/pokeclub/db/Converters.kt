package it.codeclub.pokeclub.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import it.codeclub.pokeclub.db.entities.PokemonType
import java.io.ByteArrayOutputStream

@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun getPokemonTypeFromInt(value: Int): PokemonType {
        return value.let { PokemonType.fromInt(it) }
    }

    @TypeConverter
    fun pokemonTypeToString(pokemonType: PokemonType): Int {
        return pokemonType.value
    }

    @TypeConverter
    fun getStringFromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun getBitmapFromString(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}