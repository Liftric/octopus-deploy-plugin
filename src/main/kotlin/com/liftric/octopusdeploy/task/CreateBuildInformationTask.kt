package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.Commit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class CreateBuildInformationTask : DefaultTask() {
    init {
        group = "octopus"
        description = "Creates the octopus build-information file."
        outputs.upToDateWhen { false }
    }

    lateinit var packageName: String
    lateinit var version: String
    lateinit var commits: List<Commit>

    @TaskAction
    fun execute() {
        println(packageName)
        println(version)
        println(commits)
    }
}