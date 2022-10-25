package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.liftric.octopusdeploy.api.BuildInformationCli
import com.liftric.octopusdeploy.api.WorkItem
import com.liftric.octopusdeploy.parseCommitsForJira
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

open class CreateBuildInformationTask : AbstractBuildInformationTask() {
    init {
        group = "octopus"
        description = "Creates the octopus build-information file."
        outputs.upToDateWhen { false }
    }

    @OutputDirectory
    lateinit var outputDir: File

    @TaskAction
    fun execute() {
        val workItems: List<WorkItem>? = if (parseCommitsForJiraIssues.getOrElse(false)) {
            parseCommitsForJira(commits, jiraBaseBrowseUrl.getOrElse(""))
        } else {
            null
        }
        outputFile = File(outputDir, "build-information.json").apply {
            writeText(
                jacksonObjectMapper().apply {
                    propertyNamingStrategy = PropertyNamingStrategies.UPPER_CAMEL_CASE
                    setSerializationInclusion(JsonInclude.Include.NON_NULL)
                }.writeValueAsString(BuildInformationCli().apply {
                    PackageId = packageName.get()
                    Version = version.get()
                    VcsType = "Git"
                    Commits = commits
                    buildInformationAddition()
                    issueTrackerName.orNull.let { IssueTrackerName = it }
                    workItems?.let { WorkItems = it }
                }).also { println(it) }
            )
        }
    }
}
