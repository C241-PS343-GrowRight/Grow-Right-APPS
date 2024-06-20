package by.marcel.grow_right.ui.homepage


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import by.marcel.grow_right.R
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.ApiConfig
import by.marcel.grow_right.api.ApiService
import by.marcel.grow_right.databinding.ActivityHomePageBinding
import by.marcel.grow_right.ui.analyze.AnalyzeActivity
import by.marcel.grow_right.ui.history.HistoryActivity
import by.marcel.grow_right.ui.login.LoginActivity
import by.marcel.grow_right.ui.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomePageActivity : AppCompatActivity() {

    private lateinit var homeBinding : ActivityHomePageBinding
    private lateinit var token: String
    private lateinit var userId: String
    private  lateinit var setSession: ManageSession
    private  lateinit var apiService: ApiService

    @SuppressLint("StringFormatInvalid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        supportActionBar?.hide()

        setSession = ManageSession(this)
        token = intent.getStringExtra(EXTRA_TOKEN) ?: setSession.fetchAuthToken() ?: ""
        userId = intent.getSerializableExtra(EXTRA_USER_ID).toString()
        apiService = ApiConfig.createApiService()

        fetchProfile()
        setupUI()
    }

    private fun setupUI(){
        homeBinding.btnHistory.setOnClickListener {
            getHistory(token, userId)
        }
        homeBinding.btnAnalyzeStunting.setOnClickListener {
            handleSuccess(token, userId)
        }
        homeBinding.btnNutrisi.setOnClickListener {
            intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.halodoc.com/artikel/ini-5-nutrisi-yang-efektif-untuk-mencegah-stunting-pada-anak"))
            startActivity(intent)
        }
        homeBinding.btnStuntingInformation.setOnClickListener {
            intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.halodoc.com/kesehatan/stunting"))
            startActivity(intent)
        }
        homeBinding.btnProfile.setOnClickListener {
            getProfile(token, userId)
        }
    }

    private fun handleSuccess(token: String, userId: String) {
        AlertDialog.Builder(this).apply {
            setTitle("NOTE")
            setMessage("There are 5 data entered, namely name, gender, height (in CM units), weight (in KG units), age (in month units)")
            setPositiveButton("Next") { _, _ ->
                val intent = Intent(this@HomePageActivity, AnalyzeActivity::class.java).apply {
                    putExtra(EXTRA_TOKEN, token)
                    putExtra(EXTRA_USER_ID, userId)
                }
                startActivity(intent)
            }
            create()
            show()
        }
    }

    private fun getHistory(token: String, userId: String){
        val intent = Intent(this@HomePageActivity, HistoryActivity::class.java).apply {
            putExtra(EXTRA_TOKEN, token)
            putExtra(EXTRA_USER_ID, userId)
        }
        startActivity(intent)
    }

    private fun getProfile(token: String, userId: String){
        val intent = Intent(this@HomePageActivity, ProfileActivity::class.java).apply {
            putExtra(EXTRA_TOKEN, token)
            putExtra(EXTRA_USER_ID, userId)
        }
        startActivity(intent)
    }

    private fun fetchProfile() {
        lifecycleScope.launch {
            try {
                val response = apiService.getProfile(token, userId)
                withContext(Dispatchers.Main) {
                    homeBinding.tvUserName.text = response.data.userName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val  EXTRA_TOKEN = "extra_token"
        const val  EXTRA_USER_ID = "extra_user_id"
    }
}