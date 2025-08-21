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

        // Then
        assertTrue(initialState.pokemonDetail != null)
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `loadPokemonDetail loads data successfully and updates state`() = runTest {
        // Given
        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        // When
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        // Then
        assertEquals(mockPokemonDetail, state.pokemonDetail)
        assertEquals(25, state.pokemonDetail?.id)
        assertEquals("pikachu", state.pokemonDetail?.name)
        assertFalse(state.isLoading)
        assertNull(state.error)
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `loadPokemonDetail handles errors gracefully`() = runTest {
        // Given
        val errorMessage = "Pokemon not found"
        doThrow(RuntimeException(errorMessage)).`when`(mockRepository).getPokemonDetail(25)

        // When
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        // Then
        assertNull(state.pokemonDetail)
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `loadPokemonDetail uses cached data when available`() = runTest {
        // Given
        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        // When - First load
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Reset mock to verify it's not called again
        reset(mockRepository)
        
        // Second load - should use cache
        viewModel.loadPokemonDetail()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        // Then
        assertEquals(mockPokemonDetail, state.pokemonDetail)
        assertEquals("pikachu", state.pokemonDetail?.name)
        assertFalse(state.isLoading)
        // Repository should not be called again due to caching
        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun `clearError removes error from state`() = runTest {
        // Given
        val errorMessage = "Test error"
        doThrow(RuntimeException(errorMessage)).`when`(mockRepository).getPokemonDetail(25)
        
        // Create ViewModel with error
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Verify error exists
        var state = viewModel.uiState.first()
        assertEquals(errorMessage, state.error)

        // When
        viewModel.clearError()
        testDispatcher.scheduler.advanceUntilIdle()
        
        state = viewModel.uiState.first()

        // Then
        assertNull(state.error)
    }

    @Test
    fun `refreshData clears cache and reloads data`() = runTest {
        // Given
        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        // Create ViewModel with data
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Reset mock to verify it's called again
        reset(mockRepository)
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        // When
        viewModel.refreshData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        // Then
        assertEquals(mockPokemonDetail, state.pokemonDetail)
        assertEquals("pikachu", state.pokemonDetail?.name)
        // Repository should be called again due to cache clearing
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `viewModel uses correct pokemon ID from saved state`() = runTest {
        // Given
        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        // When
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        // Then
        assertEquals(25, state.pokemonDetail?.id)
        verify(mockRepository).getPokemonDetail(25)
    }

    @Test
    fun `viewModel handles different pokemon IDs correctly`() = runTest {
        // Given
        val newSavedStateHandle = SavedStateHandle(mapOf("pokemonId" to 1))
        val mockPokemonDetail = createMockPokemonDetail(1, "bulbasaur")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(1)
        
        // When
        val newViewModel = PokemonDetailViewModel(mockRepository, newSavedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = newViewModel.uiState.first()

        // Then
        assertEquals(1, state.pokemonDetail?.id)
        assertEquals("bulbasaur", state.pokemonDetail?.name)
        verify(mockRepository).getPokemonDetail(1)
    }

    @Test
    fun `viewModel handles missing pokemon ID gracefully`() = runTest {
        // Given
        val emptySavedStateHandle = SavedStateHandle()
        val mockPokemonDetail = createMockPokemonDetail(1, "bulbasaur")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(1)
        
        // When
        val newViewModel = PokemonDetailViewModel(mockRepository, emptySavedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = newViewModel.uiState.first()

        // Then
        assertEquals(1, state.pokemonDetail?.id) // Should use default value 1
        assertEquals("bulbasaur", state.pokemonDetail?.name)
        verify(mockRepository).getPokemonDetail(1)
    }

    @Test
    fun `viewModel preserves pokemon detail during state updates`() = runTest {
        // Given
        val mockPokemonDetail = createMockPokemonDetail(25, "pikachu")
        doReturn(mockPokemonDetail).`when`(mockRepository).getPokemonDetail(25)

        // Load data
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        var state = viewModel.uiState.first()
        assertEquals("pikachu", state.pokemonDetail?.name)

        // When - Clear error (should not affect pokemon detail)
        viewModel.clearError()
        testDispatcher.scheduler.advanceUntilIdle()
        
        state = viewModel.uiState.first()

        // Then
        assertEquals("pikachu", state.pokemonDetail?.name) // Pokemon detail preserved
        assertNull(state.error)
    }

    @Test
    fun `viewModel handles repository exceptions correctly`() = runTest {
        // Given
        doThrow(RuntimeException("Generic exception")).`when`(mockRepository).getPokemonDetail(25)

        // When
        viewModel = PokemonDetailViewModel(mockRepository, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        // Then
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
