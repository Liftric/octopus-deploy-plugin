package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.liftric.octopusdeploy.api.BuildInformation
import com.liftric.octopusdeploy.api.Commit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

open class CreateBuildInformationTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Creates the octopus build-information file."
        outputs.upToDateWhen { false }
    }

    @Input
    lateinit var packageName: String
    @Input
    lateinit var version: String
    @Input
    lateinit var commits: List<Commit>
    @Input
    lateinit var buildInformationAddition: BuildInformation.() -> Unit

    @OutputDirectory
    lateinit var outputDir: File

    @OutputFile
    @Optional
    var outputFile: File? = null

    @TaskAction
    fun execute() {
        outputFile = File(outputDir, "build-information.json").apply {
            writeText(
                jacksonObjectMapper().apply {
                    propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
                    setSerializationInclusion(JsonInclude.Include.NON_NULL)
                }.writeValueAsString(BuildInformation().apply {
                    PackageId = packageName
                    Version = version
                    VcsType = "Git"
                    Commits = commits
                    buildInformationAddition()
                }).also { println(it) }
            )
        }
    }
}