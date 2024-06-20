package by.marcel.grow_right.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.marcel.grow_right.R
import by.marcel.grow_right.adapter.factory.ViewModelFactory
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.ApiConfig
import by.marcel.grow_right.api.ApiService
import by.marcel.grow_right.databinding.ActivityProfileBinding
import by.marcel.grow_right.ui.editprofile.EditProfileActivity
import by.marcel.grow_right.ui.homepage.HomePageActivity
import by.marcel.grow_right.ui.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileActivity : AppCompatActivity() {

    private lateinit var bindingProf : ActivityProfileBinding
    private lateinit var viewModel : ProfileViewModel
    private lateinit var token: String
    private lateinit var userId: String
    private  lateinit var setSession: ManageSession
    private  lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingProf = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(bindingProf.root)

        supportActionBar?.hide()

        setSession = ManageSession(this)
        token = intent.getStringExtra(HomePageActivity.EXTRA_TOKEN) ?: setSession.fetchAuthToken() ?: ""
        userId = intent.getSerializableExtra(HomePageActivity.EXTRA_USER_ID).toString()

        apiService = ApiConfig.createApiService()

        setupViewModel()
        fetchProfile()
        buttonLogout()
        tvLogout()

        bindingProf.ivEdit.setOnClickListener {
            editProfile(token, userId)
        }

        bindingProf.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]
    }

    private fun fetchProfile() {
        lifecycleScope.launch {
            try {
                val response = apiService.getProfile(token, userId)
                withContext(Dispatchers.Main) {
                    Log.d("ProfileActivity", "Profile data: $response")
                    bindingProf.tvName.text = response.data.userName
                    bindingProf.tvEmail.text = response.data.email
                }
            } catch (e: Exception) {
                // Handle exceptions
                e.printStackTrace()
            }
        }
    }

    private fun buttonLogout(){
        bindingProf.ivLogout?.setOnClickListener {
            viewModel.logout()
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.log_out_success))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }


    private fun tvLogout(){
        bindingProf.ivLogout?.setOnClickListener {
            viewModel.logout()
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.information))
                setMessage(getString(R.string.log_out_success))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun editProfile(token: String, userId: String){
        val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java).apply {
            putExtra(EXTRA_TOKEN, token)
            putExtra(EXTRA_USER_ID, userId)
        }
        startActivity(intent)
    }

    companion object {
        const val  EXTRA_TOKEN = "extra_token"
        const val  EXTRA_USER_ID = "extra_user_id"
    }

}