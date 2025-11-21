package com.project.cs5225_a1_SARKE82200

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.project.cs5225_a1_SARKE82200.data.MealRepository
import com.project.cs5225_a1_SARKE82200.data.RetrofitInstance
import com.project.cs5225_a1_SARKE82200.databinding.ActivityDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val repo = MealRepository(RetrofitInstance.api)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("meal_id")
        if (id == null) {
            Toast.makeText(this, "No meal id", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.progress.visibility = View.VISIBLE
        repo.getMealDetails(id).enqueue(object : Callback<com.project.cs5225_a1_SARKE82200.model.MealDetailResponse> {
            override fun onResponse(call: Call<com.project.cs5225_a1_SARKE82200.model.MealDetailResponse>, response: Response<com.project.cs5225_a1_SARKE82200.model.MealDetailResponse>) {
                binding.progress.visibility = View.GONE
                if (response.isSuccessful) {
                    val detail = response.body()?.meals?.firstOrNull()
                    if (detail != null) {
                        binding.txtTitle.text = detail.strMeal ?: "Unknown"
                        binding.txtCategory.text = "Category: ${detail.strCategory ?: "N/A"}"
                        binding.txtArea.text = "Area: ${detail.strArea ?: "N/A"}"
                        binding.txtInstructions.text = detail.strInstructions ?: ""
                        binding.imgDetail.load(detail.strMealThumb)
                    } else {
                        Toast.makeText(this@DetailsActivity, "No detail available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DetailsActivity, "Failed to load details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.project.cs5225_a1_SARKE82200.model.MealDetailResponse>, t: Throwable) {
                binding.progress.visibility = View.GONE
                Toast.makeText(this@DetailsActivity, "Request failed: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
