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
    commitLinkBaseUrl = "${System.getenv("CI_PROJECT_URL")?.removeSuffix("/")}/commit"
    generateChangelogSinceLastTag = true
    val jar by tasks.existing(Jar::class)
    pushPackage = jar.get().archiveFile.get().asFile
    version = jar.get().archiveVersion.get()
    packageName = jar.get().archiveBaseName.get().removeSuffix("-")
}