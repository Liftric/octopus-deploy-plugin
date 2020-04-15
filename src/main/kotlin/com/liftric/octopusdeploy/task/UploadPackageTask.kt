package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

open class UploadPackageTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Uploades the package to octopus."
        outputs.upToDateWhen { false }
    }

    lateinit var octopusUrl: String
    lateinit var apiKey: String

    lateinit var packageName: String
    lateinit var version: String

    var overwriteMode: String? = null

    var packageFile: File? = null

    @TaskAction
    fun execute() {
        val (exitCode, inputText, _) = listOf(
            "octo",
            "push",
            "--server=${octopusUrl}",
            "--apiKey=${apiKey}",
            "--package",
            packageFile?.absolutePath ?: error("couldn't find build-information.json"),
            overwriteMode?.let { "--overwrite-mode=$it" }
        ).filterNotNull().joinToString(" ").let { shell(it) }
        if (exitCode == 0) {
            println(inputText)
        } else {
            logger.error("octo push returned non-zero exitCode: $exitCode")
            logger.error(inputText)
            throw IllegalStateException("octo push exitCode: $exitCode")
        }
    }
}