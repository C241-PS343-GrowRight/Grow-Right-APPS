package by.marcel.grow_right.handler

sealed class Outcome <out R> {
    data class Success<out T>(val data: T) : Outcome<T>()
    data class Error(val errorMessage: String) : Outcome<Nothing>()
    object Loading : Outcome<Nothing>()
}