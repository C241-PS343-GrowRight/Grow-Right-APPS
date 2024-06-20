package by.marcel.grow_right.api.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import by.marcel.grow_right.api.ApiService
import by.marcel.grow_right.api.model.response.LoginResponse
import by.marcel.grow_right.api.model.response.RegisterResponse
import by.marcel.grow_right.handler.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class GetAllRepository private constructor(private val dataStore: DataStore<Preferences>, private val apiService: ApiService) {

    fun login(email: String, password: String): LiveData<Outcome<LoginResponse>> = liveData {
        emit(Outcome.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Outcome.Success(result))
        } catch (throwable: HttpException) {
            try {
                throwable.response()?.errorBody()?.source()?.let {
                    emit(Outcome.Error(it.toString()))
                }
            } catch (exception: Exception) {
                emit(Outcome.Error(exception.message.toString()))
            }
        }
    }

    fun register(userName: String, email: String, password: String): LiveData<Outcome<RegisterResponse>> = liveData {
        emit(Outcome.Loading)
        try {
            val result = apiService.register(userName, email, password)
            emit(Outcome.Success(result))
        } catch (throwable: HttpException) {
            try {
                val errorBody = throwable.response()?.errorBody()?.string()
                val errorMessage = errorBody ?: throwable.message()
                emit(Outcome.Error(errorMessage))
            } catch (exception: Exception) {
                emit(Outcome.Error(exception.message.toString()))
            }
        }
    }

    fun isLogin(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[STATE_KEY] ?: false
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin
        }
    }

    fun getToken(): Flow<String> = dataStore.data.map { preferences ->
        preferences[TOKEN] ?: ""
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GetAllRepository? = null

        private val TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): GetAllRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = GetAllRepository(dataStore, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}
