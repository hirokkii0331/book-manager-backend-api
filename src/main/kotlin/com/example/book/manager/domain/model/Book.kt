package com.example.book.manager.domain.model

import com.example.book.manager.domain.enum.PublishStatus
import java.math.BigDecimal

data class Book(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val author: String,
    val status: PublishStatus
)