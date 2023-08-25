package ru.saynurdinov.LibraryDemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/library")
public class MainController {

    @GetMapping
    public String getMainMenu() {
        return "main";
    }
}
