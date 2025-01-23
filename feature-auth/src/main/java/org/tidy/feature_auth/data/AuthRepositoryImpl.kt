package org.tidy.feature_auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import org.tidy.feature_auth.data.dto.UserDto
import org.tidy.feature_auth.data.mapper.toDomain
import org.tidy.feature_auth.domain.AuthRepository
import org.tidy.feature_auth.domain.User
import com.google.firebase.auth.*
import org.tidy.feature_auth.domain.AuthError
import org.tidy.core.domain.Result


class AuthRepositoryImpl(private val firebaseAuth: FirebaseAuth) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User, AuthError> {

        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = UserDto.fromFirebaseUser(authResult.user)?.toDomain()
            if (user != null) {
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

    override fun logout() {
        firebaseAuth.signOut()
    }
}