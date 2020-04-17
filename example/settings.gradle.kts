rootProject.name = "example"
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.liftric.octopus-deploy-plugin") {
                val currentVersion = file("../build/version").readText().trim()
                useModule("com.liftric.octopusdeploy:octopus-deploy-plugin:${currentVersion}")
            }
        }
    }
}
