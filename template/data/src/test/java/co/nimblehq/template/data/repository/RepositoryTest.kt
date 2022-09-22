package co.nimblehq.template.data.repository

import co.nimblehq.template.data.response.Response
import co.nimblehq.template.data.response.toModels
import co.nimblehq.template.data.service.ApiService
import co.nimblehq.template.domain.repository.Repository
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {

    private lateinit var mockService: ApiService
    private lateinit var repository: Repository

    private val response = Response(id = 1)

    @Before
    fun setup() {
        mockService = mockk()
        repository = RepositoryImpl(mockService)
    }

    @Test
    fun `When request successful, it returns success`() = runBlockingTest {
        val expected = listOf(response)
        coEvery { mockService.getResponses() } returns expected

        repository.getModels().collect {
            it shouldBe expected.toModels()
        }
    }

    @Test
    fun `When request failed, it returns error`() = runBlockingTest {
        val expected = Throwable()
        coEvery { mockService.getResponses() } throws expected

        repository.getModels().catch {
            it shouldBe expected
        }.collect()
    }
}