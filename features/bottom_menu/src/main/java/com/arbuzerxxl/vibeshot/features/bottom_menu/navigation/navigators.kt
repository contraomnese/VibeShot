package com.arbuzerxxl.vibeshot.features.bottom_menu.navigation

import androidx.navigation.NavHostController
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestNavigator

fun NavHostController.interestsNavigator(externalNavigator: BottomMenuNavigator): InterestNavigator = object: InterestNavigator{

    override fun navigateToDetails(photoId: String) {
        externalNavigator.onNavigateToDetails(photoId)
    }

    override fun onNavigateUp() {
        externalNavigator.onNavigateUp()
    }

}