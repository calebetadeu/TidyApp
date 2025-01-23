package org.tidy.feature_auth.domain

data class UserUi(
    val uid: String,
    val email: String?
)

fun User.toUiModel(): UserUi {
    return UserUi(
        uid = this.uid,
        email = this.email
    )
}