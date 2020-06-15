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
    val progress: String,

    @get:JsonProperty("Deployments", required = true) @field:JsonProperty("Deployments", required = true)
    val deployments: List<Any?>,

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