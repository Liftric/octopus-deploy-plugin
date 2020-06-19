package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Project
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Projects {
    @GET("/api/projects/all")
    fun getAll(): Call<List<Project>>

    @GET("/api/projects/{idOrSlug}")
    fun get(@Path("idOrSlug") idOrSlug: String): Call<Project>

}