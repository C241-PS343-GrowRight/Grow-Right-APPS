package by.marcel.grow_right.api.model.response

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class EditProfile(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("status.code")
    val statusCode: Int,

    @field:SerializedName("data")
    val data: EditData,

)

data class EditData(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("userName")
    val userName: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("noHp")
    val noHp: String,

    @field:SerializedName("createDat")
    val createDat: String,

    @field:SerializedName("updateDat")
    val updateDat: String
)
