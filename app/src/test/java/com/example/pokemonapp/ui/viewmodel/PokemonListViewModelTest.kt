package com.example.pokemonapp.ui.viewmodel

import com.example.pokemonapp.data.model.PokemonListItem
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
class PokemonListViewModelTest {

    @Mock
    private lateinit var mockRepository: IPokeRepository

    private lateinit var viewModel: PokemonListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {

        doReturn(emptyList<PokemonListItem>()).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        val initialState = viewModel.uiState.first()

        assertTrue(initialState.pokemonList.isEmpty())
        assertTrue(initialState.filteredList.isEmpty())
        assertTrue(initialState.searchQuery.isEmpty())
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `loadPokemonList loads data successfully and updates state`() = runTest {

        val mockPokemonList = listOf(
            PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
            PokemonListItem("venusaur", "https://pokeapi.co/api/v2/pokemon/3/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(3, state.pokemonList.size)
        assertEquals(3, state.filteredList.size)
        assertEquals("bulbasaur", state.pokemonList[0].name)
        assertEquals("ivysaur", state.pokemonList[1].name)
        assertEquals("venusaur", state.pokemonList[2].name)
        assertFalse(state.isLoading)
        assertNull(state.error)
        verify(mockRepository).getPokemonList(100, 0)
    }

    @Test
    fun `loadPokemonList handles errors gracefully`() = runTest {

        val errorMessage = "Network error occurred"
        doThrow(RuntimeException(errorMessage)).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertTrue(state.pokemonList.isEmpty())
        assertTrue(state.filteredList.isEmpty())
        assertFalse(state.isLoading)
        assertEquals(errorMessage, state.error)
        verify(mockRepository).getPokemonList(100, 0)
    }

    @Test
    fun `loadPokemonList uses cached data when available`() = runTest {
        // Given
        val mockPokemonList = listOf(
            PokemonListItem("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        reset(mockRepository)

        viewModel.loadPokemonList()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(1, state.pokemonList.size)
        assertEquals("pikachu", state.pokemonList[0].name)
        assertFalse(state.isLoading)

        verifyNoMoreInteractions(mockRepository)
    }

    @Test
    fun `searchPokemon filters list correctly with query`() = runTest {

        val mockPokemonList = listOf(
            PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
            PokemonListItem("venusaur", "https://pokeapi.co/api/v2/pokemon/3/"),
            PokemonListItem("charmander", "https://pokeapi.co/api/v2/pokemon/4/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchPokemon("saur")
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(4, state.pokemonList.size)
        assertEquals(3, state.filteredList.size)
        assertEquals("bulbasaur", state.filteredList[0].name)
        assertEquals("ivysaur", state.filteredList[1].name)
        assertEquals("venusaur", state.filteredList[2].name)
        assertEquals("saur", state.searchQuery)
    }

    @Test
    fun `searchPokemon returns all items when query is empty`() = runTest {
        // Given
        val mockPokemonList = listOf(
            PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchPokemon("")
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(2, state.filteredList.size)
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `searchPokemon is case insensitive`() = runTest {

        val mockPokemonList = listOf(
            PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchPokemon("BULBA")
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(1, state.filteredList.size)
        assertEquals("bulbasaur", state.filteredList[0].name)
    }

    @Test
    fun `clearError removes error from state`() = runTest {

        val errorMessage = "Test error"
        doThrow(RuntimeException(errorMessage)).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
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

        val mockPokemonList = listOf(
            PokemonListItem("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        reset(mockRepository)
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel.refreshData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(1, state.pokemonList.size)
        assertEquals("pikachu", state.pokemonList[0].name)

        verify(mockRepository).getPokemonList(100, 0)
    }

    @Test
    fun `searchPokemon with no matching results returns empty filtered list`() = runTest {

        val mockPokemonList = listOf(
            PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
            PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/")
        )
        
        doReturn(mockPokemonList).`when`(mockRepository).getPokemonList(100, 0)

        viewModel = PokemonListViewModel(mockRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.searchPokemon("nonexistent")
        testDispatcher.scheduler.advanceUntilIdle()
        
        val state = viewModel.uiState.first()

        assertEquals(2, state.pokemonList.size) // Original list unchanged
        assertEquals(0, state.filteredList.size) // No matches
        assertEquals("nonexistent", state.searchQuery)
    }
}
