package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.BuildInformationCli
import com.liftric.octopusdeploy.api.CommitCli
import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.kotlin.dsl.property
import java.io.File

abstract class AbstractBuildInformationTask : DefaultTask() {

    @Input
    val packageName: Property<String> = project.objects.property()

    @Input
    val version: Property<String> = project.objects.property()

    @Input
    lateinit var commits: List<CommitCli>

    @Input
    var buildInformationAddition: BuildInformationCli.() -> Unit = {}

    @OutputFile
    @Optional
    var outputFile: File? = null

    @Input
    @Optional
    val issueTrackerName: Property<String> = project.objects.property()

    @Input
    @Optional
    val parseCommitsForJiraIssues: Property<Boolean> = project.objects.property()

    @Input
    @Optional
    val jiraBaseBrowseUrl: Property<String> = project.objects.property()
}
