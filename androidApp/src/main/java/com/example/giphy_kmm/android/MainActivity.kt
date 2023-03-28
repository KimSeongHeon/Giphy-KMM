package com.example.giphy_kmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.giphy_kmm.android.compose.GifGridTitle
import com.example.giphy_kmm.android.compose.GifMainLayout
import com.example.giphy_kmm.android.compose.GifSearchBox
import com.example.giphy_kmm.android.viewmodel.GiphyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val gifViewModel: GiphyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GifContainerView()
                }
            }
        }
    }

    private fun initViewModel() {
        gifViewModel.initSearchViewModel()
    }

    @Composable
    fun GifContainerView() {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GifGridTitle(gifViewModel)
            GifSearchBox(gifViewModel)
            GifMainLayout(gifViewModel)
        }
    }
}
