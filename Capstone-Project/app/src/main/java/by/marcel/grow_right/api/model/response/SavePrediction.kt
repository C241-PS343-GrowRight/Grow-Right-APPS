package by.marcel.grow_right.api.model.response

data class SavePrediction(
    val input: getInput,
    val prediction: getPrediction
)

data class InputData(
    val name: String,
    val height: Int,
    val weight: Int,
    val age: Int,
    val gender: Int
)

data class PredictionData(
    val zsWeightAge: Float,
    val zsHeightAge: Float,
    val zsWeightHeight: Float,
    val zsTotal: Float,
    val zsTotalPercentage: Float
)

data class SavePredictionResponse(
    val status: String,
    val statusCode: Int,
    val data: List<PredictionHistory>
)

data class PredictionHistory(
    val id: Int,
    val input: InputData,
    val prediction: PredictionData,
    val createDat: String
)