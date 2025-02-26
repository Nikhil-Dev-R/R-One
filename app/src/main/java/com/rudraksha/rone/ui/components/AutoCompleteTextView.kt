package com.rudraksha.rone.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.rudraksha.rone.model.CitySuggestion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
//    onValueChange: (String) -> Unit = {},
    fetchCitySuggestions: (query: String) -> List<CitySuggestion> = { emptyList<CitySuggestion>() }
) {
    var text by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf<List<CitySuggestion>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxWidth().wrapContentHeight()
    ) {
        // Input field for city name
        TextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                if (newValue.length > 2) { // Query API for inputs longer than 2 characters
                    scope.launch(Dispatchers.IO) {
                        try {
                            val result = fetchCitySuggestions(newValue)
                            suggestions = result
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    suggestions = emptyList()
                }
            },
            placeholder = {
                Text("Search for a City", style = MaterialTheme.typography.bodyMedium)
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(50.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Dropdown list for suggestions
        DropdownMenu(
            expanded = suggestions.isNotEmpty(),
            onDismissRequest = { suggestions = emptyList() }
        ) {
            suggestions.forEach { suggestion ->
                DropdownMenuItem(
                    text = { Text("${suggestion.name}, ${suggestion.country}") },
                    onClick = {
                        text = suggestion.name
                        suggestions = emptyList()
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun TePre() {
    AutoCompleteTextField()
}