package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.liftric.octopusdeploy.extensionName
import com.liftric.octopusdeploy.extensions.BuildInformationCli
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File

class CreateBuildInformationGitlabCiTaskTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testExecute() {
        println(testProjectDir.root.absolutePath)
        setupBuild()

        val envVars = mapOf(
            "PATH" to System.getenv("PATH"),
            "CI_PIPELINE_IID" to "123",
            "CI_PIPELINE_URL" to "https://gitlab.com/project/-/pipelines/123",
            "CI_COMMIT_REF_NAME" to "main",
            "CI_PROJECT_URL" to "https://gitlab.com/project",
            "CI_COMMIT_SHORT_SHA" to "abc1234",
            "CI_COMMIT_SHA" to "abc1234defghijk",
            "GITLAB_USER_NAME" to "Test User"
        )

        val result = GradleRunner.create()
            .forwardOutput()
            .withProjectDir(testProjectDir.root)
            .withArguments("createBuildInformation")
            .withEnvironment(envVars)
            .withPluginClasspath()
            .build()

        println(result.output)
        assertEquals(TaskOutcome.SUCCESS, result.task(":createBuildInformation")?.outcome)
        File("${testProjectDir.root.absolutePath}/build/$extensionName/build-information.json").apply {
            assertTrue(exists())
            val taskResult = jacksonObjectMapper().readValue<BuildInformationCli>(readText())
            println(taskResult)
            assertEquals("foo", taskResult.Id)
            assertEquals("GitLabCI", taskResult.BuildEnvironment)
            assertEquals(envVars["CI_PIPELINE_IID"], taskResult.BuildNumber)
            assertEquals(envVars["CI_PROJECT_URL"], taskResult.VcsRoot)
            assertEquals(envVars["CI_PIPELINE_URL"], taskResult.BuildUrl)
            assertEquals(envVars["CI_COMMIT_REF_NAME"], taskResult.Branch)
            assertEquals(envVars["CI_COMMIT_SHORT_SHA"], taskResult.VcsCommitNumber)
            assertEquals("${envVars["CI_PROJECT_URL"]}/commit/${envVars["CI_COMMIT_SHA"]}", taskResult.VcsCommitUrl)
            assertEquals(envVars["GITLAB_USER_NAME"], taskResult.LastModifiedBy)
        }
    }

    fun setupBuild() {
        testProjectDir.newFile("build.gradle.kts").apply {
            writeText(
                """
import com.liftric.octopusdeploy.extensions.*
plugins {
    id("com.liftric.octopus-deploy-plugin")
}
octopus {
    apiKey.set("fakefake")
    version.set("whatever")
    gitRoot.set(file("${File("").absolutePath}"))
    packageName.set("whatever")
    serverUrl.set("whatever")
    gitlabCi.set(true)
    buildInformationAddition {
       Id.set("foo")
    }
}
"""
            )
        }
    }
}