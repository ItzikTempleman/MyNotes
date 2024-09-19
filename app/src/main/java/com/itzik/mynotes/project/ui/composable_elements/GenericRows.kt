package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.CoroutineScope

sealed class GenericRows(
    var itemTitle: String,
    var itemIcon: ImageVector,
    var onClick: ((noteViewModel: NoteViewModel, coroutineScope: CoroutineScope, userViewModel: UserViewModel, user: User) -> Unit)? = null
) {

    data object RetrieveNote : GenericRows(
        itemTitle = "Retrieve note",
        itemIcon = Icons.Default.RestoreFromTrash,

    )

    data object DeleteNote : GenericRows(
        itemTitle = "Delete note forever",
        itemIcon = Icons.Default.DeleteForever,
    )
}



@Composable
fun GenericItem(
    modifier: Modifier,
    item: GenericRows,
    noteViewModel: NoteViewModel,
    coroutineScope: CoroutineScope,
    userViewModel: UserViewModel,
    user: User
) {
    ConstraintLayout(
        modifier = modifier
            .clickable {
                item.onClick?.let {
                    it(
                        noteViewModel,
                        coroutineScope,
                        userViewModel,
                        user
                    )
                }
            }
            .fillMaxWidth()
            .height(50.dp),
    ) {
        val (icon, text, divider) = createRefs()

        Icon(
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(icon) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            imageVector = item.itemIcon,
            contentDescription = null,
            tint =  Color.Blue
        )

        Text(
            modifier = Modifier
                .padding(4.dp)
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(icon.end)
                },
            text = item.itemTitle,
            fontSize = 16.sp,
            color =  Color.Black
        )

        if (item.itemTitle != "Log out") {
            HorizontalDivider(
                modifier = Modifier
                    .constrainAs(divider) {
                        bottom.linkTo(parent.bottom)
                    }.padding(horizontal = 8.dp)
            )
        }
    }
}