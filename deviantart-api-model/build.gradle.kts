plugins {
    kotlinJvm
    androidLint
    kotlinxSerialization
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}

dependencies {
    compileOnly(platform(project(":build-dep-constraints")))

    compileOnly(Libs.JetBrains.xJson)
}
