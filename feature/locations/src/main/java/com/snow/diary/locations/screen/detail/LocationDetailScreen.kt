package com.snow.diary.locations.screen.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.snow.diary.locations.R
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Location
import com.snow.diary.model.sort.SortConfig
import com.snow.diary.model.sort.SortMode
import com.snow.diary.ui.callback.DreamCallback
import com.snow.diary.ui.feed.DreamFeed
import com.snow.diary.ui.feed.DreamFeedState
import com.snow.diary.ui.screen.ErrorScreen
import com.snow.diary.ui.screen.LoadingScreen
import org.oneui.compose.base.Icon
import org.oneui.compose.layout.toolbar.CollapsingToolbarLayout
import org.oneui.compose.navigation.TabItem
import org.oneui.compose.navigation.Tabs
import org.oneui.compose.widgets.box.RoundedCornerBox
import org.oneui.compose.widgets.buttons.IconButton
import org.oneui.compose.widgets.text.TextSeparator
import dev.oneuiproject.oneui.R as IconR


@Composable
internal fun LocationDetail(
    viewModel: LocationDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onEditClick: (Location) -> Unit,
    onDreamClick: (Dream) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()

    LocationDetail(
        state = state,
        tabState = tabs,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onEditClick = onEditClick,
        onDreamClick = onDreamClick
    )
}

@Composable
private fun LocationDetail(
    state: LocationDetailState,
    tabState: LocationDetailTab,
    onEvent: (LocationDetailEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onEditClick: (Location) -> Unit,
    onDreamClick: (Dream) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CollapsingToolbarLayout(
            modifier = Modifier
                .weight(1F),
            toolbarTitle = (state as? LocationDetailState.Success)?.location?.name ?: "",
            appbarNavAction = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_back),
                    onClick = onNavigateBack
                )
            },
            appbarActions = {
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_edit_outline),
                    enabled = state is LocationDetailState.Success,
                    onClick = {
                        onEditClick((state as LocationDetailState.Success).location)
                    }
                )
                IconButton(
                    icon = Icon.Resource(IconR.drawable.ic_oui_delete_outline),
                    enabled = state is LocationDetailState.Success,
                    onClick = {
                        onNavigateBack()
                        onEvent(
                            LocationDetailEvent.Delete
                        )
                    }
                )
            }
        ) {
            when (state) {
                is LocationDetailState.Error -> ErrorScreen(title = stringResource(R.string.location_detail_error))
                LocationDetailState.Loading -> LoadingScreen(title = stringResource(R.string.location_detail_loading))
                is LocationDetailState.Success -> when (tabState) {
                    LocationDetailTab.General -> GeneralSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state
                    )

                    LocationDetailTab.Dreams -> DreamsSection(
                        modifier = Modifier
                            .fillMaxSize(),
                        state = state,
                        onDreamClick = onDreamClick,
                        onDreamFavouriteClick = {
                            onEvent(LocationDetailEvent.DreamFavouriteClick(it))
                        }
                    )
                }
            }
        }

        Tabs(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LocationDetailScreenDefaults.contentPadding)
        ) {
            LocationDetailTab.values().forEach { tab ->
                TabItem(
                    modifier = Modifier
                        .weight(1F),
                    onClick = {
                        onEvent(
                            LocationDetailEvent.ChangeTab(tab)
                        )
                    },
                    text = tab.localizedName(),
                    selected = tab == tabState,
                    enabled = tab == LocationDetailTab.General || !(state as? LocationDetailState.Success)?.dreams.isNullOrEmpty()
                )
            }
        }
    }
}

@Composable
private fun GeneralSection(
    modifier: Modifier = Modifier,
    state: LocationDetailState.Success
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        if(state.location.notes != null) {
            TextSeparator(
                text = stringResource(R.string.location_detail_note)
            )
            RoundedCornerBox(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = state.location.notes!!
                )
            }
        } else {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(LocationDetailScreenDefaults.noInfoPadding),
                text = stringResource(R.string.location_detail_no_further_information),
                textAlign = TextAlign.Center
            )
        }
    }

    //TODO: Show some fancy google maps map here
}

@Composable
private fun DreamsSection(
    modifier: Modifier = Modifier,
    state: LocationDetailState.Success,
    onDreamClick: (Dream) -> Unit,
    onDreamFavouriteClick: (Dream) -> Unit
) {
    DreamFeed(
        modifier = modifier,
        state = DreamFeedState.Success(state.dreams, true, SortConfig(SortMode.Created)),
        dreamCallback = object : DreamCallback {

            override fun onClick(dream: Dream) {
                onDreamClick(dream)
            }

            override fun onFavouriteClick(dream: Dream) {
                onDreamFavouriteClick(dream)
            }
        }
    )
}

private object LocationDetailScreenDefaults {

    val contentPadding = PaddingValues(
        horizontal = 16.dp
    )

    val noInfoPadding = PaddingValues(
        end = 16.dp,
        start = 16.dp,
        top = 32.dp
    )

}