import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.attributes.src
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable

fun main() {
    val viewModel = GiphyViewModel()

    renderComposable(rootElementId = "root") {
        LaunchedEffect(true) { viewModel.initViewModel() }

        Div({ style { padding(25.px) } }) {
            //title
            Div(
                attrs = {
                    style {
                        alignItems(AlignItems.Center)
                    }
                }
            ) {
                H2 {
                    Text(viewModel.giphyTitle.value)
                }
            }

            //input
            Div {
                H3 {
                    Input(type = InputType.Text) {
                        value(viewModel.searchQuery.value)
                        onInput { viewModel.updateSearchQuery(it.value) }
                    }

                    Button(
                        attrs = {
                            onClick { viewModel.swapSearchMode() }
                        }
                    ) {
                        Text(viewModel.searchMode.value.name)
                    }
                }
            }

            //input
            Div(
                attrs = {
                    style {
                        alignItems(AlignItems.End)
                        display(viewModel.autoTermListShow.value)
                    }
                }
            ) {
                Button(
                    attrs = {
                        onClick { viewModel.setAutoCompleteVisibility(false) }
                    }
                ) {
                    Text("Close")
                }

                Div(
                    attrs = {
                        style {
                            display(DisplayStyle.Grid)
                            gridTemplateColumns("100px 100px 100px")
                            gridTemplateRows("20px 20px 20px")
                        }
                    }
                ) {
                    viewModel.autoTermsList.forEach { uiModel ->
                        H6(
                            attrs = {
                                onClick { viewModel.updateSearchQuery(uiModel.name) }
                                style {
                                    color(Color.blue)
                                    textDecoration("underline")
                                }
                            }
                        ) {
                            Text(uiModel.name)
                        }
                    }
                }
            }

            //result
            Div(
                attrs = {
                    style {
                        display(DisplayStyle.Grid)
                        gridTemplateRows("100px 100px 100px")
                        gridTemplateColumns("100px 100px 100px")
                    }
                }
            ) {
                var gifList = viewModel.gifListStateFlow.collectAsState()

                gifList.value.forEach {
                    Div(
                        attrs = {
                            style {
                                display(DisplayStyle.Flex)
                                alignItems(AlignItems.Center)
                            }
                        }
                    ) {
                        val imageUrl = it.downsizedUrl
                        Img(
                            src = imageUrl,
                            attrs = {
                                style {
                                    width(100.px)
                                    height(100.px)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

