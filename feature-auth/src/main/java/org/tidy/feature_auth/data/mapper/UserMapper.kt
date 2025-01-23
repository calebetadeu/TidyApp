package org.tidy.feature_auth.data.mapper

import org.tidy.feature_auth.data.dto.UserDto
import org.tidy.feature_auth.domain.User

fun UserDto.toDomain(): User {
    return User(
        uid = this.uid,
        email = this.email
    )
}