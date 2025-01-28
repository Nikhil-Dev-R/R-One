package com.rudraksha.trackme.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun RCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    colors: CardColors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ),
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .size(150.dp)
            .padding(8.dp)
            .clickable {
                onClick()
            },
        shape = shape,
        colors = colors,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 12.dp,
            focusedElevation = 12.dp,
            hoveredElevation = 8.dp,
            draggedElevation = 16.dp,
            disabledElevation = (-4).dp
        ),
    ) {
        content()
    }
}