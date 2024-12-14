package com.example.book.manager.domain.model

import java.time.LocalDate

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val publishedDate: LocalDate
)