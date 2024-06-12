package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.repositories.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: IRepo
) : ViewModel() {

    private val privateLoggedInUsersList = MutableStateFlow<List<User>>(emptyList())
    val exposedLoggedInUsersList: StateFlow<List<User>> = privateLoggedInUsersList

    init {
        viewModelScope.launch {
            privateLoggedInUsersList.value = repo.fetchLoggedInUsers()
        }
    }

    suspend fun fetchLoggedInUsers() {
        viewModelScope.launch {
            val users=repo.fetchLoggedInUsers()
            privateLoggedInUsersList.value=users
        }
    }


    suspend fun registerUser(newUser:User){
        viewModelScope.launch {
            repo.insertUser(newUser)
            fetchLoggedInUsers()
        }
    }


    fun createUser(
        name: String,
        email: String,
        password: String,
        phoneNumber: Long,
        profileImage: String
    ): User {
        return User(
            userName = name,
            email = email,
            password = password,
            isLoggedIn = true,
            phoneNumber = phoneNumber,
            profileImage = profileImage
        )
    }

    //suspend fun insertUser(user: User) = repo.insertUser(user)

    fun fetchAllUsers(): Flow<MutableList<User>> {
        val userList = flow {
            val updatedUserList = repo.fetchAllUsers()
            if (updatedUserList.isNotEmpty()) {
                emit(updatedUserList)
            } else return@flow
        }
        return userList
    }

    suspend fun getUserFromUserNameAndPassword(userName: String, password: String): Flow<User?> {
        val user = flow {
            val updatedUser = repo.getUserFromEmailAndPassword(userName, password)
            emit(updatedUser)
        }
        return user
    }

    suspend fun updateIsLoggedIn(user: User) = repo.updateIsLoggedIn(user)















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

    suspend fun updateProfileImage(profileImageString: String) {
        val user = repo.fetchLoggedInUsers().firstOrNull()
        user?.let {
            val updatedUser: User = it.copy(profileImage = profileImageString)
            repo.updateProfileImage(updatedUser)
        }
    }
}

