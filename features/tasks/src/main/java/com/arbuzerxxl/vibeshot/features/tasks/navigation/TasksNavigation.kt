package com.arbuzerxxl.vibeshot.features.tasks.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arbuzerxxl.vibeshot.core.design.icon.VibeShotIcons
import com.arbuzerxxl.vibeshot.core.navigation.TopLevelDestination
import com.arbuzerxxl.vibeshot.features.tasks.di.tasksModule
import com.arbuzerxxl.vibeshot.features.tasks.presentation.TasksRoute
import com.arbuzerxxl.vibeshot.ui.R
import kotlinx.serialization.Serializable
import org.koin.compose.module.rememberKoinModules
import org.koin.core.annotation.KoinExperimentalAPI

@Serializable
object TasksDestination

data class TasksTopLevelDestination(
    override val icon: ImageVector = VibeShotIcons.Tasks,
    override val titleId: Int = R.string.tasks,
    override val route: Any = TasksDestination,
    override val destinationRoute: String = TasksDestination::class.java.name

): TopLevelDestination




fun NavController.navigateToTasks(navOptions: NavOptions? = null) {
    navigate(TasksDestination, navOptions)
}

@OptIn(KoinExperimentalAPI::class)
fun NavGraphBuilder.tasks() {

    composable<TasksDestination> {
        rememberKoinModules(unloadOnForgotten = true) { listOf(tasksModule) }

        TasksRoute()
    }
}