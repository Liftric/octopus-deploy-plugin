package com.liftric.octopusdeploy.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue


@Suppress("UNCHECKED_CAST")
private fun <T> ObjectMapper.convert(k: kotlin.reflect.KClass<*>, fromJson: (JsonNode) -> T, toJson: (T) -> String, isUnion: Boolean = false) = registerModule(SimpleModule().apply {
    addSerializer(k.java as Class<T>, object : StdSerializer<T>(k.java as Class<T>) {
        override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) = gen.writeRawValue(toJson(value))
    })
    addDeserializer(k.java as Class<T>, object : StdDeserializer<T>(k.java as Class<T>) {
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) = fromJson(p.readValueAsTree())
    })
})

val mapper = jacksonObjectMapper().apply {
    propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

data class BuildInformationResponse (
    @get:JsonProperty("ItemType")@field:JsonProperty("ItemType")
    val itemType: String? = null,

    @get:JsonProperty("TotalResults")@field:JsonProperty("TotalResults")
    val totalResults: Long? = null,

    @get:JsonProperty("ItemsPerPage")@field:JsonProperty("ItemsPerPage")
    val itemsPerPage: Long? = null,

    @get:JsonProperty("NumberOfPages")@field:JsonProperty("NumberOfPages")
    val numberOfPages: Long? = null,

    @get:JsonProperty("LastPageNumber")@field:JsonProperty("LastPageNumber")
    val lastPageNumber: Long? = null,

    @get:JsonProperty("Items")@field:JsonProperty("Items")
    val items: List<Item>? = null,

    @get:JsonProperty("Links")@field:JsonProperty("Links")
    val links: BuildInformationResponseLinks? = null
) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<BuildInformationResponse>(json)
    }
}

data class Commit (
    @get:JsonProperty("Id")@field:JsonProperty("Id")
    val id: String? = null,

    @get:JsonProperty("LinkUrl")@field:JsonProperty("LinkUrl")
    val linkURL: Any? = null,

    @get:JsonProperty("Comment")@field:JsonProperty("Comment")
    val comment: String? = null
)

typealias ItemLinks = JsonNode

data class BuildInformationResponseLinks (
    @get:JsonProperty("Self")@field:JsonProperty("Self")
    val self: String? = null,

    @get:JsonProperty("Template")@field:JsonProperty("Template")
    val template: String? = null,

    @get:JsonProperty("Page.All")@field:JsonProperty("Page.All")
    val pageAll: String? = null,

    @get:JsonProperty("Page.Current")@field:JsonProperty("Page.Current")
    val pageCurrent: String? = null,

    @get:JsonProperty("Page.Last")@field:JsonProperty("Page.Last")
    val pageLast: String? = null
)
data class PackageResponse (
    @get:JsonProperty("ItemType")@field:JsonProperty("ItemType")
    val itemType: String? = null,

    @get:JsonProperty("TotalResults")@field:JsonProperty("TotalResults")
    val totalResults: Long? = null,

    @get:JsonProperty("ItemsPerPage")@field:JsonProperty("ItemsPerPage")
    val itemsPerPage: Long? = null,

    @get:JsonProperty("NumberOfPages")@field:JsonProperty("NumberOfPages")
    val numberOfPages: Long? = null,

    @get:JsonProperty("LastPageNumber")@field:JsonProperty("LastPageNumber")
    val lastPageNumber: Long? = null,

    @get:JsonProperty("Items")@field:JsonProperty("Items")
    val items: List<Item>? = null,

    @get:JsonProperty("Links")@field:JsonProperty("Links")
    val links: PackageResponseLinks? = null
) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<PackageResponse>(json)
    }
}

data class Item (
    @get:JsonProperty("Id")@field:JsonProperty("Id")
    val id: String? = null,

    @get:JsonProperty("PackageSizeBytes")@field:JsonProperty("PackageSizeBytes")
    val packageSizeBytes: Long? = null,

    @get:JsonProperty("Hash")@field:JsonProperty("Hash")
    val hash: String? = null,

    @get:JsonProperty("NuGetPackageId")@field:JsonProperty("NuGetPackageId")
    val nuGetPackageID: String? = null,

    @get:JsonProperty("PackageId")@field:JsonProperty("PackageId")
    val packageID: String? = null,

    @get:JsonProperty("NuGetFeedId")@field:JsonProperty("NuGetFeedId")
    val nuGetFeedID: String? = null,

    @get:JsonProperty("FeedId")@field:JsonProperty("FeedId")
    val feedID: String? = null,

    @get:JsonProperty("Title")@field:JsonProperty("Title")
    val title: String? = null,

    @get:JsonProperty("Summary")@field:JsonProperty("Summary")
    val summary: Any? = null,

    @get:JsonProperty("Version")@field:JsonProperty("Version")
    val version: String? = null,

    @get:JsonProperty("Description")@field:JsonProperty("Description")
    val description: Any? = null,

    @get:JsonProperty("Published")@field:JsonProperty("Published")
    val published: String? = null,

    @get:JsonProperty("ReleaseNotes")@field:JsonProperty("ReleaseNotes")
    val releaseNotes: Any? = null,

    @get:JsonProperty("FileExtension")@field:JsonProperty("FileExtension")
    val fileExtension: String? = null,

    @get:JsonProperty("PackageVersionBuildInformation")@field:JsonProperty("PackageVersionBuildInformation")
    val packageVersionBuildInformation: PackageVersionBuildInformation? = null,

    @get:JsonProperty("Links")@field:JsonProperty("Links")
    val links: ItemLinks? = null,

    @get:JsonProperty("BuildEnvironment")@field:JsonProperty("BuildEnvironment")
    val buildEnvironment: Any? = null,

    @get:JsonProperty("BuildNumber")@field:JsonProperty("BuildNumber")
    val buildNumber: Any? = null,

    @get:JsonProperty("BuildUrl")@field:JsonProperty("BuildUrl")
    val buildURL: Any? = null,

    @get:JsonProperty("Branch")@field:JsonProperty("Branch")
    val branch: Any? = null,

    @get:JsonProperty("VcsType")@field:JsonProperty("VcsType")
    val vcsType: String? = null,

    @get:JsonProperty("VcsRoot")@field:JsonProperty("VcsRoot")
    val vcsRoot: Any? = null,

    @get:JsonProperty("VcsCommitNumber")@field:JsonProperty("VcsCommitNumber")
    val vcsCommitNumber: Any? = null,

    @get:JsonProperty("VcsCommitUrl")@field:JsonProperty("VcsCommitUrl")
    val vcsCommitURL: Any? = null,

    @get:JsonProperty("IssueTrackerName")@field:JsonProperty("IssueTrackerName")
    val issueTrackerName: Any? = null,

    @get:JsonProperty("WorkItems")@field:JsonProperty("WorkItems")
    val workItems: List<Any?>? = null,

    @get:JsonProperty("Commits")@field:JsonProperty("Commits")
    val commits: List<Commit>? = null,

    @get:JsonProperty("IncompleteDataWarning")@field:JsonProperty("IncompleteDataWarning")
    val incompleteDataWarning: Any? = null,

    @get:JsonProperty("Created")@field:JsonProperty("Created")
    val created: String? = null
)

data class PackageVersionBuildInformation (
    @get:JsonProperty("Id")@field:JsonProperty("Id")
    val id: String? = null,

    @get:JsonProperty("PackageId")@field:JsonProperty("PackageId")
    val packageID: String? = null,

    @get:JsonProperty("Version")@field:JsonProperty("Version")
    val version: String? = null,

    @get:JsonProperty("BuildEnvironment")@field:JsonProperty("BuildEnvironment")
    val buildEnvironment: Any? = null,

    @get:JsonProperty("BuildNumber")@field:JsonProperty("BuildNumber")
    val buildNumber: Any? = null,

    @get:JsonProperty("BuildUrl")@field:JsonProperty("BuildUrl")
    val buildURL: Any? = null,

    @get:JsonProperty("Branch")@field:JsonProperty("Branch")
    val branch: Any? = null,

    @get:JsonProperty("VcsType")@field:JsonProperty("VcsType")
    val vcsType: String? = null,

    @get:JsonProperty("VcsRoot")@field:JsonProperty("VcsRoot")
    val vcsRoot: Any? = null,

    @get:JsonProperty("VcsCommitNumber")@field:JsonProperty("VcsCommitNumber")
    val vcsCommitNumber: Any? = null,

    @get:JsonProperty("VcsCommitUrl")@field:JsonProperty("VcsCommitUrl")
    val vcsCommitURL: Any? = null,

    @get:JsonProperty("IssueTrackerName")@field:JsonProperty("IssueTrackerName")
    val issueTrackerName: Any? = null,

    @get:JsonProperty("WorkItems")@field:JsonProperty("WorkItems")
    val workItems: List<Any?>? = null,

    @get:JsonProperty("Commits")@field:JsonProperty("Commits")
    val commits: List<Commit>? = null,

    @get:JsonProperty("IncompleteDataWarning")@field:JsonProperty("IncompleteDataWarning")
    val incompleteDataWarning: Any? = null,

    @get:JsonProperty("Created")@field:JsonProperty("Created")
    val created: String? = null,

    @get:JsonProperty("Links")@field:JsonProperty("Links")
    val links: PackageVersionBuildInformationLinks? = null
)

typealias PackageVersionBuildInformationLinks = JsonNode

data class PackageResponseLinks (
    @get:JsonProperty("Self")@field:JsonProperty("Self")
    val self: String? = null,

    @get:JsonProperty("Template")@field:JsonProperty("Template")
    val template: String? = null,

    @get:JsonProperty("Page.All")@field:JsonProperty("Page.All")
    val pageAll: String? = null,

    @get:JsonProperty("Page.Current")@field:JsonProperty("Page.Current")
    val pageCurrent: String? = null,

    @get:JsonProperty("Page.Last")@field:JsonProperty("Page.Last")
    val pageLast: String? = null
)