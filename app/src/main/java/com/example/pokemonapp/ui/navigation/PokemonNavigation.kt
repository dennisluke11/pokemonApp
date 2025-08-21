package com.example.pokemonapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemonapp.ui.screen.PokemonDetailScreen
import com.example.pokemonapp.ui.screen.PokemonListScreen
import com.example.pokemonapp.ui.viewmodel.PokemonDetailViewModel
import com.example.pokemonapp.ui.viewmodel.PokemonListViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemon_list")
    object PokemonDetail : Screen("pokemon_detail/{pokemonId}") {
        fun createRoute(pokemonId: Int) = "pokemon_detail/$pokemonId"
    }
}

@Composable
fun PokemonNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route
    ) {
        composable(Screen.PokemonList.route) {
            val viewModel: PokemonListViewModel = koinViewModel()
            PokemonListScreen(
                viewModel = viewModel,
                onPokemonClick = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                }
            )
        }
        
        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument("pokemonId") { 
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) {
            val viewModel: PokemonDetailViewModel = koinViewModel()
            PokemonDetailScreen(
                viewModel = viewModel,
                onBackClick = { 
                    try {
                        navController.popBackStack()
                    } catch (e: Exception) {
                        navController.navigate(Screen.PokemonList.route) {
                            popUpTo(Screen.PokemonList.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
