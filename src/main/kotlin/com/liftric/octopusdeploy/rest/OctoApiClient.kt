package com.liftric.octopusdeploy.rest

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.gradle.internal.impldep.org.apache.http.client.utils.URLEncodedUtils
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.nio.charset.Charset

val mapper = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

class OctoApiClient(
    private val baseUrl: String,
    private val apiKey: String,
    private val logLevel: HttpLoggingInterceptor.Level
) {
    private val apiKeyHeaderName = "X-Octopus-ApiKey"
    private val httpClient by lazy {
        OkHttpClient.Builder().addInterceptor { chain ->
            val original = chain.request()
            val newRequest = original.newBuilder()
                .addHeader(
                    apiKeyHeaderName,
                    apiKey
                )
            chain.proceed(newRequest.build())
        }.addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                println(message)
            }
        }).apply {
            redactHeader(apiKeyHeaderName)
            setLevel(logLevel)
        }).build()
    }
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl.removeSuffix("/"))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(mapper))
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return retrofitBuilder.client(httpClient).build().create(serviceClass)
    }

    fun shutdown() {
        httpClient.dispatcher.executorService.shutdownNow()
        httpClient.connectionPool.evictAll()
    }
}


fun main() {
    val octoApiClient = OctoApiClient(
        "<redacted>",
        "<redacted>",
        HttpLoggingInterceptor.Level.BASIC
    )
    val projects = octoApiClient.createService(Projects::class.java)
    projects.getAll().execute().body()?.forEach { println(it.slug) } ?: error("getAll failed!")
    projects.get("atlantis").execute().body()?.let { println(it.id) }

    val environments = octoApiClient.createService(Environments::class.java)
    environments.getAll().execute().body()!!.forEach { println(it) }

    val deployments = octoApiClient.createService(Deployments::class.java)
    deployments.getAllFirstPage().execute().body()!!.let { println(it) }

    println()
    println()
    println()
    URLEncodedUtils.parse("/api/Spaces-1/deployments?skip=30&take=30".split("?")[1], Charset.defaultCharset()).forEach {
        println(it)
    }
    println()
    println()
    deployments.getAllPaginated().forEach { println("${it.releaseID}") }
    println()
    println()
    println()
    println()
    println()

    val releases = octoApiClient.createService(Releases::class.java)
    val progressions = octoApiClient.createService(Progressions::class.java)

    val preventisPortal = projects.get("preventis-portal").execute().body()!!
    val devEnvironment = environments.getAll().execute().body()!!.first { it.name == "dev" }

    val preventisPortalDevDeployments = deployments.getFilteredPaginated(preventisPortal.id, devEnvironment.id)
    println()
    preventisPortalDevDeployments.forEach {
        println(it)
    }

    val releaseId =
        preventisPortalDevDeployments.first { it.links.release.contains("Releases-350") }.links.extractReleaseId()
    println(releaseId)
    println(releases.get(releaseId).execute().body()!!)
    println(progressions.get(releaseId).execute().body()!!)

    println()
    println()
    println()
    println()
    releases.allFiltered("portal-backend", "0.0.52")
        .forEach {
            println(it.id)
            println(it.version)
            println(it.links.progression)
            val progression = progressions.get(it.id).execute().body()!!
            println(progression.phases.map { "${it.name}: ${it.deployments.map { it.task!!.isCompleted }}" })
            println("anyOngoingTask: ${progression.anyOngoingTask()}")
        }
    octoApiClient.shutdown()
}
