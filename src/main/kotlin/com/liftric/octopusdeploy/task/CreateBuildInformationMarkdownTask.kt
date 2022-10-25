package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.BuildInformationCli
import com.liftric.octopusdeploy.api.WorkItem
import com.liftric.octopusdeploy.extensionName
import com.liftric.octopusdeploy.parseCommitsForJira
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

open class CreateBuildInformationMarkdownTask : AbstractBuildInformationTask() {
    init {
        group = "octopus"
        description = "Creates a markdown file freom the build-information for octo cli release creation."
        outputs.upToDateWhen { false }
    }

    @OutputDirectory
    val outputDirectory: DirectoryProperty = project.objects.directoryProperty().convention(project.layout.buildDirectory.dir(extensionName))

    @OutputFile
    val outputMarkdown: RegularFileProperty = project.objects.fileProperty().convention(outputDirectory.file("build-information.md"))

    @TaskAction
    fun execute() {
        val workItems: List<WorkItem>? = if (parseCommitsForJiraIssues.getOrElse(false)) {
            parseCommitsForJira(commits, jiraBaseBrowseUrl.getOrElse(""))
        } else {
            null
        }
        val buildInformationCli = BuildInformationCli().apply(buildInformationAddition)
        outputMarkdown.get().asFile.apply {
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
