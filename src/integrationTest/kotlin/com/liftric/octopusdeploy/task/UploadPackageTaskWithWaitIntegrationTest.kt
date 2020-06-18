package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.apiKey
import com.liftric.octopusdeploy.getPackageResponse
import com.liftric.octopusdeploy.serverUrl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.random.Random

class UploadPackageTaskWithWaitIntegrationTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testExecute() {
        val major = Random.Default.nextInt(0, 100)
        val minor = Random.Default.nextInt(0, 100)
        val micro = Random.Default.nextInt(0, 100)
        println(testProjectDir.root.absolutePath)
        setupBuild(major, minor, micro)

        val result = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("build", "uploadPackage")
            .withPluginClasspath()
            .build()
        println(result.output)
        assertEquals(TaskOutcome.SUCCESS, result.task(":uploadPackage")?.outcome)

        val packageItem = getPackageResponse()
            .items
            ?.filter {
                it.version == "$major.$minor.$micro"
            }
            ?.firstOrNull()
        assertNotNull(packageItem)
        assertEquals(".jar", packageItem?.fileExtension)

        val secondResult = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("build", "uploadPackage")
            .withPluginClasspath()
            .buildAndFail()
        println(secondResult.output)
        // override is not set
        assertEquals(TaskOutcome.FAILED, secondResult.task(":uploadPackage")?.outcome)
    }

    fun setupBuild(major: Int, minor: Int, micro: Int) {
        testProjectDir.newFile("build.gradle.kts").apply {
            writeText(
                """
import com.liftric.octopusdeploy.task.UploadPackageTask
plugins {
    java
    id("com.liftric.octopus-deploy-plugin")
}
group = "com.liftric.test"
version = "$major.$minor.$micro"

tasks {
    withType<Jar> {
        archiveFileName.set(
            "${'$'}{archiveBaseName.get()
                    .removeSuffix("-")}.${'$'}{archiveVersion.get()}.${'$'}{archiveExtension.get()}"
        )
    }
    withType<UploadPackageTask>{
        waitForReleaseDeployments.set(true)
        waitTimeoutSeconds.set(1L)
        delayBetweenChecksSeconds.set(2L)
        httpLogLevel.set(okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS)
    }
}
octopus {
    serverUrl.set("$serverUrl")
    apiKey.set("$apiKey")

    generateChangelogSinceLastTag = true

    val jar by tasks.existing(Jar::class)
    packageName.set(jar.get().archiveBaseName.get().removeSuffix("-"))
    version.set(jar.get().archiveVersion.get())
    pushPackage.set(jar.get().archiveFile)
        
}
"""
            )
        }
        testProjectDir.root.setupGitRepoCopy()
    }
}
