package com.arbuzerxxl.vibeshot.features.tasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arbuzerxxl.vibeshot.core.design.theme.VibeShotThemePreview
import com.arbuzerxxl.vibeshot.core.design.theme.baseButtonHeight
import com.arbuzerxxl.vibeshot.core.design.theme.cornerSize16
import com.arbuzerxxl.vibeshot.core.design.theme.padding16
import com.arbuzerxxl.vibeshot.core.design.theme.padding4
import com.arbuzerxxl.vibeshot.core.ui.DevicePreviews
import com.arbuzerxxl.vibeshot.core.ui.widgets.LoadingIndicator
import com.arbuzerxxl.vibeshot.core.ui.widgets.TaskTags
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.MoodResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.SeasonResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TopicResource
import com.arbuzerxxl.vibeshot.ui.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun TasksRoute(
    modifier: Modifier = Modifier,
    viewmodel: TasksViewModel = koinViewModel(),
) {

    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    TasksScreen(
        modifier = modifier,
        uiState = uiState,
        onMoodClick = viewmodel::onMoodClick,
        onSeasonClick = viewmodel::onSeasonClick,
        onTopicClick = viewmodel::onTopicClick,
        onGenerateTaskClick = viewmodel::onGenerateTaskClick,
    )
}

@Composable
internal fun TasksScreen(
    modifier: Modifier = Modifier,
    uiState: TasksUiState,
    onMoodClick: (String) -> Unit,
    onSeasonClick: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    onGenerateTaskClick: () -> Unit,
) {

    Box(
        modifier = modifier
            .padding(padding16)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopStart
    ) {
        when (uiState) {
            TasksUiState.Loading -> LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            is TasksUiState.Success,
                -> TaskContent(
                uiState = uiState,
                generateTask = onGenerateTaskClick,
                onMoodClick = onMoodClick,
                onSeasonClick = onSeasonClick,
                onTopicClick = onTopicClick
            )
        }
    }
}

@Composable
private fun TaskContent(
    modifier: Modifier = Modifier,
    uiState: TasksUiState.Success,
    onMoodClick: (String) -> Unit,
    onSeasonClick: (String) -> Unit,
    onTopicClick: (String) -> Unit,
    generateTask: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {

        TaskTags(titleId = R.string.select_mood, items = uiState.categories.mood.moods, onClick = onMoodClick)
        TaskTags(titleId = R.string.select_season, items = uiState.categories.season.seasons, onClick = onSeasonClick)
        TaskTags(titleId = R.string.select_topic, items = uiState.categories.topic.topics, onClick = onTopicClick)

        TextButton(
            modifier = modifier
                .padding(vertical = padding16)
                .wrapContentWidth()
                .requiredHeight(baseButtonHeight)
                .align(Alignment.CenterHorizontally),
            onClick = { generateTask() },
            contentPadding = PaddingValues(vertical = padding4),
            colors = ButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(cornerSize16)
        ) {
            Text(
                text = "Generate", style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Light,
                )
            )
        }

        uiState.task?.let { task ->
            Text(
                text = "Task - $task", style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@DevicePreviews
@Composable
fun TasksScreenPreview() {
    VibeShotThemePreview {
        TasksScreen(
            uiState = TasksUiState.Success(
                categories = TaskCategoryResource(
                    mood = MoodResource(listOf()),
                    season = SeasonResource(listOf()),
                    topic = TopicResource(listOf())
                ),
                task = null, mood = "sad", season = "winter", topic = "nature"
            ),
            onGenerateTaskClick = {},
            onMoodClick = {}, onSeasonClick = {}, onTopicClick = {}
        )
    }
}
