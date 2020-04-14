package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class PreviousTagTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Calls git describe to receive the previous tag name. Will fail if no tag is found."
        outputs.upToDateWhen { false }
    }

    @OutputDirectory
    lateinit var outputDir: File

    @Input
    lateinit var workingDir: File

    var outputFile: File? = null

    @TaskAction
    fun execute() {
        val (exitCode, inputText, errorText) = workingDir.shell("git describe --tags --abbrev=0 @^")
        if (exitCode == 0) {
            logger.info("previous tag: $inputText")
            outputFile = File(outputDir, "previousTagName").apply {
                writeText(inputText)
            }
        } else {
            logger.error("git describe returned non-zero exitCode: $exitCode")
            logger.error(errorText)
        }
    }
}