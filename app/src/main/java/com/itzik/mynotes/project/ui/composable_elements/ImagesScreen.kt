package com.itzik.mynotes.project.ui.composable_elements

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.itzik.mynotes.R
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
fun ImagesScreen(
    modifier: Modifier,
    userViewModel: UserViewModel,
    onImageSelected: (image: String) -> Unit,
    coroutineScope: CoroutineScope,
    onScreenExit: (isScreenClosed: Boolean) -> Unit
) {
Log.d("TAG", "in gallery")
    var searchParam by remember { mutableStateOf("") }
    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Green)
    ) {
        val (searchBar, searchBtn,  exitIcon, imageGallery) = createRefs()

        TextField(
            modifier = Modifier.constrainAs(searchBar){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }.width(200.dp).padding(8.dp),
            value = searchParam,
            onValueChange = {
                searchParam = it
            },placeholder={
                Text(text = stringResource(R.string.search_images))
            }

        )

        Button(
            shape = RoundedCornerShape(180.dp),
            modifier = Modifier.padding(8.dp).constrainAs(searchBtn){
                top.linkTo(parent.top)
                start.linkTo(searchBar.end)
            },
            onClick = {

            }
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
        }

        IconButton(
            modifier = Modifier.constrainAs(exitIcon){
                top.linkTo(parent.top)
                end.linkTo(parent.end)
            }.padding(8.dp),
            onClick = {
                onScreenExit(false)
            }

        ) {
            Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
        }

    }
}