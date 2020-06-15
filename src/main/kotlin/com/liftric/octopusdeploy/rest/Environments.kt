package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Environment
import retrofit2.Call
import retrofit2.http.GET

interface Environments {
    @GET("/api/environments/all")
    fun getAll(): Call<List<Environment>>
}