package ru.saynurdinov.LibraryDemo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.saynurdinov.LibraryDemo.model.Book;
import ru.saynurdinov.LibraryDemo.model.Reader;
import ru.saynurdinov.LibraryDemo.service.BookService;
import ru.saynurdinov.LibraryDemo.service.ReaderService;
import ru.saynurdinov.LibraryDemo.util.BookValidator;

import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final ReaderService readerService;
    private final BookValidator bookValidator;

    @Autowired
    public BookController(BookService bookService, ReaderService readerService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.readerService = readerService;
        this.bookValidator = bookValidator;
    }


    @GetMapping
    public String index(Model model, @RequestParam(value = "sort_by_year", required = false) Boolean isSorted, @RequestParam(required = false, name="page") Integer page,
                        @RequestParam(required = false, name="items_per_page") Integer items) {
        if (page != null && items != null) {
            model.addAttribute("bookList", bookService.getAll(page, items, Objects.requireNonNullElse(isSorted, false)));
        } else {
            model.addAttribute("bookList", bookService.getAll(Objects.requireNonNullElse(isSorted, false)));
        }
        model.addAttribute("bookSearch", new Book());
        return "book/index";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") int id, @ModelAttribute("reader") Reader reader) {
        Optional<Book> book = bookService.get(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            Optional<Reader> bookOwner = Optional.ofNullable(book.get().getReader());
            bookOwner.ifPresentOrElse(owner -> model.addAttribute("owner", owner), () -> model.addAttribute("readerList", readerService.getAll()));
            return "book/book";
        } else {
            return "book/not-found";
        }
    }

    @PatchMapping("/{id}/appoint")
    public String appointOwner(@PathVariable("id") int id, @ModelAttribute("reader") Reader reader) {
        bookService.appoint(id, reader);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseOwner(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/new")
    public String getForm(Model model) {
        model.addAttribute("book", new Book());
        return "book/new";
    }

    @GetMapping("/search")
    public String getSearchPage(@ModelAttribute("bookSearch") Book book, Model model) {
        model.addAttribute("bookList", bookService.searchByTitle(book.getTitle()));
        return "book/search";
    }

    @PostMapping
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "book/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable("id") int id) {
        Optional<Book> book = bookService.get(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "book/edit";
        } else {
            return "book/not-found";
        }
    }

    @PatchMapping("/{id}")
    public String editBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        bookService.update(book, id);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.remove(id);
        return "redirect:/books";
    }

}
