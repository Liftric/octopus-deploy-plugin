import net.nemerosa.versioning.tasks.VersionDisplayTask
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper

plugins {
    kotlin("jvm") version "1.3.61"
    `java-gradle-plugin`
    id("org.gradle.kotlin.kotlin-dsl") version "1.3.4"
    `maven-publish`
    id("com.gradle.plugin-publish") version "0.11.0"
    id("net.nemerosa.versioning") version "2.12.0"
    id("com.avast.gradle.docker-compose") version "0.10.7"
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
sourceSets {
    create("integrationTest") {
        compileClasspath += sourceSets.main.get().output
        compileClasspath += sourceSets.test.get().output
        runtimeClasspath += sourceSets.main.get().output
        runtimeClasspath += sourceSets.test.get().output
    }
}
val integrationTestImplementation by configurations.getting {
    extendsFrom(configurations.testImplementation.get())
}
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())
repositories {
    mavenCentral()
    jcenter()
}
val kotlinVersion = plugins.getPlugin(KotlinPluginWrapper::class.java).kotlinPluginVersion

dependencies {
    implementation(gradleApi())
    implementation(kotlin("gradle-plugin"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.6")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    implementation("com.squareup.retrofit2:retrofit:2.6.2")
    implementation("com.squareup.retrofit2:converter-jackson:2.6.2")
    implementation("com.squareup.retrofit2:converter-scalars:2.6.2")
    implementation("com.squareup.okhttp3:logging-interceptor:4.7.2")

    testImplementation(gradleTestKit())
    testImplementation("junit:junit:4.12")
    testImplementation("com.github.stefanbirkner:system-rules:1.19.0")
    integrationTestImplementation("junit:junit:4.12")
    integrationTestImplementation("org.apache.httpcomponents:httpclient:4.5.12")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    withType<Test> {
        testLogging.showStandardStreams = true
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

    //
    val composeUp by existing
    val composeDownForced by existing
    val integrationTest by creating(Test::class) {
        description = "Runs integration tests."
        group = "verification"

        testClassesDirs = sourceSets["integrationTest"].output.classesDirs
        classpath = sourceSets["integrationTest"].runtimeClasspath
        shouldRunAfter("test")
        dependsOn(composeUp)
        finalizedBy(composeDownForced)
    }
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
dockerCompose {
    useComposeFiles = listOf("docker-compose.yml")
    waitForTcpPorts = true
    captureContainersOutput = true
    stopContainers = true
    removeContainers = true
    buildBeforeUp = true
}
