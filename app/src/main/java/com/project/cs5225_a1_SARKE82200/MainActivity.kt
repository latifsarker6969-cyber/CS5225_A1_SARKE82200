package com.project.cs5225_a1_SARKE82200

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.cs5225_a1_SARKE82200.data.MealRepository
import com.project.cs5225_a1_SARKE82200.data.RetrofitInstance
import com.project.cs5225_a1_SARKE82200.model.MealSummary
import com.project.cs5225_a1_SARKE82200.ui.MealAdapter
import com.project.cs5225_a1_SARKE82200.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val repo = MealRepository(RetrofitInstance.api)
    private var isRequestRunning = false
    private lateinit var adapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MealAdapter(emptyList()) { summary -> openDetails(summary) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadCategoriesIntoSpinner()
        binding.btnGo.setOnClickListener {
            if (!isRequestRunning) fetchMeals()
        }
    }

    private fun loadCategoriesIntoSpinner() {
        repo.getCategories().enqueue(object : Callback<com.project.cs5225_a1_SARKE82200.model.CategoryResponse> {
            override fun onResponse(call: Call<com.project.cs5225_a1_SARKE82200.model.CategoryResponse>, response: Response<com.project.cs5225_a1_SARKE82200.model.CategoryResponse>) {
                val cats = response.body()?.meals?.mapNotNull { it.strCategory } ?: listOf("Chicken", "Beef", "Seafood")
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, cats)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter
            }

            override fun onFailure(call: Call<com.project.cs5225_a1_SARKE82200.model.CategoryResponse>, t: Throwable) {
                val fallback = listOf("Chicken", "Beef", "Seafood", "Pasta", "Vegetarian")
                val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, fallback)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerCategory.adapter = adapter
                binding.txtStatus.text = "Failed to load categories"
            }
        })
    }

    private fun fetchMeals() {
        val category = binding.spinnerCategory.selectedItem?.toString() ?: return
        isRequestRunning = true
        binding.btnGo.isEnabled = false
        binding.txtStatus.text = "Loading..."
        binding.progress.visibility = View.VISIBLE

        repo.getMealsByCategory(category).enqueue(object : Callback<com.project.cs5225_a1_SARKE82200.model.MealListResponse> {
            override fun onResponse(call: Call<com.project.cs5225_a1_SARKE82200.model.MealListResponse>, response: Response<com.project.cs5225_a1_SARKE82200.model.MealListResponse>) {
                binding.progress.visibility = View.GONE
                isRequestRunning = false
                binding.btnGo.isEnabled = true

                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    binding.txtStatus.text = "Loaded ${meals.size} meals"
                    adapter.updateData(meals)
                } else {
                    binding.txtStatus.text = "No results"
                    adapter.updateData(emptyList())
                }
            }

            override fun onFailure(call: Call<com.project.cs5225_a1_SARKE82200.model.MealListResponse>, t: Throwable) {
                binding.progress.visibility = View.GONE
                isRequestRunning = false
                binding.btnGo.isEnabled = true
                binding.txtStatus.text = "Request failed: ${t.localizedMessage ?: "error"}"
                adapter.updateData(emptyList())
            }
        })
    }

    private fun openDetails(summary: MealSummary) {
        val id = summary.idMeal ?: return
        val i = Intent(this, DetailsActivity::class.java)
        i.putExtra("meal_id", id)
        startActivity(i)
    }
}
