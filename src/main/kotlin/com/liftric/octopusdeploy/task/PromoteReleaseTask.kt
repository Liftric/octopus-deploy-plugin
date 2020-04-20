package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class PromoteReleaseTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Promotes a release."
        outputs.upToDateWhen { false }
    }

    @Input
    val octopusUrl = project.objects.property(String::class.java)

    @Input
    val apiKey = project.objects.property(String::class.java)

    /**
     * Target octopus project name
     */
    @Input
    val projectName = project.objects.property(String::class.java)

    /**
     * Source octopus environment name
     */
    @Input
    val from = project.objects.property(String::class.java)

    /**
     * Target octopus environment name
     */
    @Input
    val to = project.objects.property(String::class.java)

    @TaskAction
    fun execute() {
        val (exitCode, inputText, errorText) = listOf(
            "octo",
            "promote-release",
            "--server=${octopusUrl.get()}",
            "--apiKey=${apiKey.get()}",
            "--project=${projectName.get()}",
            "--from=${from.get()}",
            "--to=${to.get()}"
        ).joinToString(" ").let { shell(it) }
        if (exitCode == 0) {
            println(inputText)
            println(errorText)
        } else {
            logger.error("octo promote-release returned non-zero exitCode: $exitCode")
            logger.error(inputText)
            logger.error(errorText)
            throw IllegalStateException("octo promote-release exitCode: $exitCode")
        }
    }
}
