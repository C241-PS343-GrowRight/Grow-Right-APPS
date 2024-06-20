package by.marcel.grow_right.api


import by.marcel.grow_right.api.model.response.EditProfile
import by.marcel.grow_right.api.model.response.EditRequest
import by.marcel.grow_right.api.model.response.HomePageResponse
import by.marcel.grow_right.api.model.response.LoginResponse
import by.marcel.grow_right.api.model.response.PredictionHistoryResponse
import by.marcel.grow_right.api.model.response.PredictionResponse
import by.marcel.grow_right.api.model.response.ProfileResponse
import by.marcel.grow_right.api.model.response.RegisterResponse
import by.marcel.grow_right.api.model.response.SavePrediction
import by.marcel.grow_right.api.model.response.SavePredictionResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("userName") userName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("home")
    suspend fun getHome(
        @Header("Authorization") token: String
    ): HomePageResponse

    @POST("prediction/{userId}")
    fun getPrediction(
        @Header("Authorization") authHeader: String,
        @Path("userId") userId: String,
        @Body request: PredictionRequest
    ): Call<PredictionResponse>

    data class PredictionRequest(
        val name: String,
        val height: Int,
        val weight: Int,
        val age: Int,
        val gender: Int
    )

    @GET("prediction/{userId}")
    fun getPredictionHistory(
        @Header("token") token: String,
        @Path("userId") userId: String
    ): Call<PredictionHistoryResponse>


//    @POST("prediction/{userId}")
//    suspend fun savePrediction(
//        @Header("Authorization") token: String,
//        @Path("userId") userId: String,
//        @Body prediction: PredictionRequest
//    ): Response<SavePrediction>


    @GET("profile/{userId}")
    suspend fun getProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): ProfileResponse

    @PUT("profile/{userId}")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Body request: EditRequest
    ): Response<EditProfile>

//    @FormUrlEncoded
//    @PUT("profile/{userId}")
//    suspend fun editProfile(
//        @Header("Authorization") token: String,
//        @Path("userId") userId: String,
//        @Field("name") name: String,
//        @Field("email") email: String
//    ): ResponseApi
}
