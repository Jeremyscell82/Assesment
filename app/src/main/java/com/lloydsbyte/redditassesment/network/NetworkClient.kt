package com.lloydsbyte.redditassesment.network

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import java.util.concurrent.TimeUnit

interface NetworkClient {
    companion object {

        private fun buildClient(): OkHttpClient.Builder {
            //Add any time out limits here & retry
            return OkHttpClient.Builder().apply {
                connectTimeout(120, TimeUnit.SECONDS)
                readTimeout(120, TimeUnit.SECONDS)
                retryOnConnectionFailure(true)
            }
        }

        fun create(context: Context, baseUrl: String): NetworkClient {
            val gson = GsonBuilder()
                .setLenient() //Just in case, because I do not control the server
                .create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(buildClient().build())
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(NetworkClient::class.java)
        }

    }


    //Getters for network calls
    @Headers("Accept: application/json")
    @GET(NetworkConstants.characterEndpoint)
    suspend fun getCharacters(): Response<CharacterResponseModel.CharacterResponse>

}