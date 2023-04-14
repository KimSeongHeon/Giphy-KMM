package com.example.giphy_kmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.giphy_kmm.android.compose.BottomNavItem
import com.example.giphy_kmm.android.compose.GifContainerView
import com.example.giphy_kmm.android.compose.GifScrapView
import com.example.giphy_kmm.android.viewmodel.GiphyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val gifViewModel: GiphyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        bottomBar = { BottomNavigation(navController = navController) }
                    ) {
                        Box(modifier = Modifier.padding(it)) {
                            NavHost(
                                navController = navController,
                                startDestination = BottomNavItem.Grid.screenRoute
                            ) {
                                composable(BottomNavItem.Grid.screenRoute) {
                                    GifContainerView(gifViewModel = gifViewModel)
                                }
                                composable(BottomNavItem.Scrap.screenRoute) {
                                    GifScrapView(gifViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomNavigation(navController: NavHostController) {
        BottomNavigation(
            contentColor = MaterialTheme.colors.background
        ) {
            val items = listOf(BottomNavItem.Grid, BottomNavItem.Scrap)

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute == item.screenRoute,
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.screenRoute,
                            modifier = Modifier
                                .width(26.dp)
                                .height(26.dp)
                        )
                    },
                    label = { Text(item.screenRoute) },
                    onClick = {
                        navController.navigate(item.screenRoute) {
                            navController.graph.startDestinationRoute?.let {
                                popUpTo(it) { saveState = true }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }

    private fun initViewModel() {
        gifViewModel.initSearchViewModel()
    }
}
