package com.liftric.octopusdeploy.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.readValue
import com.liftric.octopusdeploy.rest.mapper

data class DeploymentsPaginated(
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
    val items: List<Deployment>,

    @get:JsonProperty("Links", required = true) @field:JsonProperty("Links", required = true)
    val links: DeploymentsPaginatedLinks
) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<DeploymentsPaginated>(json)
    }
}

data class Deployment(
    @get:JsonProperty("Id", required = true) @field:JsonProperty("Id", required = true)
    val id: String,

    @get:JsonProperty("ReleaseId", required = true) @field:JsonProperty("ReleaseId", required = true)
    val releaseID: String,

    @get:JsonProperty("ChannelId", required = true) @field:JsonProperty("ChannelId", required = true)
    val channelID: String,

    @get:JsonProperty("DeploymentProcessId", required = true) @field:JsonProperty(
        "DeploymentProcessId",
        required = true
    )
    val deploymentProcessID: String,

    @get:JsonProperty("Changes", required = true) @field:JsonProperty("Changes", required = true)
    val changes: List<Change>,

    @get:JsonProperty("ChangesMarkdown", required = true) @field:JsonProperty("ChangesMarkdown", required = true)
    val changesMarkdown: String,

    @get:JsonProperty("EnvironmentId", required = true) @field:JsonProperty("EnvironmentId", required = true)
    val environmentID: String,

    @get:JsonProperty("TenantId") @field:JsonProperty("TenantId")
    val tenantID: Any? = null,

    @get:JsonProperty("ForcePackageDownload", required = true) @field:JsonProperty(
        "ForcePackageDownload",
        required = true
    )
    val forcePackageDownload: Boolean,

    @get:JsonProperty("ForcePackageRedeployment", required = true) @field:JsonProperty(
        "ForcePackageRedeployment",
        required = true
    )
    val forcePackageRedeployment: Boolean,

    @get:JsonProperty("SkipActions", required = true) @field:JsonProperty("SkipActions", required = true)
    val skipActions: List<Any?>,

    @get:JsonProperty("SpecificMachineIds", required = true) @field:JsonProperty("SpecificMachineIds", required = true)
    val specificMachineIDS: List<Any?>,

    @get:JsonProperty("ExcludedMachineIds", required = true) @field:JsonProperty("ExcludedMachineIds", required = true)
    val excludedMachineIDS: List<Any?>,

    @get:JsonProperty("ManifestVariableSetId") @field:JsonProperty("ManifestVariableSetId")
    val manifestVariableSetID: String? = null,

    @get:JsonProperty("TaskId", required = true) @field:JsonProperty("TaskId", required = true)
    val taskID: String,

    @get:JsonProperty("ProjectId", required = true) @field:JsonProperty("ProjectId", required = true)
    val projectID: String,

    @get:JsonProperty("UseGuidedFailure", required = true) @field:JsonProperty("UseGuidedFailure", required = true)
    val useGuidedFailure: Boolean,

    @get:JsonProperty("Comments") @field:JsonProperty("Comments")
    val comments: Any? = null,

    @get:JsonProperty("FormValues", required = true) @field:JsonProperty("FormValues", required = true)
    val formValues: FormValues,

    @get:JsonProperty("QueueTime") @field:JsonProperty("QueueTime")
    val queueTime: Any? = null,

    @get:JsonProperty("QueueTimeExpiry") @field:JsonProperty("QueueTimeExpiry")
    val queueTimeExpiry: Any? = null,

    @get:JsonProperty("Name", required = true) @field:JsonProperty("Name", required = true)
    val name: String,

    @get:JsonProperty("Created", required = true) @field:JsonProperty("Created", required = true)
    val created: String,

    @get:JsonProperty("SpaceId", required = true) @field:JsonProperty("SpaceId", required = true)
    val spaceID: String,

    @get:JsonProperty("TentacleRetentionPeriod", required = true) @field:JsonProperty(
        "TentacleRetentionPeriod",
        required = true
    )
    val tentacleRetentionPeriod: TentacleRetentionPeriod,

    @get:JsonProperty("DeployedBy", required = true) @field:JsonProperty("DeployedBy", required = true)
    val deployedBy: String,

    @get:JsonProperty("DeployedById", required = true) @field:JsonProperty("DeployedById", required = true)
    val deployedByID: String,

    @get:JsonProperty("FailureEncountered", required = true) @field:JsonProperty("FailureEncountered", required = true)
    val failureEncountered: Boolean,

    @get:JsonProperty("DeployedToMachineIds", required = true) @field:JsonProperty(
        "DeployedToMachineIds",
        required = true
    )
    val deployedToMachineIDS: List<String>,

    @get:JsonProperty("Links", required = true) @field:JsonProperty("Links", required = true)
    val links: ItemLinks
) {
    override fun toString(): String {
        return "Deployment(id='$id', releaseID='$releaseID', channelID='$channelID', deploymentProcessID='$deploymentProcessID', " +
                "environmentID='$environmentID', tenantID=$tenantID, forcePackageDownload=$forcePackageDownload, " +
                "forcePackageRedeployment=$forcePackageRedeployment, skipActions=$skipActions, specificMachineIDS=$specificMachineIDS, " +
                "excludedMachineIDS=$excludedMachineIDS, manifestVariableSetID=$manifestVariableSetID, taskID='$taskID', " +
                "projectID='$projectID', useGuidedFailure=$useGuidedFailure, queueTime=$queueTime, queueTimeExpiry=$queueTimeExpiry, " +
                "name='$name', created='$created', spaceID='$spaceID', tentacleRetentionPeriod=$tentacleRetentionPeriod, " +
                "deployedBy='$deployedBy', deployedByID='$deployedByID', failureEncountered=$failureEncountered, deployedToMachineIDS=$deployedToMachineIDS), Links=$links"
    }
}

data class Change(
    @get:JsonProperty("Version", required = true) @field:JsonProperty("Version", required = true)
    val version: String,

    @get:JsonProperty("ReleaseNotes") @field:JsonProperty("ReleaseNotes")
    val releaseNotes: String? = null,

    @get:JsonProperty("BuildInformation", required = true) @field:JsonProperty("BuildInformation", required = true)
    val buildInformation: List<BuildInformation>,

    @get:JsonProperty("WorkItems", required = true) @field:JsonProperty("WorkItems", required = true)
    val workItems: List<Any?>,

    @get:JsonProperty("Commits", required = true) @field:JsonProperty("Commits", required = true)
    val commits: List<Commit>
)

data class BuildInformation (
    @get:JsonProperty("PackageId", required=true)@field:JsonProperty("PackageId", required=true)
    val packageID: String,

    @get:JsonProperty("Version", required=true)@field:JsonProperty("Version", required=true)
    val version: String,

    @get:JsonProperty("BuildEnvironment")@field:JsonProperty("BuildEnvironment")
    val buildEnvironment: String? = null,

    @get:JsonProperty("BuildNumber")@field:JsonProperty("BuildNumber")
    val buildNumber: String? = null,

    @get:JsonProperty("BuildUrl")@field:JsonProperty("BuildUrl")
    val buildURL: String? = null,

    @get:JsonProperty("Branch")@field:JsonProperty("Branch")
    val branch: String? = null,

    @get:JsonProperty("VcsType", required=true)@field:JsonProperty("VcsType", required=true)
    val vcsType: String,

    @get:JsonProperty("VcsRoot")@field:JsonProperty("VcsRoot")
    val vcsRoot: String? = null,

    @get:JsonProperty("VcsCommitNumber")@field:JsonProperty("VcsCommitNumber")
    val vcsCommitNumber: String? = null,

    @get:JsonProperty("VcsCommitUrl")@field:JsonProperty("VcsCommitUrl")
    val vcsCommitURL: String? = null,

    @get:JsonProperty("IssueTrackerName")@field:JsonProperty("IssueTrackerName")
    val issueTrackerName: Any? = null,

    @get:JsonProperty("WorkItems", required=true)@field:JsonProperty("WorkItems", required=true)
    val workItems: List<Any?>,

    @get:JsonProperty("Commits", required=true)@field:JsonProperty("Commits", required=true)
    val commits: List<Commit>
)

data class Commit(
    @get:JsonProperty("Id", required = true) @field:JsonProperty("Id", required = true)
    val id: String,

    @get:JsonProperty("LinkUrl") @field:JsonProperty("LinkUrl")
    val linkURL: String? = null,

    @get:JsonProperty("Comment", required = true) @field:JsonProperty("Comment", required = true)
    val comment: String
)

typealias FormValues = JsonNode

data class ItemLinks(
    @get:JsonProperty("Self", required = true) @field:JsonProperty("Self", required = true)
    val self: String,

    @get:JsonProperty("Release", required = true) @field:JsonProperty("Release", required = true)
    val release: String,

    @get:JsonProperty("Environment", required = true) @field:JsonProperty("Environment", required = true)
    val environment: String,

    @get:JsonProperty("Project", required = true) @field:JsonProperty("Project", required = true)
    val project: String,

    @get:JsonProperty("Task", required = true) @field:JsonProperty("Task", required = true)
    val task: String,

    @get:JsonProperty("Web", required = true) @field:JsonProperty("Web", required = true)
    val web: String,

    @get:JsonProperty("Artifacts", required = true) @field:JsonProperty("Artifacts", required = true)
    val artifacts: String,

    @get:JsonProperty("Interruptions", required = true) @field:JsonProperty("Interruptions", required = true)
    val interruptions: String,

    @get:JsonProperty("DeploymentProcess", required = true) @field:JsonProperty("DeploymentProcess", required = true)
    val deploymentProcess: String,

    @get:JsonProperty("Variables") @field:JsonProperty("Variables")
    val variables: String? = null
) {
    fun extractReleaseId(): String = release.split("/").last()
}

data class TentacleRetentionPeriod(
    @get:JsonProperty("Unit", required = true) @field:JsonProperty("Unit", required = true)
    val unit: String,

    @get:JsonProperty("QuantityToKeep", required = true) @field:JsonProperty("QuantityToKeep", required = true)
    val quantityToKeep: Long,

    @get:JsonProperty("ShouldKeepForever", required = true) @field:JsonProperty("ShouldKeepForever", required = true)
    val shouldKeepForever: Boolean
)

data class DeploymentsPaginatedLinks(
    @get:JsonProperty("Self", required = true) @field:JsonProperty("Self", required = true)
    val self: String,

    @get:JsonProperty("Template", required = true) @field:JsonProperty("Template", required = true)
    val template: String,

    @get:JsonProperty("Page.All", required = true) @field:JsonProperty("Page.All", required = true)
    val pageAll: String,

    @get:JsonProperty("Page.Next") @field:JsonProperty("Page.Next")
    val pageNext: String? = null,

    @get:JsonProperty("Page.Current", required = true) @field:JsonProperty("Page.Current", required = true)
    val pageCurrent: String,

    @get:JsonProperty("Page.Last", required = true) @field:JsonProperty("Page.Last", required = true)
    val pageLast: String,

    @get:JsonProperty("Page.Previous") @field:JsonProperty("Page.Previous")
    val pagePrevious: String? = null
)