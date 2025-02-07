package org.tidy.upload.data.model

data class Report(
    val Prefixo: String,
    val No_Titulo: String,
    val Parcela: String?,
    val Cliente: String,
    val Nome: String,
    val Dt_Comissao: String,
    val Vencto: String,
    val Origem: String,
    val Dt_Baixa: String,
    val Data_Pagto: String,
    val Pedido: String,
    val Vlr_Titulo: String,
    val Vlr_Base: String,
    val Percentual: String,
    val Comissao_Tipo: String,
    val Ajuste: String,
    val Vendedor: String
)
