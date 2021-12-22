plugins {
    kotlinJvm
    androidLint
    kotlinxSerialization
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}

dependencies {
    compileOnly(platformDepConstraints)

    compileOnly(Libs.JetBrains.xJson)
}
