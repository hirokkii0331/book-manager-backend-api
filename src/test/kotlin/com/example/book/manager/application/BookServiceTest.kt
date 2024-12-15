package com.example.book.manager.application

import com.example.book.manager.domain.model.Author
import com.example.book.manager.domain.model.Book
import com.example.book.manager.domain.enum.PublishStatus
import com.example.book.manager.domain.repository.BookRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.math.BigDecimal
import java.time.LocalDate

class BookServiceTest {

    private val bookRepository: BookRepository = mock()
    private val bookService = BookService(bookRepository)

    @Test
    fun `register should save and return the book`() {

        val book = Book(
            id = null,
            title = "テスト",
            price = BigDecimal("100.0"),
            authors = listOf(Author(id = 1L, name = "テスト太郎", birthDate = LocalDate.of(1980, 1, 1))),
            status = PublishStatus.UNPUBLISHED
        )
        val savedBook = book.copy(id = 1L)

        whenever(bookRepository.register(any())).thenReturn(savedBook)

        val result = bookService.register(book)

        assertEquals(savedBook, result)
        verify(bookRepository).register(book)
    }

    @Test
    fun `update should update and return the book`() {

        val book = Book(
            id = 1L,
            title = "更新テスト",
            price = BigDecimal("150.0"),
            authors = listOf(Author(id = 1L, name = "テスト次郎", birthDate = LocalDate.of(1980, 1, 1))),
            status = PublishStatus.PUBLISHED
        )

        whenever(bookRepository.update(any())).thenReturn(book)

        val result = bookService.update(book)

        assertEquals(book, result)
        verify(bookRepository).update(book)
    }

    @Test
    fun `getBooksByAuthor should return books by the given author`() {

        val authorId = 1L
        val books = listOf(
            Book(
                id = 1L,
                title = "テスト1",
                price = BigDecimal("100.0"),
                authors = listOf(Author(id = authorId, name = "テスト太郎1", birthDate = LocalDate.of(1980, 1, 1))),
                status = PublishStatus.UNPUBLISHED
            ),
            Book(
                id = 2L,
                title = "テスト2",
                price = BigDecimal("200.0"),
                authors = listOf(Author(id = authorId, name = "テスト太郎1", birthDate = LocalDate.of(1980, 1, 1))),
                status = PublishStatus.PUBLISHED
            )
        )

        whenever(bookRepository.findByAuthor(authorId)).thenReturn(books)

        val result = bookService.getBooksByAuthor(authorId)

        assertEquals(books, result)
        verify(bookRepository).findByAuthor(authorId)
    }
}
