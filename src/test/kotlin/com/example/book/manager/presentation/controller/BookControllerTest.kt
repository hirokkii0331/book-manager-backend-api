package com.example.book.manager.presentation.controller

import com.example.book.manager.application.BookService
import com.example.book.manager.domain.enum.PublishStatus
import com.example.book.manager.domain.model.Author
import com.example.book.manager.domain.model.Book
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var bookService: BookService

    @Test
    fun `POST register should register a book`() {
        val book = Book(
            id = 1L,
            title = "テスト",
            price = BigDecimal("100.0"),
            authors = listOf(Author(id = 1L, name = "テスト太郎", birthDate = LocalDate.of(1980, 1, 1))),
            status = PublishStatus.UNPUBLISHED
        )

        whenever(bookService.register(any())).thenReturn(book)

        mockMvc.perform(
            MockMvcRequestBuilders.post("/books/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "テスト",
                        "price": 100.0,
                        "authors": [{"id": 1, "name": "テスト太郎", "birthDate": "1980-01-01"}],
                        "status": "UNPUBLISHED"
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("テスト"))
            .andExpect(jsonPath("$.price").value(100.0))
            .andExpect(jsonPath("$.authors[0].name").value("テスト太郎"))
    }

    @Test
    fun `PUT id should update a book`() {
        val book = Book(
            id = 1L,
            title = "テスト",
            price = BigDecimal("150.0"),
            authors = listOf(Author(id = 1L, name = "テスト次郎", birthDate = LocalDate.of(1980, 1, 1))),
            status = PublishStatus.PUBLISHED
        )

        whenever(bookService.update(any())).thenReturn(book)

        mockMvc.perform(
            MockMvcRequestBuilders.put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "テスト",
                        "price": 150.0,
                        "authors": [{"id": 1, "name": "テスト次郎", "birthDate": "1980-01-01"}],
                        "status": "PUBLISHED"
                    }
                """.trimIndent())
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("テスト"))
            .andExpect(jsonPath("$.price").value(150.0))
            .andExpect(jsonPath("$.status").value("PUBLISHED"))
    }

    @Test
    fun `GET authorId should return books by author`() {
        val books = listOf(
            Book(
                id = 1L,
                title = "テスト1",
                price = BigDecimal("100.0"),
                authors = listOf(Author(id = 1L, name = "テスト太郎1", birthDate = LocalDate.of(1980, 1, 1))),
                status = PublishStatus.UNPUBLISHED
            ),
            Book(
                id = 2L,
                title = "テスト2",
                price = BigDecimal("200.0"),
                authors = listOf(Author(id = 1L, name = "テスト太郎2", birthDate = LocalDate.of(1980, 1, 1))),
                status = PublishStatus.PUBLISHED
            )
        )

        whenever(bookService.getBooksByAuthor(1L)).thenReturn(books)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/books/author/1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].title").value("テスト1"))
            .andExpect(jsonPath("$[1].title").value("テスト2"))
    }
}
