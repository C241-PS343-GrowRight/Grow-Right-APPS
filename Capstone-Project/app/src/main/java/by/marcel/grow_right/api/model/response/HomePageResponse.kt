package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName

data class HomePageResponse(

    val status: String,
    val statusCode: Int,
    val data: UserData,

    @field:SerializedName("token")
    val token: String
)
data class HomeData(
    val userName: String,
    val email: String,
    val password: String
)