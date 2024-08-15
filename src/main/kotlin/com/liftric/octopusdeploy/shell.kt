package com.liftric.octopusdeploy

import org.slf4j.Logger
import java.io.File

internal fun shell(cmd: String, logger: Logger): ShellResult = File(".").shell(cmd, logger)

internal fun File.shell(cmd: String, logger: Logger): ShellResult {
    val tmpDir = System.getProperty("java.io.tmpdir")
    // GitHub Workflow Fix for Integration Test
    val env = System.getenv().toMutableMap()
    env["GITHUB_PATH"]?.let { githubPath ->
        env["PATH"] = "${env["PATH"]}:$githubPath"
    }
    val envArray = env.map { (key, value) -> "$key=$value" }.toTypedArray()

    // DOTNET_BUNDLE_EXTRACT_BASE_DIR is a workaround for https://github.com/dotnet/runtime/issues/3846
    val cmdarray = arrayOf("sh", "-c", "DOTNET_BUNDLE_EXTRACT_BASE_DIR=$tmpDir $cmd")
    val cmdDir = this
    logger.debug("shell: cmdarray='${cmdarray.toList()}'")
    logger.debug("shell: cmdDir='$cmdDir'")
    logger.debug("shell: tmpDir='$tmpDir'")
    val process = Runtime.getRuntime().exec(cmdarray, envArray, cmdDir)
    val exitCode = process.waitFor()
    val inputText = process.inputStream.bufferedReader().readText().trim()
    val errorText = process.errorStream.bufferedReader().readText().trim()
    val result = ShellResult(exitCode, inputText, errorText)
    logger.debug("shell: result=$result")
    return result
}

data class ShellResult(val exitCode: Int, val inputText: String, val errorText: String)
