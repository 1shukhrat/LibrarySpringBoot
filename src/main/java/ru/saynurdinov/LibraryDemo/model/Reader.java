package ru.saynurdinov.LibraryDemo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "reader")
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id")
    private int id;
    @NotNull(message = "Поле должно быть заполнено")
    @Size(min = 5, message = "Слишком короткое ФИО")
    @Column(name = "full_name")
    private String fullName;

    @Min(value = 14, message = "Возраст должен быть 14+")
    @Column(name = "age")
    private int age;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.PERSIST)
    private List<Book> bookList;

}
