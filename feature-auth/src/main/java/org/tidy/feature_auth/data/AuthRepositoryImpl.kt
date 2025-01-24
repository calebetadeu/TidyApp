package org.tidy.feature_auth.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import org.tidy.feature_auth.domain.AuthError

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import org.tidy.core.domain.Result
import org.tidy.feature_auth.data.dto.UserDto
import org.tidy.feature_auth.data.mapper.toDomain
import org.tidy.feature_auth.domain.model.User
import org.tidy.feature_auth.domain.repositories.AuthRepository

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val dataStore: DataStore<Preferences> // Adicionando DataStore
) : AuthRepository {

    companion object {
        private val AUTHENTICATED_KEY = booleanPreferencesKey("is_authenticated")
        private val EMAIL_KEY = stringPreferencesKey("user_email")
    }

    override val isAuthenticated: Flow<Boolean> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences -> preferences[AUTHENTICATED_KEY] ?: false }

    override val userEmail: Flow<String?> = dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { preferences -> preferences[EMAIL_KEY] }

    override suspend fun login(email: String, password: String): Result<User, AuthError> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = UserDto.fromFirebaseUser(authResult.user)?.toDomain()
            if (user != null) {
                saveUser(email) // Salvar usuário no DataStore
                Result.Success(user)
            } else {
                Result.Error(AuthError.UnknownError)
            }
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.Error(AuthError.UserNotFound)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.Error(AuthError.InvalidCredentials)
        } catch (e: Exception) {
            Result.Error(AuthError.UnknownError)
        }
    }

    override suspend fun register(email: String, password: String): Result<User, AuthError> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = UserDto.fromFirebaseUser(authResult.user)?.toDomain()
            if (user != null) {
                saveUser(email) // Salvar usuário no DataStore
                Result.Success(user)
            } else {
                Result.Error(AuthError.UnknownError)
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.Error(AuthError.EmailAlreadyInUse)
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.Error(AuthError.WeakPassword)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.Error(AuthError.InvalidEmailFormat)
        } catch (e: Exception) {
            Result.Error(AuthError.UnknownError)
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
        clearUserData() // Limpar os dados no DataStore
    }

    override suspend fun saveUser(email: String) {
        dataStore.edit { preferences ->
            preferences[AUTHENTICATED_KEY] = true
            preferences[EMAIL_KEY] = email
        }
    }

    private suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences[AUTHENTICATED_KEY] = false
            preferences.remove(EMAIL_KEY)
        }
    }
}