plugins {
    id("kotlin")
    id("com.android.lint")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.freeCompilerArgs = emptyList()


dependencies {
    implementation(platform(project(":dep-constraints")))

    implementation(Libs.Dagger.javaxInject)
}
