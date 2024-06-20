package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class PredictionHistoryResponse(
    @field :SerializedName("status")
    val status: String,
    @field :SerializedName("status.code")
    val statusCode: Int,
    @field :SerializedName("data")
    val data: List<PredictionHistoryItem>
)


data class PredictionHistoryItem(
    @field :SerializedName("id")
    val id: Int,
    @field :SerializedName("input")
    val input: getInput,
    @field :SerializedName("prediction")
    val prediction: getPrediction,
    @field :SerializedName("createDat")
    val createDate: String
)

data class getInput(
    @field :SerializedName("name")
    val name: String,
    @field :SerializedName("height")
    val height: Int,
    @field :SerializedName("weight")
    val weight: Int,
    @field :SerializedName("age")
    val age: Int,
    @field :SerializedName("gender")
    val gender: Int
)

data class getPrediction(
    @field :SerializedName("zsWeightAge")
    val zsWeightAge: Float,
    @field :SerializedName("zsHeightAge")
    val zsHeightAge: Float,
    @field :SerializedName("zsWeightHeight")
    val zsWeightHeight: Float,
    @field :SerializedName("zsTotal")
    val zsTotal: Float,
    @field :SerializedName("zsTotalPercentage")
    val zsTotalPercentage: Float
)
