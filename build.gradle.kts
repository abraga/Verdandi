@file:OptIn(ExperimentalWasmDsl::class)
@file:Suppress("UnstableApiUsage")

import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

group = "io.github.abraga"
version = providers.gradleProperty("verdandi.version").get()

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.androidLint)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.dokka)
    alias(libs.plugins.mavenPublish)
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.composeHotReload) apply false
}

kotlin {
    jvm()

    wasmJs { browser() }

    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework { baseName = "${project.name}Kit" }
    }

    macosX64()
    macosArm64()

    linuxX64()
    linuxArm64()

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            kotlin.srcDir("src/common/kotlin")
            resources.srcDir("src/common/resources")
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    compilerOptions {
        freeCompilerArgs.addAll("-Xnested-type-aliases", "-Xexplicit-backing-fields")
    }
}

dependencies {
    commonMainImplementation(libs.kotlinx.serialization.json)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates("io.github.abraga", "verdandi", version.toString())

    pom {
        name.set("Verdandi")
        description.set("Kotlin Multiplatform date/time manipulation library")
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
        it.namespace = "io.github.abraga.verdandi"
        it.optimization.consumerKeepRules.apply {
            publish = true
            files.add(project.file("consumer-rules.pro"))
        }
    }
}
