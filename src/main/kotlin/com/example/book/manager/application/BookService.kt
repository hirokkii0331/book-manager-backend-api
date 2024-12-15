package com.example.book.manager.application

import com.example.book.manager.domain.model.Book
import com.example.book.manager.domain.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(private val bookRepository: BookRepository) {

    @Transactional
    fun register(book: Book): Book {
        return bookRepository.register(book)
    }

    @Transactional
    fun update(book: Book): Book {
        return bookRepository.update(book)
    }

    fun getBooksByAuthor(authorId: Long): List<Book> {
        return bookRepository.findByAuthor(authorId)
    }
}