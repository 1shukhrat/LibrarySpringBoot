package ru.saynurdinov.LibraryDemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.saynurdinov.LibraryDemo.model.Book;
import ru.saynurdinov.LibraryDemo.service.BookService;

@Component
public class BookValidator implements Validator {
    private final BookService bookService;

    @Autowired
    public BookValidator(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (bookService.get(book.getTitle(), book.getAuthor(), book.getId()).isPresent()) {
            errors.rejectValue("title", "", "Данная книга уже в библиотеке");
            errors.rejectValue("author", "", "Данная книга уже в библиотеке");
        }
    }
}
