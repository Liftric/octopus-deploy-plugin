# octopus-deploy-plugin (gradle plugin)
![GitHub](https://img.shields.io/github/license/Liftric/octopus-deploy-plugin)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/Liftric/octopus-deploy-plugin)
[![CircleCI](https://circleci.com/gh/Liftric/octopus-deploy-plugin/tree/master.svg?style=svg)](https://circleci.com/gh/Liftric/octopus-deploy-plugin/tree/master)

![Gradle meets Octopus Deploy](./gradle-octopus.webp)

The octopus-deploy-plugin generates build-information and uploads packages and generated build-information to Octopus Deploy instances.

Requirements:
 * [Octopus Deploy CLI](https://octopus.com/downloads/octopuscli)
 * git (for commit/changelog calculation)
 
# Usage
The plugin can be configured with the `octopus` DSL block:
```kotlin
plugins {
    id("com.liftric.octopus-deploy-plugin") version "whatever"
}
// ...
octopus {
    serverUrl.set("http://localhost:8080/")
    apiKey.set("API-TESTTEST123TRESDTSDD")

    generateChangelogSinceLastTag.set(true)

    val jar by tasks.existing(Jar::class)
    packageName.set(jar.get().archiveBaseName.get())
    version.set(jar.get().archiveVersion.get())
    pushPackage.set(jar.get().archiveFile)
}
```

See the example directory for a minimal, working example.

## tasks
The following tasks are added by the plugin:

Task | Description
---|---
previousTag | Calls git describe to receive the previous tag name. Will fail if no tag is found.
firstCommitHash | Calls git log to get the first commit hash of the current history tree
commitsSinceLastTag | Calls git log to receive all commits since the previous tag or the first commit of the current history.
createBuildInformation | Creates the octopus build-information file.
uploadBuildInformation | Uploads the created octopus build-information file.
uploadPackage | Uploads the package to octopus.
PromoteReleaseTask | Promotes octopus project from one environment to another

For normal use-cases, only `uploadBuildInformation` and `uploadPackage` are needed to call explicitly. Depending
tasks will be called implicitly by both as needed.

**Noteworthy**: The build-information can be uploaded before the package itself. 
Useful when creating automatic releases and using the commits in the release notes in octopus.

### PromoteReleaseTask
The PromoteReleaseTask has no default implementation and must be created explicitly:
```kotlin
import com.liftric.octopusdeploy.task.PromoteReleaseTask
// ...
tasks {
    val devToDemo by creating(PromoteReleaseTask::class) {
        projectName.set("example-project")
        from.set("dev")
        to.set("demo")
    }
}
```
Calling `./gradlew devToDemo` on this example will promote the current **from** release of the octopus project **example-project**
to the **to** environment.

## naming
Octopus deploy expects the name and version in the following format: `<name>.<version>.<extension>`

For Java archives, `<name>-<version>.<extension>` is conventional, so should be changed to get picked up properly by octopus.
Otherwise the first version number part will be parsed as part of the name.

## configuration
After the plugin is applied, the octopus extension is registered with the following settings:

Property | Description | default value 
---|---|---
apiKey | Octopus deploy server API key | -
serverUrl | Octopus deploy server URL | -
generateChangelogSinceLastTag | Enable to calculate the commits for the changelog when uploading build-information (needs git installed) | false
commitLinkBaseUrl | Prefix / Baseurl for the build-information commit urls | http://git.example.com/repo/commits/
outputDir | Output folder for files generated by the plugin | build/octopus
gitRoot | Directory to run the git helpers in. By default the projects root dir | project.rootDir
pushPackage | Target file (package) which will be uploaded to octopus | -
version | Package version | -
packageName | Package name | -
buildInformationOverwriteMode | octo build-information OverwriteMode | -
pushOverwriteMode | octo push OverwriteMode | -
buildInformationAddition | Customize the final octopus build-information before uploading | {}
gitlab | Default `buildInformationAddition` implementation adding context from the CI environment for Gitlab CI. Also sets `commitLinkBaseUrl`. | not applied
httpLogLevel | configures the http logging while using the Octopus API | `HttpLoggingInterceptor.Level.NONE`
issueTrackerName | When parsing issues the target issue tracker name is needed. Currently only `Jira` supported | **optional/none**
parseCommitsForJiraIssues | Enable Jira Issue parsing. This needs the changelog generation enabled to parse the commits there. | **optional/none**
jiraBaseBrowseUrl | For proper Jira URLs we need the base URL, something like `https://testric.atlassian.net/browse/`. | **optional/none**
useShortCommitHashes | Use short (7 char) commit hashes. | true

`generateChangelogSinceLastTag` extracts all commits between the HEAD and the last tag. 
If no tag is found, the first commit in the history tree is used instead.

You can configure `serverUrl` and `apiKey` using a provider which enables configuring them on demand, not at configuration time:
```kotlin
apiKey.set(provider {
    "API-TESTTEST123TRESDTSDD"
    // read from file / vault / etc.
})
```

To customize the build information, use the `buildInformationAddition` block:
```kotlin
buildInformationAddition.set({
    Id = "custom-id"
    // Add other properties as needed
})

```

For GitLab CI integration, set the `gitlab` block:
```kotlin
gitlab {
    Id = "custom-id"
    // Add other properties as needed
}
```

### task specific configuration
Both the **PromoteReleaseTask** and **UploadPackageTask** provide support for waiting
for any deployment on octopus deploy and will block task finishing until all octopus deploy tasks are finished.

Basically if the upload or progression triggers a release in octopus deploy, the tasks will wait until they are completed.

Configuration is done on the tasks themself:

Property | Description                                                 | default value 
---|---|---
waitForReleaseDeployments | If the task should wait                                     | false
waitTimeoutSeconds | max time to poll the Ocotopus API                           | 600
delayBetweenChecksSeconds | how long to delay between polls                             | 5
initialWaitSeconds | initial delay before waitForReleaseDeployments logic starts | -

Example:
```kotlin
import com.liftric.octopusdeploy.task.PromoteReleaseTask
import com.liftric.octopusdeploy.task.UploadPackageTask
// ...
tasks {
    val devToDemo by creating(PromoteReleaseTask::class) {
        waitForReleaseDeployments.set(true)
        projectName.set("example-project")
        from.set("dev")
        to.set("staging")
    }
    withType<UploadPackageTask>() {
        waitForReleaseDeployments.set(true)
    }
}
```

# Credits
Thanks to [Octopus Deploy](https://octopus.com/) for the permission to use the header image.
