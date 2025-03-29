package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "books")
@Builder
@Entity
@Table(name = "Author")
@BatchSize(size = 2)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AuthorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Builder.Default
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookEntity> books = new ArrayList<>();


    public void addBook(BookEntity book) {
        books.add(book);
        book.setAuthor(this);
    }

}
