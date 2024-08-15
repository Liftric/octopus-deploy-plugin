package com.liftric.octopusdeploy

import com.liftric.octopusdeploy.extensions.OctopusDeployExtension
import com.liftric.octopusdeploy.task.*
import org.gradle.api.Plugin
import org.gradle.api.Project

internal const val extensionName = "octopus"

class OctopusDeployPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(extensionName, OctopusDeployExtension::class.java, project)
        extension.outputDir.convention(
            project.layout.buildDirectory.dir(extensionName)
        )
        extension.gitRoot.convention(
            project.rootProject.layout.projectDirectory
        )
        extension.commitLinkBaseUrl.convention("https://git.example.com/repo/commits/")
        extension.generateChangelogSinceLastTag.convention(false)
        extension.buildInformationAddition.convention({ /* no-op */ })
        extension.useShortCommitHashes.convention(true)

        val getFirstCommitHashTask = project.tasks.create("firstCommitHash", FirstCommitHashTask::class.java).apply {
            group = "octopus"
            description = "Calls git log to get the first commit hash of the current history tree"
            gitRoot.set(extension.gitRoot)
            outputFile.set(extension.outputDir.file("firstCommitHash"))
        }

        val getPreviousTagTask = project.tasks.create("previousTag", PreviousTagTask::class.java).apply {
            group = "octopus"
            description = "Calls git describe to receive the previous tag name. Will fail if no tag is found."
            gitRoot.set(extension.gitRoot)
            outputFile.set(extension.outputDir.file("previousTagName"))
            mustRunAfter(getFirstCommitHashTask)
        }

        val commitsSinceLastTagTask =
            project.tasks.create("commitsSinceLastTag", CommitsSinceLastTagTask::class.java).apply {
                group = "octopus"
                description =
                    "Calls git log to receive all commits since the previous tag or the first commit of the current history."
                gitRoot.set(extension.gitRoot)
                useShortCommitHashes.set(extension.useShortCommitHashes)
                commitLinkBaseUrl.set(extension.commitLinkBaseUrl)
                firstCommitFile.set(getFirstCommitHashTask.outputFile)
                previousTagFile.set(getPreviousTagTask.outputFile)
                dependsOn(getFirstCommitHashTask, getPreviousTagTask)
            }

        val createBuildInformationTask =
            project.tasks.create("createBuildInformation", CreateBuildInformationTask::class.java).apply {
                group = "octopus"
                description = "Creates the octopus build-information file."
                packageName.set(extension.packageName)
                issueTrackerName.set(extension.issueTrackerName)
                parseCommitsForJiraIssues.set(extension.parseCommitsForJiraIssues)
                jiraBaseBrowseUrl.set(extension.jiraBaseBrowseUrl)
                version.set(extension.version)
                buildInformationAddition.set(extension.buildInformationAddition)
                outputFile.set(extension.outputDir.file("build-information.json"))
                commits.set(commitsSinceLastTagTask.commits)
                dependsOn(commitsSinceLastTagTask)
            }


        project.tasks.create("createBuildInformationMarkdown", CreateBuildInformationMarkdownTask::class.java).apply {
            group = "octopus"
            description = "Creates a markdown file from the build-information for octo cli release creation."
            packageName.set(extension.packageName)
            commits.set(commitsSinceLastTagTask.commits)
            issueTrackerName.set(extension.issueTrackerName)
            parseCommitsForJiraIssues.set(extension.parseCommitsForJiraIssues)
            jiraBaseBrowseUrl.set(extension.jiraBaseBrowseUrl)
            version.set(extension.version)
            buildInformationAddition.set(extension.buildInformationAddition)
            outputFile.set(extension.outputDir.file("build-information.md"))
            dependsOn(commitsSinceLastTagTask)
        }

        project.tasks.create("uploadBuildInformation", UploadBuildInformationTask::class.java).apply {
            group = "octopus"
            description = "Uploads the created octopus build-information file."
            apiKey.set(extension.apiKey)
            octopusUrl.set(extension.serverUrl)
            version.set(extension.version)
            packageName.set(extension.packageName)
            overwriteMode.set(extension.buildInformationOverwriteMode)
            buildInformationFile.set(createBuildInformationTask.outputFile)
            dependsOn(createBuildInformationTask)
        }

        project.tasks.create("uploadPackage", UploadPackageTask::class.java).apply {
            group = "octopus"
            description = "Uploads the package to octopus."
            apiKey.set(extension.apiKey)
            octopusUrl.set(extension.serverUrl)
            version.set(extension.version)
            packageName.set(extension.packageName)
            pushPackage.set(extension.pushPackage)
            overwriteMode.set(extension.pushOverwriteMode)
            httpLogLevel.set(extension.httpLogLevel)
        }

        project.tasks.withType(PromoteReleaseTask::class.java) {
            group = "octopus"
            description = "Promotes a release."
            apiKey.set(extension.apiKey)
            octopusUrl.set(extension.serverUrl)
            httpLogLevel.set(extension.httpLogLevel)
        }

        project.tasks.withType(CreateReleaseTask::class.java) {
            group = "octopus"
            description = "Creates a new release."
            apiKey.set(extension.apiKey)
            octopusUrl.set(extension.serverUrl)
        }
    }
}

fun Project.octopus(): OctopusDeployExtension {
    return extensions.getByName(extensionName) as? OctopusDeployExtension
        ?: throw IllegalStateException("$extensionName is not of the correct type")
}
