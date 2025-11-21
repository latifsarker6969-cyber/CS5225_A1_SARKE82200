package com.project.cs5225_a1_SARKE82200.model

data class CategoryResponse(val meals: List<CategoryItem>?)
data class CategoryItem(val strCategory: String?)

data class MealListResponse(val meals: List<MealSummary>?)
data class MealSummary(
    val idMeal: String?,
    val strMeal: String?,
    val strMealThumb: String?
)

data class MealDetailResponse(val meals: List<MealDetail>?)
data class MealDetail(
    val idMeal: String?,
    val strMeal: String?,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strMealThumb: String?
)
