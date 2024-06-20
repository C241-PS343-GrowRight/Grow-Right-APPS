package by.marcel.grow_right.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import by.marcel.grow_right.api.model.response.RegisterResponse
import by.marcel.grow_right.api.remote.GetAllRepository
import by.marcel.grow_right.handler.Outcome

class RegisterViewModel (private val repository: GetAllRepository ) : ViewModel() {
    fun register(userName: String, email: String, password: String): LiveData<Outcome<RegisterResponse>> {
        return repository.register(userName, email, password)
    }
}