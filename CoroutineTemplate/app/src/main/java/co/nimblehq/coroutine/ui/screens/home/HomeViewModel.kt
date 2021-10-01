package co.nimblehq.coroutine.ui.screens.home

import androidx.lifecycle.viewModelScope
import co.nimblehq.coroutine.model.User
import co.nimblehq.coroutine.ui.base.BaseViewModel
import co.nimblehq.coroutine.ui.base.NavigationEvent
import co.nimblehq.coroutine.ui.screens.second.SecondBundle
import co.nimblehq.coroutine.usecase.GetUsersUseCase
import co.nimblehq.coroutine.usecase.UseCaseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

interface Output {

    val users: StateFlow<List<User>>

    fun navigateToSecond(bundle: SecondBundle)

    fun navigateToCompose()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
) : BaseViewModel(), Output {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    override val users: StateFlow<List<User>>
        get() = _users

    init {
        fetchUsers()
    }

    override fun navigateToSecond(bundle: SecondBundle) {
        viewModelScope.launch {
            _navigator.emit(NavigationEvent.Second(bundle))
        }
    }

    override fun navigateToCompose() {
        viewModelScope.launch {
            _navigator.emit(NavigationEvent.Compose)
        }
    }

    private fun fetchUsers() {
        showLoading()
        execute(run {
            {
                when (val result = getUsersUseCase.execute()) {
                    is UseCaseResult.Success -> _users.value = result.data
                    is UseCaseResult.Error -> _error.emit(result.exception.message.orEmpty())
                }
                hideLoading()
            }
        })
    }
}
