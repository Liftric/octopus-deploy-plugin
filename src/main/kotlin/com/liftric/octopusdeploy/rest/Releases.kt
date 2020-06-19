package com.liftric.octopusdeploy.rest

import com.liftric.octopusdeploy.api.Release
import com.liftric.octopusdeploy.api.ReleasesPaginatedResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.UnsupportedEncodingException
import java.net.URL
import java.net.URLDecoder
import java.util.*

interface Releases {
    @GET("/api/releases/{releaseId}")
    fun getByReleaseId(@Path("releaseId") releaseId: String): Call<Release>

    @GET("/api/projects/{projectId}/releases")
    fun getByProjectFirstPage(@Path("projectId") projectId: String): Call<ReleasesPaginatedResult>

    @GET("/api/projects/{projectId}/releases")
    fun getByProjectAtPage(
        @Path("projectId") projectId: String,
        @Query("skip") skip: Long,
        @Query("take") take: Long
    ): Call<ReleasesPaginatedResult>

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

fun Releases.getByProjectIdPaginated(projectId: String): List<Release> {
    val result = mutableListOf<Release>()
    var currentResults = getByProjectFirstPage(projectId).execute().body() ?: error("getAllFirstPage failed!")
    result.addAll(currentResults.items)
    while (currentResults.links.pageNext != null) {
        val (nextSkip, nextTake) = nextSkipAndTake(currentResults)
        currentResults =
            getByProjectAtPage(projectId, nextSkip, nextTake).execute().body() ?: error("getAllAtPage failed!")
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
    val params = splitQuery(currentResults.links.pageNext)
    val nextSkip = params.getValue("skip").first().toLong()
    val nextTake = params.getValue("take").first().toLong()
    return Pair(nextSkip, nextTake)
}

/**
 * https://stackoverflow.com/a/13592567
 */
@Throws(UnsupportedEncodingException::class)
fun splitQuery(url: String?): MutableMap<String, MutableList<String>> {
    val tmpUrl = URL("https://test.com$url")
    val queryPairs: MutableMap<String, MutableList<String>> =
        LinkedHashMap()
    val pairs = tmpUrl.query.split("&".toRegex()).toTypedArray()
    pairs.forEach { pair ->
        val idx = pair.indexOf("=")
        val key = if (idx > 0) URLDecoder.decode(pair.substring(0, idx), "UTF-8") else pair
        if (!queryPairs.containsKey(key)) {
            queryPairs[key] = LinkedList()
        }
        val value =
            if (idx > 0 && pair.length > idx + 1) URLDecoder.decode(pair.substring(idx + 1), "UTF-8") else null
        value?.let {
            queryPairs[key]?.add(it)
        }
    }
    return queryPairs
}
