import app.playground.buildsrc.ApplicationOptions

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    namespace = "just.android.library.assertion"
    compileSdk = ApplicationOptions.COMPILE_SDK

    defaultConfig {
        minSdk = ApplicationOptions.MIN_SDK
        targetSdk = ApplicationOptions.COMPILE_SDK
    }
}

fun assert(name: String, actual: Any, expect: Any) {

    if (actual == expect) {
        return
    }

    throw Exception(
        String.format(
            "invalid %s=%s, expected %s, please check project root build.gradle.",
            name,
            actual,
            expect,
        ),
    )
}

// This simple verification for root build.gradle.kts configuration
afterEvaluate {

    assert(
        name = "android.compileOptions.sourceCompatibility",
        actual = android.compileOptions.sourceCompatibility,
        expect = JavaVersion.VERSION_1_8,
    )

    assert(
        name = "android.lint.xmlReport",
        actual = android.lint.xmlReport,
        expect = false,
    )

    assert(
        name = "kapt.useBuildCache",
        actual = kapt.useBuildCache,
        expect = true,
    )
}
