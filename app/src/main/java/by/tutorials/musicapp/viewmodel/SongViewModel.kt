package by.tutorials.musicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.tutorials.musicapp.data.model.Song
import by.tutorials.musicapp.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongViewModel(
    private val repo: MusicRepository
) : ViewModel() {

    private val _songs    = MutableStateFlow<List<Song>>(emptyList())
    private val _loading  = MutableStateFlow(false)
    private val _error    = MutableStateFlow<String?>(null)

    val songs: StateFlow<List<Song>> = _songs
    val loading: StateFlow<Boolean>     = _loading
    val error: StateFlow<String?>       = _error

    init {
        searchSongs("pop")
    }

    fun searchSongs(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value   = null
            try {
                _songs.value = repo.searchSongs(query)
            } catch (t: Throwable) {
                _error.value = t.localizedMessage ?: "error"
            } finally {
                _loading.value = false
            }
        }
    }
}