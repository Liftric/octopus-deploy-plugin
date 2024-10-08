package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.extensionName
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class FirstCommitHashTaskTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testExecute() {
        println(testProjectDir.root.absolutePath)
        setupBuild()
        val result = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("firstCommitHash")
            .withPluginClasspath()
            .build()
        println(result.output)
        assertEquals(TaskOutcome.SUCCESS, result.task(":firstCommitHash")?.outcome)
        File("${testProjectDir.root.absolutePath}/build/$extensionName/firstCommitHash").apply {
            assertTrue(exists())
            // this is the first commit hash of the octopus-deploy-plugin git repo itself
            assertEquals("9c82501b25fd6c03bd6f3074739496b498cf3938", readText())
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
    apiKey.set("fakefake")
    version.set("whatever")
    gitRoot.set(file("${File("").absolutePath}"))
    packageName.set("whatever")
    serverUrl.set("whatever")
}
tasks {
    val firstCommitHash by existing {
        doLast {
            println("firstCommitHash=${'$'}{file("build/octopus/firstCommitHash").readText()}")
        }
    }
}
"""
            )
        }
    }
}