package by.tutorials.musicapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.tutorials.musicapp.data.model.Song
import by.tutorials.musicapp.data.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongViewModel(private val repo: MusicRepository) : ViewModel() {
    private val _songs    = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs.asStateFlow()

    private val _loading  = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error    = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _endReached = MutableStateFlow(false)
    val endReached: StateFlow<Boolean> = _endReached.asStateFlow()

     private val pageSize  = 8
    private var currentOffset = 0
    private var currentTerm   = ""


    fun searchSongs(term: String) {
        currentTerm    = term
        currentOffset  = 0
        _songs.value   = emptyList()
        _endReached.value = false
        loadMore()
    }

    fun loadMore() {
        if (_loading.value || _endReached.value) return

        viewModelScope.launch {
            _loading.value = true
            _error.value   = null

            try {
                val page = repo.search(currentTerm, pageSize, currentOffset)

                _songs.value = (_songs.value + page)
                    .distinctBy { it.id }

                _endReached.value = page.size < pageSize
                currentOffset    += page.size
            } catch (t: Throwable) {
                _error.value = t.message
            } finally {
                _loading.value = false
            }
        }
    }

}