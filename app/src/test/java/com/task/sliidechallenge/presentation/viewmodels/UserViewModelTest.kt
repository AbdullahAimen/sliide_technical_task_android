package com.task.sliidechallenge.presentation.viewmodels

/**
 * @Author Abdullah Abo El~Makarem on 22/07/2024.
 */
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.times
import com.task.sliidechallenge.data.models.User
import com.task.sliidechallenge.domain.repos.UserRepository
import io.mockk.clearAllMocks
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var repository: UserRepository
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(repository)
    }


    @Test
    fun `loadUsers should update state to Success when repository returns users`() = runTest {
        val users = listOf(User(1, "Qwerty", "Qwerty@example.com"))
        `when`(repository.getUsers()).thenReturn(users)

        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.first() is UiState.Success)
        assert((viewModel.uiState.first() as UiState.Success).users == users)
    }

    @Test
    fun `loadUsers should update state to Error when repository throws exception`() = runTest {
        `when`(repository.getUsers()).thenThrow(RuntimeException("Network error"))

        viewModel.loadUsers()
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.first() is UiState.Error)
        assert((viewModel.uiState.first() as UiState.Error).message == "Network error")
    }

    @Test
    fun `createUser should call loadUsers when successful`() = runTest {
        val newUser = User(0, "Name", "Name@example.com")
        `when`(repository.createUser(newUser)).thenReturn(Response.success(201, newUser))

        viewModel.createUser("Name", "Name@example.com")
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).createUser(newUser)
        //two times one while creating viewmodel instance and the second while executing
        verify(repository, times(2)).getUsers()
    }

    @Test
    fun `createUser should update state to Error when unsuccessful`() = runTest {
        val newUser = User(0, "Name", "Name@example.com")
        `when`(repository.createUser(newUser)).thenReturn(
            Response.error(
                400,
                mockk(relaxed = true)
            )
        )

        viewModel.createUser("Name", "Name@example.com")
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.first() is UiState.Error)
        assert((viewModel.uiState.first() as UiState.Error).message == "error while adding user")
    }

    @Test
    fun `deleteUser should call loadUsers when successful`() = runTest {
        val user = User(1, "Qwerty", "Qwerty@example.com")
        `when`(repository.deleteUser(user.id)).thenReturn(Response.success(204, Unit))
        `when`(repository.getUsers()).thenReturn(listOf(user))
        viewModel.deleteUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(repository).deleteUser(user.id)
        //two times one while creating viewmodel instance and the second while executing
        verify(repository, times(2)).getUsers()
    }

    @Test
    fun `deleteUser should update state to Error when unsuccessful`() = runTest {
        val user = User(1, "Qwerty", "Qwerty@example.com")
        `when`(repository.deleteUser(user.id)).thenReturn(
            Response.error(
                400,
                mockk(relaxed = true)
            )
        )

        viewModel.deleteUser(user)
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.uiState.first() is UiState.Error)
        assert((viewModel.uiState.first() as UiState.Error).message == "error while deleting user")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }
}