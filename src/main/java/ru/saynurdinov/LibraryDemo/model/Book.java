package ru.saynurdinov.LibraryDemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;
    @NotNull(message = "Поле должно быть заполнено")
    @Size(min = 2, message = "Слишком короткое название")
    @Column(name = "title")
    private String title;
    @NotNull(message = "Поле должно быть заполнено")
    @Size(min = 5, message = "Слишком короткое имя автора")
    @Column(name = "author")
    private String author;
    @Min(value = 1000, message = "Некорректный год издания")
    @Column(name = "year")
    private int year;

    @ManyToOne
    @JoinColumn(name = "reader_id", referencedColumnName = "reader_id")
    private Reader reader;

    @Column(name="date_of_appointment")
    @Temporal(value = TemporalType.DATE)
    @DateTimeFormat(style = "dd/MM/yyyy")
    private LocalDate date;


    public void appointReader(Reader reader) {
        if (reader.getBookList() == null) {
            reader.setBookList(new ArrayList<>());
        }
        this.setReader(reader);
        reader.getBookList().add(this);
        this.setDate(LocalDate.now());
    }

    public void releaseReader() {
        reader.getBookList().remove(this);
        this.setReader(null);
        this.setDate(null);
    }

    public boolean isExpired() {
        return ChronoUnit.DAYS.between(date, LocalDate.now()) > 10L;
    }

}
