package com.example.musicfreshener2.ui.navigation

import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.musicfreshener2.ui.home.HomeDestination
import com.example.musicfreshener2.ui.home.HomeScreen
import com.example.musicfreshener2.ui.music.MusicAddDestination
import com.example.musicfreshener2.ui.music.MusicAddScreen
import com.example.musicfreshener2.ui.music.MusicEditDestination
import com.example.musicfreshener2.ui.music.MusicEditScreen

@Composable
fun MusicNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToAddMusic = { navHostController.navigate(MusicAddDestination.route) },
                navigateToEditMusic = { navHostController.navigate("${MusicEditDestination.route}/$it") }
            )
        }
        composable(route = MusicAddDestination.route) {
            MusicAddScreen(
                navigateBack = { navHostController.popBackStack() },
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
        composable(
            route = MusicEditDestination.routeWithArgs,
            arguments = listOf(navArgument(MusicEditDestination.musicIdArg) {
                type = NavType.IntType
            })
        ) {
            MusicEditScreen(
                navigateBack = { navHostController.popBackStack() },
                onNavigateUp = { navHostController.navigateUp() }
            )
        }
    }
}