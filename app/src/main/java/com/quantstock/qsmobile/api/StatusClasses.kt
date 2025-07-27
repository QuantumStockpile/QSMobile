package com.quantstock.qsmobile.api

enum class ItemStatus {
    AVAILABLE,
    CHECKED_OUT,
    UNDER_REPAIR,
    RETIRED
} // helper class for item status

enum class RequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    RETURNED
} // helper class for request status