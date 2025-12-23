package com.fattyleo.defanceshotokankarate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.fattyleo.defanceshotokankarate.ui.theme.DefanceShotokanKarateTheme
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DefanceShotokanKarateTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val navigator = koinInject<Navigator>()

                    NavHost(
                        navController = navController,
                        startDestination = navigator.startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        navigation<Destination.AuthGraph>(
                            startDestination = Destination.LoginScreen
                        ){
                            composable<Destination.LoginScreen> {
                                Box (
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Button(onClick = { /*TODO()*/ }) {
                                        Text(text = "Login")
                                    }
                                }
                            }
                        }

                        navigation<Destination.HomeGraph>(
                            startDestination = Destination.HomeScreen
                        ){
                            composable<Destination.HomeScreen> {
                                Box (
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Button(onClick = { /*TODO()*/ }) {
                                        Text(text = "Go to detail")
                                    }
                                }
                            }

                            composable<Destination.DetailsScreen> {
                                val args = it.toRoute<Destination.DetailsScreen>()
                                Column (
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    Text(text = "ID: ${args.id}")
                                    Button(onClick = { /*TODO()*/ }) {
                                        Text(text = "Go back")
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

