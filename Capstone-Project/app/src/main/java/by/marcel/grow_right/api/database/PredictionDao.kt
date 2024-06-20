package by.marcel.grow_right.api.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PredictionDao {

    @Query("SELECT * FROM Tb_prediction")
    fun getAllPredictions(): LiveData<List<PredictionDB>>

//    @Query("SELECT * FROM Tb_prediction")
//    fun getAllStories(): PagingSource<Int, DataLocationStory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prediction: PredictionDB)

    @Query("DELETE FROM Tb_prediction")
    fun deleteAllStories()
}
