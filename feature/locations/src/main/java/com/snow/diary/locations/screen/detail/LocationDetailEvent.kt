package com.snow.diary.locations.screen.detail;

import com.snow.diary.model.data.Dream

internal sealed class LocationDetailEvent {

    data class DreamFavouriteClick(
        val dream: Dream
    ): LocationDetailEvent()

    data class ChangeTab(
        val tab: LocationDetailTab
    ): LocationDetailEvent()


    data object Delete: LocationDetailEvent()

}