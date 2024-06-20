package by.marcel.grow_right.adapter.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import by.marcel.grow_right.adapter.session.SetInjection
import by.marcel.grow_right.api.remote.GetAllRepository
import by.marcel.grow_right.ui.login.LoginViewModel
import by.marcel.grow_right.ui.profile.ProfileViewModel
import by.marcel.grow_right.ui.register.RegisterViewModel

class ViewModelFactory(
    private val userRepo: GetAllRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepo) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepo) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    SetInjection.provideRepository(context)
                )
            }.also { instance = it }
    }
}