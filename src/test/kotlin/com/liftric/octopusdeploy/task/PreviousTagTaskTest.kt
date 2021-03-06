package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.extensionName
import com.liftric.octopusdeploy.shell
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class PreviousTagTaskTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testExecute() {
        println(testProjectDir.root.absolutePath)
        setupBuild()
        val result = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("previousTag")
            .withPluginClasspath()
            .build()
        println(result.output)
        assertEquals(TaskOutcome.SUCCESS, result.task(":previousTag")?.outcome)
        File("${testProjectDir.root.absolutePath}/build/$extensionName/previousTagName").apply {
            assertTrue(exists())
            assertEquals("first-one", readText())
        }
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
 version.set("whatever")
 packageName.set("whatever")
 serverUrl.set("whatever")
}
"""
            )
        }
        testProjectDir.root.setupGitRepo()
    }
}
