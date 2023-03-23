package com.example.giphy_kmm.android.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged

private const val COLUMN_COUNT = 3
private const val GIF_TEXT_FIELD_LABEL = "Search"
private const val GIF_SEARCH_LIMIT_COUNT = 25

@Composable
fun GifGridTitle(viewModel: GifViewModel) {
    //compose Text : https://developer.android.com/jetpack/compose/text
    val title by viewModel.gridTitle.observeAsState()

    Row {
        Text(
            text = title ?: "",
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifAutoCompleteWindow(viewModel: GifViewModel) {
    val isAutoCompleteVisible = viewModel.autoTermListShow
    val autoTermList = viewModel.autoTermsList
    val listState = rememberLazyListState()

    if (isAutoCompleteVisible.value) {
        Column {
            Button(onClick = { viewModel.hideAutoComplete()}, modifier = Modifier.align(Alignment.End)) {
                Text(text = "X")
            }

            LazyVerticalGrid(
                cells = GridCells.Fixed(COLUMN_COUNT + 1),
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(autoTermList) { term ->
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(color = Color.Blue)) {
                                append(term)
                            }
                        },
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily.Serif,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.clickable { viewModel.updateSearchQuery(term) },
                        maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun GifSearchBox(viewModel: GifViewModel) {
    var text by remember { viewModel.searchQuery }
    val searchModeText by viewModel.searchMode
    
    Column {
        Row {
            TextField(
                value = text,
                onValueChange = {
                    text = it
                    viewModel.updateTitle(it)
                    viewModel.updateSearchQuery(it)
                },
                label = { Text(GIF_TEXT_FIELD_LABEL) },
                leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifMainLayout(viewModel: GifViewModel) {
    val gifObjects = viewModel.gifList

    if (gifObjects.isEmpty()) {
        GifLoadingLayout()
    } else {
        GifMainLayout(gifObjects, viewModel)
    }
}

@Composable
fun GifLoadingLayout() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifMainLayout(gifObjects: List<GifObject>, viewModel: GifViewModel) {
    val listState = rememberLazyListState()

    LazyVerticalGrid(
        cells = GridCells.Fixed(COLUMN_COUNT),
        state = listState
    ) {
        itemsIndexed(gifObjects) { index, gifObject ->
            GifGridItems(viewModel, index, gifObject)
        }
    }

    EndlessListHandler(listState = listState, buffer = 2) {
        val searchOffset = gifObjects.size / GIF_SEARCH_LIMIT_COUNT
        viewModel.loadMoreItem(searchOffset)
    }
}

@Composable
fun EndlessListHandler(listState: LazyListState, buffer: Int, callback: () -> Unit) {
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
fun GifGridItems(viewModel: GifViewModel, index: Int, gifObject: GifObject) {
    val inVisible = viewModel.inVisibleList[index]
    GlideImage(
        alpha = if (inVisible) 1f else 0f,
        imageModel = gifObject.images.downsized.url,
        placeHolder = ImageVector.vectorResource(id = R.drawable.ic_launcher_foreground),
        modifier = Modifier
            .height(150.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        viewModel.downloadGif(gifObject.images.downsized.url, gifObject.title)
                    }
                )
            }
    )
}
