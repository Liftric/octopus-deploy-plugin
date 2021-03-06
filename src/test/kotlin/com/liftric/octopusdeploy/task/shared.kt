package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.shell
import junit.framework.TestCase.assertEquals
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

private val logger: Logger = LoggerFactory.getLogger("shared")
fun File.setupGitRepo() {
    println("setupGitRepo=${this.absolutePath}")
    try {
        verboseTestShell("git init .")
        verboseTestShell("git add .")
        verboseTestShell("git config user.email \"you@example.com\"")
        verboseTestShell("git config user.name \"Your Name\"")
        verboseTestShell("git commit -m \"initial commit\"")
        verboseTestShell("git tag first-one")
        verboseTestShell("touch secondfile")
        verboseTestShell("git add .")
        verboseTestShell("git commit -m \"second commit\"")
        verboseTestShell("git tag second-one")
    } catch (e: Exception) {
        println(e.message)
        e.printStackTrace()
        val (exitCode, inputText, errorText) = this.shell("ls -lah", logger)
        println("exitCode=$exitCode")
        println("inputText=$inputText")
        println("errorText=$errorText")
        throw e
    }
}

private fun File.verboseTestShell(cmd: String) {
    println("verboseTestShell=$cmd")
    val (exitCode, inputText, errorText) = this.shell(cmd, logger)
    println("verboseTestShell exitCode=$exitCode")
    println("verboseTestShell inputText=$inputText")
    println("verboseTestShell errorText=$errorText")
    exitCode mustBe 0
}

infix fun Int.mustBe(expected: Int) {
    assertEquals(expected, this)
}
