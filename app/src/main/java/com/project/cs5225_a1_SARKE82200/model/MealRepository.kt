package com.project.cs5225_a1_SARKE82200.data

class MealRepository(private val api: MealApi = RetrofitInstance.api) {

    fun getCategories() = api.getCategories()

    fun getMealsByCategory(category: String) = api.getMealsByCategory(category)

    fun getMealDetails(id: String) = api.getMealDetails(id)
}
