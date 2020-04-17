plugins {
    java
    id("com.liftric.octopus-deploy-plugin") version "whatever"
}
group = "com.liftric"
version = "1.2.3"

tasks {
    withType<Jar> {
        // example-1.2.3.jar would be parsed as package: example-1 with version 2.3,
        // so the name is changed to example.1.2.3.jar to support the octopus deploy naming convention
        archiveFileName.set(
            "${archiveBaseName.get()
                .removeSuffix("-")}.${archiveVersion.get()}.${archiveExtension.get()}"
        )
    }
}
octopus {
    // targets the GITROOT/docker-compose.yml ocotpus deploy instance
    serverUrl = "http://localhost:8080/"
    apiKey = "API-TESTTEST123TRESDTSDD"

    commitLinkBaseUrl = "${System.getenv("CI_PROJECT_URL")?.removeSuffix("/")}/commit"
    generateChangelogSinceLastTag = true

    val jar by tasks.existing(Jar::class)
    packageName = jar.get().archiveBaseName.get().removeSuffix("-")
    version = jar.get().archiveVersion.get()
    pushPackage = jar.get().archiveFile.get().asFile

}
