package com.example.book.manager.presentation.controller

import com.example.book.manager.application.BookService
import com.example.book.manager.domain.model.Book
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(private val bookService: BookService) {

    @PostMapping("/register")
    fun register(@RequestBody book: Book): ResponseEntity<Book> {
        return ResponseEntity.ok(bookService.register(book))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody book: Book): ResponseEntity<Book> {
        return ResponseEntity.ok(bookService.update(book.copy(id = id)))
    }

    @GetMapping("/author/{authorId}")
    fun getBooksByAuthor(@PathVariable authorId: Long): ResponseEntity<List<Book>> {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId))
    }
}