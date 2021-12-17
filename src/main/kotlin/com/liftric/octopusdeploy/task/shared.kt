package com.liftric.octopusdeploy.task

import com.liftric.octopusdeploy.api.ProgressEnum
import com.liftric.octopusdeploy.rest.*
import kotlinx.coroutines.*
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Duration
import kotlin.time.seconds

/**
 * repeatedly poll the octopus api for all releases for the given [packageNameValue] [versionValue] combination.
 *
 * @param checkLogic Remoting used to determin if any task is ongoing.
 *                   Difference between upload and progress makes this necessary.
 * @param initialWaitSeconds Octopus server might need longer for the deployment to trigger, so we can define an
 *                           additional, initial, minimum wait before the actual release check logic even happens
 */
internal fun awaitReleaseLogic(
    octopusUrlValue: String,
    apiKeyValue: String,
    waitTimeoutSeconds: Long,
    delayBetweenChecksSeconds: Long,
    apiLogLevel: HttpLoggingInterceptor.Level,
    checkLogic: OctoApiClient.() -> Boolean,
    initialWaitSeconds: Long?
) {
    println("checking is upload triggered any release with auto deployment (${waitTimeoutSeconds}s max)")

    val octoApiClient = OctoApiClient(
        octopusUrlValue,
        apiKeyValue,
        apiLogLevel
    )

    val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
        octoApiClient.shutdown()
    }
    runBlocking(handler) {
        initialWaitSeconds?.let {
            println("initial check delay: ${it}s")
            delay(it * 1000)
            println("initial check delay done")
        }
        withTimeout(Duration.ofSeconds(waitTimeoutSeconds).toMillis()) {
            var anyOngoingTask = octoApiClient.checkLogic()
            while (anyOngoingTask) {
                println("found ongoing task, delaying $delayBetweenChecksSeconds before next check")
                delay(Duration.ofSeconds(delayBetweenChecksSeconds).toMillis())
                anyOngoingTask = octoApiClient.checkLogic()
            }
        }
    }
    println("nothing found or all done")
}

/**
 * Check for ongoing tasks after uploading
 */
internal fun OctoApiClient.determinAnyOngoingTask(
    packageNameValue: String,
    versionValue: String
): Boolean {
    val releases = createService(Releases::class.java)
    val progressions = createService(Progressions::class.java)
    return releases.allFiltered(packageNameValue, versionValue)
        .any {
            progressions.get(it.id).execute().body()?.anyOngoingTask() ?: false
        }
}

/**
 * Check for ongoing tasks after progressing
 */
internal fun OctoApiClient.determinAnyOngoingTask(
    projectName: String,
    fromEnv: String,
    toEnv: String
): Boolean {
    val projects = createService(Projects::class.java)
    val releases = createService(Releases::class.java)
    val progressions = createService(Progressions::class.java)

    val projectId = projects.get(projectName).execute().body()?.id ?: error("error getting $projectName project")
    val latestRelease = releases.getByProjectIdPaginated(projectId).first()
    val progression =
        progressions.get(latestRelease.id).execute().body()
            ?: error("error getting latest $projectName progression")
    require(progression.phases.first { it.name == fromEnv }.progress == ProgressEnum.Complete) { "$fromEnv not Complete!" }
    return progression.phases.first { it.name == toEnv }.deployments.any { it.task?.isCompleted == false }
}
