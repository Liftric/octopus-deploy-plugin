package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Deployment
import com.liftric.octopusdeploy.api.DeploymentsPaginatedResult
import com.liftric.octopusdeploy.api.Release
import com.liftric.octopusdeploy.api.ReleasesPaginatedResult
import org.gradle.internal.impldep.org.apache.http.client.utils.URLEncodedUtils
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.nio.charset.Charset

interface Releases {
    @GET("/api/releases/{releaseId}")
    fun get(@Path("releaseId") releaseId: String): Call<Release>

    @GET("/api/releases")
    fun getAllFirstPage(): Call<ReleasesPaginatedResult>

    @GET("/api/releases")
    fun getAllAtPage(@Query("skip") skip: Long, @Query("take") take: Long): Call<ReleasesPaginatedResult>
}

fun Releases.getAllPaginated(): List<Release> {
    val result = mutableListOf<Release>()
    var currentResults = getAllFirstPage().execute().body() ?: error("getAllFirstPage failed!")
    result.addAll(currentResults.items)
    while (currentResults.links.pageNext != null) {
        val (nextSkip, nextTake) = nextSkipAndTake(currentResults)
        currentResults = getAllAtPage(nextSkip, nextTake).execute().body() ?: error("getAllAtPage failed!")
        result.addAll(currentResults.items)
    }
    return result
}

fun Releases.allFiltered(packageID: String, version: String): List<Release> = getAllPaginated()
    .catchingFilter {
        it.buildInformation.first().packageID == packageID
                && it.buildInformation.first().version == version
    }

/**
 * handle exceptions as false
 */
inline fun <T> Iterable<T>.catchingFilter(predicate: (T) -> Boolean): List<T> {
    return filter {
        try {
            predicate(it)
        } catch (e: Exception) {
            false
        }
    }
}

private fun nextSkipAndTake(currentResults: ReleasesPaginatedResult): Pair<Long, Long> {
    val params = URLEncodedUtils.parse(currentResults.links.pageNext!!.split("?")[1], Charset.defaultCharset())
    val nextSkip = params.first { it.name == "skip" }.value.toLong()
    val nextTake = params.first { it.name == "take" }.value.toLong()
    return Pair(nextSkip, nextTake)
}
