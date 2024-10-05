package com.itzik.mynotes.project.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.itzik.mynotes.R
import com.itzik.mynotes.project.model.WallpaperResponse
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@Composable
fun ImagesScreen(
    modifier: Modifier,
    userViewModel: UserViewModel,
    onImageSelected: (image: String) -> Unit,
    coroutineScope: CoroutineScope,
    onScreenExit: (isScreenClosed: Boolean) -> Unit
) {
    var imagesList by remember {
        mutableStateOf(WallpaperResponse(emptyList()))
    }

    var searchParam by remember { mutableStateOf("") }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val (searchBar, imageGallery) = createRefs()

        Card(
            modifier = Modifier
                .constrainAs(searchBar) {
                    top.linkTo(parent.top)
                }
                .fillMaxWidth().height(70.dp)
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(12.dp),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Row(
                modifier=Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            coroutineScope.launch {
                                userViewModel.getWallpaperList(searchParam).collect {
                                    imagesList = it
                                }
                            }
                        }
                    ), colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.White
                    ),
                    value = searchParam,
                    onValueChange = {
                        searchParam = it
                    }, placeholder = {
                        Text(text = stringResource(R.string.search_images))
                    }
                )
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            userViewModel.getWallpaperList(searchParam).collect {
                                imagesList = it
                            }
                        }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
                VerticalDivider(modifier = Modifier.padding(12.dp))
                IconButton(
                    onClick = {
                        onScreenExit(false)
                    }

                ) {
                    Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                }
            }
        }


        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(imageGallery) {
                    top.linkTo(searchBar.bottom)
                },
            columns = GridCells.Fixed(3),
        ) {
            items(imagesList.hits) { imageItem ->
                ImageItem(
                    imageItem.largeImageURL,
                    modifier = Modifier.clickable {
                        onImageSelected(imageItem.largeImageURL)
                    }
                )
            }
        }
    }
}

@Composable
fun ImageItem(imageUrl: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )
    }
}