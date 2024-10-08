package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.listProperty
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Gradle would require more information to cache this task")
abstract class CreateReleaseTask : DefaultTask() {
    @get:Input
    abstract val octopusUrl: Property<String>

    @get:Input
    abstract val apiKey: Property<String>

    @get:Input
    abstract val projectName: Property<String>

    @get:Input
    @get:Optional
    abstract val releaseNumber: Property<String>

    @get:Input
    @get:Optional
    abstract val dryRun: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val waitForReleaseDeployments: Property<Boolean>

    @get:Optional
    @get:InputFile
    abstract val releaseNoteFile: RegularFileProperty

    /**
     * Version number to use for a package
     * in the release. Format: StepName:Version or
     * PackageID:Version or
     * StepName:PackageName:Version. StepName,
     * PackageID, and PackageName can be replaced with
     * an asterisk. An asterisk will be assumed for
     * StepName, PackageID, or PackageName if they are
     * omitted.
     */
    @Input
    @Optional
    val packages: ListProperty<String> = project.objects.listProperty()

    @TaskAction
    fun execute() {
        val dryRun = if(dryRun.getOrElse(false)) {
            "--whatif"
        } else {
            null
        }
        val waitForReleaseDeployments = if(waitForReleaseDeployments.getOrElse(false)) {
            "--waitForDeployment"
        } else {
            null
        }
        val releaseNotes = releaseNoteFile.orNull
        println(" DEBUG: releaseNotes=$releaseNotes")
        val (exitCode, inputText, errorText) = listOfNotNull(
            "octo",
            "create-release",
            "--server=${octopusUrl.get()}",
            "--apiKey=${apiKey.get()}",
            "--project=\"${projectName.get()}\"",
            releaseNumber.orNull?.let { "--releaseNumber=\"$it\"" },
            *packages.orNull?.map { "--package=\"$it\"" }?.toTypedArray() ?: emptyArray(),
            releaseNotes?.let { "--releaseNoteFile=\"${it.asFile.absolutePath}\"" },
            waitForReleaseDeployments,
            dryRun,
        ).joinToString(" ").let { shell(it, logger) }
        if (exitCode == 0) {
            println(inputText)
            println(errorText)
        } else {
            logger.error("octo create-release returned non-zero exitCode: $exitCode")
            logger.error(inputText)
            throw IllegalStateException("octo create-release exitCode: $exitCode")
        }
    }
}
