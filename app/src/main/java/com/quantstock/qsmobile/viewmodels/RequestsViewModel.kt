package com.quantstock.qsmobile.viewmodels

import androidx.lifecycle.ViewModel
import com.quantstock.qsmobile.api.Request
import com.quantstock.qsmobile.api.RequestItem
import com.quantstock.qsmobile.api.RequestStatus
import com.quantstock.qsmobile.ui.common.ItemMockups
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class HardcodedUser(
    val id: Int,
    val username: String,
    val email: String
)

@HiltViewModel
class RequestsViewModel @Inject constructor() : ViewModel() {
    private val currentUser = HardcodedUser(
        id = 1,
        username = "John Doe",
        email = "john.doe@mail.com"
    )

    private val _requests = MutableStateFlow<List<Request>>(emptyList())
    val requests: StateFlow<List<Request>> = _requests

    init {
        _requests.value = buildMockRequests(currentUser)
    }



    fun buildMockRequests(user: HardcodedUser): List<Request> {
        return listOf(
            Request(
                id = 1,
                user = user,
                status = RequestStatus.PENDING,
                requested_at = System.currentTimeMillis(),
                approved_by = null,
                approved_at = null,
                returnedAt = null,
                note = "Need for project X",
                items = listOf(
                    RequestItem(
                        id = 1,
                        requestId = 1,
                        equipment = ItemMockups.testEquipments[0],
                        borrowFrom = "2025-07-20",
                        borrowTo = "2025-08-01"
                    ),
                    RequestItem(
                        id = 2,
                        requestId = 2,
                        equipment = ItemMockups.singleEquipment,
                        borrowFrom = "2025-07-22",
                        borrowTo = "2025-07-30"
                    )
                )
            ),
            Request(
                id = 2,
                user = user,
                status = RequestStatus.APPROVED,
                requested_at = System.currentTimeMillis() - 86400000L,
                approved_by = user,
                approved_at = System.currentTimeMillis() - 86000000L,
                returnedAt = null,
                note = "For conference",
                items = listOf(
                    RequestItem(
                        id = 3,
                        requestId = 3,
                        equipment = ItemMockups.testEquipments[1],
                        borrowFrom = "2025-07-25",
                        borrowTo = "2025-07-27"
                    )
                )
            )
        )
    }
}