package com.liftric.octopusdeploy

import com.liftric.octopusdeploy.task.*
import org.gradle.api.Plugin
import org.gradle.api.Project

internal const val extensionName = "octopus"

class OctopusDeployPlugin : Plugin<Project> {
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
                project.afterEvaluate {
                    if (extension.generateChangelogSinceLastTag) {
                        dependsOn(commitsSinceLastTagTask)
                    }
                    outputDir = extension.outputDir
                }
                doFirst {
                    packageName = extension.packageName ?: error("$extensionName: didn't specify packageName!")
                    version = extension.version ?: error("$extensionName: didn't specify version!")
                    commits = commitsSinceLastTagTask.commits
                    buildInformationAddition = extension.buildInformationAddition
                }
            }
        val uploadBuildInformationTask =
            project.tasks.create("UploadBuildInformationTask", UploadBuildInformationTask::class.java).apply {
                dependsOn(createBuildInformationTask)
                doFirst {
                    apiKey = extension.apiKey ?: error("$extensionName: didn't specify apiKey!")
                    octopusUrl = extension.serverUrl ?: error("$extensionName: didn't specify serverUrl!")
                    packageName = extension.packageName ?: error("$extensionName: didn't specify packageName!")
                    version = extension.version ?: error("$extensionName: didn't specify version!")
                    buildInformation = createBuildInformationTask.outputFile
                    overwriteMode = extension.buildInformationOverwriteMode?.name
                }
            }
    }
}

fun Project.octopus(): OctopusDeployExtension {
    return extensions.getByName(extensionName) as? OctopusDeployExtension
        ?: throw IllegalStateException("$extensionName is not of the correct type")
}
