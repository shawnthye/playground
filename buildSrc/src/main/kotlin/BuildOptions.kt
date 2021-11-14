import org.gradle.api.JavaVersion

object BuildOptions {
    /**
     * We use this for Target Sdk also, since our best pratice is also keep them the same
     */
    const val COMPILE_SDK = 31

    // temporary set to 26, check this https://github.com/mannodermaus/android-junit5/issues/225
    const val MIN_SDK = 21

    val JAVA_VERSION = JavaVersion.VERSION_1_8

    /**
     * We use half of the machine for test
     */
    val AVAILABLE_PROCESSORS = maxOf(Runtime.getRuntime().availableProcessors().div(2), 1)
}
