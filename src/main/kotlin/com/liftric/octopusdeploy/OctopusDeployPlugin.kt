package com.liftric.octopusdeploy

import com.liftric.octopusdeploy.task.*
import org.gradle.api.Plugin
import org.gradle.api.Project

internal const val extensionName = "octopus"

class OctopusDeployPlugin : Plugin<Project> {
    @Suppress("UNUSED_VARIABLE")
    override fun apply(project: Project) {
        val extension = project.extensions.create(extensionName, OctopusDeployExtension::class.java, project)
        extension.outputDir.apply {
            mkdirs()
        }
        val getFirstCommitHashTask =
            project.tasks.create("firstCommitHash", FirstCommitHashTask::class.java).apply {
                project.afterEvaluate {
                    outputDir = extension.outputDir
                    workingDir = extension.gitRoot
                }
            }
        val getPreviousTagTask =
            project.tasks.create("previousTag", PreviousTagTask::class.java).apply {
                project.afterEvaluate {
                    outputDir = extension.outputDir
                    workingDir = extension.gitRoot
                }
            }
        val commitsSinceLastTagTask =
            project.tasks.create("commitsSinceLastTag", CommitsSinceLastTagTask::class.java).apply {
                dependsOn(getFirstCommitHashTask, getPreviousTagTask)
                project.afterEvaluate {
                    useShortCommitHashes.set(extension.useShortCommitHashes)
                    workingDir = extension.gitRoot
                    commitLinkBaseUrl = extension.commitLinkBaseUrl
                }
                doFirst {
                    firstCommitFile = getFirstCommitHashTask.outputFile
                    previousTagFile = getPreviousTagTask.outputFile
                }
            }
        val createBuildInformationTask =
            project.tasks.create("createBuildInformation", CreateBuildInformationTask::class.java).apply {
                val task = this
                project.afterEvaluate {
                    if (extension.generateChangelogSinceLastTag) {
                        dependsOn(commitsSinceLastTagTask)
                    }
                    commits = emptyList()
                    outputDir = extension.outputDir
                    packageName.set(extension.packageName)
                    issueTrackerName.set(extension.issueTrackerName)
                    parseCommitsForJiraIssues.set(extension.parseCommitsForJiraIssues)
                    jiraBaseBrowseUrl.set(extension.jiraBaseBrowseUrl)
                    task.version.set(extension.version)
                }
                doFirst {
                    commits = commitsSinceLastTagTask.commits
                    buildInformationAddition = extension.buildInformationAddition
                }
            }
        val createBuildInformationMarkdownTask =
            project.tasks.create("createBuildInformationMarkdown", CreateBuildInformationMarkdownTask::class.java)
                .apply {
                    val task = this
                    project.afterEvaluate {
                        if (extension.generateChangelogSinceLastTag) {
                            dependsOn(commitsSinceLastTagTask)
                        }
                        commits = emptyList()
                        packageName.set(extension.packageName)
                        issueTrackerName.set(extension.issueTrackerName)
                        parseCommitsForJiraIssues.set(extension.parseCommitsForJiraIssues)
                        jiraBaseBrowseUrl.set(extension.jiraBaseBrowseUrl)
                        task.version.set(extension.version)
                    }
                    doFirst {
                        commits = commitsSinceLastTagTask.commits
                        buildInformationAddition = extension.buildInformationAddition
                    }
                }
        val uploadBuildInformationTask =
            project.tasks.create("uploadBuildInformation", UploadBuildInformationTask::class.java).apply {
                dependsOn(createBuildInformationTask)
                val task = this
                project.afterEvaluate {
                    apiKey.set(extension.apiKey)
                    octopusUrl.set(extension.serverUrl)
                    packageName.set(extension.packageName)
                    task.version.set(extension.version)
                    overwriteMode = extension.buildInformationOverwriteMode?.name
                }
                doFirst {
                    buildInformation = createBuildInformationTask.outputFile
                }
            }
        val uploadPackageTask =
            project.tasks.create("uploadPackage", UploadPackageTask::class.java).apply {
                val task = this
                project.afterEvaluate {
                    apiKey.set(extension.apiKey)
                    octopusUrl.set(extension.serverUrl)
                    packageFile.set(extension.pushPackage)
                    overwriteMode = extension.pushOverwriteMode?.name
                    packageName.set(extension.packageName)
                    task.version.set(extension.version)
                    httpLogLevel.set(extension.httpLogLevel)
                }
            }
        project.tasks.withType(PromoteReleaseTask::class.java) {
            project.afterEvaluate {
                apiKey.set(extension.apiKey)
                octopusUrl.set(extension.serverUrl)
                httpLogLevel.set(extension.httpLogLevel)
            }
        }
        project.tasks.withType(CreateReleaseTask::class.java) {
            apiKey.convention(extension.apiKey)
            octopusUrl.convention(extension.serverUrl)
        }
    }
}

fun Project.octopus(): OctopusDeployExtension {
    return extensions.getByName(extensionName) as? OctopusDeployExtension
        ?: throw IllegalStateException("$extensionName is not of the correct type")
}
