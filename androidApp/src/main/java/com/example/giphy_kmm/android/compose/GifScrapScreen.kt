package com.example.giphy_kmm.android.compose

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import com.example.giphy_kmm.android.R
import com.example.giphy_kmm.android.model.GifUiModel
import com.example.giphy_kmm.android.viewmodel.GiphyViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun GifScrapView(viewModel: GiphyViewModel) {
    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> { viewModel.getAllScraps() }
            else -> Unit
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GifScrapTitle(viewModel)
        GifScrapLayout(viewModel)
    }
}

@Composable
fun GifScrapTitle(viewModel: GiphyViewModel) {
    val scrapGifList by viewModel.scrapListStateFlow.collectAsState()

    Row {
        Text(
            text = "${scrapGifList.size} ScrapGifs",
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
fun GifScrapLayout(viewModel: GiphyViewModel) {
    val listState = rememberLazyGridState()
    val gifObjects = viewModel.scrapListStateFlow.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        state = listState
    ) {
        itemsIndexed(gifObjects.value) { index, gifObject ->
            GifScrapGridItems(viewModel, gifObject)
        }
    }
}

@Composable
fun GifScrapGridItems(viewModel: GiphyViewModel, gifUiModel: GifUiModel) {
    val context = LocalContext.current
    GlideImage(
        imageModel = gifUiModel.downsizedUrl,
        placeHolder = ImageVector.vectorResource(id = R.drawable.baseline_downloading_24),
        modifier = Modifier
            .height(150.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        viewModel.removeScrap(gifUiModel)
                        Toast.makeText(context, "Remove Scrap!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
    )
}
