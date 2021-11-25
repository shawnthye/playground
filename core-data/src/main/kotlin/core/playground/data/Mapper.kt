package core.playground.data

abstract class Mapper<F, T> {
    suspend operator fun invoke(from: F) = parse(from)
    protected abstract suspend fun parse(from: F): T
}
