plugins {
    java
    id("com.liftric.octopus-deploy-plugin") version "whatever"
}
group = "com.liftric"
version = "1.2.3"

tasks {
    withType<Jar> {
        archiveFileName.set(
            "${archiveBaseName.get()
                .removeSuffix("-")}.${archiveVersion.get()}.${archiveExtension.get()}"
        )
    }
}
octopus {
    serverUrl = "http://localhost:8080/"
    apiKey = "API-TESTTEST123TRESDTSDD"

    commitLinkBaseUrl = "${System.getenv("CI_PROJECT_URL")?.removeSuffix("/")}/commit"
    generateChangelogSinceLastTag = true

    val jar by tasks.existing(Jar::class)
    packageName = jar.get().archiveBaseName.get().removeSuffix("-")
    version = jar.get().archiveVersion.get()
    pushPackage = jar.get().archiveFile.get().asFile

}