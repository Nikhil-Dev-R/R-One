package com.rudraksha.rone.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rudraksha.rone.R
import com.rudraksha.rone.ui.Screen
import com.rudraksha.rone.ui.components.RCard
import com.rudraksha.rone.ui.theme.TrackMeTheme
import com.rudraksha.rone.util.getDefaultGradientColors

@Composable
fun HomeScreen(
    navController: NavController,
) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = getDefaultGradientColors()
                )
            )
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Show a profile card
        RCard(
            modifier = Modifier
                .size(200.dp)
                .padding(24.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            onClick = { }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(100.dp),
                    contentDescription = null,
                    imageVector = Icons.Default.Person,
                    tint = Color.Cyan
                )
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Profile"
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                RCard(
                    modifier = Modifier.size(150.dp),
                    onClick = {
                        navController.navigate(
                            route = Screen.Weather.route,
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally),
                            contentDescription = null,
                            painter = painterResource(R.drawable.weather),
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            text = "Weather"
                        )
                    }
                }
            }

            item {
                RCard(
                    modifier = Modifier.size(150.dp),
                    onClick = {
                        try {
                            navController.navigate(Screen.Location.route)
                        } catch (e: Exception) {
                            println("Navigation error: ${e.message}")
                        }
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally),
                            contentDescription = null,
                            imageVector = Icons.Default.LocationOn,
                            tint = Color(red = 103, green = 58, blue = 183, alpha = 255)
                        )
                        Text(
                            modifier = Modifier.fillMaxSize().wrapContentSize(),
                            text = stringResource(id = R.string.location)
                        )
                    }
                }
            }

            item {
                RCard(
                    modifier = Modifier.size(150.dp),
                    onClick = {
                        navController.navigate(
                            route = Screen.News.route,
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 8.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.news_icon),
                            tint = Color(red = 255, green = 50, blue = 0)
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            text = "News"
                        )
                    }
                }
            }

            item {
                RCard(
                    modifier = Modifier.size(150.dp),
                    onClick = {
                        navController.navigate(
                            route = Screen.Facebook.route,
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally),
                            contentDescription = null,
                            imageVector = Icons.Filled.Facebook,
                            tint = Color.Blue
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            text = "Facebook"
                        )
                    }
                }
            }

            item {
                RCard(
                    modifier = Modifier.size(150.dp),
                    onClick = {
                        navController.navigate(
                            route = Screen.X.route,
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 12.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.x_icon),
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            text = "X"
                        )
                    }
                }
            }

            item {
                RCard(
                    modifier = Modifier.size(150.dp),
                    onClick = {
                        navController.navigate(
                            route = Screen.Instagram.route,
                        )
                    }
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .size(100.dp)
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 12.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.instagram_icon),
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            text = "Instagram"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomePreview() {
    TrackMeTheme {
        HomeScreen(rememberNavController())
    }
}
