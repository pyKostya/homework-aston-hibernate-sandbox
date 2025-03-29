package org.example.entity;

import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

import java.util.ArrayList;
import java.util.List;

@FetchProfile(name = "withPublisher", fetchOverrides = {
        @FetchProfile.FetchOverride(
                entity = BookEntity.class,
                association = "publisherBooks",
                mode = FetchMode.JOIN
        )
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "publisherBooks")
@Builder
@Entity
@Table(name = "Book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PublisherBookEntity> publisherBooks = new ArrayList<>();

}
