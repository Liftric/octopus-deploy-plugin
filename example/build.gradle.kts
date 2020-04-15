import com.liftric.vault.vault

plugins {
    java
    id("com.liftric.vault-client-plugin") version ("1.0.0")
    id("com.liftric.octopus-deploy-plugin") version "whatever"
}
group = "com.liftric"
version = "1.2.3"

vault {
    vaultTokenFilePath = "${System.getProperty("user.home")}${File.separator}.vault-token"
}
tasks {
    withType<Jar> {
        archiveFileName.set(
            "${archiveBaseName.get()
                .removeSuffix("-")}.${archiveVersion.get()}.${archiveExtension.get()}"
        )
    }
}
octopus {
    val octopus: Map<String, String> = project.vault("secret/credentials/liftric/octopus_deploy")
    apiKey = octopus.getValue("api_key")
    serverUrl = octopus.getValue("server_url")

    commitLinkBaseUrl = "${System.getenv("CI_PROJECT_URL")?.removeSuffix("/")}/commit"
    generateChangelogSinceLastTag = true
    val jar by tasks.existing(Jar::class)
    pushPackage = jar.get().archiveFile.get().asFile
    version = jar.get().archiveVersion.get()
    packageName = jar.get().archiveBaseName.get().removeSuffix("-")
}