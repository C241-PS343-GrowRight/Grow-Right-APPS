package by.marcel.grow_right.adapter.factory

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import by.marcel.grow_right.R
import by.marcel.grow_right.api.database.PredictionDB
import by.marcel.grow_right.databinding.ListHistoryBinding

class HistoryAdapter (private var predictionsLiveData: LiveData<List<PredictionDB>>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {


    private var predictions: List<PredictionDB> = emptyList()

    private val observer = Observer<List<PredictionDB>> { newPredictions ->
        predictions = newPredictions.reversed()
        notifyDataSetChanged()
    }

    init {
        predictionsLiveData.observeForever(observer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val prediction = predictions[position]
        holder.bind(prediction)
    }

    override fun getItemCount(): Int {
        return predictions.size
    }


    inner class ViewHolder(private val binding: ListHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(prediction: PredictionDB) {
            binding.apply {
                tvNameResult.text = prediction.name
                tvGenderResult.text = if (prediction.gender == 0) "Male" else "Female"
                tvAge.text = prediction.age.toString()
                tvHeight.text = prediction.height.toString()
                tvWeight.text = prediction.weight.toString()
                zsHeight.text = prediction.resultHeight.toString()
                zsWeight.text = prediction.resultWeight.toString()
                zsAge.text = prediction.resultAge.toString()
                val resultPercentage = prediction.resultPercentage
                var stunting = "Stunting";
                var notStunting = "Tidak Stunting";
                var maxStunting = "Severed Stunting";

                if (resultPercentage <= 19) {
                    tvDescription.text = "The results of your analysis  " + resultPercentage + "%, based on these results you are included " + notStunting;
                } else if (resultPercentage >= 20 && resultPercentage <= 50) {
                    tvDescription.text = "The results of your analysis  " + resultPercentage + "%, based on these results you are included " + stunting;
                } else if (resultPercentage >= 51 && resultPercentage <= 100) {
                    tvDescription.text = "The results of your analysis  " + resultPercentage + "%, based on these results you are included " + maxStunting;
                } else {
                    tvDescription.text = "Invalid percentage value. Enter a value between 0 and 100.";
                }

            }
        }
    }
}