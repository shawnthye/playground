plugins {
    androidLibrary
    kapt
}

android {
    namespace = "just.android.library.assertion"
    compileSdk = BuildOptions.COMPILE_SDK

    defaultConfig {
        minSdk = BuildOptions.MIN_SDK
        targetSdk = BuildOptions.COMPILE_SDK
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = false
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
        expect = BuildOptions.JAVA_VERSION,
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
