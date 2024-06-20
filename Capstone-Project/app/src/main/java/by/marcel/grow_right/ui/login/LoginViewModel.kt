package by.marcel.grow_right.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.marcel.grow_right.api.model.response.LoginResponse
import by.marcel.grow_right.api.remote.GetAllRepository
import by.marcel.grow_right.handler.Outcome
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: GetAllRepository) : ViewModel() {

    fun setLogin(email: String, password: String): LiveData<Outcome<LoginResponse>> {
        return repository.login(email, password)
    }
}