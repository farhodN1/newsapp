package com.example.news.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    suspend fun searchNews(
        @Query("apiKey") apiKey: String,
        @Query("category") category: String
    ): Response<NewsResponse>

    @GET("everything")
    suspend fun searchNewsForSearch(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String,
    ): Response<NewsResponse>
}