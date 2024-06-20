package by.marcel.grow_right.ui.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import by.marcel.grow_right.adapter.factory.HistoryAdapter
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.database.AppDatabase
import by.marcel.grow_right.api.database.PredictionDB
import by.marcel.grow_right.databinding.ActivityResultBinding
import by.marcel.grow_right.ui.history.HistoryActivity
import by.marcel.grow_right.ui.homepage.HomePageActivity
import by.marcel.grow_right.ui.profile.ProfileActivity
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var setSession: ManageSession
    private lateinit var token: String
    private lateinit var userId: String

    private var resultHeight: Float = 0f
    private var resultWeight: Float = 0f
    private var resultAge: Float = 0f
    private var resultPercentage: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setSession = ManageSession(this)

        token = setSession.fetchAuthToken() ?: intent.getStringExtra(HomePageActivity.EXTRA_TOKEN) ?: ""
        userId = intent.getSerializableExtra(HomePageActivity.EXTRA_USER_ID).toString()

        val name = intent.getStringExtra("USERNAME")
        val age = intent.getIntExtra("AGE", 0)
        val weight = intent.getFloatExtra("WEIGHT", 0f)
        val height = intent.getFloatExtra("HEIGHT", 0f)
        val gender = intent.getFloatExtra("GENDER", 0f)
        resultHeight = intent.getFloatExtra("RESULT_HEIGHT", 0f)
        resultWeight = intent.getFloatExtra("RESULT_WEIGHT", 0f)
        resultAge = intent.getFloatExtra("RESULT_AGE", 0f)
        resultPercentage = intent.getFloatExtra("RESULT_PERSENTAGE", 0f)

        binding.apply {
            tvNameResult.text = name
            tvAge.text = age.toString()
            tvWeight.text = weight.toString()
            tvHeight.text = height.toString()
            tvGenderResult.text = if (gender == 0f) "Male" else "Female"
            zsHeight.text = resultHeight.toString()
            zsWeight.text = resultWeight.toString()
            zsAge.text = resultAge.toString()

            var stunting = "Stunting";
            var notStunting = "Tidak Stunting";
            var maxStunting = "Severed Stunting";

            if (resultPercentage <= 19) {
                tvDescription.text = "The results of your analysis  " + resultPercentage + "%, based on these results you are included " + notStunting;
            } else if (resultPercentage >= 20 && resultPercentage <= 50) {
                tvDescription.text = "The results of your analysis  " + resultPercentage + "%, based on these results you are included " + stunting;
            } else if (resultPercentage >= 51 && resultPercentage <= 100) {
                tvDescription.text = "The results of your analysis  " + resultPercentage + "%, based on these results you are included " + maxStunting;
            } else {
                tvDescription.text = "Invalid percentage value. Enter a value between 0 and 100.";
            }


        }


        binding.resultBack.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
            savePrediction(token, userId, name, age, weight, height, gender, resultHeight, resultWeight, resultAge, resultPercentage)
        }

    }

    private fun savePrediction(userId: String, token: String, name: String?, age: Int, weight: Float, height: Float, gender: Float, resultHeight: Float, resultWeight: Float, resultAge: Float, resultPercentage: Float) {
        val prediction = PredictionDB(
            userId = userId,
            token = token,
            name = name ?: "",
            age = age,
            weight = weight,
            height = height,
            gender = gender.toInt(),
            resultHeight = resultHeight,
            resultWeight = resultWeight,
            resultAge = resultAge,
            resultPercentage = resultPercentage
        )

        val db = AppDatabase.getDatabase(applicationContext)
        val predictionDao = db.predictionDao()

        lifecycleScope.launch {
            predictionDao.insert(prediction)
            navigateToHistoryActivity(token, userId)
        }
    }

    private fun navigateToHistoryActivity(token: String, userId: String) {
        val intent = Intent(this, HistoryActivity::class.java).apply {
            putExtra(EXTRA_TOKEN, token)
            putExtra(EXTRA_USER_ID, userId)
            putExtra("RESULT_HEIGHT", resultHeight)
            putExtra("RESULT_WEIGHT", resultWeight)
            putExtra("RESULT_AGE", resultAge)
            putExtra("RESULT_PERCENTAGE", resultPercentage)
        }
        startActivity(intent)
    }

    companion object {
        const val  EXTRA_TOKEN = "extra_token"
        const val  EXTRA_USER_ID = "extra_user_id"
    }

}