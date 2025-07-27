package com.quantstock.qsmobile.ui.common

import com.quantstock.qsmobile.api.Equipment
import com.quantstock.qsmobile.api.EquipmentType
import com.quantstock.qsmobile.api.ItemStatus
import com.quantstock.qsmobile.api.Location
import java.time.LocalDateTime

// item mockups
object ItemMockups {
    val fakeTypeLaptop = EquipmentType(id = 1, name = "Laptop")
    val fakeTypePhone = EquipmentType(id = 2, name = "Phone")

    val fakeLocationHQ = Location(id = 1, name = "Headquarters", description = "Mqstoto")
    val fakeLocationWH = Location(id = 2, name = "Warehouse", description = "Sklad")

    val testEquipments = listOf(
        Equipment(
            id = 1,
            name = "MacBook Pro",
            type = fakeTypeLaptop,
            serialNumber = "SN12345",
            status = ItemStatus.AVAILABLE,
            condition = 8,
            location = fakeLocationHQ,
            photoUrl = "https://picsum.photos/200", // random test image
            qrCodeData = "macbook_qr",
            metadata = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ),
        Equipment(
            id = 2,
            name = "iPhone 14",
            type = fakeTypePhone,
            serialNumber = "SN67890",
            status = ItemStatus.CHECKED_OUT,
            condition = 10,
            location = fakeLocationWH,
            photoUrl = null, // test default image fallback
            qrCodeData = "iphone_qr",
            metadata = null,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    )

    val singleEquipment = Equipment(
        id = 3,
        name = "MacBook Lite",
        type = fakeTypeLaptop,
        serialNumber = "SN12347",
        status = ItemStatus.AVAILABLE,
        condition = 8,
        location = fakeLocationHQ,
        photoUrl = "https://picsum.photos/200", // random test image
        qrCodeData = "macbook_qr",
        metadata = null,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

}