package by.marcel.grow_right.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.marcel.grow_right.api.remote.GetAllRepository
import kotlinx.coroutines.launch

class ProfileViewModel  (private val repository: GetAllRepository) : ViewModel() {

    fun logout() {
        viewModelScope. launch {
            repository. logout()
        }
    }
}