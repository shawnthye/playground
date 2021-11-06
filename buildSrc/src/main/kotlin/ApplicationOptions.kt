import org.gradle.api.JavaVersion

object ApplicationOptions {
    const val COMPILE_SDK = 31
    const val MIN_SDK = 21

    val JAVA_VERSION = JavaVersion.VERSION_1_8
}

object TestOptions {

    /**
     * We use half of the machine for test
     */
    val HALF_AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors().div(2)
}
