package com.example.book.manager.domain.repository

import com.example.book.manager.domain.enum.PublishStatus
import com.example.book.manager.domain.model.Author
import com.example.book.manager.domain.model.Book
import org.jooq.DSLContext
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Repository
@Transactional
class BookRepositoryImpl(private val dsl: DSLContext) : BookRepository {

    override fun findByAuthor(authorId: Long): List<Book> {
        val records = dsl.select(
            field("b.id").`as`("book_id"),
            field("b.title"),
            field("b.price"),
            field("b.status"),
            field("a.id").`as`("author_id"),
            field("a.name").`as`("author_name"),
            field("a.birth_date").`as`("author_birth_date")
        )
            .from(table("books").`as`("b"))
            .join(table("book_authors").`as`("ba")).on(field("b.id").eq(field("ba.book_id")))
            .join(table("authors").`as`("a")).on(field("ba.author_id").eq(field("a.id")))
            .where(field("a.id").eq(authorId))
            .fetch()

        return records.groupBy { it["book_id"] as Long }
            .map { (bookId, bookRecords) ->
                Book(
                    id = bookId,
                    title = bookRecords.first()["title"] as String,
                    price = bookRecords.first()["price"] as BigDecimal,
                    status = PublishStatus.valueOf(bookRecords.first()["status"] as String),
                    authors = bookRecords.map { record ->
                        Author(
                            id = record["author_id"] as Long,
                            name = record["author_name"] as String,
                            birthDate = record["author_birth_date"] as LocalDate
                        )
                    }
                )
            }
    }

    override fun register(book: Book): Book {
        val bookId = dsl.insertInto(table("books"))
            .set(field("title"), book.title)
            .set(field("price"), book.price)
            .set(field("status"), book.status.name)
            .returningResult(field("id"))
            .fetchOne()!!
            .value1() as Long

        book.authors.forEach { author ->
            val authorId = author.id
                ?: dsl.insertInto(table("authors"))
                    .set(field("name"), author.name)
                    .set(field("birth_date"), author.birthDate)
                    .returningResult(field("id"))
                    .fetchOne()!!
                    .value1() as Long

            dsl.insertInto(table("book_authors"))
                .set(field("book_id"), bookId)
                .set(field("author_id"), authorId)
                .execute()
        }

        return book.copy(id = bookId)
    }

    override fun update(book: Book): Book {
        val currentStatus = dsl.select(field("status"))
            .from(table("books"))
            .where(field("id").eq(book.id))
            .fetchOne()
            ?.value1() as String

        if (currentStatus == PublishStatus.PUBLISHED.name && book.status == PublishStatus.UNPUBLISHED) {
            throw IllegalStateException("Cannot change status from PUBLISHED to UNPUBLISHED")
        }

        dsl.update(table("books"))
            .set(field("title"), book.title)
            .set(field("price"), book.price)
            .set(field("status"), book.status.name)
            .where(field("id").eq(book.id))
            .execute()

        dsl.deleteFrom(table("book_authors"))
            .where(field("book_id").eq(book.id))
            .execute()

        book.authors.forEach { author ->
            val authorId = author.id
                ?: dsl.insertInto(table("authors"))
                    .set(field("name"), author.name)
                    .set(field("birth_date"), author.birthDate)
                    .returningResult(field("id"))
                    .fetchOne()!!
                    .value1() as Long

            dsl.insertInto(table("book_authors"))
                .set(field("book_id"), book.id)
                .set(field("author_id"), authorId)
                .execute()
        }

        return book
    }
}
