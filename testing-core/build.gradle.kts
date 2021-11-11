plugins {
    id("kotlin")
    id("com.android.lint")
}

dependencies {
    api(platform(project(":build-dep-constraints")))

    testImplementation(Libs.Test.junit)
    testImplementation(Libs.Coroutines.core)
    testImplementation(Libs.Coroutines.test)
}
