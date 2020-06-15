package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.liftric.octopusdeploy.api.BuildInformationCli
import com.liftric.octopusdeploy.api.CommitCli
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

    @TaskAction
    fun execute() {
        outputFile = File(outputDir, "build-information.json").apply {
            writeText(
                jacksonObjectMapper().apply {
                    propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
                    setSerializationInclusion(JsonInclude.Include.NON_NULL)
                }.writeValueAsString(BuildInformationCli().apply {
                    PackageId = packageName.get()
                    Version = version.get()
                    VcsType = "Git"
                    Commits = commits
                    buildInformationAddition()
                }).also { println(it) }
            )
        }
    }
}
