package com.rudraksha.rone.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.motion.widget.MotionLayout
import com.rudraksha.rone.model.Article

@Preview
@Composable
fun NewsDescScreen(
    article: Article = Article()
) {
    Text("News Desc ${article.url}")
    val scrollState = rememberScrollState()
    val progress by remember(scrollState) {
        derivedStateOf {
            (scrollState.value / 400f).coerceIn(0f, 1f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
//        MotionLayoutHeader(progress)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                colors = CardColors (
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                modifier = Modifier.padding(4.dp),
            ) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Natus ipsum " +
                            "delectus repellendus suscipit eum quisquam obcaecati dolorum velit perferendis " +
                            "dolore doloremque itaque, ullam aliquid? Dolores deserunt perferendis, laborum " +
                            "rerum quidem velit blanditiis sed a totam debitis, reiciendis soluta! Dignissimos " +
                            "voluptates rerum quibusdam vero molestias temporibus provident nihil dolores " +
                            "architecto beatae cumque vitae pariatur tenetur non eveniet corrupti nemo " +
                            "possimus aperiam libero deserunt voluptatem explicabo blanditiis eligendi ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp),
                )
            }

            Card(
                colors = CardColors (
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContentColor = MaterialTheme.colorScheme.onTertiary,
                ),
                modifier = Modifier.padding(4.dp),
            ) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Natus ipsum " +
                            "delectus repellendus suscipit eum quisquam obcaecati dolorum velit perferendis " +
                            "dolore doloremque itaque, ullam aliquid? Dolores deserunt perferendis, laborum " +
                            "rerum quidem velit blanditiis sed a totam debitis, reiciendis soluta! Dignissimos " +
                            "voluptates rerum quibusdam vero molestias temporibus provident nihil dolores " +
                            "architecto beatae cumque vitae pariatur tenetur non eveniet corrupti nemo " +
                            "possimus aperiam libero deserunt voluptatem explicabo blanditiis eligendi ",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(4.dp),
                )
            }

        }
    }
}

/*@Composable
fun MotionLayoutHeader(progress: Float) {
    Mot(
        start = ConstraintSet {
            val image = createRefFor("image")
            val title = createRefFor("title")
            constrain(image) {
                width = Dimension.value(ExpandedImageSize)
                height = Dimension.value(ExpandedImageSize)
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
//                this.centerHorizontallyTo(parent)
            }
            constrain(title) {
                top.linkTo(image.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 8.dp)
            }
        },
        end = ConstraintSet {
            val image = createRefFor("image")
            val title = createRefFor("title")
            constrain(image) {
                width = Dimension.value(CollapsedImageSize)
                height = Dimension.value(CollapsedImageSize)
                top.linkTo(parent.top, margin = 4.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 4.dp)
            }
            constrain(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(image.start)
                bottom.linkTo(parent.bottom)
//                this.centerVerticallyTo(parent)
            }
        },
        progress = progress,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF00FD53),
                        Color(0xFF00FDA2),
                        Color(0xFF007AFD),
                        Color(0xFF0A0DFD),
                    )
                )
            ),
        transition = Transition {
            this.motionArc = Arc.StartHorizontal
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.nougat),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .layoutId("image")
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "Nougat",
            fontSize = (40 - (10 * progress)).sp,
            color = Color.White,
            modifier = Modifier.layoutId("title"),
//            fontFamily = FontFamily.Cursive
        )
    }
}

val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionLayoutHeader2(progress: Float) {
    MotionLayout(
        start = ConstraintSet {
            val image = createRefFor("image")
            val title = createRefFor("title")
            val box = createRefFor("box")
            constrain(box) {
                width = Dimension.matchParent
                height = Dimension.value(MaxTitleOffset)
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            constrain(image) {
                width = Dimension.value(ExpandedImageSize)
                height = Dimension.value(ExpandedImageSize)
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(title) {
                top.linkTo(image.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom, margin = 8.dp)
            }
        },
        end = ConstraintSet {
            val image = createRefFor("image")
            val title = createRefFor("title")
            val box = createRefFor("box")
            constrain(box) {
                width = Dimension.matchParent
                height = Dimension.value(MinTitleOffset)
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }
            constrain(image) {
                width = Dimension.value(CollapsedImageSize)
                height = Dimension.value(CollapsedImageSize)
                top.linkTo(parent.top, margin = 4.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 4.dp)
            }
            constrain(title) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(image.start)
                bottom.linkTo(parent.bottom)
//                this.centerVerticallyTo(parent)
            }
        },
        progress = progress,
        modifier = Modifier
            .wrapContentSize(),
//            .background(
//                Brush.linearGradient(
//                    colors = listOf(
//                        Color(0xFF00FD53),
//                        Color(0xFF00FDA2),
//                        Color(0xFF007AFD),
//                        Color(0xFF0A0DFD),
//                    )
//                )
//            ),
        transition = Transition {
            this.motionArc = Arc.StartHorizontal
        }
    ) {
        Box(
            modifier = Modifier
                .layoutId("box")
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF00FD53),
                            Color(0xFF00FDA2),
                            Color(0xFF007AFD),
                            Color(0xFF0A0DFD),
                        )
                    )
                ),
        )
        Image(
            painter = painterResource(id = R.drawable.nougat),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .layoutId("image")
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "Nougat",
            fontSize = (40 - (10 * progress)).sp,
            color = Color.White,
            modifier = Modifier.layoutId("title"),
//            fontFamily = FontFamily.Cursive
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetchatAppBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit = { },
    title: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .clip(ChatBubbleShape),
        actions = actions,
        title = title,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "",
                modifier = Modifier
                    .size(64.dp)
                    .clickable(onClick = onNavIconPressed)
                    .padding(16.dp)
            )
        }
    )
}*/
