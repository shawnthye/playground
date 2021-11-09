import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import java.nio.file.Path

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
        // classpath("org.jacoco:org.jacoco.core:0.8.7")
        // classpath("com.google.gms:google-services:4.3.10")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

plugins {
    id("com.diffplug.spotless") version "6.0.0"
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.osacky.doctor") version "0.7.3" // enable to check performance
    jacoco
}

jacoco {
    toolVersion = Versions.JACOCO
}

tasks.withType<JacocoReport> {
    reports {
        html.required.set(true)
        xml.required.set(false)
    }
}

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isEnabled = true
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val modules = subprojects

configure(modules) {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        encoding("UTF-8")
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint(Versions.KTLINT).userData(
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
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(Versions.KTLINT)
        }
    }

    pluginManager.withPlugin("com.android.application") {
        configure<BaseAppModuleExtension> {
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

            testCoverage {
                jacocoVersion = Versions.JACOCO
            }

            testOptions {
                animationsDisabled = true
                // execution = "ANDROID_TEST_ORCHESTRATOR"
                unitTests {
                    isIncludeAndroidResources = true
                    isReturnDefaultValues = true
                }
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

            testCoverage {
                jacocoVersion = Versions.JACOCO
            }

            testOptions {
                animationsDisabled = true
                // execution = "ANDROID_TEST_ORCHESTRATOR"
                unitTests {
                    isIncludeAndroidResources = true
                    isReturnDefaultValues = true
                }
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
}

val coverage by rootProject.tasks.registering(JacocoReport::class) {
    group = "Verification"
    description = "Generate and merge all report in to one"
    setOnlyIf { true }

    doLast {
        println("Report generated in ${Path.of(outputs.files.asPath, "index.html")}")
    }
}

configure(modules) {
    val projectScoped = this
    pluginManager.withPlugin("jacoco") {
        val isApplication = pluginManager.hasPlugin("com.android.application")
        val isLibrary = pluginManager.hasPlugin("com.android.library")
        if (!isApplication && !isLibrary) {
            projectScoped.tasks.withType<JacocoReport> {
                dependsOn(projectScoped.tasks.withType(Test::class))
            }

            val jacocoTasks = projectScoped.tasks.withType(JacocoReport::class).map { it }
            coverage.configure {
                this.dependsOn(jacocoTasks)
                additionalSourceDirs.setFrom(
                    files(
                        jacocoTasks.map { it.sourceDirectories.files },
                        sourceDirectories.files,
                    ),
                )
                sourceDirectories.setFrom(
                    files(
                        jacocoTasks.map { it.sourceDirectories.files },
                        sourceDirectories.files,
                    ),
                )
                classDirectories.setFrom(
                    files(
                        jacocoTasks.map { it.classDirectories.files },
                        classDirectories.files,
                    ),
                )
                executionData(
                    files(
                        jacocoTasks.map { it.executionData.files },
                        executionData.files,
                    ),
                )

            }
        } else {
            val fileTreeConfig: (ConfigurableFileTree) -> Unit = {
                it.exclude(
                    "**/R.class",
                    "**/R$*.class",
                    "**/BuildConfig.*",
                    "**/Manifest*.*",
                    "**/*BR*.*",
                    /*
                     * AndroidX
                     */
                    "androidx/**/*.class",
                    /*
                     * Databinding
                     */
                    "**/databinding/*Binding.class",
                    "**/databinding/*BindingImpl.class",
                    "**/DataBindingTriggerClass.class",
                    "**/DataBinderMapperImpl*.class",
                    /*
                     * Room
                     */
                    "**/*_Impl*.class",
                    // "**/*Impl$*.*",
                    /*
                     * Dagger
                     */
                    "**/*Provide*Factory*.class",
                    "**/dagger/hilt/internal/*",
                    "**/hilt_aggregated_deps/*",
                    "**/*Hilt*.class",
                    /*
                     * kotlin
                     **/
                    "**/*\$inlined$*.*",
                    /*
                     * java
                     **/
                    "**/*\$Lambda$*.*",
                )

                // it.exclude(
                //     // data binding
                //     "android/databinding/**/*.class",
                //     "**/android/databinding/*Binding.class",
                //     "**/android/databinding/*",
                //     "**/androidx/databinding/*",
                //     "**/BR.*",
                //     // android
                //     "**/R.class",
                //     "**/R$*.class",
                //     "**/BuildConfig.*",
                //     "**/Manifest*.*",
                //     "**/*Test*.*",
                //     "android/**/*.*",
                //     // dagger
                //     "**/*_MembersInjector.class",
                //     "**/Dagger*Component.class",
                //     // "**/Dagger*Component$Builder.class",
                //     "**/*Module_*Factory.class",
                //     "**/di/module/*",
                //     "**/*_Factory*.*",
                //     "**/*Module*.*",
                //     "**/*Dagger*.*",
                //     "**/*Hilt*.*",
                //     // kotlin
                //     "**/*MapperImpl*.*",
                //     // "**/*$ViewInjector*.*",
                //     // "**/*$ViewBinder*.*",
                //     "**/BuildConfig.*",
                //     "**/*Component*.*",
                //     "**/*BR*.*",
                //     "**/Manifest*.*",
                //     "**/*Companion*.*",
                //     "**/*Module*.*",
                //     "**/*Dagger*.*",
                //     "**/*Hilt*.*",
                //     "**/*MembersInjector*.*",
                //     "**/*_MembersInjector.class",
                //     "**/*_Factory*.*",
                //     "**/*_Provide*Factory*.*",
                //     "**/*Extensions*.*",
                //     // sealed and data classes
                //     // "**/*$Result.*",
                //     // "**/*$Result$*.*",
                // )
            }

            val flavorCandidates = listOfNotNull(
                "main",
                if (isApplication) {
                    "production"
                } else {
                    null
                },
            )

            val sourceCandidates = listOf("kotlin", "java").flatMap { lang ->
                flavorCandidates.map { "${Path.of("$projectDir", "src", it, lang)}" }
            }

            val kotlinClassesDir = Path.of(
                "$buildDir",
                "tmp",
                "kotlin-classes",
                if (isApplication) "productionDebug" else "debug",
            )

            val javaClassesDir = Path.of(
                "$buildDir",
                "intermediates",
                "javac",
                if (isApplication) "productionDebug" else "debug",
            )

            val executionDataTree = fileTree(Path.of("$buildDir", "outputs")) {
                include(
                    "unit_test_code_coverage/**/*.exec",
                    "code_coverage/**/*.ec",
                )
            }

            val classesFile = files(
                fileTree(kotlinClassesDir, fileTreeConfig),
                fileTree(javaClassesDir, fileTreeConfig),
            )

            val jacocoTask = projectScoped.task("jacocoTestReport", JacocoReport::class) {
                group = "Verification"
                description = "Custom Generates code coverage report for the test task."
                dependsOn(
                    if (isApplication) {
                        "testProductionDebugUnitTest"
                    } else {
                        "testDebugUnitTest"
                    },
                    if (isApplication) {
                        "connectedProductionDebugAndroidTest"
                    } else {
                        "connectedDebugAndroidTest"
                    },
                )

                additionalSourceDirs.setFrom(files("${projectDir}/src/main/res"))
                sourceDirectories.setFrom(
                    files(sourceCandidates),
                )
                classDirectories.setFrom(classesFile)
                executionData(files(executionDataTree))
            }

            coverage.configure {
                dependsOn(jacocoTask)
                additionalSourceDirs.setFrom(files(sourceCandidates, sourceDirectories.files))
                sourceDirectories.setFrom(files(sourceCandidates, sourceDirectories.files))
                classDirectories.setFrom(files(classesFile, classDirectories.files))
                executionData(files(executionDataTree, executionData.files))
            }
        }

        configure<JacocoPluginExtension> {
            toolVersion = Versions.JACOCO
        }

        tasks.withType<JacocoReport> {
            reports {
                html.required.set(true)
                xml.required.set(true)
            }
        }

        tasks.withType<Test> {
            configure<JacocoTaskExtension> {
                isEnabled = true
                isIncludeNoLocationClasses = true
                excludes = listOf("jdk.internal.*")
            }

            maxParallelForks = if (TestOptions.HALF_AVAILABLE_PROCESSORS >= 1) {
                TestOptions.HALF_AVAILABLE_PROCESSORS
            } else {
                1
            }

            // testLogging {
            //     // set options for log level LIFECYCLE
            //     events "failed"
            //     exceptionFormat "short"
            //
            //     // set options for log level DEBUG
            //     debug {
            //         events "started", "skipped", "failed"
            //         exceptionFormat "full"
            //     }
            //
            //     // remove standard output/error logging from --info builds
            //     // by assigning only 'failed' and 'skipped' events
            //     info.events = ["failed", "skipped"]
            // }
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
