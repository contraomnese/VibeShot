package com.arbuzerxxl.vibeshot.features.bottom_menu.navigation

import androidx.navigation.NavHostController
import com.arbuzerxxl.vibeshot.features.interests.navigation.InterestNavigator
import com.arbuzerxxl.vibeshot.features.searching.navigation.SearchNavigator

fun NavHostController.interestsNavigator(externalNavigator: BottomMenuNavigator): InterestNavigator = object: InterestNavigator{

    override fun navigateToDetails(initialPhotoPosition: Int, parentDestination: String) {
        externalNavigator.onNavigateToDetails(initialPhotoPosition, parentDestination)
    }

    override fun onNavigateUp() {
        externalNavigator.onNavigateUp()
    }

}

fun NavHostController.searchNavigator(externalNavigator: BottomMenuNavigator): SearchNavigator = object: SearchNavigator{

    override fun navigateToDetails(initialPhotoPosition: Int, parentDestination: String) {
        externalNavigator.onNavigateToDetails(initialPhotoPosition, parentDestination)
    }

    override fun onNavigateUp() {
        externalNavigator.onNavigateUp()
    }

}