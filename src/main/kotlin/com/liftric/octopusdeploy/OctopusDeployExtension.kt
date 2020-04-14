package com.liftric.octopusdeploy

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import java.io.File

open class OctopusDeployExtension(project: Project) {
    var apiKey: String? = null
    var serverUrl: String? = null
    var generateChangelogSinceLastTag: Boolean = false
    var commitLinkBaseUrl: String = "http://example.com/repo/commits/"

    @OutputDirectory
    var outputDir: File = project.file("${project.buildDir}/$extensionName")

    var gitRoot: File = project.rootDir

    @InputFile
    var pushPackage: File? = null
    @Input
    var version: String? = null
    @Input
    var packageName: String? = null

    override fun toString(): String {
        return "OctopusDeployExtension(apiKey=$apiKey, serverUrl=$serverUrl, generateChangelogSinceLastTag=$generateChangelogSinceLastTag, commitLinkBaseUrl='$commitLinkBaseUrl', outputDir=$outputDir, gitRoot=$gitRoot, pushPackage=$pushPackage, version=$version, packageName=$packageName)"
    }
}