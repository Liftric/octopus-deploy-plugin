package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import okhttp3.logging.HttpLoggingInterceptor
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.property

open class UploadPackageTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Uploads the package to octopus."
        outputs.upToDateWhen { false }
    }

    @Input
    val octopusUrl: Property<String> = project.objects.property()

    @Input
    val apiKey: Property<String> = project.objects.property()

    @Input
    @Optional
    var overwriteMode: String? = null

    @Input
    @Optional
    val packageName: Property<String> = project.objects.property()

    @Input
    @Optional
    val version: Property<String> = project.objects.property()

    @Optional
    @InputFile
    val packageFile: RegularFileProperty = project.objects.fileProperty()

    @Input
    @Optional
    val waitForReleaseDeployments: Property<Boolean> = project.objects.property()

    @Input
    @Optional
    val waitTimeoutSeconds: Property<Long> = project.objects.property()

    /**
     * Octopus server might need longer for the deployment to trigger, so we can define an
     * additional, initial, minimum wait before the actual release check logic even happens
     */
    @Input
    @Optional
    val initialWaitSeconds: Property<Long> = project.objects.property()

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
        val (exitCode, inputText, errorText) = listOf(
            "octo",
            "push",
            "--server=$octopusUrlValue",
            "--apiKey=$apiKeyValue",
            "--package",
            packageFile.get().asFile.absolutePath ?: error("couldn't find build-information.json"),
            overwriteMode?.let { "--overwrite-mode=$it" }
        ).filterNotNull().joinToString(" ").let { shell(it, logger) }
        if (exitCode == 0) {
            println(inputText)
            println(errorText)
        } else {
            logger.error("octo push returned non-zero exitCode: $exitCode")
            logger.error(inputText)
            throw IllegalStateException("octo push exitCode: $exitCode")
        }
        if (awaitReleases.not()) return
        val packageNameValue = packageName.get()
        val versionValue = version.get()
        awaitReleaseLogic(
            octopusUrlValue = octopusUrlValue,
            apiKeyValue = apiKeyValue,
            waitTimeoutSeconds = waitTimeoutSeconds.getOrElse(600),
            delayBetweenChecksSeconds = delayBetweenChecksSeconds.getOrElse(5),
            apiLogLevel = httpLogLevel.getOrElse(HttpLoggingInterceptor.Level.NONE),
            checkLogic = {
                determinAnyOngoingTask(packageNameValue = packageNameValue, versionValue = versionValue)
            },
            initialWaitSeconds = initialWaitSeconds.orNull
        )
    }
}
