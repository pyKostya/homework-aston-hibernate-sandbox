package org.example.tasks;

import org.example.entity.BookEntity;
import org.example.entity.PublisherBookEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;
import java.util.Map;

public class NPlusOneProblem {

    public static void demo() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {

            session.beginTransaction();

            System.out.println("\n@BatchSize solver\n");

            List<BookEntity> books = session.createQuery("select b from BookEntity b", BookEntity.class)
                    .list();


            books.stream()
                    .map(BookEntity::getAuthor)
                    .forEach(System.out::println);

            session.clear();

            System.out.println("\n@Fetch solver\n");

            books = session.createQuery("select b from BookEntity b", BookEntity.class)
                    .list();

            books.stream()
                    .map(BookEntity::getPublisherBooks)
                    .flatMap(List::stream)
                    .map(PublisherBookEntity::getPublisher)
                    .forEach(System.out::println);

            session.clear();

            System.out.println("\nfetch query solver\n");

            books = session.createQuery("select b from BookEntity b " +
                            "join fetch b.publisherBooks p" +
                            " join fetch p.publisher", BookEntity.class)
                    .list();

            books.stream()
                    .map(BookEntity::getPublisherBooks)
                    .flatMap(List::stream)
                    .map(PublisherBookEntity::getPublisher)
                    .forEach(System.out::println);

            session.clear();

            System.out.println("\n@FetchProfile solver\n");

            session.enableFetchProfile("withPublisher");

            BookEntity book = session.get(BookEntity.class, 2L);

            book.getPublisherBooks().stream()
                    .map(PublisherBookEntity::getPublisher)
                    .forEach(System.out::println);

            session.disableFetchProfile("withPublisher");

            session.clear();

            System.out.println("\nEntityGraph solver\n");

            RootGraph<BookEntity> bookGraph = session.createEntityGraph(BookEntity.class);
            bookGraph.addAttributeNodes("publisherBooks");
            SubGraph<PublisherBookEntity> publisherBooks = bookGraph.addSubgraph("publisherBooks", PublisherBookEntity.class);
            publisherBooks.addAttributeNodes("publisher");

            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJakartaHintName(), bookGraph
            );

            book = session.find(BookEntity.class, 2L, properties);

            book.getPublisherBooks().stream()
                    .map(PublisherBookEntity::getPublisher)
                    .forEach(System.out::println);

            session.clear();

            List<BookEntity> bookAllWithGraph = session.createQuery("select b from BookEntity b", BookEntity.class)
                    .setHint(GraphSemantic.LOAD.getJakartaHintName(), bookGraph)
                    .list();

            bookAllWithGraph.stream()
                    .map(BookEntity::getPublisherBooks)
                    .flatMap(List::stream)
                    .map(PublisherBookEntity::getPublisher)
                    .forEach(System.out::println);

            session.getTransaction().commit();
        }
    }
}
