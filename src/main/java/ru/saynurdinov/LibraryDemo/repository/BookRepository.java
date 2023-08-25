package ru.saynurdinov.LibraryDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.saynurdinov.LibraryDemo.model.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitleAndAuthorAndIdNot(String title, String author, int id);

    List<Book> findByTitleContainingIgnoreCase(String title);

}
