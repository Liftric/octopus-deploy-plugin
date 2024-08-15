package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Gradle would require more information to cache this task")
abstract class PreviousTagTask : DefaultTask() {
    @get:Internal
    abstract val gitRoot: DirectoryProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val (exitCode, inputText, errorText) = gitRoot.get().asFile.shell(
            "git describe --tags --abbrev=0 @^",
            logger
        )
        if (exitCode == 0) {
            logger.info("previous tag: $inputText")
            outputFile.get().asFile.writeText(inputText)
        } else {
            logger.error("git describe returned non-zero exitCode: $exitCode")
            logger.error(errorText)
            throw IllegalStateException("git describe exitCode: $exitCode")
        }
    }
}
