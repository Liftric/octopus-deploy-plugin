package com.liftric.octopusdeploy.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.liftric.octopusdeploy.rest.mapper

data class Project (
    @get:JsonProperty("Id", required=true)@field:JsonProperty("Id", required=true)
    val id: String,

    @get:JsonProperty("VariableSetId", required=true)@field:JsonProperty("VariableSetId", required=true)
    val variableSetID: String,

    @get:JsonProperty("DeploymentProcessId", required=true)@field:JsonProperty("DeploymentProcessId", required=true)
    val deploymentProcessID: String,

    @get:JsonProperty("ClonedFromProjectId")@field:JsonProperty("ClonedFromProjectId")
    val clonedFromProjectID: Any? = null,

    @get:JsonProperty("DiscreteChannelRelease", required=true)@field:JsonProperty("DiscreteChannelRelease", required=true)
    val discreteChannelRelease: Boolean,

    @get:JsonProperty("IncludedLibraryVariableSetIds", required=true)@field:JsonProperty("IncludedLibraryVariableSetIds", required=true)
    val includedLibraryVariableSetIDS: List<String>,

    @get:JsonProperty("DefaultToSkipIfAlreadyInstalled", required=true)@field:JsonProperty("DefaultToSkipIfAlreadyInstalled", required=true)
    val defaultToSkipIfAlreadyInstalled: Boolean,

    @get:JsonProperty("TenantedDeploymentMode", required=true)@field:JsonProperty("TenantedDeploymentMode", required=true)
    val tenantedDeploymentMode: String,

    @get:JsonProperty("DefaultGuidedFailureMode", required=true)@field:JsonProperty("DefaultGuidedFailureMode", required=true)
    val defaultGuidedFailureMode: String,

    @get:JsonProperty("VersioningStrategy", required=true)@field:JsonProperty("VersioningStrategy", required=true)
    val versioningStrategy: VersioningStrategy,

    @get:JsonProperty("ReleaseCreationStrategy", required=true)@field:JsonProperty("ReleaseCreationStrategy", required=true)
    val releaseCreationStrategy: ReleaseCreationStrategy,

    @get:JsonProperty("Templates", required=true)@field:JsonProperty("Templates", required=true)
    val templates: List<Any?>,

    @get:JsonProperty("AutoDeployReleaseOverrides", required=true)@field:JsonProperty("AutoDeployReleaseOverrides", required=true)
    val autoDeployReleaseOverrides: List<Any?>,

    @get:JsonProperty("ReleaseNotesTemplate")@field:JsonProperty("ReleaseNotesTemplate")
    val releaseNotesTemplate: String? = null,

    @get:JsonProperty("DeploymentChangesTemplate")@field:JsonProperty("DeploymentChangesTemplate")
    val deploymentChangesTemplate: Any? = null,

    @get:JsonProperty("SpaceId", required=true)@field:JsonProperty("SpaceId", required=true)
    val spaceID: String,

    @get:JsonProperty("ExtensionSettings", required=true)@field:JsonProperty("ExtensionSettings", required=true)
    val extensionSettings: List<Any?>,

    @get:JsonProperty("Name", required=true)@field:JsonProperty("Name", required=true)
    val name: String,

    @get:JsonProperty("Slug", required=true)@field:JsonProperty("Slug", required=true)
    val slug: String,

    @get:JsonProperty("Description", required=true)@field:JsonProperty("Description", required=true)
    val description: String,

    @get:JsonProperty("IsDisabled", required=true)@field:JsonProperty("IsDisabled", required=true)
    val isDisabled: Boolean,

    @get:JsonProperty("ProjectGroupId", required=true)@field:JsonProperty("ProjectGroupId", required=true)
    val projectGroupID: String,

    @get:JsonProperty("LifecycleId", required=true)@field:JsonProperty("LifecycleId", required=true)
    val lifecycleID: String,

    @get:JsonProperty("AutoCreateRelease", required=true)@field:JsonProperty("AutoCreateRelease", required=true)
    val autoCreateRelease: Boolean,

    @get:JsonProperty("ProjectConnectivityPolicy", required=true)@field:JsonProperty("ProjectConnectivityPolicy", required=true)
    val projectConnectivityPolicy: ProjectConnectivityPolicy,

    @get:JsonProperty("Links", required=true)@field:JsonProperty("Links", required=true)
    val links: Links
) {
    fun toJson() = mapper.writeValueAsString(this)
    override fun toString(): String {
        return "Project(id='$id', variableSetID='$variableSetID', deploymentProcessID='$deploymentProcessID', clonedFromProjectID=$clonedFromProjectID, discreteChannelRelease=$discreteChannelRelease, includedLibraryVariableSetIDS=$includedLibraryVariableSetIDS, defaultToSkipIfAlreadyInstalled=$defaultToSkipIfAlreadyInstalled, tenantedDeploymentMode='$tenantedDeploymentMode', defaultGuidedFailureMode='$defaultGuidedFailureMode', versioningStrategy=$versioningStrategy, releaseCreationStrategy=$releaseCreationStrategy, autoDeployReleaseOverrides=$autoDeployReleaseOverrides, deploymentChangesTemplate=$deploymentChangesTemplate, spaceID='$spaceID', extensionSettings=$extensionSettings, name='$name', slug='$slug', description='$description', isDisabled=$isDisabled, projectGroupID='$projectGroupID', lifecycleID='$lifecycleID', autoCreateRelease=$autoCreateRelease, projectConnectivityPolicy=$projectConnectivityPolicy)"
    }

    companion object {
        fun fromJson(json: String) = mapper.readValue<Project>(json)
    }


}

data class Links (
    @get:JsonProperty("Self", required=true)@field:JsonProperty("Self", required=true)
    val self: String,

    @get:JsonProperty("Releases", required=true)@field:JsonProperty("Releases", required=true)
    val releases: String,

    @get:JsonProperty("Channels", required=true)@field:JsonProperty("Channels", required=true)
    val channels: String,

    @get:JsonProperty("Triggers", required=true)@field:JsonProperty("Triggers", required=true)
    val triggers: String,

    @get:JsonProperty("ScheduledTriggers", required=true)@field:JsonProperty("ScheduledTriggers", required=true)
    val scheduledTriggers: String,

    @get:JsonProperty("OrderChannels", required=true)@field:JsonProperty("OrderChannels", required=true)
    val orderChannels: String,

    @get:JsonProperty("Variables", required=true)@field:JsonProperty("Variables", required=true)
    val variables: String,

    @get:JsonProperty("Progression", required=true)@field:JsonProperty("Progression", required=true)
    val progression: String,

    @get:JsonProperty("RunbookTaskRunDashboardItemsTemplate", required=true)@field:JsonProperty("RunbookTaskRunDashboardItemsTemplate", required=true)
    val runbookTaskRunDashboardItemsTemplate: String,

    @get:JsonProperty("DeploymentProcess", required=true)@field:JsonProperty("DeploymentProcess", required=true)
    val deploymentProcess: String,

    @get:JsonProperty("Web", required=true)@field:JsonProperty("Web", required=true)
    val web: String,

    @get:JsonProperty("Logo", required=true)@field:JsonProperty("Logo", required=true)
    val logo: String,

    @get:JsonProperty("Metadata", required=true)@field:JsonProperty("Metadata", required=true)
    val metadata: String,

    @get:JsonProperty("Runbooks", required=true)@field:JsonProperty("Runbooks", required=true)
    val runbooks: String,

    @get:JsonProperty("RunbookSnapshots", required=true)@field:JsonProperty("RunbookSnapshots", required=true)
    val runbookSnapshots: String
)

data class ProjectConnectivityPolicy (
    @get:JsonProperty("SkipMachineBehavior", required=true)@field:JsonProperty("SkipMachineBehavior", required=true)
    val skipMachineBehavior: String,

    @get:JsonProperty("TargetRoles", required=true)@field:JsonProperty("TargetRoles", required=true)
    val targetRoles: List<Any?>,

    @get:JsonProperty("AllowDeploymentsToNoTargets", required=true)@field:JsonProperty("AllowDeploymentsToNoTargets", required=true)
    val allowDeploymentsToNoTargets: Boolean,

    @get:JsonProperty("ExcludeUnhealthyTargets", required=true)@field:JsonProperty("ExcludeUnhealthyTargets", required=true)
    val excludeUnhealthyTargets: Boolean
)

data class ReleaseCreationStrategy (
    @get:JsonProperty("ChannelId")@field:JsonProperty("ChannelId")
    val channelID: String? = null,

    @get:JsonProperty("ReleaseCreationPackage")@field:JsonProperty("ReleaseCreationPackage")
    val releaseCreationPackage: Package? = null,

    @get:JsonProperty("ReleaseCreationPackageStepId")@field:JsonProperty("ReleaseCreationPackageStepId")
    val releaseCreationPackageStepID: String? = null
)

data class Package (
    @get:JsonProperty("DeploymentAction", required=true)@field:JsonProperty("DeploymentAction", required=true)
    val deploymentAction: String,

    @get:JsonProperty("PackageReference", required=true)@field:JsonProperty("PackageReference", required=true)
    val packageReference: String
)

data class VersioningStrategy (
    @get:JsonProperty("Template")@field:JsonProperty("Template")
    val template: String? = null,

    @get:JsonProperty("DonorPackage")@field:JsonProperty("DonorPackage")
    val donorPackage: Package? = null,

    @get:JsonProperty("DonorPackageStepId")@field:JsonProperty("DonorPackageStepId")
    val donorPackageStepID: String? = null
)
