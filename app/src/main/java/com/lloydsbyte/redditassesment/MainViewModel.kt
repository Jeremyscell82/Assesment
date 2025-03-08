package com.lloydsbyte.redditassesment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lloydsbyte.redditassesment.database.AppDatabase
import com.lloydsbyte.redditassesment.network.CharacterResponseModel
import com.lloydsbyte.redditassesment.network.NetworkClient
import com.lloydsbyte.redditassesment.network.NetworkConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import timber.log.Timber
import java.net.ConnectException

class MainViewModel(val database: AppDatabase): ViewModel() {

    var retryCount: Int = 0 // This is a simple retry method, that only tries once... more logic can be added
    var characterSelected: CharacterResponseModel.CharacterModel? = null

    //For this example I will just pass in the database, running out of time, normally I would create  repo to handle some of the logic
    fun getCharactersFlow(): Flow<List<CharacterResponseModel.CharacterModel>> = database.characterDao().getAllCharacters().distinctUntilChanged()

    fun initApp(context: Context) {
        val database = AppDatabase.getDatabase(context.applicationContext)

        viewModelScope.launch(Dispatchers.IO) {
            val count = database.characterDao().countItems()
            if (count == 0){
                try {
                    val networkClient = NetworkClient.create(context, NetworkConstants.baseUrl)
                    val response = networkClient.getCharacters()

                    if (response.isSuccessful && response.body() != null) {
                        //Now save to the database
                        database.characterDao().addCharacters(response.body()!!.results)
                    }
                } catch (e: ConnectException){
                    //Log Error
                    Timber.d("JL_ Something has gone wrong: ${e.message}")
                    if (retryCount == 0){
                        retryCount = 1
                        initApp(context)
                    }
                }

            } else {
                //Data is loaded, nothing to do
            }
        }
    }

}