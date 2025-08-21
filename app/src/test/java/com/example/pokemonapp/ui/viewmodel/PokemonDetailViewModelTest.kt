package com.example.pokemonapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.Sprites
import com.example.pokemonapp.data.repository.IPokeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PokemonDetailViewModelTest {

    @Mock
    private lateinit var mockRepository: IPokeRepository

    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: PokemonDetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(mapOf("pokemonId" to 25))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Given - Set up mock before creating ViewModel
        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)
        
        // When
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val initialState = viewModel.uiState.first()

        assertTrue(initialState.pokemonDetail != null)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `loadPokemonDetail loads data successfully and updates state`() = runTest {

        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(mockPokemonDetail, state.pokemonDetail)
        assertEquals(25, state.pokemonDetail?.id)
        assertEquals("pikachu", state.pokemonDetail?.name)
        assertFalse(state.isLoading)
        assertNull(state.error)
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `loadPokemonDetail handles errors gracefully`() = runTest {

        val errorMessage = "Pokemon not found"
        doThrow(RuntimeException(errorMessage)).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertNull(state.pokemonDetail)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `loadPokemonDetail uses cached data when available`() = runTest {

        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        reset(mockRepository)

        viewModel.loadPokemonDetail()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(mockPokemonDetail, state.pokemonDetail)
        assertEquals("pikachu", state.pokemonDetail?.name)
        assertFalse(state.isLoading)

        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun `clearError removes error from state`() = runTest {

        val errorMessage = "Test error"
        doThrow(RuntimeException(errorMessage)).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        var state = viewModel.uiState.first()
        assertEquals(errorMessage, state.error)

        viewModel.clearError()
        testDispatcher.scheduler.advanceUntilIdle()
        
        state = viewModel.uiState.first()

        assertNull(state.error)
    }

    @Test
    fun `refreshData clears cache and reloads data`() = runTest {

        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        reset(mockRepository)
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        viewModel.refreshData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(mockPokemonDetail, state.pokemonDetail)
        assertEquals("pikachu", state.pokemonDetail?.name)

        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `viewModel uses correct pokemon ID from saved state`() = runTest {

        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(25, state.pokemonDetail?.id)
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `viewModel handles different pokemon IDs correctly`() = runTest {

        val newSavedStateHandle = SavedStateHandle(mapOf("pokemonId" to 1))
        val mockPokemonDetail = createMockPokemonDetail(1, "bulbasaur")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(1)

        val newViewModel = PokemonDetailViewModel(mockRepository, newSavedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = newViewModel.uiState.first()

        assertEquals(1, state.pokemonDetail?.id)
        assertEquals("bulbasaur", state.pokemonDetail?.name)
        verify(mockRepository).getPokemonDetail(1)
    }

    @Test
    fun `viewModel handles missing pokemon ID gracefully`() = runTest {

        val emptySavedStateHandle = SavedStateHandle()
        val mockPokemonDetail = createMockPokemonDetail(1, "bulbasaur")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(1)

        val newViewModel = PokemonDetailViewModel(mockRepository, emptySavedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = newViewModel.uiState.first()

        assertEquals(1, state.pokemonDetail?.id) // Should use default value 1
        assertEquals("bulbasaur", state.pokemonDetail?.name)
        verify(mockRepository).getPokemonDetail(1)
    }

    @Test
    fun `viewModel preserves pokemon detail during state updates`() = runTest {

        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        var state = viewModel.uiState.first()
        assertEquals("pikachu", state.pokemonDetail?.name)

        viewModel.clearError()
        testDispatcher.scheduler.advanceUntilIdle()
        
        state = viewModel.uiState.first()

        assertEquals("pikachu", state.pokemonDetail?.name) // Pokemon detail preserved
        assertNull(state.error)
    }

    @Test
    fun `viewModel handles repository exceptions correctly`() = runTest {

        doThrow(RuntimeException("Generic exception")).`when`(mockRepository).getPokemonDetail(25)

        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertNull(state.pokemonDetail)
        assertFalse(state.isLoading)
        assertEquals("Generic exception", state.error)
        verify(mockRepository).getPokemonDetail(25)
    }

    private fun createMockPokemonDetail(id: Int, name: String): PokemonDetail {
        return PokemonDetail(
            id = id,
            name = name,
            height = 4,
            weight = 60,
            sprites = Sprites(null, null, null),
            stats = emptyList(),
            types = emptyList(),
            abilities = emptyList()
        )
    }
}
