package by.marcel.grow_right.ui.editprofile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.ApiConfig
import by.marcel.grow_right.api.ApiService
import by.marcel.grow_right.api.model.response.EditRequest
import by.marcel.grow_right.databinding.ActivityEditProfileBinding
import by.marcel.grow_right.ui.analyze.AnalyzeActivity
import by.marcel.grow_right.ui.homepage.HomePageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException


class EditProfileActivity : AppCompatActivity() {

    private lateinit var bindingEdit: ActivityEditProfileBinding
    private lateinit var token: String
    private lateinit var userId: String
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingEdit = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(bindingEdit.root)

        supportActionBar?.hide()

        token = intent.getStringExtra(HomePageActivity.EXTRA_TOKEN) ?: ""
        userId = intent.getStringExtra(HomePageActivity.EXTRA_USER_ID) ?: ""

        apiService = ApiConfig.createApiService()

        bindingEdit.saveProfile.setOnClickListener {
            val userName = bindingEdit.username.text.toString()
            val email = bindingEdit.email.text.toString()
            val noHp = bindingEdit.phone.text.toString()

            if (userName.isNotBlank() && email.isNotBlank() && noHp.isNotBlank()) {

                lifecycleScope.launch {
                    try {
                        updateProfile(userName, email, noHp)
                    } catch (e: Exception) {
                        Toast.makeText(this@EditProfileActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        bindingEdit.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private suspend fun updateProfile(userName: String, email: String, noHp: String) {
        try {
            val response = apiService.updateProfile(
                "Bearer $token",
                userId,
                EditRequest(userName, email, noHp)
            )

            if (response.isSuccessful) {
                val editProfileResponse = response.body()
                editProfileResponse?.let {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        handleSuccess(token, userId)
                    }
                    Log.d("EditProfileActivity", "Profile updated: $it")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditProfileActivity, "Failed to update profile: $errorBody", Toast.LENGTH_SHORT).show()
                }
                Log.e("EditProfileActivity", "Error: $errorBody")
            }
        } catch (e: HttpException) {

            val errorMessage = e.response()?.errorBody()?.string()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditProfileActivity, "HTTP Error: $errorMessage", Toast.LENGTH_SHORT).show()
            }
            Log.e("EditProfileActivity", "HTTP Exception: $errorMessage", e)
        } catch (e: Exception) {

            Log.e("EditProfileActivity", "Exception: ${e.message}", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@EditProfileActivity, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun handleSuccess(token: String, userId: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Save Successfuly")
            setMessage("Do you want to return to the Home Page")
            setPositiveButton("Next") { _, _ ->
                val intent = Intent(this@EditProfileActivity, HomePageActivity::class.java).apply {
                    putExtra(EXTRA_TOKEN, token)
                    putExtra(EXTRA_USER_ID, userId)
                }
                startActivity(intent)
            }
            create()
            show()
        }
    }

    companion object {
        const val  EXTRA_TOKEN = "extra_token"
        const val  EXTRA_USER_ID = "extra_user_id"
    }
}
