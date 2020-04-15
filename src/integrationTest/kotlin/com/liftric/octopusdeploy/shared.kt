package com.liftric.octopusdeploy

import com.liftric.octopusdeploy.api.BuildInformationResponse
import com.liftric.octopusdeploy.api.PackageResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils

val serverUrl = "http://localhost:8080"
val apiKey = "API-TESTTEST123TRESDTSDD"

val client = HttpClients.createDefault()
fun getBuildInformationResponse(): BuildInformationResponse {
    val response = client.execute(HttpGet("$serverUrl/api/build-information").apply {
        addHeader("accept", "application/json")
        addHeader("X-Octopus-ApiKey", apiKey)
    })
    val responsePayload = EntityUtils.toString(response.entity)
    return BuildInformationResponse.fromJson(responsePayload)
}
fun getPackageResponse(): PackageResponse {
    val response = client.execute(HttpGet("$serverUrl/api/packages").apply {
        addHeader("accept", "application/json")
        addHeader("X-Octopus-ApiKey", apiKey)
    })
    val responsePayload = EntityUtils.toString(response.entity)
    return PackageResponse.fromJson(responsePayload)
}