import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.giphy_kmm.GiphySharedEngine
import com.example.giphy_kmm.data.SearchMode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import model.GifAutoCompleteUiModel
import model.GifUiModel
import org.slf4j.event.Level
import java.util.logging.Logger

class GiphyViewModel {
    private val logger = Logger.getLogger(":::")
    private val giphyEngine = GiphySharedEngine()

    private val allApiJob: Job = Job()
    private val defaultDispatcher = Dispatchers.Default + allApiJob

    private var randomApiJob: Job? = null
    private var queryApiJob: Job? = null
    private var autoCompleteApiJob: Job? = null

    val giphyTitle = mutableStateOf(GIF_INITIAL_TITLE)

    private val _gifListStateFlow = MutableStateFlow<List<GifUiModel>>(listOf())
    val gifListStateFlow: StateFlow<List<GifUiModel>> get() = _gifListStateFlow

    val searchQuery = mutableStateOf("")
    val autoTermsList = mutableStateListOf<GifAutoCompleteUiModel>()
    val autoTermListShow = mutableStateOf(false)
    val searchMode = mutableStateOf(SearchMode.DEBOUNCING)

    private var searchOffset = 0

    fun initViewModel() {
        logger.log(java.util.logging.Level.ALL, "initViewModel")
        getRandomGifList(GIF_COUNT)
    }

    fun updateSearchQuery(query: String) {
        CoroutineScope(defaultDispatcher).launch {
            randomApiJob?.cancelAndJoin()
            queryApiJob?.cancelAndJoin()
            autoCompleteApiJob?.cancelAndJoin()

            _gifListStateFlow.emit(listOf())
            autoTermsList.clear()

            searchOffset = 0
            setAutoCompleteVisibility(false)

            searchQuery.value = query

            if (query.isEmpty()) {
                getRandomGifList(GIF_COUNT)
            } else {
                getGifObjectFromQuery(query)
                getAutoCompleteTerms(query)
            }

            updateTitle(query)
        }
    }

    fun swapSearchMode() {
        if (searchMode.value == SearchMode.DEBOUNCING) {
            searchMode.value = SearchMode.THROTTLING
        } else {
            searchMode.value = SearchMode.DEBOUNCING
        }
    }

    fun setAutoCompleteVisibility(visible: Boolean) {
        autoTermListShow.value = visible
    }

    fun loadMoreItem(searchOffset: Int) {
        this.searchOffset = searchOffset
        updateSearchQuery(searchQuery.value)
    }

    private fun updateTitle(query: String) {
        if (query.isEmpty()) {
            giphyTitle.value = GIF_INITIAL_TITLE
        } else {
            giphyTitle.value = GIF_SEARCH_PREFIX + query + GIF_SEARCH_POSTFIX
        }
    }

    private fun getRandomGifList(count: Int) {
        randomApiJob = CoroutineScope(defaultDispatcher).launch {
            (0 until count)
                .asSequence()
                .asFlow()
                .onEach { delay(API_CALL_DELAY) }
                .flatMapConcat { giphyEngine.getRandomGif() }
                .collect { entity ->
                    val uiModel = GifUiModel(entity.title, entity.url, entity.downsizedUrl)
                    logger.log(java.util.logging.Level.ALL, uiModel.toString())
                    _gifListStateFlow.emit(_gifListStateFlow.value + listOf(uiModel))
                }
        }
    }

    private fun getGifObjectFromQuery(query: String) {
        queryApiJob = CoroutineScope(defaultDispatcher).launch {
            giphyEngine.getGifFromSearchQuery(query, searchOffset)
                .collectLatest { newList ->
                    val newUiModelList =
                        newList.map { GifUiModel(it.title, it.url, it.downsizedUrl) }
                    _gifListStateFlow.emit(gifListStateFlow.value + newUiModelList)
                }
        }
    }

    private fun getAutoCompleteTerms(query: String) {
        if (query.length < 2 || autoTermsList.isNotEmpty()) return

        CoroutineScope(defaultDispatcher).launch {
            giphyEngine.getAutoCompleteTerms(query)
                .collectLatest { autoTermList ->
                    val uiModelList = autoTermList.map { GifAutoCompleteUiModel(it.name) }
                    if (uiModelList.isNotEmpty()) {
                        setAutoCompleteVisibility(true)
                        autoTermsList.addAll(uiModelList)
                    }
                }
        }
    }

    fun cancelAllJob() {
        allApiJob.cancel()
    }

    companion object {
        private const val GIF_INITIAL_TITLE = "Giphy Random GIFs"
        private const val GIF_SEARCH_PREFIX = "Search "
        private const val GIF_SEARCH_POSTFIX = " ..."
        private const val API_CALL_DELAY = 200L

        private const val GIF_COUNT = 30
    }
}
