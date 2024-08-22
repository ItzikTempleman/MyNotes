package com.itzik.mynotes.project.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.itzik.mynotes.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@SuppressLint("RememberReturnType")
@Composable
fun <T> SwipeToDeleteOrRetrieve(
    note: T,
    onRetrieve: (T) -> Unit,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit
) {
    val boxSizeDp = 50.dp
    val boxSizePx = with(LocalDensity.current) { boxSizeDp.toPx() }
    val maxSwipeDistancePx = boxSizePx * 2
    val swipeState = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart = {

                    },
                    onDragEnd = {

                    },
                    onDragCancel = {

                    }
                ) { change, dragAmount ->
                    change.consume()
                    coroutineScope.launch {
                        val newSwipeValue = (swipeState.value + dragAmount).coerceIn(
                            -maxSwipeDistancePx,
                            0f
                        )
                        swipeState.snapTo(newSwipeValue)
                    }
                }
            }
            .offset { IntOffset(swipeState.value.roundToInt(), 0) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .offset { IntOffset(swipeState.value.roundToInt(), 0) }
        ) {
            content(note)
        }

        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.clickable(
                    indication = null,
                    interactionSource = interactionSource
                ){}
                    .fillMaxHeight()
                    .offset { IntOffset(maxSwipeDistancePx.toInt(), 0) },
                horizontalArrangement = Arrangement.End
            ) {

                Box(
                    modifier = Modifier
                        .size(boxSizeDp)
                        .background(colorResource(id = R.color.darker_blue))
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            onRetrieve(note)
                            coroutineScope.launch {
                                    swipeState.snapTo(0f)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Restore,
                        tint = Color.White,
                        contentDescription = null
                    )
                }

                Box(
                    modifier = Modifier
                        .size(boxSizeDp)
                        .background(Color.Red)
                        .clickable(
                            indication = null,
                            interactionSource = interactionSource
                        ) {
                            onDelete(note)
                            coroutineScope.launch {
                                swipeState.snapTo(0f)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Delete,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
            HorizontalDivider(
                thickness = 1.dp,
            )
        }
    }

    LaunchedEffect(swipeState.value) {
        if (swipeState.value < -maxSwipeDistancePx / 3) {
            swipeState.animateTo(-maxSwipeDistancePx)
        } else {
            swipeState.animateTo(0f)
        }
    }
}