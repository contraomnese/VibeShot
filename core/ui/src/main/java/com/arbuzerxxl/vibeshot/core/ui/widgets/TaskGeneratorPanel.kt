package com.arbuzerxxl.vibeshot.core.ui.widgets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arbuzerxxl.vibeshot.domain.models.photo_tasks.TaskCategoryResource
import com.arbuzerxxl.vibeshot.ui.R

@Composable
fun ColumnScope.TaskGeneratorPanel(
    modifier: Modifier = Modifier,
    categoryResource: TaskCategoryResource,
    currentMood: String,
    currentSeason: String,
    currentTopic: String,
    onMoodClicked: (String) -> Unit,
    onSeasonClicked: (String) -> Unit,
    onTopicClicked: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit
) {

    CollapsibleWidget(
        modifier = modifier.align(Alignment.CenterHorizontally),
        isExpanded = isExpanded,
        onExpandedChange = onExpandedChange
    ) {
        TaskTags(
            titleId = R.string.select_mood,
            items = categoryResource.moods.map { it.title },
            currentItem = currentMood,
            onClick = onMoodClicked
        )
        TaskTags(
            titleId = R.string.select_season,
            items = categoryResource.seasons.map { it.title },
            currentItem = currentSeason,
            onClick = onSeasonClicked
        )
        TaskTags(
            titleId = R.string.select_topic,
            items = categoryResource.topics.map { it.title },
            currentItem = currentTopic,
            onClick = onTopicClicked
        )
    }
}