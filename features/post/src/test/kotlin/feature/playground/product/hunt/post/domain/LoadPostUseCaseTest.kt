package feature.playground.product.hunt.post.domain

import app.playground.store.database.entities.Post
import core.playground.domain.Result
import feature.playground.product.hunt.post.data.PostRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import testing.playground.core.CoroutineTest

internal class LoadPostUseCaseTest : CoroutineTest() {

    @MockK
    internal lateinit var postRepository: PostRepository

    private lateinit var loadPostUseCase: LoadPostUseCase

    private val postId = ""

    private val fakePost = Post(
        0,
        "",
        "",
        "",
    )

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        loadPostUseCase = LoadPostUseCase(
            testDispatcher,
            postRepository,
        )
    }

    @Test
    fun `exception emits Result#Error`() = runTest {
        every { postRepository.observePost(postId) } returns flowOf(Result.Error(RuntimeException()))
        val result = loadPostUseCase(postId)
        MatcherAssert.assertThat(
            result.last(),
            CoreMatchers.instanceOf(Result.Error::class.java),
        )
    }

    @Test
    fun `success emit Result#Success`() = runTest {
        every { postRepository.observePost(postId) } returns flowOf(Result.Success(fakePost))
        val result = loadPostUseCase(postId)
        MatcherAssert.assertThat(
            (result.last() as? Result.Success)?.data,
            CoreMatchers.allOf(
                CoreMatchers.notNullValue(),
                CoreMatchers.`is`(fakePost),
            ),
        )
    }

    @Test
    fun `Result#Loading then Result#Success`() = runTest {
        every { postRepository.observePost(postId) } returns flowOf(
            Result.Loading(null),
            Result.Success(fakePost),
        )
        val result = loadPostUseCase(postId)
        MatcherAssert.assertThat(
            result.first(),
            CoreMatchers.instanceOf(Result.Loading::class.java)
        )
        MatcherAssert.assertThat(
            (result.last() as? Result.Success)?.data,
            CoreMatchers.`is`(fakePost)
        )
    }
}
