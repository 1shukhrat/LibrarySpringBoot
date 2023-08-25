package ru.saynurdinov.LibraryDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.saynurdinov.LibraryDemo.model.Reader;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Integer> {

    Optional<Reader> findByFullNameAndIdNot(String fullName, int id);

    List<Reader> findByFullNameContainingIgnoreCase(String fullName);
}
