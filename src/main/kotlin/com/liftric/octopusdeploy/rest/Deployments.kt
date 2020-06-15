package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Deployment
import com.liftric.octopusdeploy.api.DeploymentsPaginated
import org.gradle.internal.impldep.org.apache.http.client.utils.URLEncodedUtils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.nio.charset.Charset

interface Deployments {
    @GET("/api/deployments")
    fun getAllFirstPage(): Call<DeploymentsPaginated>

    @GET("/api/deployments")
    fun getAllAtPage(@Query("skip") skip: Long, @Query("take") take: Long): Call<DeploymentsPaginated>

    //
    @GET("/api/deployments")
    fun getFilteredFirstPage(@Query("projects") projectId: String, @Query("environments") environmentId: String): Call<DeploymentsPaginated>

    @GET("/api/deployments")
    fun getFilteredAtPage(
        @Query("projects") projectId: String, @Query("environments") environmentId: String, @Query("skip") skip: Long, @Query(
            "take"
        ) take: Long
    ): Call<DeploymentsPaginated>
}

fun Deployments.getAllPaginated(): List<Deployment> {
    val result = mutableListOf<Deployment>()
    var currentResults = getAllFirstPage().execute().body() ?: error("getAllFirstPage failed!")
    result.addAll(currentResults.items)
    while (currentResults.links.pageNext != null) {
        val (nextSkip, nextTake) = nextSkipAndTake(currentResults)
        currentResults = getAllAtPage(nextSkip, nextTake).execute().body() ?: error("getAllAtPage failed!")
        result.addAll(currentResults.items)
    }
    return result
}

fun Deployments.getFilteredPaginated(projectId: String, environmentId: String): List<Deployment> {
    val result = mutableListOf<Deployment>()
    var currentResults =
        getFilteredFirstPage(projectId, environmentId).execute().body() ?: error("getAllFirstPage failed!")
    result.addAll(currentResults.items)
    while (currentResults.links.pageNext != null) {
        val (nextSkip, nextTake) = nextSkipAndTake(currentResults)
        currentResults = getFilteredAtPage(projectId, environmentId, nextSkip, nextTake).execute().body()
            ?: error("getAllAtPage failed!")
        result.addAll(currentResults.items)
    }
    return result
}

private fun nextSkipAndTake(currentResults: DeploymentsPaginated): Pair<Long, Long> {
    val params = URLEncodedUtils.parse(currentResults.links.pageNext!!.split("?")[1], Charset.defaultCharset())
    val nextSkip = params.first { it.name == "skip" }.value.toLong()
    val nextTake = params.first { it.name == "take" }.value.toLong()
    return Pair(nextSkip, nextTake)
}