package by.marcel.grow_right.api.model.response


import com.google.gson.annotations.SerializedName

data class PredictionResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field :SerializedName("data")
    val data: Data,

    @field:SerializedName("message")
    val message: String?
)

data class Data(
    @field :SerializedName("user")
    val user: User,

    @field :SerializedName("input")
    val input: Input,

    @field :SerializedName("prediction")
    val prediction: Prediction,

    @field :SerializedName("description")
    val description: Description
)

data class User(
    @field :SerializedName("userName")
    val userName: String,

    @field :SerializedName("email")
    val email: String
)

data class Input(
    @field :SerializedName("name")
    val name: String,

    @field :SerializedName("height")
    val height: Float,

    @field :SerializedName("weight")
    val weight: Float,

    @field :SerializedName("age")
    val age: Int,

    @field :SerializedName("gender")
    val gender: Int
)

data class Prediction(
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

data class Description(
    @field :SerializedName("WeightAge")
    val weightAge: String,

    @field :SerializedName("HeightAge")
    val heightAge: String,

    @field :SerializedName("WeightHeight")
    val weightHeight: String,

    @field :SerializedName("Conclusion")
    val conclusion: String
)
