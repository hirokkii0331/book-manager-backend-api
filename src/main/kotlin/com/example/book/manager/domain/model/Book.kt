package com.example.book.manager.domain.model

import com.example.book.manager.domain.enum.PublishStatus
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal

data class Book(
    val id: Long? = null,
    @field:NotBlank val title: String,
    @field:Min(0) val price: BigDecimal,
    @field:NotEmpty val authors: List<Author>,
    val status: PublishStatus
)