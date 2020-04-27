package com.liftric.octopusdeploy.task

import junit.framework.TestCase
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
        println(testProjectDir.root.absolutePath)
        setupBuild()
        val result = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("commitsSinceLastTag")
            .withPluginClasspath()
            .build()
        println(result.output)
        assertEquals(TaskOutcome.SUCCESS, result.task(":commitsSinceLastTag")?.outcome)
    }

    fun setupBuild() {
        testProjectDir.newFile("build.gradle.kts").apply {
            writeText(
                """
plugins {
    id("com.liftric.octopus-deploy-plugin")
}
octopus {
 apiKey.set("whatever")
 version = "whatever"
 packageName = "whatever"
 serverUrl.set("whatever")
}"""
            )
        }
        testProjectDir.root.setupGitRepo()
    }
}
