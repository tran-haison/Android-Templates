package co.nimblehq.sample.compose.domain.usecase

import co.nimblehq.sample.compose.domain.model.Model
import co.nimblehq.sample.compose.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UseCase @Inject constructor(private val repository: Repository) {

    fun execute(): Flow<List<Model>> {
        return repository.getModels()
    }
}