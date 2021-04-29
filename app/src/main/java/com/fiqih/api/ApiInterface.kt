package com.fiqih.api


import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @GET("/latihanAPI/public/api/ceo")
    fun getCEOs(): Call<ArrayList<CEOModel>>

    @POST("latihanAPI/public/api/ceo")
    fun addCEO(@Body newCEOModel : CEOModel) : Call<CEOModel>
}