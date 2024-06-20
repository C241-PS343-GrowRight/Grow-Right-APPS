package by.marcel.grow_right.adapter.session

import android.content.Context
import android.content.SharedPreferences

class ManageSession (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences("grow_right_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}