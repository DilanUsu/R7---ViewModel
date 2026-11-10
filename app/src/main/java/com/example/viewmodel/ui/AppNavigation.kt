package com.example.viewmodel.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object Routes {
    const val INICIO = "inicio"
    const val JUEGO = "juego"
    const val RESULTADO = "resultado"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.INICIO) {
        composable(Routes.INICIO) {
            StartScreen(
                onStart = { nombre ->
                    navController.navigate("${Routes.JUEGO}/$nombre")
                }
            )
        }

        composable(
            route = "${Routes.JUEGO}/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            GameScreen(
                playerName = nombre,
                onGameOver = { score ->
                    navController.navigate("${Routes.RESULTADO}/$nombre/$score") {
                        popUpTo("${Routes.JUEGO}/{nombre}") { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = "${Routes.RESULTADO}/{nombre}/{puntaje}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("puntaje") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val puntaje = backStackEntry.arguments?.getInt("puntaje") ?: 0
            ResultScreen(
                playerName = nombre,
                score = puntaje,
                onPlayAgain = {
                    navController.navigate("${Routes.JUEGO}/$nombre") {
                        popUpTo(Routes.INICIO)
                    }
                },
                onBackToStart = {
                    navController.popBackStack(Routes.INICIO, inclusive = false)
                }
            )
        }
    }
}
