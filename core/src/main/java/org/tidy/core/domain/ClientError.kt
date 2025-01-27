package org.tidy.core.domain

import android.os.Message


sealed class ClientError(private  val message: String) : Error {
    object NotFound : ClientError("Cliente não encontrado")
    data class DatabaseError(val message: String) : ClientError("Erro ao acessar o banco de dados")
    object NetworkError : ClientError("Erro de conexão com o servidor")
    object InvalidData : ClientError("Dados inválidos fornecidos")
    object UnknownError : ClientError("Ocorreu um erro inesperado")
}