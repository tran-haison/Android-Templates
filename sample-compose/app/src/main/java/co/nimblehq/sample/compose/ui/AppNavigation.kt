package co.nimblehq.sample.compose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.*
import co.nimblehq.sample.compose.model.UiModel
import co.nimblehq.sample.compose.ui.screens.home.HomeScreen
import co.nimblehq.sample.compose.ui.screens.second.SecondScreen
import co.nimblehq.sample.compose.ui.screens.third.ThirdScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = AppDestination.Home.destination
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(destination = AppDestination.Home) {
            HomeScreen(
                navigator = { destination ->
                    navController.navigate(destination, destination.parcelableArgument)
                }
            )
        }

        composable(destination = AppDestination.Second) { backStackEntry ->
            SecondScreen(
                navigator = { destination -> navController.navigate(destination) },
                id = backStackEntry.arguments?.getString(KeyId).orEmpty()
            )
        }

        composable(destination = AppDestination.Third) {
            ThirdScreen(
                navigator = { destination -> navController.navigate(destination) },
                model = navController.previousBackStackEntry?.savedStateHandle?.get<UiModel>(KeyModel)
            )
        }
    }
}

private fun NavGraphBuilder.composable(
    destination: AppDestination,
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = destination.route,
        arguments = destination.arguments,
        deepLinks = deepLinks,
        content = content
    )
}

/**
 * Navigate to provided [AppDestination] with a Pair of key value String and Data [parcel]
 * Caution to use this method. This method use savedStateHandle to store the Parcelable data.
 * When previousBackstackEntry is popped out from navigation stack, savedStateHandle will return null and cannot retrieve data.
 * eg.Login -> Home, the Login screen will be popped from the back-stack on logging in successfully.
 */
private fun NavHostController.navigate(appDestination: AppDestination, parcel: Pair<String, Any?>? = null) {
    when (appDestination) {
        is AppDestination.Up -> navigateUp()
        else -> {
            parcel?.let { (key, value) ->
                currentBackStackEntry?.savedStateHandle?.set(key, value)
            }
            navigate(route = appDestination.destination)
        }
    }
}
