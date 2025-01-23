package org.tidy.feature_auth.domain

import org.tidy.core.domain.Error


sealed class AuthError(message: String) : Error {
    object UserNotFound : AuthError("Usuário não encontrado")
    object InvalidCredentials : AuthError("E-mail ou senha inválidos")
    object EmailAlreadyInUse : AuthError("E-mail já cadastrado")
    object WeakPassword : AuthError("A senha precisa ter pelo menos 6 caracteres")
    object InvalidEmailFormat : AuthError("Formato de e-mail inválido")
    object UnknownError : AuthError("Ocorreu um erro inesperado")
}