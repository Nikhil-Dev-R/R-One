package com.rudraksha.rone.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rudraksha.rone.model.DailyWeather
import com.rudraksha.rone.model.HourlyWeather
import com.rudraksha.rone.repo.CityCoordinates
import com.rudraksha.rone.viewmodel.WeatherViewModel
import coil.compose.AsyncImage
import com.rudraksha.rone.R
import com.rudraksha.rone.model.WeatherResponse
import com.rudraksha.rone.util.defaultWeatherGradientColors
import com.rudraksha.rone.util.getWeatherGradientColors
import com.rudraksha.rone.util.getWeatherIcon
import com.rudraksha.rone.util.toDateAndTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel = viewModel()
) {
    var isSearchShown by remember { mutableStateOf(false) }
    var fetch by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    var cityName by remember { mutableStateOf("") }
    var currentCityIndex by remember { mutableIntStateOf(-1) }
    var suggestions by remember { mutableStateOf<List<CityCoordinates>>(emptyList()) }
    var isFocused = remember { mutableStateOf(false) }

    val weatherUiState = weatherViewModel.uiState.collectAsState().value
    Log.d("My icon", weatherUiState.weatherData.weather.firstOrNull()?.icon ?: "No")
    val icon = getWeatherIcon(weatherUiState.weatherData.weather.firstOrNull()?.icon ?: "01d")
    val gradientColors = getWeatherGradientColors(weatherUiState.weatherData.weather.firstOrNull()?.icon ?: "01d")
    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.Default).launch {
            weatherViewModel.fetchWeatherData(
                "London"
            )
        }
    }
    LaunchedEffect(fetch) {
        if (currentCityIndex != -1) {
            weatherViewModel.fetchWeatherData(suggestions[currentCityIndex].name)
//            weatherViewModel.fetchWeatherData(
//                suggestions[currentCityIndex].lat,
//                suggestions[currentCityIndex].lon,
//            )

            cityName = suggestions[currentCityIndex].name
            Log.d("My Loc", "${suggestions[currentCityIndex].lat} ${suggestions[currentCityIndex].lon}")

//            currentCityIndex = -1
            isSearchShown = false
        } else {
            weatherViewModel.fetchWeatherData("London")
        }
    }
    LaunchedEffect(cityName) {
        if (cityName.length > 2) {
            weatherViewModel.fetchCitySuggestions(cityName)
        }
    }
    suggestions = weatherUiState.cityCoordinates

    Scaffold(
        modifier = Modifier.background(
            brush = Brush.linearGradient(
                defaultWeatherGradientColors
            )
        ),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Weather",
                        style = MaterialTheme.typography.headlineLarge,
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            fetch = !fetch
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                        )
                    }

                    IconButton(
                        onClick = {
                            isSearchShown = !isSearchShown
                        }
                    ) {
                        Icon(
                            imageVector = if (isSearchShown) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search/Close Icon",
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF00F3FD),
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            gradientColors
                        )
                    )
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                /*WeatherIcon(
                    iconCode = weatherUiState.weatherData.weather.firstOrNull()?.icon ?: "01d"
                )*/
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    // Main temperature display
                    Column {
                        Text(
                            text = "${weatherUiState.weatherData.main.temp}° C",
                            style = MaterialTheme.typography.headlineMedium,
                            fontSize = 48.sp,
                            color = Color.White
                        )

                        Text(
                            text = weatherUiState.weatherData.weather.firstOrNull()?.description?.capitalize(
                                Locale.current
                            ) ?: "Weather",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White
                        )
                    }

                    Icon(
                        imageVector = icon.icon,
                        contentDescription = "Weather Icon",
                        tint = icon.tint,
                        modifier = modifier.size(80.dp),
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (currentCityIndex != -1)
                        suggestions[currentCityIndex].name + ", " + suggestions[currentCityIndex].country
                    else "London",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Cursive,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Feels like ",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Text(
                            text = weatherUiState.weatherData.main.feels_like.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = (weatherUiState.weatherData.main.temp_min).toString() + "°C/" + (weatherUiState.weatherData.main.temp_max) + "°C",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Hourly forecast
                if (weatherUiState.oneCallResponse.hourly.isNotEmpty()) {
                    Text(
                        text = "Hourly forecast",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(weatherUiState.oneCallResponse.hourly) { forecast ->
                            HourlyWeatherCard(forecast)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Daily forecast
                if (weatherUiState.oneCallResponse.daily.isNotEmpty()) {
                    Text(
                        text = "Daily forecast",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(weatherUiState.oneCallResponse.daily) { forecast ->
                            DailyWeatherCard(forecast)
                        }
                    }
                }

                // Other Weather details
                WeatherDetailsGrid(weatherUiState.weatherData)
            }

            if (isSearchShown) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    // Input field for city name
                    TextField(
                        value = cityName,
                        onValueChange = { newValue ->
                            cityName = newValue
                        },
                        placeholder = {
                            Text(
                                text = "Search for a City",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp)
                            .onFocusChanged { focusState ->
                                isFocused.value = focusState.isFocused
                            }
                            .clickable {
                                focusRequester.requestFocus()
                            }
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFF0090FD),
                                        Color(0xFF2700F5),
                                        Color(0xFF8700FD),
                                    ),
                                ),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )

                    LazyColumn {
                        items(suggestions.size) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .clickable {
                                        isSearchShown = !isSearchShown
                                        currentCityIndex = index
                                        fetch = !fetch
//                                        itemClicked = !itemClicked
                                        Log.d("currentCityIndex", currentCityIndex.toString())
                                    }
                                    .background(
                                        brush = Brush.linearGradient(
                                            colors = listOf(
                                                Color(0xFF00F3FD),
                                                Color(0xFF0090FD),
                                                Color(0xFF2700F5),
                                                Color(0xFF8700FD),
                                            )
                                        )
                                    )
                            ) {
                                Text(
                                    text = "${suggestions[index].name}, ${suggestions[index].country}",
                                    modifier = modifier
                                        .fillMaxSize()
                                        .border(
                                            width = 2.dp,
                                            color = Color.Black,
                                            /*brush = Brush.linearGradient(
                                            0.0f to Color.Red,
                                            0.3f to Color.Green,
                                            1.0f to Color.Black,
                                            start = Offset(0.0f, 50.0f),
                                            end = Offset(
                                                0.0f, 100.0f
                                            ),
                                        ),*/
//                                        shape = RectangleShape
                                        )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HourlyWeatherCard(forecast: HourlyWeather) {
    Column(
        modifier = Modifier
            .size(80.dp)
            .background(Color(0x33000000), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = forecast.dt.toString(), color = Color.White)

        /*Icon(
            painter = painterResource(id = forecast.icon),
            contentDescription = "Weather Icon",
            tint = Color.Unspecified
        )*/

        Text(text = "${forecast.temp}°", color = Color.White)
    }
}

@Composable
fun DailyWeatherCard(forecast: DailyWeather) {
    Column(
        modifier = Modifier
            .size(100.dp)
            .background(Color(0x33000000), shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = forecast.dt.toString(), color = Color.White)

        /*Icon(
            painter = painterResource(id = forecast.weatherIcons.),
            contentDescription = "Weather Icon",
            tint = Color.Unspecified
        )*/

        Text(text = "${forecast.temp.max}° / ${forecast.temp.min}°", color = Color.White)
    }
}

@Composable
fun WeatherIcon(iconCode: String, modifier: Modifier = Modifier) {
    val imageUrl = "http://openweathermap.org/img/w/$iconCode.png"
    AsyncImage(
        model = imageUrl,
        placeholder = painterResource(R.drawable.one),
        contentDescription = "Weather Icon",
        modifier = modifier.size(48.dp),
        onLoading = {
            Log.d("AsyncImage", "Loading image")
        },
        onSuccess = {
            Log.d("AsyncImage", "Image loaded successfully")
        },
        onError = {
            Log.e("AsyncImage", "Error loading image: ${it.result.throwable}")
        }
    )
}

// Data class to hold weather detail information
data class WeatherDetail(
    val title: String,
    val value: String,
    val icon: ImageVector,
    val iconTint: Color
)

@Composable
fun WeatherDetailsGrid(weatherUiState: WeatherResponse) {
    // Prepare the list of weather details
    val weatherDetails = listOf(
        WeatherDetail(
            "Sunrise",
            weatherUiState.sys.sunrise.toDateAndTime().second,
            Icons.Default.WbSunny,
            Color.Yellow
        ),
        WeatherDetail(
            "Sunset",
            weatherUiState.sys.sunset.toDateAndTime().second,
            Icons.Default.WbSunny,
            Color.Black
        ),
        WeatherDetail(
            "Cloudiness",
            weatherUiState.clouds.all.toString(),
            Icons.Default.Cloud,
            Color.Gray
        ),
        WeatherDetail(
            "Wind",
            "${weatherUiState.wind.speed}km/h ${weatherUiState.wind.deg}°",
            Icons.Default.Air,
            Color.LightGray
        ),
        WeatherDetail(
            "Visibility",
            weatherUiState.visibility.toString(),
            Icons.Default.Visibility,
            Color.White
        ),
        WeatherDetail(
            "Humidity",
            weatherUiState.main.humidity.toString(),
            Icons.Default.WaterDrop,
            Color.Black
        ),
        WeatherDetail(
            "Pressure",
            weatherUiState.main.pressure.toString(),
            Icons.AutoMirrored.Filled.TrendingDown,
            Color.Gray
        )
    )

    // Display the details in a grid
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 columns
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color(0x33000000),
                shape = MaterialTheme.shapes.medium
            ),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items(weatherDetails) { detail ->
            WeatherDetailCard(detail)
        }
    }
}

@Composable
fun WeatherDetailCard(detail: WeatherDetail) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .size(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x1AFFFFFF)
        )
    ) {
        // Background Icon
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = detail.icon,
                contentDescription = detail.title,
                tint = detail.iconTint.copy(alpha = 0.35f), // Make the icon semi-transparent
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
//                .align(Alignment.Center)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = detail.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = detail.value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun WeatherPreview() {
    WeatherScreen(
//        weatherData = WeatherData(
//            location = "Loc",
//            date = "01/01/2024",
//            currentTemp = 15,
//            weatherCondition = "We Con",
//            hourlyForecast = listOf(
//                HourlyForecast(
//                    time = "12:00",
//                    icon = R.drawable.ic_launcher_foreground,
//                    temp = 15
//                ),
//                HourlyForecast(
//                    time = "12:00",
//                    icon = R.drawable.ic_launcher_foreground,
//                    temp = 15
//                ),
//                HourlyForecast(
//                    time = "12:00",
//                    icon = R.drawable.ic_launcher_foreground,
//                    temp = 15
//                ),
//                HourlyForecast(
//                    time = "12:00",
//                    icon = R.drawable.ic_launcher_foreground,
//                    temp = 15
//                ),
//                HourlyForecast(
//                    time = "12:00",
//                    icon = R.drawable.ic_launcher_foreground,
//                    temp = 15
//                ),
//                HourlyForecast(
//                    time = "12:00",
//                    icon = R.drawable.ic_launcher_foreground,
//                    temp = 15
//                ),
//            ),
//            dailyForecast = listOf(
//                DailyForecast(
//                    day = "Mon",
//                    icon = R.drawable.ic_launcher_foreground,
//                    highTemp = 20,
//                    lowTemp = 10
//                ),
//                DailyForecast(
//                    day = "Mon",
//                    icon = R.drawable.ic_launcher_foreground,
//                    highTemp = 20,
//                    lowTemp = 10
//                ),
//                DailyForecast(
//                    day = "Mon",
//                    icon = R.drawable.ic_launcher_foreground,
//                    highTemp = 20,
//                    lowTemp = 10
//                ),
//                DailyForecast(
//                    day = "Mon",
//                    icon = R.drawable.ic_launcher_foreground,
//                    highTemp = 20,
//                    lowTemp = 10
//                ),
//            ),
//        )
    )
}
