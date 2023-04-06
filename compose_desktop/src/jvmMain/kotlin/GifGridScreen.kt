import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seiko.imageloader.rememberAsyncImagePainter
import kotlinx.coroutines.flow.distinctUntilChanged
import model.GifUiModel

private const val COLUMN_COUNT = 3
private const val GIF_TEXT_FIELD_LABEL = "Search"
private const val GIF_SEARCH_LIMIT_COUNT = 25

@Composable
fun GifGridTitle(viewModel: GiphyViewModel) {
    Row {
        Text(
            text = viewModel.giphyTitle.value,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(10.dp)
                .weight(1F, true),
            fontFamily = FontFamily.Monospace,
            maxLines = 1,
            style = TextStyle(textAlign = TextAlign.Center)
        )
    }
}

@Composable
fun GifAutoCompleteWindow(viewModel: GiphyViewModel) {
    val isAutoCompleteVisible = viewModel.autoTermListShow
    val autoTermList = viewModel.autoTermsList
    val listState = rememberLazyGridState()

    if (isAutoCompleteVisible.value) {
        Column {
            Button(
                onClick = { viewModel.setAutoCompleteVisibility(false) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "X")
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(COLUMN_COUNT + 1),
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(autoTermList) { term ->
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append(term.name)
                            }
                        },
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { viewModel.updateSearchQuery(term.name) },
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun GifSearchBox(viewModel: GiphyViewModel) {
    var text by remember { viewModel.searchQuery }
    val searchModeText by viewModel.searchMode

    Column {
        Row {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.updateSearchQuery(it)
                },
                label = { Text(GIF_TEXT_FIELD_LABEL) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .weight(weight = 1.0F, fill = true)
                    .testTag("TextField"),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray
                )
            )

            Button(onClick = { viewModel.swapSearchMode() }) {
                Text(text = searchModeText.name)
            }
        }

        GifAutoCompleteWindow(viewModel = viewModel)
    }

}

@Composable
fun GifMainLayout(viewModel: GiphyViewModel) {
    val gifObjects = viewModel.gifListStateFlow.collectAsState()

    GifMainLayout(gifObjects.value, viewModel)
}
@Composable
fun GifMainLayout(gifObjects: List<GifUiModel>, viewModel: GiphyViewModel) {
    val listState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(COLUMN_COUNT),
        state = listState
    ) {
        itemsIndexed(gifObjects) { index, gifObject ->
            GifGridItems(index, gifObject)
        }
    }

    EndlessListHandler(listState = listState, buffer = 2) {
        val searchOffset = gifObjects.size / GIF_SEARCH_LIMIT_COUNT
        viewModel.loadMoreItem(searchOffset)
    }
}

@Composable
fun EndlessListHandler(listState: LazyGridState, buffer: Int, callback: () -> Unit) {
    val loadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemNum = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemNum - buffer)
        }
    }

    LaunchedEffect(key1 = loadMore) {
        snapshotFlow { loadMore.value }
            .distinctUntilChanged()
            .collect {
                callback()
            }
    }
}

@Composable
fun GifGridItems(index: Int, gifUiModel: GifUiModel) {
    Image(
        painter = rememberAsyncImagePainter(gifUiModel.downsizedUrl),
        modifier = Modifier
            .height(150.dp),
        contentDescription = gifUiModel.title
    )
}

