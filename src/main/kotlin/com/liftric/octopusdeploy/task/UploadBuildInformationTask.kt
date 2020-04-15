package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File

open class UploadBuildInformationTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Uploades the created octopus build-information file."
        outputs.upToDateWhen { false }
    }

    @Input
    lateinit var octopusUrl: String
    @Input
    lateinit var apiKey: String

    @Input
    lateinit var packageName: String
    @Input
    lateinit var version: String

    @Input
    @Optional
    var overwriteMode: String? = null

    @InputFile
    @Optional
    var buildInformation: File? = null

    @TaskAction
    fun execute() {
        val (exitCode, inputText, errorText) = listOf(
            "octo",
            "build-information",
            "--server=${octopusUrl}",
            "--apiKey=${apiKey}",
            "--file",
            buildInformation?.absolutePath ?: error("couldn't find build-information.json"),
            "--package-id",
            packageName,
            "--version=$version",
            overwriteMode?.let { "--overwrite-mode=$it" }
        ).filterNotNull().joinToString(" ").let { shell(it) }
        if (exitCode == 0) {
            println(inputText)
            println(errorText)
        } else {
            logger.error("octo build-information returned non-zero exitCode: $exitCode")
            logger.error(inputText)
            throw IllegalStateException("octo build-information exitCode: $exitCode")
        }
    }
}