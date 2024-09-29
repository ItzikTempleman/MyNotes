package com.itzik.mynotes.project.ui.composable_elements

sealed class EditProfileOptions(
   val onItemSelected: ()->Unit,
   val itemName:String,
    
) {

    data object EditEmail:EditProfileOptions(
        itemName="Edit user name",
        onItemSelected = {

        }
    )

    data object EditPassword:EditProfileOptions(
        itemName="Change password",
        onItemSelected = {

        }
    )

    data object EditPhoneNumber:EditProfileOptions(
        itemName="Edit phone number",
        onItemSelected = {

        }
    )

}