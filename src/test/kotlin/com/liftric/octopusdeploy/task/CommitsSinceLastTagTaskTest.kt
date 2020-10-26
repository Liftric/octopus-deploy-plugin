package com.liftric.octopusdeploy.task

import junit.framework.TestCase.assertEquals
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class CommitsSinceLastTagTaskTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testExecute() {
        testCommitsSinceLastTag(expectedHashLength = 7, useLongHashes = false)
    }

    @Test
    fun testLongHashExecute() {
        testCommitsSinceLastTag(expectedHashLength = 40, useLongHashes = true)
    }

    private fun testCommitsSinceLastTag(expectedHashLength: Int, useLongHashes: Boolean) {
        println(testProjectDir.root.absolutePath)
        setupBuild(useLongHashes)
        val result = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("commitsSinceLastTag")
            .withPluginClasspath()
            .build()
        println(result.output)
        assertEquals(TaskOutcome.SUCCESS, result.task(":commitsSinceLastTag")?.outcome)
        result.output
            .split("\n")
            .filter { it.startsWith("CommitCli") }
            .firstOrNull { it.matches(Regex("CommitCli\\(Id=\\w{$expectedHashLength}?, LinkUrl=http.+?, Comment=.+?\\)")) }
            ?: error("didn't find $expectedHashLength char commit hash!")
    }

    fun setupBuild(useLongHashes: Boolean) {
        testProjectDir.newFile("build.gradle.kts").apply {
            writeText(
                """
plugins {
    id("com.liftric.octopus-deploy-plugin")
}
octopus {
 apiKey.set("whatever")
 version.set("whatever")
 packageName.set("whatever")
 serverUrl.set("whatever")
 ${if (useLongHashes) "useShortCommitHashes.set(false)" else ""}
}"""
            )
        }
        testProjectDir.root.setupGitRepo()
    }
}
