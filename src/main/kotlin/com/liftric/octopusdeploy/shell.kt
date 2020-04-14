package com.liftric.octopusdeploy

import java.io.File

internal fun shell(cmd: String): ShellResult = File(".").shell(cmd)

internal fun File.shell(cmd: String): ShellResult {
    val process = Runtime.getRuntime().exec(arrayOf("sh", "-c", cmd), emptyArray(), this)
    val exitCode = process.waitFor()
    val inputText = process.inputStream.bufferedReader().readText().trim()
    val errorText = process.errorStream.bufferedReader().readText().trim()
    return ShellResult(exitCode, inputText, errorText)
}

data class ShellResult(val exitCode: Int, val inputText: String, val errorText: String)