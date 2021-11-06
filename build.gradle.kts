// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    // keep below 1 line for reference only :)
    // val compose_version by extra("1.0.1")

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Libs.Android.pluginGradle)
        classpath(Libs.Kotlin.pluginGradle)
        classpath(Libs.Dagger.hiltPluginGradle)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        // classpath("com.google.gms:google-services:4.3.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("com.diffplug.spotless") version "5.17.1"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.osacky.doctor") version "0.7.3" // enable to check performance
    jacoco
}

jacoco {
    toolVersion = "0.8.7"
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
        configure<com.android.build.gradle.internal.dsl.BaseAppModuleExtension> {
            compileOptions {
                isCoreLibraryDesugaringEnabled = true
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }

            lint {
                // Disable lintVital. Not needed since lint is run on CI
                isCheckReleaseBuilds = false
                // Ignore any tests
                isIgnoreTestSources = false
                // Make the build fail on any lint errors
                isAbortOnError = true
                // Allow lint to check dependencies
                isCheckDependencies = true
                // We don't want any warning
                isWarningsAsErrors = true
                xmlReport = false
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

            lint {
                // Disable lintVital. Not needed since lint is run on CI
                isCheckReleaseBuilds = false
                // Ignore any tests
                isIgnoreTestSources = false
                // Make the build fail on any lint errors
                isAbortOnError = true
                // Allow lint to check dependencies
                isCheckDependencies = true
                // We don't want any warning
                isWarningsAsErrors = true
                xmlReport = false
            }
        }
    }

    pluginManager.withPlugin("kotlin-kapt") {
        configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
            correctErrorTypes = true
            useBuildCache = true
        }
    }

    // for pure java module
    pluginManager.withPlugin("kotlin") {
        configure<JavaPluginExtension> {
            sourceCompatibility = ApplicationOptions.JAVA_VERSION
            targetCompatibility = ApplicationOptions.JAVA_VERSION
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true

            freeCompilerArgs = (
                freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
                )

            jvmTarget = ApplicationOptions.JAVA_VERSION.toString()
        }
    }

    tasks.withType<Test> {
        maxParallelForks = if (TestOptions.HALF_AVAILABLE_PROCESSORS >= 1) {
            TestOptions.HALF_AVAILABLE_PROCESSORS
        } else {
            1
        }
    }
}


tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    gradleReleaseChannel = "current"
    checkConstraints = true

    rejectVersionIf {
        val current = DependencyUpdates.versionToRelease(currentVersion)

        // If we're using a NON RELEASE, ignore since we must be doing so for a reason.
        if (current != ReleaseType.RELEASE) return@rejectVersionIf true

        // Otherwise we reject if the candidate is more 'unstable' than our version
        val candidate = DependencyUpdates.versionToRelease(candidate.version)
        candidate.isLessStableThan(current)
    }
}
