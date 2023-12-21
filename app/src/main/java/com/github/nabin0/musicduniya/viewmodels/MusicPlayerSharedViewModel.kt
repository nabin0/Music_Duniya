package com.github.nabin0.musicduniya.viewmodels

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.github.nabin0.audioplayer.models.Audio
import com.github.nabin0.musicduniya.utils.globalAudioList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerSharedViewModel @Inject constructor(savedStateHandle: SavedStateHandle,): ViewModel() {

    @OptIn(SavedStateHandleSaveableApi::class)
    var audioList by savedStateHandle.saveable { mutableStateOf(listOf<Audio>()) }

    var currentSelectedAudio by savedStateHandle.saveable{ mutableIntStateOf(-1) }

    init {
        viewModelScope.launch {
            loadAudioData()
        }
    }

    fun loadAudioData() {
        viewModelScope.launch {
            globalAudioList.collect {
                audioList = it
            }
        }
    }

}