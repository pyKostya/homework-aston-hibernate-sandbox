package org.example.entity;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
@Entity
@Table(name = "publisher_book")
public class PublisherBookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private PublisherEntity publisher;

    @ManyToOne
    private BookEntity book;


    public void setPublisher(PublisherEntity publisher) {
        this.publisher = publisher;
        this.publisher.getPublisherBooks().add(this);
    }

    private void setBook(BookEntity book) {
        this.book = book;
        this.book.getPublisherBooks().add(this);
    }

}
