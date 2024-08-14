package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.api.WorkItem
import com.liftric.octopusdeploy.extensions.BuildInformationAdditionBuilder
import com.liftric.octopusdeploy.extensions.BuildInformationCli
import com.liftric.octopusdeploy.parseCommitsForJira
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Gradle would require more information to cache this task")
abstract class CreateBuildInformationTask : DefaultTask() {

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val version: Property<String>

    @get:Internal
    abstract val commits: ListProperty<CommitCli>

    @get:Nested
    @get:Optional
    abstract val buildInformationAddition: Property<BuildInformationAdditionBuilder>

    @get:Optional
    @get:Input
    abstract val issueTrackerName: Property<String>

    @get:Input
    @get:Optional
    abstract val parseCommitsForJiraIssues: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val jiraBaseBrowseUrl: Property<String>

    @get:Input
    @get:Optional
    abstract val gitlabCi: Property<Boolean>

    @TaskAction
    fun execute() {
        val commits = commits.getOrElse(listOf())
        val gitlabCi = gitlabCi.getOrElse(false)
        val buildInformationCli: BuildInformationCli = if (gitlabCi) {
            buildInformationAddition.orNull?.buildForGitlabCi()
                ?: BuildInformationAdditionBuilder(project).buildForGitlabCi()
        } else {
            buildInformationAddition.orNull?.build() ?: BuildInformationAdditionBuilder(project).build()
        }

        val workItems: List<WorkItem>? = if (parseCommitsForJiraIssues.getOrElse(false)) {
            parseCommitsForJira(commits, jiraBaseBrowseUrl.getOrElse(""))
        } else {
            null
        }
        outputFile.get().asFile.writeText(
            jacksonObjectMapper().apply {
                propertyNamingStrategy = PropertyNamingStrategies.UPPER_CAMEL_CASE
                setSerializationInclusion(JsonInclude.Include.NON_NULL)
            }.writeValueAsString(
                buildInformationCli.apply {
                    PackageId = packageName.get()
                    Version = version.get()
                    VcsType = "Git"
                    Commits = commits
                    issueTrackerName.orNull.let { IssueTrackerName = it }
                    workItems?.let { WorkItems = it }
                }
            ).also { println(it) }
        )
    }
}
