package core.playground.data

abstract class Mapper<F, T> {
    suspend operator fun invoke(from: F) = map(from)
    protected abstract suspend fun map(from: F): T
}
