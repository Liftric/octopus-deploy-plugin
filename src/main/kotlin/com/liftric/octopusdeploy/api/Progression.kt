package com.liftric.octopusdeploy.api

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.liftric.octopusdeploy.rest.mapper


data class Progression(
    @get:JsonProperty("Phases", required = true) @field:JsonProperty("Phases", required = true)
    val phases: List<Phase>,

    @get:JsonProperty("NextDeployments", required = true) @field:JsonProperty("NextDeployments", required = true)
    val nextDeployments: List<String>,

    @get:JsonProperty(
        "NextDeploymentsMinimumRequired",
        required = true
    ) @field:JsonProperty("NextDeploymentsMinimumRequired", required = true)
    val nextDeploymentsMinimumRequired: Long,

    @get:JsonProperty("Links") @field:JsonProperty("Links")
    val links: Any? = null
) {
    fun toJson() = mapper.writeValueAsString(this)

    companion object {
        fun fromJson(json: String) = mapper.readValue<Progression>(json)
    }
}

data class Phase(
    @get:JsonProperty("Id") @field:JsonProperty("Id")
    val id: Any? = null,

    @get:JsonProperty("Name", required = true) @field:JsonProperty("Name", required = true)
    val name: String,

    @get:JsonProperty("Blocked", required = true) @field:JsonProperty("Blocked", required = true)
    val blocked: Boolean,

    @get:JsonProperty("Progress", required = true) @field:JsonProperty("Progress", required = true)
    val progress: ProgressEnum,

    @get:JsonProperty("Deployments", required = true) @field:JsonProperty("Deployments", required = true)
    val deployments: List<DeploymentElement>,

    @get:JsonProperty("AutomaticDeploymentTargets", required = true) @field:JsonProperty(
        "AutomaticDeploymentTargets",
        required = true
    )
    val automaticDeploymentTargets: List<String>,

    @get:JsonProperty("OptionalDeploymentTargets", required = true) @field:JsonProperty(
        "OptionalDeploymentTargets",
        required = true
    )
    val optionalDeploymentTargets: List<String>,

    @get:JsonProperty(
        "MinimumEnvironmentsBeforePromotion",
        required = true
    ) @field:JsonProperty("MinimumEnvironmentsBeforePromotion", required = true)
    val minimumEnvironmentsBeforePromotion: Long,

    @get:JsonProperty("IsOptionalPhase", required = true) @field:JsonProperty("IsOptionalPhase", required = true)
    val isOptionalPhase: Boolean
) {
    override fun toString(): String {
        return "Phase(id=$id, name='$name', blocked=$blocked, progress='$progress', automaticDeploymentTargets=$automaticDeploymentTargets, optionalDeploymentTargets=$optionalDeploymentTargets, minimumEnvironmentsBeforePromotion=$minimumEnvironmentsBeforePromotion, isOptionalPhase=$isOptionalPhase)"
    }
}

data class DeploymentElement(
    @get:JsonProperty("Task") @field:JsonProperty("Task")
    val task: Task? = null,

    @get:JsonProperty("Deployment") @field:JsonProperty("Deployment")
    val deployment: DeploymentDeployment? = null
)

data class DeploymentDeployment(
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
    val changes: List<Any?>,

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

    @get:JsonProperty("FormValues") @field:JsonProperty("FormValues")
    val formValues: Any? = null,

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
    val deployedToMachineIDS: List<Any?>,

    @get:JsonProperty("Links", required = true) @field:JsonProperty("Links", required = true)
    val links: DeploymentDeploymentLinks
)

data class DeploymentDeploymentLinks(
    @get:JsonProperty("Self") @field:JsonProperty("Self")
    val self: String? = null,

    @get:JsonProperty("Release") @field:JsonProperty("Release")
    val release: String? = null,

    @get:JsonProperty("Environment") @field:JsonProperty("Environment")
    val environment: String? = null,

    @get:JsonProperty("Project") @field:JsonProperty("Project")
    val project: String? = null,

    @get:JsonProperty("Task") @field:JsonProperty("Task")
    val task: String? = null,

    @get:JsonProperty("Web") @field:JsonProperty("Web")
    val web: String? = null,

    @get:JsonProperty("Artifacts") @field:JsonProperty("Artifacts")
    val artifacts: String? = null,

    @get:JsonProperty("Interruptions") @field:JsonProperty("Interruptions")
    val interruptions: String? = null,

    @get:JsonProperty("DeploymentProcess") @field:JsonProperty("DeploymentProcess")
    val deploymentProcess: String? = null,

    @get:JsonProperty("Variables") @field:JsonProperty("Variables")
    val variables: String? = null
)

data class Task(
    @get:JsonProperty("Id") @field:JsonProperty("Id")
    val id: String? = null,

    @get:JsonProperty("SpaceId") @field:JsonProperty("SpaceId")
    val spaceID: String? = null,

    @get:JsonProperty("Name") @field:JsonProperty("Name")
    val name: String? = null,

    @get:JsonProperty("Description") @field:JsonProperty("Description")
    val description: String? = null,

    @get:JsonProperty("Arguments") @field:JsonProperty("Arguments")
    val arguments: Arguments? = null,

    @get:JsonProperty("State") @field:JsonProperty("State")
    val state: String? = null,

    @get:JsonProperty("QueueTime") @field:JsonProperty("QueueTime")
    val queueTime: String? = null,

    @get:JsonProperty("QueueTimeExpiry") @field:JsonProperty("QueueTimeExpiry")
    val queueTimeExpiry: Any? = null,

    @get:JsonProperty("StartTime") @field:JsonProperty("StartTime")
    val startTime: String? = null,

    @get:JsonProperty("LastUpdatedTime") @field:JsonProperty("LastUpdatedTime")
    val lastUpdatedTime: String? = null,

    @get:JsonProperty("CompletedTime") @field:JsonProperty("CompletedTime")
    val completedTime: Any? = null,

    @get:JsonProperty("ServerNode") @field:JsonProperty("ServerNode")
    val serverNode: String? = null,

    @get:JsonProperty("Duration") @field:JsonProperty("Duration")
    val duration: String? = null,

    @get:JsonProperty("ErrorMessage") @field:JsonProperty("ErrorMessage")
    val errorMessage: String? = null,

    @get:JsonProperty("HasBeenPickedUpByProcessor") @field:JsonProperty(
        "HasBeenPickedUpByProcessor",
        required = true
    )
    val hasBeenPickedUpByProcessor: Boolean? = null,

    @get:JsonProperty("IsCompleted") @field:JsonProperty("IsCompleted")
    val isCompleted: Boolean? = null,

    @get:JsonProperty("FinishedSuccessfully") @field:JsonProperty(
        "FinishedSuccessfully",
        required = true
    )
    val finishedSuccessfully: Boolean? = null,

    @get:JsonProperty("HasPendingInterruptions") @field:JsonProperty(
        "HasPendingInterruptions",
        required = true
    )
    val hasPendingInterruptions: Boolean? = null,

    @get:JsonProperty("CanRerun") @field:JsonProperty("CanRerun")
    val canRerun: Boolean? = null,

    @get:JsonProperty("HasWarningsOrErrors") @field:JsonProperty(
        "HasWarningsOrErrors",
        required = true
    )
    val hasWarningsOrErrors: Boolean? = null,

    @get:JsonProperty("Links") @field:JsonProperty("Links")
    val links: TaskLinks? = null
)

data class Arguments(
    @get:JsonProperty("DeploymentId") @field:JsonProperty("DeploymentId")
    val deploymentID: String? = null
)

data class TaskLinks(
    @get:JsonProperty("Self") @field:JsonProperty("Self")
    val self: String? = null,

    @get:JsonProperty("Web") @field:JsonProperty("Web")
    val web: String? = null,

    @get:JsonProperty("Raw") @field:JsonProperty("Raw")
    val raw: String? = null,

    @get:JsonProperty("Rerun") @field:JsonProperty("Rerun")
    val rerun: String? = null,

    @get:JsonProperty("Cancel") @field:JsonProperty("Cancel")
    val cancel: String? = null,

    @get:JsonProperty("State") @field:JsonProperty("State")
    val state: String? = null,

    @get:JsonProperty("QueuedBehind") @field:JsonProperty("QueuedBehind")
    val queuedBehind: String? = null,

    @get:JsonProperty("Details") @field:JsonProperty("Details")
    val details: String? = null,

    @get:JsonProperty("Artifacts") @field:JsonProperty("Artifacts")
    val artifacts: String? = null,

    @get:JsonProperty("Interruptions") @field:JsonProperty("Interruptions")
    val interruptions: String? = null
)

enum class ProgressEnum {
    Pending, // lifecycle not ready (previous stages still to do)
    Current, // lifecycle ready but not triggered
    Complete // rolled out
}
