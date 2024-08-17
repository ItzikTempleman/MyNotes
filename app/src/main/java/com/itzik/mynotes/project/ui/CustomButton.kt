package com.itzik.mynotes.project.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout


@Composable
fun CustomButton(
    text: String,
    modifier: Modifier,
    onButtonClick: () -> Unit,
    isEnabled: Boolean,
    fontSize: TextUnit,
    containerColor: Color,
    contentColor: Color,
    borderColor:Color,
    roundedShape: Dp
) {
    Button(
        enabled = isEnabled,
        shape = RoundedCornerShape(roundedShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        modifier = modifier.border(BorderStroke(0.7.dp, borderColor), shape = RoundedCornerShape(roundedShape))
            .fillMaxWidth()
            .height(50.dp),
        onClick = {
            onButtonClick()
        }
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (thisText) = createRefs()
            Text(
                modifier = Modifier
                    .constrainAs(thisText) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                text = text,
                fontSize = fontSize,
            )
        }

    }
}


