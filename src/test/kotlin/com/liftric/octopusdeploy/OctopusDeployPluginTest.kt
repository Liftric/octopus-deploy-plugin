package com.liftric.octopusdeploy

import junit.framework.TestCase.assertNotNull
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class OctopusDeployPluginTest {
    @Test
    fun testApply() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.liftric.octopus-deploy-plugin")
        assertNotNull(project.plugins.getPlugin(OctopusDeployPlugin::class.java))
    }

    @Test
    fun testExtension() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.liftric.octopus-deploy-plugin")
        assertNotNull(project.octopus())
    }

    @Test
    fun testTasks() {
        val project = ProjectBuilder.builder().build()
        project.pluginManager.apply("com.liftric.octopus-deploy-plugin")
        assertNotNull(project.tasks.findByName("firstCommitHash"))
        assertNotNull(project.tasks.findByName("previousTag"))
    }
}