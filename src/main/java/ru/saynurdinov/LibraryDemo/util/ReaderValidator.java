package ru.saynurdinov.LibraryDemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.saynurdinov.LibraryDemo.model.Reader;
import ru.saynurdinov.LibraryDemo.service.ReaderService;

@Component
public class ReaderValidator implements Validator {

    private final ReaderService readerService;

    @Autowired
    public ReaderValidator(ReaderService readerService) {
        this.readerService = readerService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Reader.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Reader reader = (Reader) target;
        if (readerService.get(reader.getFullName(), reader.getId()).isPresent()) {
            errors.rejectValue("fullName", "", "Читатель с данным ФИО уже существует");
        }
    }
}
