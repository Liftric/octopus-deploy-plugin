package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import okhttp3.logging.HttpLoggingInterceptor
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

    @Input
    val waitForReleaseDeployments: Property<Boolean> = project.objects.property()

    @Input
    val waitTimeoutSeconds: Property<Long> = project.objects.property()

    @Input
    val delayBetweenChecksSeconds: Property<Long> = project.objects.property()

    /**
     * Configures the http logging of the underlying okhttp client used for octopus api requests
     */
    @Input
    val httpLogLevel: Property<HttpLoggingInterceptor.Level> = project.objects.property()

    @TaskAction
    fun execute() {
        val awaitReleases = waitForReleaseDeployments.getOrElse(false)
        val packageNameValue = packageName.get()
        val versionValue = version.get()
        val octopusUrlValue = octopusUrl.get()
        val apiKeyValue = apiKey.get()
        val (exitCode, inputText, errorText) = listOf(
            "octo",
            "build-information",
            "--server=$octopusUrlValue",
            "--apiKey=$apiKeyValue",
            "--file",
            buildInformation?.absolutePath ?: error("couldn't find build-information.json"),
            "--package-id",
            packageNameValue,
            "--version=$versionValue",
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
        if (awaitReleases.not()) return
        awaitReleaseLogic(
            octopusUrlValue = octopusUrlValue,
            apiKeyValue = apiKeyValue,
            waitTimeoutSeconds = waitTimeoutSeconds.getOrElse(600),
            delayBetweenChecksSeconds = delayBetweenChecksSeconds.getOrElse(5),
            apiLogLevel = httpLogLevel.getOrElse(HttpLoggingInterceptor.Level.NONE),
            checkLogic = { determinAnyOngoingTask(packageNameValue = packageNameValue, versionValue = versionValue) }
        )
    }


}
