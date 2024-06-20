package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: UserData,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("message")
    val message: String?
)

data class UserData(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("name")
    val userName: String,

    @field:SerializedName("email")
    val email: String
)
