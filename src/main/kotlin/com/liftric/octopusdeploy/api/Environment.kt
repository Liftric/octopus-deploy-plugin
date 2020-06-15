package com.liftric.octopusdeploy.api

import com.fasterxml.jackson.annotation.JsonProperty

data class Environment(
    @get:JsonProperty("Id", required = true) @field:JsonProperty("Id", required = true)
    val id: String,

    @get:JsonProperty("Name", required = true) @field:JsonProperty("Name", required = true)
    val name: String,

    @get:JsonProperty("Description", required = true) @field:JsonProperty("Description", required = true)
    val description: String,

    @get:JsonProperty("SortOrder", required = true) @field:JsonProperty("SortOrder", required = true)
    val sortOrder: Long,

    @get:JsonProperty("UseGuidedFailure", required = true) @field:JsonProperty("UseGuidedFailure", required = true)
    val useGuidedFailure: Boolean,

    @get:JsonProperty("AllowDynamicInfrastructure", required = true) @field:JsonProperty(
        "AllowDynamicInfrastructure",
        required = true
    )
    val allowDynamicInfrastructure: Boolean,

    @get:JsonProperty("SpaceId", required = true) @field:JsonProperty("SpaceId", required = true)
    val spaceID: String,

    @get:JsonProperty("ExtensionSettings", required = true) @field:JsonProperty("ExtensionSettings", required = true)
    val extensionSettings: List<Any?>,

    @get:JsonProperty("Links", required = true) @field:JsonProperty("Links", required = true)
    val links: EnvironmentLinks
)

data class EnvironmentLinks(
    @get:JsonProperty("Self", required = true) @field:JsonProperty("Self", required = true)
    val self: String,

    @get:JsonProperty("Machines", required = true) @field:JsonProperty("Machines", required = true)
    val machines: String,

    @get:JsonProperty("SinglyScopedVariableDetails", required = true) @field:JsonProperty(
        "SinglyScopedVariableDetails",
        required = true
    )
    val singlyScopedVariableDetails: String,

    @get:JsonProperty("Metadata", required = true) @field:JsonProperty("Metadata", required = true)
    val metadata: String
)