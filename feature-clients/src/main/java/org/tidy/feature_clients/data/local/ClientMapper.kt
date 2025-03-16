package org.tidy.feature_clients.data.local

import org.tidy.feature_clients.data.remote.ClientDto
import org.tidy.feature_clients.domain.model.Client

fun ClientDto.toEntity(): ClientEntity = ClientEntity(
    id = this.id,
    codigoDitrator = this.codigo_ditrator,
    codigoCasaDosRolamentos = this.codigo_casa_dos_rolamentos,
    codigoRomarMann = this.codigo_romar_mann,
    nomeFantasia = this.nome_fantasia,
    razaoSocial = this.razao_social,
    rota = this.rota,
    cidade = this.cidade,
    estado = this.estado,
    empresasTrabalhadas = this.empresas_trabalhadas,
    latitude = this.latitude,
    longitude = this.longitude,
    cnpj = this.cnpj
)

fun ClientEntity.toDomain(): Client = Client(
    id = this.id,
    codigoDitrator = this.codigoDitrator,
    codigoCasaDosRolamentos = this.codigoCasaDosRolamentos,
    codigoRomarMann = this.codigoRomarMann,
    nomeFantasia = this.nomeFantasia,
    razaoSocial = this.razaoSocial,
    rota = this.rota,
    cidade = this.cidade,
    estado = this.estado,
    empresasTrabalhadas = this.empresasTrabalhadas,
    latitude = this.latitude,
    longitude = this.longitude,
    cnpj = this.cnpj
)

fun Client.toEntity(): ClientEntity = ClientEntity(
    id = this.id,
    codigoDitrator = this.codigoDitrator,
    codigoCasaDosRolamentos = this.codigoCasaDosRolamentos,
    codigoRomarMann = this.codigoRomarMann,
    nomeFantasia = this.nomeFantasia,
    razaoSocial = this.razaoSocial,
    rota = this.rota,
    cidade = this.cidade,
    estado = this.estado,
    empresasTrabalhadas = this.empresasTrabalhadas,
    latitude = this.latitude,
    longitude = this.longitude,
    cnpj = this.cnpj
)