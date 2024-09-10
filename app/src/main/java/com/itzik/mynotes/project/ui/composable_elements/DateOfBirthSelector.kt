package com.itzik.mynotes.project.ui.composable_elements

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.LocalDate

enum class Format(val format: String) {
    MMDDYYYY("MM/dd/yyyy"),
    DDMMYYYY("dd/MM/yyyy"),
    YYYYMMDD("yyyy/MM/dd"),
    YYYYDDMM("yyyy/dd/MM")
}

object DateTextFieldDefaults {
    val MainTextStyle = androidx.compose.ui.text.TextStyle.Default

    val HintTextStyle = MainTextStyle.copy(color = MainTextStyle.color.copy(alpha = 0.5f))

    val DelimiterSpacing = 4.dp
}

@Composable
fun DateOfBirthSelector(
    modifier: Modifier,
    initialValue: LocalDate? = null,
    onValueChanged: (LocalDate?) -> Unit,
    format: Format = Format.DDMMYYYY,
    minDate: LocalDate = LocalDate.of(1900, 1, 1),
    maxDate: LocalDate = LocalDate.of(2100, 12, 31),
    delimiter: Char = '/',
    cursorBrush: Brush = SolidColor(Color.Black),
    textStyle: TextStyle = DateTextFieldDefaults.HintTextStyle,
    hintTextStyle: TextStyle = DateTextFieldDefaults.HintTextStyle,
    deliminatorSpacing: Dp = DateTextFieldDefaults.DelimiterSpacing,
    readOnly: Boolean = false
) {


}

