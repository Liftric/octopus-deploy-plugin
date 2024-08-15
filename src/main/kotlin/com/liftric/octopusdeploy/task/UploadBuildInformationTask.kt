package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.OverwriteMode
import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Gradle would require more information to cache this task")
abstract class UploadBuildInformationTask : DefaultTask() {
    @get:Input
    abstract val octopusUrl: Property<String>

    @get:Input
    abstract val apiKey: Property<String>

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val version: Property<String>

    @get:Input
    @get:Optional
    abstract val overwriteMode: Property<OverwriteMode?>

    @get:InputFile
    abstract val buildInformationFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val (exitCode, inputText, errorText) = listOf(
            "octo",
            "build-information",
            "--server=${octopusUrl.get()}",
            "--apiKey=${apiKey.get()}",
            "--file",
            buildInformationFile.get().asFile,
            "--package-id",
            "\"${packageName.get()}\"",
            "--version=${version.get()}",
            overwriteMode.orNull?.let { "--overwrite-mode=$it" }
        ).filterNotNull().joinToString(" ").let { shell(it, logger) }
        if (exitCode == 0) {
            println(inputText)
            println(errorText)
        } else {
            logger.error("octo build-information returned non-zero exitCode: $exitCode")
            logger.error("inputStream='$inputText'")
            logger.error("errorStream='$errorText'")
            throw IllegalStateException("octo build-information exitCode: $exitCode")
        }
    }
}
