package com.liftric.octopusdeploy.task

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
        result.task(":commitsSinceLastTag")?.outcome == TaskOutcome.SUCCESS
    }

    fun setupBuild() {
        testProjectDir.newFile("build.gradle.kts").apply {
            writeText(
                """
plugins {
    id("com.liftric.octopus-deploy-plugin")
}
octopus {
 apiKey = "whatever"
 version = "whatever"
 packageName = "whatever"
 serverUrl = "whatever"
}"""
            )
        }
        testProjectDir.root.setupGitRepo()
    }
}