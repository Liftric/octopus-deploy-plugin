import net.nemerosa.versioning.tasks.VersionDisplayTask

plugins {
    kotlin("jvm") version "1.3.61"
    `java-gradle-plugin`
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.3"
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.10.1"
    id("net.nemerosa.versioning") version "2.12.0"
}

group = "com.liftric.octopusdeploy"
allprojects {
    version = with(versioning.info) {
        if (branch == "HEAD" && dirty.not()) {
            tag
        } else {
            full
        }
    }
}

repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("com.github.stefanbirkner:system-rules:1.19.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<VersionDisplayTask> {
        doLast {
            println("[VersionDisplayTask] version=$version")
        }
    }
    val createVersionFile by creating {
        doLast {
            mkdir(buildDir)
            file("$buildDir/version").apply {
                if (exists()) delete()
                createNewFile()
                writeText(project.version.toString())
            }
        }
    }
    val build by existing
    val publish by existing
    val publishToMavenLocal by existing
    listOf(build.get(), publish.get(), publishToMavenLocal.get()).forEach { it.dependsOn(createVersionFile) }

    val setupPluginsLogin by creating {
        // see: https://github.com/gradle/gradle/issues/1246
        val publishKey: String? = System.getenv("GRADLE_PUBLISH_KEY")
        val publishSecret: String? = System.getenv("GRADLE_PUBLISH_SECRET")
        if (publishKey != null && publishSecret != null) {
            println("[setupPluginsLogin] seeting plugin portal credentials from env")
            System.getProperties().setProperty("gradle.publish.key", publishKey)
            System.getProperties().setProperty("gradle.publish.secret", publishSecret)
        }
    }
    val publishPlugins by existing
    publishPlugins.get().dependsOn(setupPluginsLogin)
}
publishing {
    repositories {
        mavenLocal()
    }
}
gradlePlugin {
    plugins {
        create("OctopusDeployPlugin") {
            id = "com.liftric.octopus-deploy-plugin"
            displayName = "octopus-deploy-plugin"
            implementationClass = "com.liftric.octopusdeploy.OctopusDeployPlugin"
            description = "Common tasks for Octopus Deploy interaction, like package or build-information uploading"
        }
    }
}
pluginBundle {
    website = "https://github.com/Liftric/octopus-deploy-plugin"
    vcsUrl = "https://github.com/Liftric/octopus-deploy-plugin"
    description = "Common tasks for Octopus Deploy interaction, like package or build-information uploading"
    tags = listOf("octopus", "deploy", "releases", "build-information", "upload", "packages")
}
