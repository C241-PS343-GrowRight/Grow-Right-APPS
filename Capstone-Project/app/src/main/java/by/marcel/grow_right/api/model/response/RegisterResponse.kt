package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,

    @field:SerializedName("userData")
    val data: RegisData,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("message")
    val message: String?
)
data class RegisData(
    @field:SerializedName("userName")
    val userName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)