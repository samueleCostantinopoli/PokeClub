package it.codeclub.pokeclub.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import it.codeclub.pokeclub.db.Converters
import it.codeclub.pokeclub.db.PokemonDao
import it.codeclub.pokeclub.db.PokemonDatabase
import it.codeclub.pokeclub.db.PokemonRepository
import it.codeclub.pokeclub.db.PokemonRepositoryDbImpl
import it.codeclub.pokeclub.local.SharedPrefsRepository
import it.codeclub.pokeclub.remote.PokeAPI
import it.codeclub.pokeclub.utils.Constants.BASE_URL
import it.codeclub.pokeclub.utils.Constants.DB_NAME
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokeApi(): PokeAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeAPI::class.java)
    }

    @Provides
    fun providePokemonDao(pokemonDatabase: PokemonDatabase): PokemonDao {
        return pokemonDatabase.pokemonDao()
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext appContext: Context): PokemonDatabase {
        return Room.databaseBuilder(
            appContext,
            PokemonDatabase::class.java,
            DB_NAME
        )
            .addTypeConverter(Converters())
            .build()
    }

    @Provides
    fun providePokemonRepository(
        pokemonDatabase: PokemonDatabase
    ): PokemonRepository {
        return PokemonRepositoryDbImpl(pokemonDatabase.pokemonDao())
    }

    @Provides
    fun provideSharedPrefsRepository(
        @ApplicationContext context: Context
    ): SharedPrefsRepository =
        SharedPrefsRepository(context)
}