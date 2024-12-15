package com.example.book.manager.domain.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import java.time.LocalDate

data class Author(
    val id: Long? = null,
    @field:NotBlank val name: String,
    @field:NotEmpty val birthDate: LocalDate
)