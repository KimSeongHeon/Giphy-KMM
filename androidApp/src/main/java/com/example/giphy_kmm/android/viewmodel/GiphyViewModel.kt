package com.example.giphy_kmm.android.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.giphy_kmm.GiphySharedEngine
import com.example.giphy_kmm.android.model.GifAutoCompleteUiModel
import com.example.giphy_kmm.android.model.GifUiModel
import com.example.giphy_kmm.data.SearchMode
import com.example.giphy_kmm.domain.entity.GifEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GiphyViewModel @Inject constructor(
    private val giphyEngine: GiphySharedEngine
) : BaseViewModel() {
    private var randomApiJob: Job? = null
    private var queryApiJob: Job? = null
    private var autoCompleteApiJob: Job? = null

    private val _gridTitle = MutableLiveData(GIF_INITIAL_TITLE)
    val gridTitle: LiveData<String>
        get() = _gridTitle

    private val _gifListStateFlow = MutableStateFlow<List<GifUiModel>>(listOf())
    val gifListStateFlow: StateFlow<List<GifUiModel>> get() = _gifListStateFlow

    private val _scrapListStateFlow = MutableStateFlow<List<GifUiModel>>(listOf())
    val scrapListStateFlow: StateFlow<List<GifUiModel>> get() = _scrapListStateFlow

    val searchQuery = mutableStateOf("")
    val autoTermsList = mutableStateListOf<GifAutoCompleteUiModel>()
    val autoTermListShow = mutableStateOf(false)
    val searchMode = mutableStateOf(SearchMode.DEBOUNCING)

    private val throttlingBehaviorSubject = BehaviorSubject.create<String>()
    private val debounceBehaviorSubject = BehaviorSubject.create<String>()

    private var searchOffset = 0

    fun initSearchViewModel() {
        addDisposable(debounceBehaviorSubject
            .debounce(DEBOUNCING_TIME, TimeUnit.MILLISECONDS)
            .subscribe { query ->
                if (query.isEmpty()) {
                    getRandomGifList(GIF_COUNT)
                } else {
                    getGifObjectsFromQuery(query)
                    getAutoCompleteTerms(query)
                }
            })


        addDisposable(throttlingBehaviorSubject
            .throttleLatest(THROTTLING_TIME, TimeUnit.MILLISECONDS)
            .subscribe { query ->
                if (query.isEmpty()) {
                    getRandomGifList(GIF_COUNT)
                } else {
                    getGifObjectsFromQuery(query)
                    getAutoCompleteTerms(query)
                }
            })

        searchResult("")
    }

    fun updateTitle(query: String) {
        if (query.isEmpty()) {
            _gridTitle.postValue(GIF_INITIAL_TITLE)
        } else {
            _gridTitle.postValue(GIF_SEARCH_PREFIX + query + GIF_SEARCH_POSTFIX)
        }
    }

    fun updateSearchQuery(query: String) {
        viewModelScope.launch {
            clearAllPreviousJob()

            autoTermListShow.value = false
            searchQuery.value = query
            searchResult(query)
        }
    }

    fun loadMoreItem(searchOffset: Int) {
        val query = searchQuery.value
        this.searchOffset = searchOffset

        searchResult(query)
    }

    fun hideAutoComplete() {
        viewModelScope.launch {
            autoTermListShow.value = false
            autoCompleteApiJob?.cancel()
        }
    }

    fun getAllScraps() {
        viewModelScope.launch {
            giphyEngine.getScrapGifs()
                .collect { newList ->
                    val newUiModelList =
                        newList.map { GifUiModel(it.id, it.title, it.url, it.downsizedUrl) }
                    _scrapListStateFlow.emit(newUiModelList)
                }
        }
    }

    fun addScrap(model: GifUiModel) {
        viewModelScope.launch {
            val entity = GifEntity(model.id, model.title, model.url, model.downsizedUrl)
            giphyEngine.addScrap(entity)
        }
    }

    fun removeScrap(model: GifUiModel) {
        viewModelScope.launch {
            val entity = GifEntity(model.id, model.title, model.url, model.downsizedUrl)
            giphyEngine.removeScrap(entity)
        }
    }

    fun swapSearchMode() {
        if (searchMode.value == SearchMode.DEBOUNCING) {
            this.searchMode.value = (SearchMode.THROTTLING)
        } else {
            this.searchMode.value = (SearchMode.DEBOUNCING)
        }
    }

    private fun getRandomGifList(count: Int) {
        randomApiJob = viewModelScope.launch {
            (0 until count)
                .asSequence()
                .asFlow()
                .onEach { delay(API_CALL_DELAY) }
                .flatMapConcat { giphyEngine.getRandomGif() }
                .collect { entity ->
                    val uiModel =
                        GifUiModel(entity.id, entity.title, entity.url, entity.downsizedUrl)
                    _gifListStateFlow.emit(_gifListStateFlow.value + listOf(uiModel))
                }
        }
    }

    /*
    * Throttle : 이벤트를 일정한 주기마다 발생하도록 함
    * Debounce : 이벤트를 그룹화하여 특정시간이 지난 후, 하나의 이벤트만 발생시키도록 한다.
     */
    private fun getGifObjectsFromQuery(query: String) {
        queryApiJob = viewModelScope.launch {
            giphyEngine.getGifFromSearchQuery(query, searchOffset)
                .collectLatest { newList ->
                    val newUiModelList =
                        newList.map { GifUiModel(it.id, it.title, it.url, it.downsizedUrl) }
                    _gifListStateFlow.emit(gifListStateFlow.value + newUiModelList)
                }
        }
    }

    private fun getAutoCompleteTerms(query: String) {
        if (query.length < 2 || autoTermsList.isNotEmpty()) return

        viewModelScope.launch {
            giphyEngine.getAutoCompleteTerms(query)
                .collectLatest { autoTermList ->
                    val uiModelList = autoTermList.map { GifAutoCompleteUiModel(it.name) }
                    if (uiModelList.isNotEmpty()) {
                        autoTermListShow.value = true
                        autoTermsList.addAll(uiModelList)
                    }
                }
        }
    }

    private fun searchResult(query: String) {
        when (searchMode.value) {
            SearchMode.THROTTLING -> searchResultByThrottle(query)
            SearchMode.DEBOUNCING -> searchResultByDebounce(query)
        }
    }

    // https://nuritech.tistory.com/31
    // 이벤트 발생과 동시에 타이머 실행. 타이머 중 새로운 이벤트 오면 해당 이벤트 기준으로 타이머. 마지막 이벤트만 발행함
    private fun searchResultByDebounce(query: String) {
        debounceBehaviorSubject.onNext(query)
    }

    // https://nuritech.tistory.com/31
    // 새로운 이벤트가 들어온 후, 일정한 시간 간격(intervalDuration) 이 지난 후에 해당 간격 중 가장 마지막으로 들어온 이벤트를 발생시킨다.
    private fun searchResultByThrottle(query: String) {
        throttlingBehaviorSubject.onNext(query)
    }

    private suspend fun clearAllPreviousJob() {
        randomApiJob?.cancelAndJoin()
        queryApiJob?.cancelAndJoin()
        autoCompleteApiJob?.cancelAndJoin()

        _gifListStateFlow.emit(listOf())
        autoTermsList.clear()

        searchOffset = 0
    }

    fun downloadGif(url: String, fileName: String) = Unit

    companion object {
        const val TWINKLE_LIST_SIZE = 50
        const val API_CALL_DELAY = 200L
        const val GIF_INITIAL_TITLE = "Giphy Random GIFs"
        const val GIF_SEARCH_PREFIX = "search "
        const val GIF_SEARCH_POSTFIX = " ..."
        private const val GET_GIF_OBJETS_TAG = "GET_GIF_OBJETS_TAG"
        private const val GET_AUTO_COMPLETE_TERMS = "GET_AUTO_COMPLETE_TERMS"
        private const val THROTTLING_TIME = 200L
        private const val DEBOUNCING_TIME = 200L

        private const val GIF_COUNT = 30
    }
}
