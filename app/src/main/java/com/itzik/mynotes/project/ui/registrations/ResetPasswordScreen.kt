package com.itzik.mynotes.project.ui.registrations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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

    var restOfPhoneNumber by remember {
        mutableStateOf("")
    }


    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (screenTitle, enterPhoneNumberText, phoneNumber) = createRefs()
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

        Row(
            modifier = Modifier
                .constrainAs(phoneNumber) {
                    top.linkTo(enterPhoneNumberText.bottom)
                }
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CountryCodePicker(
                modifier = Modifier,
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

            OutlinedTextField(
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) ,
                value=restOfPhoneNumber,
                onValueChange={
                    restOfPhoneNumber=it
                }
            )
            IconButton(
                onClick = {
                    val fullPhoneNumber = country.countryCode.toInt() + restOfPhoneNumber.toInt()
                    userViewModel?.sendMessage(fullPhoneNumber)
                }
            ) {
                Icon(
                    contentDescription = null,
                    imageVector = Icons.Rounded.Send
                )
            }
        }
    }
}
