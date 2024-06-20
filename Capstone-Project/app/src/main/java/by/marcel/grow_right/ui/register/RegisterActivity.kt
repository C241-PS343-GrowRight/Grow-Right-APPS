package by.marcel.grow_right.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import by.marcel.grow_right.R
import by.marcel.grow_right.adapter.factory.ViewModelFactory
import by.marcel.grow_right.adapter.helper.animateVisibilityWithFade
import by.marcel.grow_right.databinding.ActivityRegisterBinding
import by.marcel.grow_right.handler.Outcome
import by.marcel.grow_right.ui.login.LoginActivity
import by.marcel.grow_right.ui.login.LoginActivity.Companion.EXTRA_USERNAME

class RegisterActivity : AppCompatActivity() {

    private lateinit var bindingRegis : ActivityRegisterBinding
    private lateinit var viewModelRegister: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingRegis = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingRegis.root)

        supportActionBar?.hide()

        setupUI()
        setupViewModel()
    }


    private fun setupUI() {
        bindingRegis.btnRegister.setOnClickListener {
            register()
        }
    }

    private fun setupViewModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        viewModelRegister = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }


    @SuppressLint("StringFormatInvalid")
    private fun register() {
        val userName = bindingRegis.username.text.toString().trim()
        val email = bindingRegis.email.text.toString().trim()
        val password = bindingRegis.password.text.toString()

        Log.d("RegisterActivity", "Registering with userName: $userName, email: $email")
        when {
            userName.isEmpty() -> {
                bindingRegis.username.error = resources.getString(R.string.confirm_validation, "name")
            }
            email.isEmpty() -> {
                bindingRegis.email.error = resources.getString(R.string.confirm_validation, "email")
            }
            password.length < 8 -> {
                bindingRegis.password.error = resources.getString(R.string.confirm_validation)
            }
            else -> {
                viewModelRegister.register(userName, email, password).observe(this) { summary ->
                    if (summary != null) {
                        when (summary) {
                            is Outcome.Loading -> {
                                showLoading(true)
                            }
                            is Outcome.Success -> {
                                showLoading(false)
                                val user = summary.data
                                if (user.status == "success") {
                                    val userName = user.data.userName
                                    showSuccessDialog(userName)
                                } else {
                                    Toast.makeText(this@RegisterActivity, "Registration Failed: ${user.statusCode}", Toast.LENGTH_SHORT).show()
                                }
                            }
                            is Outcome.Error -> {
                                showLoading(false)
                                val errorMessage = summary.errorMessage ?: resources.getString(R.string.register_failed)
                                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }
            }
        }
    }


    private fun showSuccessDialog(userName: String) {
        AlertDialog.Builder(this@RegisterActivity).apply {
            setTitle("Yeah!")
            setMessage("Your account has been successfully created!")
            setPositiveButton("Next") { _, _ ->
                navigateToLogin(userName)
            }
            create()
            show()
        }
    }

    private fun navigateToLogin(userName: String) {
        val intent = Intent(this, LoginActivity::class.java).apply {
            putExtra(LoginActivity.EXTRA_USERNAME, userName) // Add this line
        }
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        bindingRegis.apply {
            username.isEnabled = !isLoading
            email.isEnabled = !isLoading
            password.isEnabled = !isLoading
            btnRegister.isEnabled = !isLoading

            progressBar.animateVisibilityWithFade(isLoading)
        }
    }
}