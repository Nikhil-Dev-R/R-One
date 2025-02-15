package com.rudraksha.trackme.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rudraksha.trackme.model.DailyWeather
import com.rudraksha.trackme.model.HourlyWeather
import com.rudraksha.trackme.repo.CityCoordinates
import com.rudraksha.trackme.viewmodel.WeatherViewModel
import coil.compose.AsyncImage
import com.rudraksha.trackme.R
import com.rudraksha.trackme.util.getWeatherIcon
import com.rudraksha.trackme.util.toDateAndTime

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

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeatherData(
            "London"
        )
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
                listOf(
                    Color(0xFF8700FD),
                    Color(0xFF2700F5),
                    Color(0xFF0090FD),
                    Color(0xFF00F3FD),
                )
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
                            listOf(
                                Color(0xFF8700FD),
                                Color(0xFF2700F5),
                                Color(0xFF0090FD),
                                Color(0xFF00F3FD),
                            )
                        )
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (currentCityIndex != -1)
                        suggestions[currentCityIndex].name + ", " + suggestions[currentCityIndex].country
                    else "London",
                    style = MaterialTheme.typography.headlineMedium,
                    fontFamily = FontFamily.Cursive,
                    color = Color.White
                )

                /*WeatherIcon(
                    iconCode = weatherUiState.weatherData.weather.firstOrNull()?.icon ?: "01d"
                )*/
                Icon(
                    imageVector = icon,
                    contentDescription = "Weather Icon",
                    tint = Color.White,
                    modifier = modifier.size(48.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Main temperature display
                Text(
                    text = "${weatherUiState.weatherData.main.temp}° C",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White
                )

                Text(
                    text = weatherUiState.weatherData.weather.firstOrNull()?.description?.capitalize(
                        Locale.current
                    ) ?: "Weather",
                    style = MaterialTheme.typography.bodyLarge,
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

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp))
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(
                            color = Color(0x33000000),
                        ),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Cloudiness",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Cloud,
                                contentDescription = "Cloudiness",
                                tint = Color.Gray
                            )
                            Text(
                                text = weatherUiState.weatherData.clouds.all.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Sunrise",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunrise",
                                tint = Color.Yellow
                            )
                            Text(
                                text = weatherUiState.weatherData.sys.sunrise.toDateAndTime().second,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Sunrise",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.WbSunny,
                                contentDescription = "Sunnset",
                                tint = Color.Black
                            )
                            Text(
                                text = weatherUiState.weatherData.sys.sunset.toDateAndTime().second,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Wind",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "wind",
                                tint = Color.LightGray
                            )
                            Text(
                                text = weatherUiState.weatherData.wind.speed.toString() + "km/h " + weatherUiState.weatherData.wind.deg.toString() + "°",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Visibility",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Visibility",
                                tint = Color.White
                            )
                            Text(
                                text = weatherUiState.weatherData.visibility.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Humidity",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Humidity",
                                tint = Color.Black
                            )
                            Text(
                                text = weatherUiState.weatherData.main.humidity.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }

                    item {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Pressure",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingDown,
                                contentDescription = "Visibility",
                                tint = Color.Gray
                            )
                            Text(
                                text = weatherUiState.weatherData.main.pressure.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                        }
                    }
                }
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
