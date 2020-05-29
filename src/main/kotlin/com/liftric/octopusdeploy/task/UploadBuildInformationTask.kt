package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property
import java.io.File

open class UploadBuildInformationTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Uploads the created octopus build-information file."
        outputs.upToDateWhen { false }
    }

    @Input
    val octopusUrl: Property<String> = project.objects.property()
    @Input
    val apiKey: Property<String> = project.objects.property()

    @Input
    val packageName: Property<String> = project.objects.property()
    @Input
    val version: Property<String> = project.objects.property()

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
            "--server=${octopusUrl.get()}",
            "--apiKey=${apiKey.get()}",
            "--file",
            buildInformation?.absolutePath ?: error("couldn't find build-information.json"),
            "--package-id",
            packageName.get(),
            "--version=${version.get()}",
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
