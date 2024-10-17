package com.itzik.mynotes.project.ui.registrations

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.itzik.mynotes.R
import com.itzik.mynotes.project.viewmodels.UserViewModel
import com.rejowan.ccpc.Country
import com.rejowan.ccpc.CountryCodePicker
import com.rejowan.ccpc.PickerCustomization
import com.rejowan.ccpc.ViewCustomization
import kotlinx.coroutines.CoroutineScope

@Composable
fun ResetPasswordScreen(
    coroutineScope: CoroutineScope,
    rootNavController: NavHostController,
    userViewModel: UserViewModel?,
) {
    var country by remember {
        mutableStateOf(Country.Israel)
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (screenTitle, enterPhoneNumberText, ccp) = createRefs()
        Text(
            modifier = Modifier
                .constrainAs(screenTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .padding(8.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            text = stringResource(R.string.reset_screen)
        )

        Text(
            modifier = Modifier
                .constrainAs(enterPhoneNumberText) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(8.dp),
            fontSize = 16.sp,
            text = stringResource(R.string.enter_phone_number_to_reset_with)
        )



        CountryCodePicker(
            modifier = Modifier
                .constrainAs(ccp) {
                    top.linkTo(enterPhoneNumberText.bottom)
                    start.linkTo(parent.start)
                }
                .padding(8.dp),
            selectedCountry = country,
            onCountrySelected = {
                country = it
            },
            viewCustomization = ViewCustomization(
                showFlag = true,
                showCountryIso = false,
                showCountryName = false,
                showCountryCode = true,
                clipToFull = false
            ),
            pickerCustomization = PickerCustomization(
                showFlag = false,
            ),
            showSheet = true,
        )
    }
}
