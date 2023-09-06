package it.codeclub.pokeclub.pokemondetails

import android.graphics.BitmapFactory
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.entities.PokemonAndDetails
import it.codeclub.pokeclub.db.entities.PokemonEntity
import it.codeclub.pokeclub.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    var pokemonDetails = mutableStateOf<Resource<PokemonAndDetails>>(Resource.Loading())

    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            pokemonRepository.getPokemonDetails(pokemonName).collect {
                pokemonDetails.value = Resource.Success(it)
            }
        }
    }

    fun toggleFavourite() {
        pokemonDetails.value.data!!.pokemon.pokemonEntity.isFavourite =
            !pokemonDetails.value.data!!.pokemon.pokemonEntity.isFavourite
        downloadPokemonImage(pokemonDetails.value.data!!.pokemon.pokemonEntity)
    }

    fun toggleCaptured() {
        pokemonDetails.value.data!!.pokemon.pokemonEntity.isCaptured =
            !pokemonDetails.value.data!!.pokemon.pokemonEntity.isCaptured
        update(pokemonDetails.value.data!!.pokemon.pokemonEntity)
    }

    private fun update(pokemon: PokemonEntity) {
        viewModelScope.launch {
            pokemonRepository.update(pokemon)
        }
    }

    private fun downloadPokemonImage(pokemon: PokemonEntity) {
        val request = Request.Builder()
            .url(pokemon.imageUrl)
            .build()

        val okHttpClient = OkHttpClient.Builder().build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                pokemon.image = null
                update(pokemon)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful)
                    response.body?.let { responseBody ->
                        val imageByteArray = responseBody.byteStream().readBytes()
                        val bmp = BitmapFactory.decodeByteArray(
                            imageByteArray,
                            0,
                            imageByteArray.size
                        )
                        pokemon.image = bmp
                        update(pokemon)
                    }
            }
        })
    }
}
