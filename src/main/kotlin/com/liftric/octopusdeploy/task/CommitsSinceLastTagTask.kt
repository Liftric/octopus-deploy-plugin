package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.shell
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.property
import java.io.File

open class CommitsSinceLastTagTask : DefaultTask() {
    init {
        group = "octopus"
        description =
            "Calls git log to receive all commits since the previous tag or the first commit of the current history."
        outputs.upToDateWhen { false }
    }

    @InputDirectory
    lateinit var workingDir: File

    @Input
    lateinit var commitLinkBaseUrl: String

    @InputFile
    @Optional
    var firstCommitFile: File? = null

    @InputFile
    @Optional
    var previousTagFile: File? = null

    @Input
    @Optional
    val useShortCommitHashes: Property<Boolean> = project.objects.property()

    @Input
    var commits: List<CommitCli> = emptyList()

    @TaskAction
    fun execute() {
        val useShortHash = useShortCommitHashes.getOrElse(true)

        val previousTag: String? = previousTagFile?.readText()
        if (previousTag == null) {
            logger.info("couldn't get previous tag, will use the first commit instead.")
        }
        val firstCommitHash: String =
            firstCommitFile?.readText() ?: error("couldn't read firstCommitFile!")
        val (exitCode, inputText, errorText) = workingDir.shell(
            "git log --pretty='format:%H#%s \\(%an\\)' ${previousTag ?: firstCommitHash}..HEAD",
            logger
        )
        if (exitCode == 0) {
            logger.info("previous tag: $inputText")
            commits = inputText.trim()
                .split("\n")
                .map {
                    it.split("#")
                }.filter { it.size >= 2 }
                .map {
                    CommitCli(
                        Id = it[0].shorten(useShortHash),
                        Comment = it.subList(1, it.size).joinToString(" ").replace("\\", ""),
                        LinkUrl = "${commitLinkBaseUrl.removeSuffix("/")}/${it[0]}"
                    )
                }.also { commits ->
                    commits.forEach {
                        logger.debug(it.toString())
                        println(it.toString())
                    }
                }
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
