package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault(because = "Gradle would require more information to cache this task")
abstract class CommitsSinceLastTagTask : DefaultTask() {
    @get:Internal
    abstract val gitRoot: DirectoryProperty

    @get:Input
    abstract val commitLinkBaseUrl: Property<String>

    @get:InputFile
    abstract val firstCommitFile: RegularFileProperty

    @get:InputFile
    @get:Optional
    abstract val previousTagFile: RegularFileProperty

    @get:InputFile
    @get:Optional
    abstract val gitlabCi: Property<Boolean>

    @get:Input
    abstract val useShortCommitHashes: Property<Boolean>

    @get:Internal
    abstract val commits: ListProperty<CommitCli>

    @TaskAction
    fun execute() {
        val useShortHash = useShortCommitHashes.getOrElse(true)
        val gitlabCiValue = gitlabCi.getOrElse(false)
        val commitLinkBaseUrlValue = if (gitlabCiValue) {
            "${System.getenv("CI_PROJECT_URL")?.removeSuffix("/")}/commit/"
        } else {
            commitLinkBaseUrl.get()
        }

        val previousTag: String? = previousTagFile.orNull?.asFile?.readText()
        if (previousTag == null) {
            logger.info("Couldn't get previous tag, will use the first commit instead.")
        }
        val firstCommitHash: String = firstCommitFile.get().asFile.readText()
        val (exitCode, inputText, errorText) = gitRoot.get().asFile.shell(
            "git log --pretty='format:%H#%s \\(%an\\)' ${previousTag ?: firstCommitHash}..HEAD",
            logger
        )
        if (exitCode == 0) {
            logger.info("previous tag: $inputText")
            commits.set(inputText.trim()
                .split("\n")
                .map {
                    it.split("#")
                }.filter { it.size >= 2 }
                .map {
                    CommitCli(
                        Id = it[0].shorten(useShortHash),
                        Comment = it.subList(1, it.size).joinToString(" ").replace("\\", ""),
                        LinkUrl = "${commitLinkBaseUrlValue.removeSuffix("/")}/${it[0]}"
                    )
                }.onEach {
                    logger.debug(it.toString())
                    println(it.toString())
                })
        } else {
            logger.error("git describe returned non-zero exitCode: $exitCode")
            logger.error(errorText)
            throw IllegalStateException("git describe exitCode: $exitCode")
        }
    }
}

private fun String.shorten(useShortHash: Boolean): String = if (useShortHash && length > 6) {
    substring(0, 7)
} else {
    this
}
