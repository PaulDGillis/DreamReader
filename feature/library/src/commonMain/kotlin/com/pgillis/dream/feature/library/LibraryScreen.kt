package com.pgillis.dream.feature.library

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.eygraber.compose.placeholder.PlaceholderHighlight
import com.eygraber.compose.placeholder.material3.fade
import com.eygraber.compose.placeholder.material3.placeholder
import com.pgillis.dream.core.designsystem.theme.DreamReaderTheme
import com.pgillis.dream.core.model.Book
import com.pgillis.dream.core.model.MetaData
import com.pgillis.dream.core.ui.DevicePreviews
import io.github.vinceglb.filekit.compose.rememberDirectoryPickerLauncher
import io.github.vinceglb.filekit.core.PlatformDirectory
import org.koin.androidx.compose.koinViewModel

@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = koinViewModel(),
    onDirectorySelected: (PlatformDirectory) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LibraryScreen(state = state, onDirectorySelected = {
        onDirectorySelected(it) // This goes back to platform binding to see if permission request is needed
        viewModel.onDirectorySelected(it)
    })
}

@Composable
fun LibraryScreen(
    state: LibraryUIState,
    onDirectorySelected: (PlatformDirectory) -> Unit = {}
) {
    LibraryTopBar {
        Box(modifier = Modifier.padding(it).fillMaxSize()) {
            when (state) {
                LibraryUIState.Loading -> LibraryScreenLoading()
                LibraryUIState.NeedsDirectory -> LibrarySelectDir(onDirectorySelected = onDirectorySelected)
                is LibraryUIState.Success -> LibraryBooks(state = state)
            }
        }
    }
}

@Composable
private fun LibraryTopBar(
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(modifier) {
        content(it)
    }
}

@Composable
private fun LibraryScreenLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(modifier.size(200.dp))
    }
}

@Composable
private fun LibrarySelectDir(
    modifier: Modifier = Modifier,
    onDirectorySelected: (PlatformDirectory) -> Unit
) {
    Column(
        modifier.padding(10.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        // FileKit Compose
        val launcher = rememberDirectoryPickerLauncher(
            title = "Pick a library directory"
        ) { directory -> if (directory != null) onDirectorySelected(directory) }

        Text("Please select a directory for dream-reader to use as a library.")
        Button(onClick = launcher::launch) {
            Text("Select Directory")
        }
    }
}

@Composable
private fun LibraryBooks(
    modifier: Modifier = Modifier,
    state: LibraryUIState.Success
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.Top)
    ) {
        items(state.books, key = { it.id }) { book ->
            Card {
                Column(modifier = Modifier.wrapContentSize(),
                    verticalArrangement = Arrangement.Top) {
                    AsyncImage(
                        modifier = Modifier.wrapContentWidth()
                            .aspectRatio(0.666f)
                            .placeholder(
                                visible = book.coverUri == null,
                                highlight = PlaceholderHighlight.fade()
                            ),
                        model = ImageRequest.Builder(LocalContext.current)
                                    .data(book.coverUri?.let { Uri.parse(it) })
                                    .build(),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds
                    )
                    Column(Modifier.padding(5.dp)) {
                        Text(book.metaData.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        Text(book.metaData.creator, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun LibraryScreenPreview() {
    DreamReaderTheme {
        LibraryScreen(
            state = LibraryUIState.Success(listOf(
                Book(
                    id = "Book1",
                    metaData = MetaData("Title", "lang", "creator"),
                    manifest = emptyMap(),
                    spine = LinkedHashSet(),
                    coverUri = null
                ),
                Book(
                    id = "Book2",
                    metaData = MetaData("Title", "lang", "creator"),
                    manifest = emptyMap(),
                    spine = LinkedHashSet(),
                    coverUri = null
                )
            ))
        )
    }
}