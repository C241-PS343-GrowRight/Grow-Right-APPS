package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName


data class ProfileResponse(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("data")
    val data: ProfileData
)
data class ProfileData(
    @field: SerializedName ("userName")
    val userName: String,

    @field: SerializedName ("email")
    val email: String
)