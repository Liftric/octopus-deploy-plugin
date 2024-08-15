package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.OverwriteMode
import com.liftric.octopusdeploy.shell
import okhttp3.logging.HttpLoggingInterceptor
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Gradle would require more information to cache this task")
abstract class UploadPackageTask : DefaultTask() {
    @get:Input
    abstract val octopusUrl: Property<String>

    @get:Input
    abstract val apiKey: Property<String>

    @get:Input
    @get:Optional
    abstract val overwriteMode: Property<OverwriteMode?>

    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val version: Property<String>

    @get:Optional
    @get:InputFile
    abstract val pushPackage: RegularFileProperty

    @get:Input
    @get:Optional
    abstract val waitForReleaseDeployments: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val waitTimeoutSeconds: Property<Long>

    /**
     * Octopus server might need longer for the deployment to trigger, so we can define an
     * additional, initial, minimum wait before the actual release check logic even happens
     */
    @get:Input
    @get:Optional
    abstract val initialWaitSeconds: Property<Long>

    @get:Input
    @get:Optional
    abstract val delayBetweenChecksSeconds: Property<Long>

    /**
     * Configures the http logging of the underlying okhttp client used for octopus api requests
     */
    @get:Input
    @get:Optional
    abstract val httpLogLevel: Property<HttpLoggingInterceptor.Level>

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
            pushPackage.get().asFile.absolutePath ?: error("couldn't find pushPackage"),
            overwriteMode.orNull?.let { "--overwrite-mode=$it" }
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
