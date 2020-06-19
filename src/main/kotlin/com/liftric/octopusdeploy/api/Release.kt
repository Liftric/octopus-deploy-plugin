package com.liftric.octopusdeploy.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.liftric.octopusdeploy.rest.mapper

data class ReleasesPaginatedResult(
    @get:JsonProperty("ItemType", required = true) @field:JsonProperty("ItemType", required = true)
    val itemType: String,

    @get:JsonProperty("TotalResults", required = true) @field:JsonProperty("TotalResults", required = true)
    val totalResults: Long,

    @get:JsonProperty("ItemsPerPage", required = true) @field:JsonProperty("ItemsPerPage", required = true)
    val itemsPerPage: Long,

    @get:JsonProperty("NumberOfPages", required = true) @field:JsonProperty("NumberOfPages", required = true)
    val numberOfPages: Long,

    @get:JsonProperty("LastPageNumber", required = true) @field:JsonProperty("LastPageNumber", required = true)
    val lastPageNumber: Long,

    @get:JsonProperty("Items", required = true) @field:JsonProperty("Items", required = true)
    val items: List<Release>,

    @get:JsonProperty("Links", required = true) @field:JsonProperty("Links", required = true)
    val links: PaginatedLinks
) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<ReleasesPaginatedResult>(json)
    }
}

data class Release (
    @get:JsonProperty("Id", required=true)@field:JsonProperty("Id", required=true)
    val id: String,

    @get:JsonProperty("Version", required=true)@field:JsonProperty("Version", required=true)
    val version: String,

    @get:JsonProperty("ChannelId", required=true)@field:JsonProperty("ChannelId", required=true)
    val channelID: String,

    @get:JsonProperty("ReleaseNotes")@field:JsonProperty("ReleaseNotes")
    val releaseNotes: String? = null,

    @get:JsonProperty("ProjectDeploymentProcessSnapshotId", required=true)@field:JsonProperty("ProjectDeploymentProcessSnapshotId", required=true)
    val projectDeploymentProcessSnapshotID: String,

    @get:JsonProperty("IgnoreChannelRules", required=true)@field:JsonProperty("IgnoreChannelRules", required=true)
    val ignoreChannelRules: Boolean,

    @get:JsonProperty("BuildInformation", required=true)@field:JsonProperty("BuildInformation", required=true)
    val buildInformation: List<BuildInformation>,

    @get:JsonProperty("Assembled", required=true)@field:JsonProperty("Assembled", required=true)
    val assembled: String,

    @get:JsonProperty("ProjectId", required=true)@field:JsonProperty("ProjectId", required=true)
    val projectID: String,

    @get:JsonProperty("LibraryVariableSetSnapshotIds", required=true)@field:JsonProperty("LibraryVariableSetSnapshotIds", required=true)
    val libraryVariableSetSnapshotIDS: List<String>,

    @get:JsonProperty("SelectedPackages", required=true)@field:JsonProperty("SelectedPackages", required=true)
    val selectedPackages: List<SelectedPackage>,

    @get:JsonProperty("ProjectVariableSetSnapshotId", required=true)@field:JsonProperty("ProjectVariableSetSnapshotId", required=true)
    val projectVariableSetSnapshotID: String,

    @get:JsonProperty("SpaceId", required=true)@field:JsonProperty("SpaceId", required=true)
    val spaceID: String,

    @get:JsonProperty("Links", required=true)@field:JsonProperty("Links", required=true)
    val links: ReleaseLinks
) {
    fun toJson() = mapper.writeValueAsString(this)
    override fun toString(): String {
        return "Release(id='$id', version='$version', channelID='$channelID', projectDeploymentProcessSnapshotID='$projectDeploymentProcessSnapshotID', ignoreChannelRules=$ignoreChannelRules, assembled='$assembled', projectID='$projectID', libraryVariableSetSnapshotIDS=$libraryVariableSetSnapshotIDS, selectedPackages=$selectedPackages, projectVariableSetSnapshotID='$projectVariableSetSnapshotID', spaceID='$spaceID', links=$links)"
    }

    companion object {
        fun fromJson(json: String) = mapper.readValue<Release>(json)
    }

}

data class SelectedPackage (
    @get:JsonProperty("StepName", required=true)@field:JsonProperty("StepName", required=true)
    val stepName: String,

    @get:JsonProperty("ActionName", required=true)@field:JsonProperty("ActionName", required=true)
    val actionName: String,

    @get:JsonProperty("Version", required=true)@field:JsonProperty("Version", required=true)
    val version: String,

    @get:JsonProperty("PackageReferenceName", required=true)@field:JsonProperty("PackageReferenceName", required=true)
    val packageReferenceName: String
)


data class ReleaseLinks (
    @get:JsonProperty("Self", required=true)@field:JsonProperty("Self", required=true)
    val self: String,

    @get:JsonProperty("Project", required=true)@field:JsonProperty("Project", required=true)
    val project: String,

    @get:JsonProperty("Progression", required=true)@field:JsonProperty("Progression", required=true)
    val progression: String,

    @get:JsonProperty("Deployments", required=true)@field:JsonProperty("Deployments", required=true)
    val deployments: String,

    @get:JsonProperty("DeploymentTemplate", required=true)@field:JsonProperty("DeploymentTemplate", required=true)
    val deploymentTemplate: String,

    @get:JsonProperty("Artifacts", required=true)@field:JsonProperty("Artifacts", required=true)
    val artifacts: String,

    @get:JsonProperty("ProjectVariableSnapshot", required=true)@field:JsonProperty("ProjectVariableSnapshot", required=true)
    val projectVariableSnapshot: String,

    @get:JsonProperty("ProjectDeploymentProcessSnapshot", required=true)@field:JsonProperty("ProjectDeploymentProcessSnapshot", required=true)
    val projectDeploymentProcessSnapshot: String,

    @get:JsonProperty("Web", required=true)@field:JsonProperty("Web", required=true)
    val web: String,

    @get:JsonProperty("SnapshotVariables", required=true)@field:JsonProperty("SnapshotVariables", required=true)
    val snapshotVariables: String,

    @get:JsonProperty("Defects", required=true)@field:JsonProperty("Defects", required=true)
    val defects: String,

    @get:JsonProperty("ReportDefect", required=true)@field:JsonProperty("ReportDefect", required=true)
    val reportDefect: String,

    @get:JsonProperty("ResolveDefect", required=true)@field:JsonProperty("ResolveDefect", required=true)
    val resolveDefect: String,

    @get:JsonProperty("DeploymentPreviews", required=true)@field:JsonProperty("DeploymentPreviews", required=true)
    val deploymentPreviews: String
)
