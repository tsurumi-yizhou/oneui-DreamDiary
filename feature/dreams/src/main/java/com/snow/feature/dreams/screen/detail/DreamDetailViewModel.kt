package com.snow.feature.dreams.screen.detail;

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snow.diary.common.launchInBackground
import com.snow.diary.domain.action.dream.DeleteDream
import com.snow.diary.domain.action.dream.DreamInformation
import com.snow.diary.domain.action.person.UpdatePerson
import com.snow.diary.model.data.Dream
import com.snow.diary.model.data.Person
import com.snow.feature.dreams.nav.DreamDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class DreamDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    dreamInformation: DreamInformation,
    val updatePerson: UpdatePerson,
    val deleteDreamAct: DeleteDream
) : ViewModel() {

    private val args = DreamDetailArgs(savedStateHandle)

    val dreamDetailState = dreamInformation(args.dreamId)
        .map {
            if (it == null) DreamDetailState.Error(args.dreamId)
            else DreamDetailState.Success(
                it.dream, it.locations, it.persons
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DreamDetailState.Loading
        )

    private val _tabState = MutableStateFlow(
        DreamDetailTabState(
            tab = DreamDetailTab.General, subtab = DreamDetailSubtab.Content
        )
    )
    val tabState: StateFlow<DreamDetailTabState> = _tabState

    fun personFavouriteClick(person: Person) = viewModelScope.launchInBackground {
        updatePerson(
            person.copy(
                isFavourite = !person.isFavourite
            )
        )
    }

    fun deleteDream(dream: Dream) = viewModelScope.launchInBackground {
        //TODO: Give option to restore dream via global toast
        deleteDreamAct(dream)
    }

    fun changeTabState(tabState: DreamDetailTabState) =
        viewModelScope.launch { _tabState.emit(tabState) }

}