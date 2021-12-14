package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.liftric.octopusdeploy.api.BuildInformationCli
import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.api.WorkItem
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.property
import java.io.File

open class CreateBuildInformationTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Creates the octopus build-information file."
        outputs.upToDateWhen { false }
    }

    @Input
    val packageName: Property<String> = project.objects.property()

    @Input
    val version: Property<String> = project.objects.property()

    @Input
    lateinit var commits: List<CommitCli>

    @Input
    var buildInformationAddition: BuildInformationCli.() -> Unit = {}

    @OutputDirectory
    lateinit var outputDir: File

    @OutputFile
    @Optional
    var outputFile: File? = null

    @Input
    @Optional
    val issueTrackerName: Property<String> = project.objects.property()

    @Input
    @Optional
    val parseCommitsForJiraIssues: Property<Boolean> = project.objects.property()

    @Input
    @Optional
    val jiraBaseBrowseUrl: Property<String> = project.objects.property()

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

    private fun parseCommitsForJira(
        commits: List<CommitCli>,
        jiraBaseBrowseUrl: String
    ): List<WorkItem> {
        val jiraIssues = commits
            .mapNotNull { it.Comment }
            .map { jiraKeyRegex.findAll(it).map { it.groupValues[1] }.toList() }
            .flatten()
            .toSet()
        println("parseCommitsForJira: found $jiraIssues")
        return jiraIssues.map {
            WorkItem(
                Id = it,
                LinkUrl = "${jiraBaseBrowseUrl.removeSuffix("/")}/$it",
                Description = "some placeholder text"
            )
        }
    }
}

// from https://confluence.atlassian.com/stashkb/integrating-with-custom-jira-issue-key-313460921.html
private val jiraKeyRegex = Regex("((?<!([A-Z]{1,10})-?)[A-Z]+-\\d+)")
