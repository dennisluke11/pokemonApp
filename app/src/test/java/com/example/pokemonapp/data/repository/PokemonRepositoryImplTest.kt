package com.example.pokemonapp.data.repository

import com.example.pokemonapp.data.api.PokemonApiService
import com.example.pokemonapp.data.model.PokemonDetail
import com.example.pokemonapp.data.model.PokemonListResponse
import com.example.pokemonapp.data.model.PokemonListItem
import com.example.pokemonapp.data.model.Sprites
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class PokemonRepositoryImplTest {

    @Mock
    private lateinit var mockApiService: PokemonApiService

    private lateinit var repository: PokemonRepositoryImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = PokemonRepositoryImpl(mockApiService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getPokemonList returns pokemon list when API call succeeds`() = runTest {

        val mockResponse = PokemonListResponse(
            count = 100,
            next = null,
            previous = null,
            results = listOf(
                PokemonListItem("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/"),
                PokemonListItem("ivysaur", "https://pokeapi.co/api/v2/pokemon/2/"),
                PokemonListItem("venusaur", "https://pokeapi.co/api/v2/pokemon/3/")
            )
        )
        
        doReturn(mockResponse).`when`(mockApiService).getPokemonList(100, 0)

        val result = repository.getPokemonList(100, 0)

        assertEquals(3, result.size)
        assertEquals("bulbasaur", result[0].name)
        assertEquals("ivysaur", result[1].name)
        assertEquals("venusaur", result[2].name)
        verify(mockApiService).getPokemonList(100, 0)
    }

    @Test
    fun `getPokemonList returns empty list when API call fails`() = runTest {

        doThrow(RuntimeException("Network error")).`when`(mockApiService).getPokemonList(100, 0)

        val result = repository.getPokemonList(100, 0)

        assertTrue(result.isEmpty())
        verify(mockApiService).getPokemonList(100, 0)
    }

    @Test
    fun `getPokemonList returns empty list when API call throws exception`() = runTest {

        doThrow(RuntimeException("Any exception")).`when`(mockApiService).getPokemonList(100, 0)

        val result = repository.getPokemonList(100, 0)

        assertTrue(result.isEmpty())
        verify(mockApiService).getPokemonList(100, 0)
    }

    @Test
    fun `getPokemonList with custom limit and offset calls API correctly`() = runTest {

        val mockResponse = PokemonListResponse(
            count = 20,
            next = null,
            previous = null,
            results = listOf(
                PokemonListItem("pikachu", "https://pokeapi.co/api/v2/pokemon/25/")
            )
        )
        
        doReturn(mockResponse).`when`(mockApiService).getPokemonList(20, 10)

        val result = repository.getPokemonList(20, 10)

        assertEquals(1, result.size)
        assertEquals("pikachu", result[0].name)
        verify(mockApiService).getPokemonList(20, 10)
    }

    @Test
    fun `getPokemonDetail returns pokemon detail when API call succeeds`() = runTest {

        val mockPokemonDetail = PokemonDetail(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            sprites = Sprites(null, null, null),
            stats = emptyList(),
            types = emptyList(),
            abilities = emptyList()
        )
        
        doReturn(mockPokemonDetail).`when`(mockApiService).getPokemonDetail(25)

        val result = repository.getPokemonDetail(25)

        assertEquals(25, result.id)
        assertEquals("pikachu", result.name)
        assertEquals(4, result.height)
        assertEquals(60, result.weight)
        verify(mockApiService).getPokemonDetail(25)
    }

    @Test
    fun `getPokemonDetailByName returns pokemon detail when API call succeeds`() = runTest {

        val mockPokemonDetail = PokemonDetail(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            sprites = Sprites(null, null, null),
            stats = emptyList(),
            types = emptyList(),
            abilities = emptyList()
        )
        
        doReturn(mockPokemonDetail).`when`(mockApiService).getPokemonDetailByName("pikachu")

        val result = repository.getPokemonDetailByName("pikachu")

        assertEquals(25, result.id)
        assertEquals("pikachu", result.name)
        verify(mockApiService).getPokemonDetailByName("pikachu")
    }

    @Test
    fun `getPokemonDetailByName handles case insensitive names`() = runTest {

        val mockPokemonDetail = PokemonDetail(
            id = 25,
            name = "pikachu",
            height = 4,
            weight = 60,
            sprites = Sprites(null, null, null),
            stats = emptyList(),
            types = emptyList(),
            abilities = emptyList()
        )
        
        doReturn(mockPokemonDetail).`when`(mockApiService).getPokemonDetailByName("Pikachu")

        val result = repository.getPokemonDetailByName("Pikachu")

        assertEquals("pikachu", result.name)
        verify(mockApiService).getPokemonDetailByName("Pikachu")
    }

    @Test
    fun `repository handles null API responses gracefully`() = runTest {

        doReturn(null).`when`(mockApiService).getPokemonList(100, 0)

        val result = repository.getPokemonList(100, 0)

        assertTrue(result.isEmpty())
        verify(mockApiService).getPokemonList(100, 0)
    }

    @Test
    fun `repository handles empty API responses gracefully`() = runTest {

        val emptyResponse = PokemonListResponse(
            count = 0,
            next = null,
            previous = null,
            results = emptyList()
        )
        
        doReturn(emptyResponse).`when`(mockApiService).getPokemonList(100, 0)

        val result = repository.getPokemonList(100, 0)

        assertTrue(result.isEmpty())
        verify(mockApiService).getPokemonList(100, 0)
    }
}
