@file:OptIn(ExperimentalWasmDsl::class)
@file:Suppress("UnstableApiUsage")

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

group = "com.github.abraga"
version = providers.gradleProperty("verdandi.version").get()

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.dokka)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvm()

    wasmJs {
        browser {
            testTask { enabled = false }
        }
    }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework { baseName = "${project.name}Kit" }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            kotlin.srcDir("src/common/kotlin")
            resources.srcDir("src/common/resources")
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(libs.compose.runtime)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.ui)
                implementation(libs.compose.components.resources)
                implementation(libs.compose.components.ui.tooling.preview)
            }
        }
        commonTest {
            kotlin.srcDir("src/commonTest/kotlin")
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.compose.runtime)
            }
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll("-Xnested-type-aliases", "-Xexplicit-backing-fields")
    }
}

dependencies {
    commonMainApi(rootProject)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("com.github.abraga", "verdandi-compose", version.toString())

    pom {
        name.set("Verdandi Compose")
        description.set("Compose Multiplatform extensions for Verdandi date/time library")
        url.set("https://github.com/abraga/verdandi")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("abraga")
                name.set("Andre Braga")
                email.set("andre.amaral.braga@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/abraga/verdandi")
            connection.set("scm:git:git://github.com/abraga/verdandi.git")
            developerConnection.set("scm:git:ssh://git@github.com/abraga/verdandi.git")
        }
    }
}

androidComponents {
    finalizeDsl {
        it.compileSdk = 36
        it.minSdk = 26
        it.namespace = "com.github.abraga.verdandi.compose"
        it.optimization.consumerKeepRules.apply {
            publish = true
            files.add(project.file("consumer-rules.pro"))
        }
    }
}
