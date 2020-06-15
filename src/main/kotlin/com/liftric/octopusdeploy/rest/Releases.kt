package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Release
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Releases {
    //Release
    @GET("/api/releases/{releaseId}")
    fun get(@Path("releaseId") releaseId: String): Call<Release>
}