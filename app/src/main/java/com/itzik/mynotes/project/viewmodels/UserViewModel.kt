package com.itzik.mynotes.project.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Gender
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.model.WallpaperResponse
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
    private val repo: AppRepositoryInterface,
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

    private fun fetchLoggedInUsers() {
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

    fun getUserFromUserNameAndPassword(userName: String, password: String): Flow<User?> {
        val user = flow {
            val updatedUser = repo.getUserFromEmailAndPassword(userName, password)
            emit(updatedUser)
        }
        return user
    }

    fun getTempUserForVerification(userName: String): Flow<User> {
        val user = flow {
            val updatedUser = repo.getTempUserForVerification(userName)
            emit(updatedUser)
        }
        return user
    }


    fun fetchViewType(userId: String) {
        viewModelScope.launch {
            val isViewGrid = repo.fetchViewType(userId)
            privateUser.value = privateUser.value?.copy(isViewGrid = isViewGrid)
        }
    }

    fun updateViewType(isViewGrid: Boolean) {
        viewModelScope.launch {
            privateUser.value?.let {
                repo.updateViewType(it.userId, isViewGrid)
                privateUser.value = it.copy(isViewGrid = isViewGrid)
            }
        }
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
        phoneNumber: String,
        profileImage: String,
        gender: Gender,
        dateOfBirth: String,
        selectedWallpaper: String,
    ): User {
        return User(
            userName = name,
            email = email,
            password = password,
            isLoggedIn = true,
            phoneNumber = phoneNumber,
            profileImage = profileImage,
            gender = gender,
            dateOfBirth = dateOfBirth,
            selectedWallpaper = selectedWallpaper
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
        val regex = Regex("^(\\+\\d{1,3})?(0\\d{9}|\\d{9,11})$")
        return regex.matches(phoneNumber)
    }


    fun getAgeFromSDateString(date: String): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val birthDate = LocalDate.parse(date, dateFormatter)
        val currentDate = LocalDate.now()
        val age = Period.between(birthDate, currentDate).years
        return age.toString()
    }


    fun updateProfileImage(imageUri: String) {
        viewModelScope.launch {
            val user = privateLoggedInUsersList.value.firstOrNull()
            if (user != null) {
                val updatedUser = user.copy(profileImage = imageUri)
                repo.updateProfileImage(updatedUser)
                privateUser.value = updatedUser
                privateLoggedInUsersList.value = privateLoggedInUsersList.value.map {
                    if (it.userId == user.userId) updatedUser else it
                }
            }
        }
    }


    suspend fun updateWallpaper(imageUri: String) {
        if (imageUri.isNotEmpty()) {
            val updatedUser = privateUser.value?.copy(selectedWallpaper = imageUri)
            if (updatedUser != null) {
                repo.updateWallpaper(updatedUser)
                privateUser.value = updatedUser
            }
        } else {
            Log.d("TAG", "Empty String in updateWallpaper")
        }
    }

    fun getWallpaperList(searchQuery: String): Flow<WallpaperResponse> {
        val imagesFlow = flow {
            val response = repo.getWallpaperListByQuery(searchQuery)

            if (response.isSuccessful) {
                val responseBody = response.body()
                Log.d("TAGS", "responseBody: $responseBody")
                if (responseBody != null) {
                    emit(responseBody)
                } else Log.d("TAG", "first failure message: " + response.message())
                return@flow
            } else Log.d("TAG", "second failure message: " + response.message())
            return@flow
        }
        return imagesFlow

    }

    fun updateEmail(email: String) {
        viewModelScope.launch {
            val user = privateLoggedInUsersList.value.firstOrNull()
            if (user != null) {
                val updatedUser = privateUser.value?.copy(email = email)
                if (updatedUser != null) {
                    repo.updateProfileImage(updatedUser)
                }
                privateUser.value = updatedUser
            }
        }
    }

    fun updatePhone(phoneNumber: String) {
        viewModelScope.launch {
            val user = privateLoggedInUsersList.value.firstOrNull()
            if (user != null) {
                val updatedUser = privateUser.value?.copy(phoneNumber = phoneNumber)
                if (updatedUser != null) {
                    repo.updateProfileImage(updatedUser)
                }
                privateUser.value = updatedUser
            }
        }
    }
}

