package com.example.book.manager.domain.repository

import com.example.book.manager.domain.model.Book

interface BookRepository {
    fun findByAuthor(authorId: Long): List<Book>

    fun register(book: Book): Book

    fun update(book: Book): Book
}