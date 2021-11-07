plugins {
    id("kotlin")
    id("com.android.lint")
}

dependencies {
    api(platform(project(":dep-constraints")))

    implementation(Libs.Dagger.javaxInject)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions { freeCompilerArgs = emptyList() }
}
