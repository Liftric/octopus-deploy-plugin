package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import okhttp3.logging.HttpLoggingInterceptor
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

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

    @Input
    @Optional
    val waitForReleaseDeployments: Property<Boolean> = project.objects.property()

    @Input
    @Optional
    val waitTimeoutSeconds: Property<Long> = project.objects.property()

    @Input
    @Optional
    val delayBetweenChecksSeconds: Property<Long> = project.objects.property()

    /**
     * Configures the http logging of the underlying okhttp client used for octopus api requests
     */
    @Input
    @Optional
    val httpLogLevel: Property<HttpLoggingInterceptor.Level> = project.objects.property()

    @TaskAction
    fun execute() {
        val awaitReleases = waitForReleaseDeployments.getOrElse(false)
        val octopusUrlValue = octopusUrl.get()
        val apiKeyValue = apiKey.get()
        val projectNameValue = projectName.get()
        val fromValue = from.get()
        val toValue = to.get()
        val (exitCode, inputText, errorText) = listOf(
            "octo",
            "promote-release",
            "--server=$octopusUrlValue",
            "--apiKey=$apiKeyValue",
            "--project=$projectNameValue",
            "--from=$fromValue",
            "--to=$toValue"
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
        if (awaitReleases.not()) return
        awaitReleaseLogic(
            octopusUrlValue = octopusUrlValue,
            apiKeyValue = apiKeyValue,
            waitTimeoutSeconds = waitTimeoutSeconds.getOrElse(600),
            delayBetweenChecksSeconds = delayBetweenChecksSeconds.getOrElse(5),
            apiLogLevel = httpLogLevel.getOrElse(HttpLoggingInterceptor.Level.NONE),
            checkLogic = {
                determinAnyOngoingTask(
                    projectName = projectNameValue,
                    fromEnv = fromValue,
                    toEnv = toValue
                )
            }
        )
    }
}
