package core.playground

/**
 * Annotation to ignore unnecessary code in coverage report
 * @see [https://github.com/jacoco/jacoco/releases/tag/v0.8.2]
 * @see [https://github.com/jacoco/jacoco/pull/731]
 *
 * Use it carefully, with a meaningful comment
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.CLASS,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
)
annotation class Generated(@Suppress("unused") val comments: String)
