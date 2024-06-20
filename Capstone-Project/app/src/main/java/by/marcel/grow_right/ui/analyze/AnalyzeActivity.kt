package by.marcel.grow_right.ui.analyze

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import by.marcel.grow_right.R
import by.marcel.grow_right.adapter.session.ManageSession
import by.marcel.grow_right.api.ApiConfig
import by.marcel.grow_right.api.ApiService
import by.marcel.grow_right.api.model.response.PredictionResponse
import by.marcel.grow_right.databinding.ActivityAnalyzeBinding
import by.marcel.grow_right.ui.homepage.HomePageActivity
import by.marcel.grow_right.ui.result.ResultActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnalyzeActivity : AppCompatActivity() {
    private lateinit var analyzeBinding: ActivityAnalyzeBinding
    private lateinit var setSession: ManageSession
    private lateinit var token: String
    private lateinit var userId: String

    private val apiService: ApiService by lazy { ApiConfig.createApiService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analyzeBinding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(analyzeBinding.root)

        supportActionBar?.hide()

        setSession = ManageSession(this)

        token = setSession.fetchAuthToken() ?: intent.getStringExtra(HomePageActivity.EXTRA_TOKEN) ?: ""
        userId = intent.getSerializableExtra(HomePageActivity.EXTRA_USER_ID).toString()

        Log.d("AnalyzeActivity", "Retrieved token: $token, userid: $userId")

        analyzeBinding.btnAnalyze.setOnClickListener {
            val nameText = analyzeBinding.username.text.toString()
            val ageText = analyzeBinding.age.text.toString().toIntOrNull()
            val weightText = analyzeBinding.weight.text.toString().toIntOrNull()
            val heightText = analyzeBinding.height.text.toString().toIntOrNull()
            val genderText = analyzeBinding.gender.text.toString()

            if (nameText.isNotEmpty() && ageText != null && weightText != null && heightText != null) {
                val gender = convertGenderToFloat(genderText)
                if (gender != null) {
                    val predictionRequest = ApiService.PredictionRequest(
                        nameText,
                        heightText,
                        weightText,
                        ageText,
                        gender
                    )
                    Log.d("AnalyzeActivity", "Request Data: $predictionRequest")
                    if (token.isNotEmpty()) {
                        makeApiCall(token, userId, predictionRequest)
                    } else {
                        showToast("Authentication token is missing.")
                        Log.e("AnalyzeActivity", "Token is empty.")
                    }
                } else {
                    showToast("Invalid gender")
                }
            } else {
                showToast("Please fill all fields correctly")
            }
        }

        analyzeBinding.analyzeBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun convertGenderToFloat(genderText: String): Int? {
        return when (genderText.lowercase()) {
            "male" -> 0
            "female" -> 1
            else -> null
        }
    }

    private fun makeApiCall(token: String, userId: String, request: ApiService.PredictionRequest) {
        val authHeader = "Bearer $token"
        Log.d("AnalyzeActivity", "Authorization Header: $authHeader")
        apiService.getPrediction(authHeader, userId, request)
            .enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    if (response.isSuccessful) {
                        val predictionResponse = response.body()
                        predictionResponse?.let {
                            handleSuccess(it)
                        }
                    } else {
                        Log.e("AnalyzeActivity", "Error: ${response.code()}")
                        showToast("Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    Log.e("AnalyzeActivity", "Failure: ${t.message}")
                    showToast("Failed to connect to the server")
                }
            })
    }

    private fun handleSuccess(predictionResponse: PredictionResponse) {
        AlertDialog.Builder(this).apply {
            setTitle("Yeah!")
            setMessage("View Analysis Results")
            setPositiveButton("Next") { _, _ ->
                val intent = Intent(this@AnalyzeActivity, ResultActivity::class.java).apply {
                    putExtra("USERNAME", predictionResponse.data.input.name)
                    putExtra("AGE", predictionResponse.data.input.age)
                    putExtra("WEIGHT", predictionResponse.data.input.weight)
                    putExtra("HEIGHT", predictionResponse.data.input.height)
                    putExtra("GENDER", predictionResponse.data.input.gender.toFloat())
                    putExtra("RESULT_HEIGHT", predictionResponse.data.prediction.zsHeightAge)
                    putExtra("RESULT_WEIGHT", predictionResponse.data.prediction.zsWeightAge)
                    putExtra("RESULT_AGE", predictionResponse.data.prediction.zsWeightHeight)
                    putExtra("RESULT_PERSENTAGE", predictionResponse.data.prediction.zsTotalPercentage)
                }
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }
}
