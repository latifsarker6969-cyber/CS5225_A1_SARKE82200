package com.project.cs5225_a1_SARKE82200.data

import com.project.cs5225_a1_SARKE82200.model.MealDetailResponse
import com.project.cs5225_a1_SARKE82200.model.MealListResponse
import com.project.cs5225_a1_SARKE82200.model.CategoryResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("list.php?c=list")
    fun getCategories(): Call<CategoryResponse>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") category: String): Call<MealListResponse>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealDetailResponse>
}
