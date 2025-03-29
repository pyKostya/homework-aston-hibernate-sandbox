package org.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "publisherBooks")
@Builder
@Entity
@Table(name = "Publisher")
public class PublisherEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Fetch(FetchMode.SUBSELECT)
    @Builder.Default
    @OneToMany(mappedBy = "publisher", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PublisherBookEntity> publisherBooks = new ArrayList<>();



}
