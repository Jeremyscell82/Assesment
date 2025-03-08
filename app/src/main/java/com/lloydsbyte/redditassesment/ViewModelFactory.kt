package com.lloydsbyte.redditassesment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lloydsbyte.redditassesment.database.AppDatabase

class ViewModelFactory(
    private val database: AppDatabase
) : ViewModelProvider.NewInstanceFactory()  {
    //This might be a little overkill, but it is typically how I set up my viewmodels today

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) ->
                MainViewModel(database) as T
            else ->
                MainViewModel(database) as T
        }
    }
}