package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName

data class EditRequest(
    @field: SerializedName("userName")
    val userName: String,

    @field: SerializedName("email")
    val email: String,

    @field: SerializedName("noHp")
    val noHp: String
)