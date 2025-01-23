package org.tidy.feature_auth.data.dto

import com.google.firebase.auth.FirebaseUser

data class UserDto(
    val uid: String,
    val email: String?
) {
    companion object {
        fun fromFirebaseUser(firebaseUser: FirebaseUser?): UserDto? {
            return firebaseUser?.let {
                UserDto(uid = it.uid, email = it.email)
            }
        }
    }
}