package com.itzik.mynotes.project.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Gender
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: AppRepositoryInterface
) : ViewModel() {

    private val privateLoggedInUsersList = MutableStateFlow<List<User>>(emptyList())
    val publicLoggedInUsersList: StateFlow<List<User>> = privateLoggedInUsersList

    private val privateUser = MutableStateFlow<User?>(null)
    val publicUser: StateFlow<User?> = privateUser

    init {
        viewModelScope.launch {
            fetchLoggedInUsers()
        }
    }

    fun fetchUserById(userId: String) {
        viewModelScope.launch {
            val user = repo.getUserById(userId)
            privateUser.value = user
        }
    }

    private suspend fun fetchLoggedInUsers() {
        viewModelScope.launch {
            val users = repo.fetchLoggedInUsers()
            privateLoggedInUsersList.value = users
        }
    }

    fun registerUser(newUser: User) {
        viewModelScope.launch {
            try {
                repo.insertUser(newUser)
                fetchLoggedInUsers()
            } catch (_: Exception) {

            }
        }
    }

    suspend fun getUserFromUserNameAndPassword(userName: String, password: String): Flow<User?> {
        val user = flow {
            val updatedUser = repo.getUserFromEmailAndPassword(userName, password)
            emit(updatedUser)
        }
        return user
    }

    suspend fun updateIsLoggedIn(user: User) {
        try {
            repo.updateIsLoggedIn(user)
            fetchLoggedInUsers()
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error updating user login status: ${e.message}")
        }
    }


    fun createUser(
        name: String,
        email: String,
        password: String,
        phoneNumber: Long,
        profileImage: String,
        gender: Gender,
        dateOfBirth: String

    ): User {
        return User(
            userName = name,
            email = email,
            password = password,
            isLoggedIn = true,
            phoneNumber = phoneNumber,
            profileImage = profileImage,
            gender = gender,
            dateOfBirth = dateOfBirth
        )
    }


    fun validateName(name: String) = name.length > 4

    fun validateEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")
        return email.matches(emailRegex)
    }

    fun validatePassword(password: String): Boolean {
        val passwordRegex =
            Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
        return password.matches(passwordRegex)
    }

    fun validatePhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^\\d{9,11}$")
        return regex.matches(phoneNumber)
    }


    fun getAgeFromSDateString(date: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val birthDate = LocalDate.parse(date, dateFormatter)
        val currentDate = LocalDate.now()
        val age= Period.between(birthDate, currentDate).years
        return age.toString()
    }


    suspend fun updateProfileImage(imageUri: String) {
        viewModelScope.launch {
            val user = privateLoggedInUsersList.value.firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(profileImage = imageUri)
                repo.updateProfileImage(updatedUser)
                privateUser.value = updatedUser
                Log.d("UserViewModel", "Updating profile image: $imageUri")
                privateLoggedInUsersList.value = privateLoggedInUsersList.value.map {
                    if (it.userId == user.userId) updatedUser else it
                }
            }
        }
    }
}

