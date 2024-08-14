package com.liftric.octopusdeploy.task

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.liftric.octopusdeploy.extensions.BuildInformationCli
import com.liftric.octopusdeploy.api.CommitCli
import com.liftric.octopusdeploy.api.WorkItem
import com.liftric.octopusdeploy.extensions.BuildInformationAdditionBuilder
import junit.framework.TestCase.*
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class CreateBuildInformationBuilderTaskTest {
    @get:Rule
    val outputDir = TemporaryFolder()

    @Test
    fun testParseJira() {
        val project: Project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.liftric.octopus-deploy-plugin")

        assertTrue(project.tasks.getByName("createBuildInformation") is CreateBuildInformationTask)

        val task = project.tasks.getByName("createBuildInformation") as CreateBuildInformationTask
        val baseJiraUrl = "https://testric.atlassian.net/browser/"

        val outputFileBuildInformation = outputDir.newFile("build-information.json")
        task.apply {
            packageName.set("test-package")
            commits.set(createTestCommits())
            version.set("2.1.4")
            buildInformationAddition.set(BuildInformationAdditionBuilder(project).apply{
                Id.set("foo")
            })
            outputFile.set(outputFileBuildInformation)
            issueTrackerName.set("Jira")
            parseCommitsForJiraIssues.set(true)
            jiraBaseBrowseUrl.set(baseJiraUrl)
        }
        task.execute()
        assertTrue(task.outputFile.get().asFile.exists())
        val jsonText = task.outputFile.get().asFile.readText()
        val taskResult = jacksonObjectMapper().readValue<BuildInformationCli>(jsonText)
        assertTrue(taskResult.Id == "foo")
        assertNotNull(taskResult.WorkItems)
        taskResult.WorkItems.let { workItems ->
            assertEquals(2, workItems.size)
            workItems.verify("LIF-71", baseJiraUrl)
            workItems.verify("LIF-72", baseJiraUrl)
        }
    }

    private fun createTestCommits(): List<CommitCli> {
        return listOf(
            testCommit1,
            testCommit2,
            testCommit3,
            testCommit4,
            testCommit5
        )
    }

    companion object {
        val testCommit1 = CommitCli(
            "7119e5a28ef691cf95ec98cbf8b2e6bc4b1d84fc",
            "null/commit/7119e5a28ef691cf95ec98cbf8b2e6bc4b1d84fc",
            "[Gradle Release Plugin] - pre tag commit:  '0.1.44'.  "
        )
        val testCommit2 = CommitCli(
            "c6dc72fa67b5eb8fb134153ab240b169dea0d565",
            "null/commit/c6dc72fa67b5eb8fb134153ab240b169dea0d565",
            "Merge branch 'feature/test-octopus-integration' into 'master'  "
        )
        val testCommit3 = CommitCli(
            "cb230f92c95ee1118d1348a9e96d6d45e7c611c9",
            "null/commit/cb230f92c95ee1118d1348a9e96d6d45e7c611c9",
            "feat(build): dummy commit for octopus - LIF-71 "
        )
        val testCommit4 = CommitCli(
            "cb230f92c95ee1118d1348a9e96d6d35e7c611c9",
            "null/commit/cb230f92c95ee1218d1348a9e96d6d45e7c611c9",
            "feat(build): dummy commit 2 for octopus - LIF-72"
        )
        val testCommit5 = CommitCli(
            "0f1352918b05338eba2275cbab8d33601a810251",
            "null/commit/0f1352918b05338eba2275cbab8d33601a810251",
            "[Gradle Release Plugin] [ci skip] - new version commit:  '0.1.44-SNAPSHOT'"
        )
    }
}

private fun List<WorkItem>.verify(
    key: String,
    baseJiraUrl: String
) {
    assertEquals(1, count { it.Id == key })
    first { it.Id == key }.let { workItem ->
        assertEquals(workItem.Id, key)
        assertEquals(workItem.LinkUrl, "${baseJiraUrl}${key}")
    }
}
