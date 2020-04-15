package com.liftric.octopusdeploy

import com.liftric.octopusdeploy.api.BuildInformation
import com.liftric.octopusdeploy.api.OverwriteMode
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import java.io.File

open class OctopusDeployExtension(project: Project) {
    /**
     * Octopus deploy server API key
     */
    var apiKey: String? = null
    /**
     * Octopus deploy server URL
     */
    var serverUrl: String? = null

    /**
     * Enable to calculate the commits for the changelog when uploading build-information
     */
    var generateChangelogSinceLastTag: Boolean = false

    /**
     * Prefix / Baseurl for the build-information commit urls.
     */
    var commitLinkBaseUrl: String = "http://git.example.com/repo/commits/"

    /**
     * Output folder for files genereated by the plugin
     */
    @OutputDirectory
    var outputDir: File = project.file("${project.buildDir}/$extensionName")

    /**
     * Directory to run the git helpers in. By default the project root dir
     */
    var gitRoot: File = project.rootDir

    /**
     * Target file (package) which will be uploaded to octopus.
     */
    @InputFile
    var pushPackage: File? = null
    /**
     * Package version.
     */
    @Input
    var version: String? = null
    /**
     * Package name
     */
    @Input
    var packageName: String? = null

    /**
     * octo build-information OverwriteMode
     */
    var buildInformationOverwriteMode: OverwriteMode? = null
    /**
     * octo push OverwriteMode
     */
    var pushOverwriteMode: OverwriteMode? = null

    /**
     * Customize the final octopus build-information before uploading
     */
    var buildInformationAddition: BuildInformation.() -> Unit = {}

    /**
     * Default `buildInformationAddition` implementation adding context from the CI environment for Gitlab CI
     */
    fun gitlab(): Unit {
        buildInformationAddition = {
            BuildEnvironment = if (System.getenv("CI") != null) {
                "GitLabCI"
            } else {
                "gradle"
            }
            BuildNumber = System.getenv("CI_PIPELINE_IID")
            BuildUrl = System.getenv("CI_PIPELINE_URL")
            Branch = System.getenv("CI_COMMIT_REF_NAME")
            VcsType = "Git"
            VcsRoot = System.getenv("CI_PROJECT_URL")
            VcsCommitNumber = System.getenv("CI_COMMIT_SHORT_SHA")
            VcsCommitUrl =
                "${System.getenv("CI_PROJECT_URL")
                    ?.removeSuffix("/")}/commit/${System.getenv("CI_COMMIT_SHA")}"
            LastModifiedBy = System.getenv("GITLAB_USER_NAME")
        }
    }
}