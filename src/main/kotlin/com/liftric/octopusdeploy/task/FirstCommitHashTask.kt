package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File

open class FirstCommitHashTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Calls git log to get the first commit hash of the current history tree"
        outputs.upToDateWhen { false }
    }

    @OutputDirectory
    lateinit var outputDir: File

    @InputDirectory
    lateinit var workingDir: File

    @OutputFile
    @Optional
    var outputFile: File? = null

    @TaskAction
    fun execute() {
        val (exitCode, inputText, errorText) = workingDir.shell(
            "git log --pretty='format:%H' --reverse | head -1",
            logger
        )
        if (exitCode == 0) {
            logger.info("first commit hash: $inputText")
            outputFile = File(outputDir, "firstCommitHash").apply {
                writeText(inputText)
            }
        } else {
            logger.error("git log returned non-zero exitCode: $exitCode")
            logger.error(errorText)
            throw IllegalStateException("git log exitCode: $exitCode")
        }
    }
}
