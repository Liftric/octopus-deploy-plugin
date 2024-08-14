package com.liftric.octopusdeploy.task

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
abstract class CreateBuildInformationMarkdownTask : DefaultTask() {
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
        val workItems: List<WorkItem>? = if (parseCommitsForJiraIssues.getOrElse(false)) {
            parseCommitsForJira(commits, jiraBaseBrowseUrl.getOrElse(""))
        } else {
            null
        }
        val gitlabCi = gitlabCi.getOrElse(false)
        val buildInformationCli: BuildInformationCli = if (gitlabCi) {
            buildInformationAddition.orNull?.buildForGitlabCi()
                ?: BuildInformationAdditionBuilder(project).buildForGitlabCi()
        } else {
            buildInformationAddition.orNull?.build() ?: BuildInformationAdditionBuilder(project).build()
        }

        outputFile.get().asFile.apply {
            writeText("")
            appendText("# ${packageName.get()}: ${version.get()}\n")
            appendText("[VCS Root](${buildInformationCli.VcsRoot})\n\n")
            appendText("CI: [${buildInformationCli.VcsCommitNumber}](${buildInformationCli.VcsCommitUrl})\n\n")
            appendText("[BuildEnvironment: ${buildInformationCli.BuildEnvironment}](${buildInformationCli.BuildUrl})\n\n")
            appendText("last modified by: ${buildInformationCli.LastModifiedBy}\n\n")
            if (commits.isNotEmpty())
                appendText("## Commits\n")
            commits.forEach {
                appendText(" - ${it.Id}: [${it.Comment}](${it.LinkUrl})\n")
            }
            if (workItems?.isNotEmpty() == true)
                appendText("## WorkItems\n")
            workItems?.forEach {
                appendText(" - [${it.Id}](${it.LinkUrl})\n")
            }
        }
    }
}
