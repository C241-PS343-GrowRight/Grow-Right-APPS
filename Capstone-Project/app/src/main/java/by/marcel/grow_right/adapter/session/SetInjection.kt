package by.marcel.grow_right.adapter.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import by.marcel.grow_right.api.ApiConfig
import by.marcel.grow_right.api.remote.GetAllRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object SetInjection {
    fun provideRepository(context: Context): GetAllRepository {
        val apiService = ApiConfig.createApiService()
        return GetAllRepository.getInstance(context.dataStore, apiService)
    }
}