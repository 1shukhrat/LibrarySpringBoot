package ru.saynurdinov.LibraryDemo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saynurdinov.LibraryDemo.model.Book;
import ru.saynurdinov.LibraryDemo.model.Reader;
import ru.saynurdinov.LibraryDemo.repository.ReaderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReaderService {

    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderService(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    public List<Reader> getAll() {
        return readerRepository.findAll();
    }

    public List<Reader> getAll(int page, int itemsPerPage) {
        return readerRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    public Optional<Reader> get(int id) {
        return readerRepository.findById(id);
    }

    public Optional<Reader> get(String name, int id) {
        return readerRepository.findByFullNameAndIdNot(name, id);
    }

    public List<Reader> searchByFullName(String fullName) {
        return readerRepository.findByFullNameContainingIgnoreCase(fullName);
    }

    public List<Book> getBookList(int id) {
        List<Book> list = new ArrayList<>();
        readerRepository.findById(id).ifPresent(reader -> list.addAll(reader.getBookList()));
        return list;
    }


    @Transactional
    public void save(Reader reader) {
        readerRepository.save(reader);
    }

    @Transactional
    public void update(Reader editedReader, int id) {
        editedReader.setId(id);
        readerRepository.save(editedReader);
    }

    @Transactional
    public void remove(int id) {
        readerRepository.deleteById(id);
    }
}
