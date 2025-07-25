package com.quantstock.qsmobile.api

// HTTP error code handling classes

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