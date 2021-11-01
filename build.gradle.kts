// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // keep below 1 line for reference only :)
    // val compose_version by extra("1.0.1")

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${app.playground.buildsrc.Versions.HILT}")
        // classpath("com.google.gms:google-services:4.3.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.diffplug.spotless") version "5.17.1"
    // id("com.osacky.doctor") version "0.7.3" // enable to check performance
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        encoding("UTF-8")
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint("0.42.1").userData(
                mapOf(
                    "charset" to "utf-8",
                    "end_of_line" to "lf",
                    "indent_size" to "4",
                    "indent_style" to "space",
                    "insert_final_newline" to "true",
                    "max_line_length" to "100",
                    "ij_kotlin_allow_trailing_comma" to "true",
                    "ij_kotlin_allow_trailing_comma_on_call_site" to "true",
                ),
            )
        }
    }

    pluginManager.withPlugin("com.android.application") {
        configure<com.android.build.gradle.AppExtension> {
            compileOptions {
                isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }

    pluginManager.withPlugin("com.android.library") {
        configure<com.android.build.gradle.LibraryExtension> {
            compileOptions {
                isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }

    pluginManager.withPlugin("kotlin-kapt") {
        configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
            correctErrorTypes = true
            useBuildCache = true
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true

            freeCompilerArgs = (
                freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                )

            jvmTarget = "1.8"
        }
    }
}
