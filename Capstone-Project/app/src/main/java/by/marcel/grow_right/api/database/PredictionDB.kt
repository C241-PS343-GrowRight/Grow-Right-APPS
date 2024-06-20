package by.marcel.grow_right.api.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Tb_prediction")
data class PredictionDB (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val token : String,
    val name: String,
    val age: Int,
    val weight: Float,
    val height: Float,
    val gender: Int,
    val resultHeight: Float,
    val resultWeight: Float,
    val resultAge: Float,
    val resultPercentage: Float,
    val createdAt: Long = System.currentTimeMillis()
)