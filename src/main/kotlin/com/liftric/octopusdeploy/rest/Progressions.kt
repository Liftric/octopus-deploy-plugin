package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Progression
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Progressions {
    @GET("/api/releases/{releaseId}/progression")
    fun get(@Path("releaseId") releaseId: String): Call<Progression>
}