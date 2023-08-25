package ru.saynurdinov.LibraryDemo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.saynurdinov.LibraryDemo.model.Reader;
import ru.saynurdinov.LibraryDemo.service.ReaderService;
import ru.saynurdinov.LibraryDemo.util.ReaderValidator;

import java.util.Optional;

@Controller
@RequestMapping("/readers")
public class ReaderController {

    private final ReaderService readerService;
    private final ReaderValidator readerValidator;

    @Autowired
    public ReaderController(ReaderService readerService, ReaderValidator readerValidator) {
        this.readerService = readerService;
        this.readerValidator = readerValidator;
    }


    @GetMapping
    public String index(Model model, @RequestParam(required = false, name="page") Integer page,
                        @RequestParam(required = false, name="items_per_page") Integer items) {
        if (page != null && items != null) {
            model.addAttribute("readerList", readerService.getAll(page, items));
        } else {
            model.addAttribute("readerList", readerService.getAll());
        }
        model.addAttribute("readerSearch", new Reader());
        return "reader/index";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") int id) {
        Optional<Reader> reader = readerService.get(id);
        if (reader.isPresent()) {
            model.addAttribute("reader", reader.get());
            model.addAttribute("bookList", readerService.getBookList(id));
            return "reader/reader";
        } else {
            return "reader/not-found";
        }
    }

    @GetMapping("/new")
    public String getForm(Model model) {
        model.addAttribute("reader", new Reader());
        return "reader/new";
    }

    @GetMapping("/search")
    public String getSearchPage(@ModelAttribute("readerSearch") Reader reader, Model model) {
        model.addAttribute("readerList", readerService.searchByFullName(reader.getFullName()));
        return "reader/search";
    }

    @PostMapping
    public String addReader(@ModelAttribute("reader") @Valid Reader reader, BindingResult bindingResult) {
        readerValidator.validate(reader, bindingResult);
        if (bindingResult.hasErrors()) {
            return "reader/new";
        }
        readerService.save(reader);
        return "redirect:/readers";
    }

    @GetMapping("/{id}/edit")
    public String editForm(Model model, @PathVariable("id") int id) {
        Optional<Reader> reader = readerService.get(id);
        if (reader.isPresent()) {
            model.addAttribute("reader", reader.get());
            return "reader/edit";
        } else {
            return "reader/not-found";
        }
    }

    @PatchMapping("/{id}")
    public String editReader(@PathVariable("id") int id, @ModelAttribute("reader") @Valid Reader reader, BindingResult bindingResult) {
        readerValidator.validate(reader, bindingResult);
        if (bindingResult.hasErrors()) {
            return "reader/edit";
        }
        readerService.update(reader, id);
        return "redirect:/readers";
    }

    @DeleteMapping("/{id}")
    public String removeReader(@PathVariable("id") int id) {
        readerService.remove(id);
        return "redirect:/readers";
    }
}
