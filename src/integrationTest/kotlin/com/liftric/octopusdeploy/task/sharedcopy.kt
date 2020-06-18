package com.liftric.octopusdeploy.task

import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import java.io.File

/**
 * it didn't compile without this, no clue why this happened all of the sudden
 */
fun File.setupGitRepoCopy() {
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
        val (exitCode, inputText, errorText) = this.shell("ls -lah")
        println("exitCode=$exitCode")
        println("inputText=$inputText")
        println("errorText=$errorText")
        throw e
    }
}

private fun File.verboseTestShell(cmd: String) {
    println("verboseTestShell=$cmd")
    val (exitCode, inputText, errorText) = this.shell(cmd)
    println("verboseTestShell exitCode=$exitCode")
    println("verboseTestShell inputText=$inputText")
    println("verboseTestShell errorText=$errorText")
    exitCode mustBe 0
}

infix fun Int.mustBe(expected: Int) {
    assertEquals(expected, this)
}

internal fun shell(cmd: String): ShellResult = File(".").shell(cmd)

internal fun File.shell(cmd: String): ShellResult {
    val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd), emptyArray(), this)
    val exitCode = process.waitFor()
    val inputText = process.inputStream.bufferedReader().readText().trim()
    val errorText = process.errorStream.bufferedReader().readText().trim()
    return ShellResult(exitCode, inputText, errorText)
}

data class ShellResult(val exitCode: Int, val inputText: String, val errorText: String)
