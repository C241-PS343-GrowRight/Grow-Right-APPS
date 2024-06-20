package by.marcel.grow_right.ui.history

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.marcel.grow_right.R
import by.marcel.grow_right.adapter.factory.HistoryAdapter
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.ApiService
import by.marcel.grow_right.api.database.AppDatabase
import by.marcel.grow_right.api.database.PredictionDB
import by.marcel.grow_right.api.database.PredictionDao
import by.marcel.grow_right.databinding.ActivityHistoryBinding
import by.marcel.grow_right.ui.homepage.HomePageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var getBinding: ActivityHistoryBinding
    private lateinit var token: String
    private lateinit var userId: String
    private  lateinit var setSession: ManageSession
    private lateinit var predictionDao: PredictionDao
    private lateinit var historyAdapter: HistoryAdapter
    private  lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBinding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(getBinding.root)

        val colorDrawable = ColorDrawable(Color.parseColor("#FF42B4F4"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        setSession = ManageSession(this)
        token = intent.getStringExtra(HomePageActivity.EXTRA_TOKEN) ?: setSession.fetchAuthToken() ?: ""
        userId = intent.getSerializableExtra(HomePageActivity.EXTRA_USER_ID).toString()

        val db = AppDatabase.getDatabase(applicationContext)
        predictionDao = db.predictionDao()

        getBinding.recyclerView.layoutManager = LinearLayoutManager(this)

        val predictionsLiveData: LiveData<List<PredictionDB>> = predictionDao.getAllPredictions()

        historyAdapter = HistoryAdapter(predictionsLiveData)
        getBinding.recyclerView.adapter = historyAdapter

        getBinding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.back -> {
                onBackPressedDispatcher.onBackPressed()
            }

        }
        return super.onOptionsItemSelected(item)
    }

//    private fun handleBackButton(onBackPressed: Unit) {
//        // Create intent to navigate to HomePageActivity
//        val intent = Intent(this@HistoryActivity, HomePageActivity::class.java).apply {
//            fetchProfile()
//            putExtra(EXTRA_TOKEN, token)
//            putExtra(EXTRA_USER_ID, userId)
//        }
//        startActivity(intent)
//
//        // Call the back pressed dispatcher to handle back navigation
//        onBackPressedDispatcher.onBackPressed()
//    }

//    private fun fetchProfile() {
//        lifecycleScope.launch {
//            try {
//                val response = apiService.getProfile(token, userId)
//                withContext(Dispatchers.Main) {
//                    Log.d("ProfileActivity", "Profile data: $response")
//                     response.data.userName
//                     response.data.email
//                }
//            } catch (e: Exception) {
//                // Handle exceptions
//                e.printStackTrace()
//            }
//        }
//    }


    companion object {
        const val  EXTRA_TOKEN = "extra_token"
        const val  EXTRA_USER_ID = "extra_user_id"
    }
}