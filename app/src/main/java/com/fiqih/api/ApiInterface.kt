package com.fiqih.api

import android.telecom.Call

import retrofit2.http.*


interface ApiInterface {
    @GET("/latihanAPI/public/api/ceo")
    fun getCEOs(): retrofit2.Call<ArrayList<CEOModel>>
}