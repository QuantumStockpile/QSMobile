package com.quantstock.qsmobile.api

data class ApiErrorDetail(
    val loc: List<Any>,
    val msg: String,
    val type: String
)

data class ValidationErrorResponse(
    val detail: List<ApiErrorDetail>
)

data class GenericErrorResponse(
    val detail: String
)