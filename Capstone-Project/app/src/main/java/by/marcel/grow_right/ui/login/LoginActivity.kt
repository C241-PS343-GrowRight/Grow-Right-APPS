package by.marcel.grow_right.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import by.marcel.grow_right.R
import by.marcel.grow_right.adapter.factory.ViewModelFactory
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.model.response.LoginResponse
import by.marcel.grow_right.databinding.ActivityLoginBinding
import by.marcel.grow_right.handler.Outcome
import by.marcel.grow_right.ui.homepage.HomePageActivity
import by.marcel.grow_right.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private  lateinit var loginBinding: ActivityLoginBinding
    private  lateinit var loginViewModel: LoginViewModel
    private  lateinit var setSession: ManageSession
    private var userName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        supportActionBar?.hide()

        setSession = ManageSession(this)
        userName = intent.getStringExtra(EXTRA_USERNAME)
        setupViewModel()
        setupAction()

    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun setupAction() {
        loginBinding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        loginBinding.btnSignIn.setOnClickListener {
            val email = loginBinding.email.text.toString()
            val password = loginBinding.password.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                login()
            } else {
                Toast.makeText(this, getString(R.string.must_filled), Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private fun login() {
        val email = loginBinding.email.text.toString().trim()
        val password = loginBinding.password.text.toString().trim()
        when {
            email.isEmpty() -> {
                loginBinding.email.error = resources.getString(R.string.confirm_validation, "email")
            }
            password.isEmpty() -> {
                loginBinding.password.error = resources.getString(R.string.confirm_validation, "password")
            }
            else -> {
                loginViewModel.setLogin(email, password).observe(this) { result ->
                    when (result) {
                        is Outcome.Loading -> {
                            showLoading(true)
                        }
                        is Outcome.Success<*> -> {
                            showLoading(false)
                            val loginResponse = result.data as LoginResponse
                            if (loginResponse.status == "success") {
                                val token = loginResponse.token ?: ""
                                setSession.saveAuthToken(token)
                                val userId = loginResponse.data.userId
                                Toast.makeText(this, loginResponse.message, Toast.LENGTH_SHORT).show()


                                try {
                                    val intent = Intent(this, HomePageActivity::class.java).apply {
                                        putExtra(HomePageActivity.EXTRA_TOKEN, token)
                                        putExtra(HomePageActivity.EXTRA_USER_ID, userId)
                                    }
                                    startActivity(intent)
                                    finish()
                                } catch (e: Exception) {
                                    Log.e("LoginActivity", "Error starting HomePageActivity: ${e.message}")
                                    e.printStackTrace()
                                }
                            } else {
                                Toast.makeText(this, loginResponse.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Outcome.Error -> {
                            showLoading(false)
                            Toast.makeText(this, "Login failed: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        loginBinding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
    }
}