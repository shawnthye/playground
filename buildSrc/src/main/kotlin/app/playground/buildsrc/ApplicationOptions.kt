package app.playground.buildsrc

object ApplicationOptions {
    const val COMPILE_SDK = 31
    const val MIN_SDK = 21
}

object TestOptions {

    /**
     * We use half of the machine for test
     */
    val HALF_AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors().div(2)
}
