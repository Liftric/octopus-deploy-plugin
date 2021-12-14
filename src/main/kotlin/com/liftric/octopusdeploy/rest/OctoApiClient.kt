package com.liftric.octopusdeploy.rest

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val mapper = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
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
