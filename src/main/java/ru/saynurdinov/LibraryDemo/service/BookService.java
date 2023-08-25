package ru.saynurdinov.LibraryDemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saynurdinov.LibraryDemo.model.Book;
import ru.saynurdinov.LibraryDemo.model.Reader;
import ru.saynurdinov.LibraryDemo.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAll(Boolean isSorted) {
        if (isSorted) {
            return bookRepository.findAll(Sort.by("year"));
        }
        return bookRepository.findAll();
    }

    public List<Book> getAll(int page, int itemsPerPage, boolean isSorted) {
        if (isSorted) {
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year"))).getContent();
        }
        return bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public Optional<Book> get(int id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> get(String title, String author, int id) {
        return bookRepository.findByTitleAndAuthorAndIdNot(title, author, id);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }


    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book editedBook, int id) {
        editedBook.setId(id);
        bookRepository.save(editedBook);
    }

    @Transactional
    public void remove(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void appoint(int id, Reader reader) {
        bookRepository.findById(id).ifPresent(book -> book.appointReader(reader));
    }

    @Transactional
    public void release(int id) {
        bookRepository.findById(id).ifPresent(Book::releaseReader);
    }
}
